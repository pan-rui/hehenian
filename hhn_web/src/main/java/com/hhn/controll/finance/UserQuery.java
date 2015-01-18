package com.hhn.controll.finance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hhn.hessian.cardverify.ICardVerifyService;
import com.hhn.util.DqlcConfig;
import com.hhn.pojo.FundBankCard;
import com.hhn.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hhn.hessian.invest.IInvestProductService;
import com.hhn.hessian.query.IQueryService;
import com.hhn.pojo.FundUserAccount;
import com.hhn.util.BaseLoginAction;
import com.hhn.util.BaseReturn;
import com.hhn.util.annotaion.AvoidSubmits;

/**
 * Created by lenovo on 2014/12/9.
 */
@Controller
public class UserQuery extends BaseLoginAction {

    @Autowired
    private IInvestProductService investProductService;
    @Autowired
    private IQueryService queryService;
    //初始金额
    @Autowired
    public DqlcConfig dqlcConfig;
    @Autowired
    private ICardVerifyService cardVerifyService;
    /**
     * 查询用户账户余额
     * @return
     */
    @RequestMapping("/queryUserBalance.do")
    @ResponseBody
    public BaseReturn getUserBalance(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        String user_id = getUserId(request);
        if (user_id!=null && !"".equals(user_id)) {
            BaseReturn balance = queryService.queryUserBalance(Integer.valueOf(user_id));
            if (balance.getReturnCode() != 0 || balance.getData() == null) {
                map.put("ba", 0);
            } else {
                FundUserAccount balanceInfo = (FundUserAccount) balance.getData();
                map.put("ba", balanceInfo.getBalance_amount() == null ? 0 : balanceInfo.getBalance_amount());
            }
        }else{
            map.put("ba", "");
        }
        logger.debug("dqlcConfig.investMoneyScope:"+dqlcConfig.investMoneyScope);
        BaseReturn existProduct = queryService.queryPay();
        if (existProduct.getReturnCode()!=0 || existProduct.getData()==null){
            map.put("bt", dqlcConfig.investMoneyScope);
        }else{
            BigDecimal canInvest = (BigDecimal)existProduct.getData();
            map.put("bt", canInvest.add(new BigDecimal(dqlcConfig.investMoneyScope)));
        }
        return new BaseReturn(0, map);
    }

    /**
     * PC端
     * 进入购买明细页面
     * @return
     */
    @RequestMapping("/buyDetailPage.do")
    @AvoidSubmits(saveToken = true)
    public ModelAndView getResultPage(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            String mounth = request.getParameter("mounth");
            String amount = request.getParameter("amount");
            String source = request.getParameter("source");
            Map pmap = (HashMap)(request.getSession().getAttribute("parameterMap"));
            logger.debug("pmap:==>"+pmap);
            if (pmap!=null && pmap.size()>0){
                if(pmap.containsKey("mounth") && pmap.get("mounth")!=null) {
                    mounth = ((String[]) pmap.get("mounth"))[0];
                }else {
                    logger.debug("pmap.get(mounth) is null");
                }
                if(pmap.containsKey("amount") && pmap.get("amount")!=null) {
                    amount = ((String[]) pmap.get("amount"))[0];
                }else{
                    logger.debug("pmap.get(amount) is null");
                }
                if(pmap.containsKey("source") && pmap.get("source")!=null) {
                    source = ((String[]) pmap.get("source"))[0];
                }else{
                    logger.debug("pmap.get(source) is null");
                }
            }
            String userId = getUserId(request);
            map.put("user_id", userId);
            if (mounth != null && !"".equals(mounth)) {
                map.put("mounth", Integer.valueOf(mounth));
            } else {
                view.setViewName("conductFinancial");
                view.addObject("errorMsg", "投资期限不能为空！");
                return view;
            }
            if (amount != null && !"".equals(amount)) {
                BigDecimal investAmount = new BigDecimal(amount);
                map.put("amount", investAmount);
            } else {
                view.setViewName("conductFinancial");
                view.addObject("errorMsg", "投资金额不能为空！");
                return view;
            }
            if (source != null && !"".equals(source)) {
                map.put("source", source);
            } else {
                view.setViewName("conductFinancial");
                view.addObject("errorMsg", "投资来源不能为空！");
                return view;
            }
            BaseReturn existProduct = queryService.queryPay();
            if (existProduct.getReturnCode()!=0 || existProduct.getData()==null){
                if (new BigDecimal(amount).doubleValue()-new BigDecimal(dqlcConfig.investMoneyScope).doubleValue()>0){
                    view.setViewName("conductFinancial");
                    view.addObject("errorMsg", "投资金额已超出可投范围，当前可投金额为"+dqlcConfig.investMoneyScope+"!");
                    return view;
                }
            }
            if (existProduct.getData()!=null){
                BigDecimal canInvest = (BigDecimal)existProduct.getData();
                canInvest = canInvest.add(new BigDecimal(dqlcConfig.investMoneyScope));
                if (new BigDecimal(amount).doubleValue()-canInvest.doubleValue()>0){
                    view.setViewName("conductFinancial");
                    view.addObject("errorMsg", "投资金额已超出可投范围，当前可投金额为"+canInvest+"！");
                    return view;
                }
            }
            //判断投资金额与账户余额比较
            BaseReturn baseBalance = queryService.queryUserBalance(Integer.valueOf(userId));
            FundUserAccount balanceAmount = (FundUserAccount)baseBalance.getData();
            if (balanceAmount!=null) {
                BigDecimal balance = balanceAmount.getBalance_amount();
                balance = balance==null?new BigDecimal(0):balance;
                BigDecimal investAmount = new BigDecimal(amount);
                //投资金额小于或等于余额时
                if (investAmount.doubleValue()-balance.doubleValue() <= 0) {
                    //查询用户购买期间的设定利率
                    BaseReturn baseReturn = investProductService.getProductRate(map);
                    BigDecimal rate = (BigDecimal) (baseReturn.getData());
                    map.put("rate", rate.multiply(BigDecimal.valueOf(100)));
                    map.put("balance", balance);
                    map.put("rateMoney", rate.multiply(investAmount));
                    view.setViewName("buy-confirm");//投资确认页面
                    view.addAllObjects(map);
                    return view;
                } else {
                    //投资金额大于余额时，则去到用户充值页面
                    BaseReturn userPhone = queryService.queryPhone(Integer.valueOf(userId));
                    Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
                    String mobilePhone = (String)userMap.get("mobilePhone");
                    if (mobilePhone.startsWith("-")){
                        mobilePhone = mobilePhone.substring(1);
                    }
                    String realName = (String)userMap.get("realName");
                    logger.info("账户名：" + realName + ",手机号:" + mobilePhone);
                    map.put("userName", realName);
                    map.put("userPhone", Constants.getHidePhone(mobilePhone));
                    map.put("phone", mobilePhone);
                    //查询用户手机号
                    BaseReturn bankCard = queryService.queryBankCard(Integer.valueOf(userId));
                    FundBankCard fundBankCard = (FundBankCard)bankCard.getData();
                    map.put("bankCard", fundBankCard);
                    view.setViewName("payment"); //用户充值页面
                    map.put("balance", balance);
                    map.put("mounth", Integer.valueOf(mounth));
                    map.put("amount", investAmount.subtract(balance));
                    map.put("totalAmount", new BigDecimal(amount));
                    view.addAllObjects(map);
                    return view;
                }
            }else {
                //返回对象为空时
                BaseReturn userPhone = queryService.queryPhone(Integer.valueOf(userId));
                Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
                String mobilePhone = (String)userMap.get("mobilePhone");
                if (mobilePhone.startsWith("-")){
                    mobilePhone = mobilePhone.substring(1);
                }
                String realName = (String)userMap.get("realName");
                logger.info("账户名：" + realName + ",手机号:" + mobilePhone);
                map.put("userName",realName);
                map.put("userPhone", Constants.getHidePhone(mobilePhone));
                map.put("phone",mobilePhone);
                //查询用户手机号
                BaseReturn bankCard = queryService.queryBankCard(Integer.valueOf(userId));
                FundBankCard fundBankCard = (FundBankCard)bankCard.getData();
                map.put("bankCard", fundBankCard);
                view.setViewName("payment"); //用户充值页面
                map.put("balance", "0");
                map.put("mounth", Integer.valueOf(mounth));
                map.put("amount", map.get("amount"));
                map.put("totalAmount", map.get("amount"));
                view.addAllObjects(map);
                return view;
            }
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("conductFinancial");//投资页面
            view.addObject("errorMsg", "系统正忙请稍后重试！");
            return view;
        }
    }

    /**
     * PC端
     * 查询投资期限的利率
     * @return
     */
    @RequestMapping("/getIntPcRate.do")
    public ModelAndView getInvestPcRate(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>();
        ModelAndView view = new ModelAndView();
        String userId = getUserId(request);
        String mounth = request.getParameter("mounth");
        String totalAmount = request.getParameter("totalAmount");
        if (mounth != null && !"".equals(mounth)) {
            map.put("mounth", Integer.valueOf(mounth));
        }
        if (totalAmount!=null && !"".equals(totalAmount)){
            map.put("amount", totalAmount);
        }
        BaseReturn baseReturn = investProductService.getProductRate(map);
        BigDecimal rate = (BigDecimal) (baseReturn.getData());
        map.put("rate", rate.multiply(BigDecimal.valueOf(100)));
        BaseReturn baseBalance = queryService.queryUserBalance(Integer.valueOf(userId));
        FundUserAccount balanceAmount = (FundUserAccount)baseBalance.getData();
        if (balanceAmount!=null) {
            BigDecimal balance = balanceAmount.getBalance_amount();
            balance = balance == null ? new BigDecimal(0) : balance;
            map.put("balance", balance);
        }else{
            map.put("balance", 0);
        }
        BigDecimal rateMoney = rate.multiply(new BigDecimal(totalAmount));
        rateMoney = rateMoney.multiply(new BigDecimal(mounth));
        rateMoney = rateMoney.divide(new BigDecimal(12));
        map.put("rateMoney", rateMoney);
        view.setViewName("buy-confirm");
        view.addAllObjects(map);
        return view;
    }
    /**
     * PC端
     * 用户购买记录查询
     * @param pageNow 当前第几页
     * @param pageSize 每页多少条记录
     * @return
     */
    @RequestMapping("/queryTradeList.do")
    @ResponseBody
    public BaseReturn getTradeList(HttpServletRequest request,Integer pageNow,Integer pageSize){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", getUserId(request));
        if(pageNow==null || pageNow.toString().length()==0){
            return new BaseReturn(1,"当前页码不能为空！");
        }else {
            map.put("pageNow", pageNow);
        }
        if(pageSize==null || pageSize.toString().length()==0){
            return new BaseReturn(1,"每页条数不能为空！");
        }else {
            map.put("pageSize", pageSize);
        }
        if(pageNow>1){
            map.put("numNow", ((pageNow-1) * pageSize)-1);
        }else{
            map.put("numNow", 0);
        }
        Integer count = (Integer)(investProductService.getWebTradeCount(map).getData());
        List<Map> tradeList = null;
        if(count>0){
            tradeList = (List<Map>)(investProductService.getWebTradeList(map).getData());
        }
        map.put("tradeList", tradeList);
        map.put("totalCount", count);
        return new BaseReturn(0, map, "查询成功！");
    }

    /**
     * PC端
     * 用户投资记录查询
     * @param pageNow
     * @param pageSize
     * @return
     */
    @RequestMapping("/getProduct.do")
    @ResponseBody
    public BaseReturn getProductList(HttpServletRequest request,Integer pageNow,Integer pageSize){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", getUserId(request));
        if(pageNow==null || pageNow.toString().length()==0){
            return new BaseReturn(1,"当前页码不能为空！");
        }else {
            map.put("pageNow", pageNow);
        }
        if(pageSize==null || pageSize.toString().length()==0){
            return new BaseReturn(1,"每页条数不能为空！");
        }else {
            map.put("pageSize", pageSize);
        }
        if(pageNow>1){
            map.put("numNow", ((pageNow-1) * pageSize)-1);
        }else{
            map.put("numNow", 0);
        }
        Integer count = (Integer)(investProductService.getWebInvestmentCount(map).getData());
        List<Map> productList = null;
        if(count>0){
            productList = (List<Map>)(investProductService.getWebInvestmentList(map).getData());
        }
        map.put("productList", productList);
        map.put("totalCount", count);
        return new BaseReturn(0, map, "查询成功！");
    }

    /**
     * PC端
     * 单独充值页面
     * @param request
     * @return
     */
    @RequestMapping("/doUserCharge.do")
    public ModelAndView userCharge(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId(request);
        BaseReturn userPhone = queryService.queryPhone(Integer.valueOf(userId));
        Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
        String mobilePhone = (String)userMap.get("mobilePhone");
        if (mobilePhone.startsWith("-")){
            mobilePhone = mobilePhone.substring(1);
        }
        String realName = (String)userMap.get("realName");
        System.out.println("账户名："+realName+",手机号:"+mobilePhone);
        map.put("userName",realName);
        map.put("phone",mobilePhone);
        //查询用户手机号
        BaseReturn bankCard = queryService.queryBankCard(Integer.valueOf(userId));
        map.put("bankCard", bankCard.getData());
        view.setViewName("paymentMoney"); //用户充值页面
        view.addAllObjects(map);
        return view;
    }

    /**
     * 账户总览查询
     * @param request
     * @return
     */
    @RequestMapping("/accountQuery.do")
    public ModelAndView accountQuery(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId(request);
        //余额
        BaseReturn balance = queryService.queryUserBalance(Integer.valueOf(userId));
        if (balance==null || balance.getReturnCode()!=0 || balance.getData()==null) {
            map.put("balance","0.00");
        }else{
            FundUserAccount balanceInfo = (FundUserAccount)balance.getData();
            map.put("balance", balanceInfo.getBalance_amount()==null?0:balanceInfo.getBalance_amount());
        }
        //代收本金
        BaseReturn principal = queryService.queryTotalInvest(Integer.valueOf(userId));
        if (principal==null || principal.getReturnCode()!=0 || principal.getData()==null) {
            map.put("principal","0.00");
        }else{
            map.put("principal", principal.getData());
        }
        //冻结金额
        BaseReturn freeze = queryService.queryRound(Integer.valueOf(userId));
        if (freeze==null || freeze.getReturnCode()!=0 || freeze.getData()==null) {
            map.put("freeze","0.00");
        }else{
            map.put("freeze", freeze.getData());
        }
        //已赚利息
        BaseReturn interested = queryService.queryInterested(Integer.valueOf(userId));
        if (interested==null || interested.getReturnCode()!=0 || interested.getData()==null) {
            map.put("interested","0.00");
        }else{
            map.put("interested", interested.getData());
        }
        //待收利息
        BaseReturn interest = queryService.queryInterest(Integer.valueOf(userId));
        if (interest==null || interest.getReturnCode()!=0 || interest.getData()==null) {
            map.put("interest","0.00");
        }else{
            map.put("interest", interest.getData());
        }
        BaseReturn otherInterest = queryService.queryOtherInterest(Integer.valueOf(userId));
        if (otherInterest==null || otherInterest.getReturnCode()!=0 || otherInterest.getData()==null) {
            map.put("otherInterest","0.00");
        }else{
            map.put("otherInterest", otherInterest.getData());
        }
        view.setViewName("regularOverview");
        view.addAllObjects(map);
        return view;
    }

    /**
     * 查询绑定银行卡
     * @param request
     * @return
     */
    @RequestMapping("/queryBankCard.do")
    public ModelAndView bindBank(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, String> param = new HashMap<String, String>();
        String fromUrl = request.getParameter("fromUrl");
        if (fromUrl!=null && !"".equals(fromUrl)){
            HttpSession session = request.getSession();
            session.setAttribute("fromUrl", fromUrl);
        }
        String userId = getUserId(request);
        param.put("userId", userId);
        BaseReturn userInfo = queryService.queryPhone(Integer.valueOf(userId));
        Map<String,Object> userMap = (HashMap<String,Object>)userInfo.getData();
        String mobilePhone = (String)userMap.get("mobilePhone");
        if (mobilePhone.startsWith("-")){
            mobilePhone = mobilePhone.substring(1);
        }
        String realName = (String)userMap.get("realName");
        String idNo = (String)userMap.get("idNo");
        logger.info("账户名：" + realName + ",手机号:" + mobilePhone);
        map.put("userName",realName);
        map.put("phone", Constants.getHidePhone(mobilePhone));
        map.put("idNo", Constants.getHideIdNo(idNo));
        BaseReturn baseReturn = cardVerifyService.queryUserBindingCard(param);
        if (baseReturn!=null && baseReturn.getReturnCode()==0){
            FundBankCard fundBankCard = (FundBankCard)baseReturn.getData();
            if (fundBankCard!=null) {
                if (fundBankCard.getBank_status() == 2) {
                    map.put("awardPage", 1);
                } else {
                    map.put("awardPage", 0);
                }
                map.put("bankCard", fundBankCard);
                view.setViewName("modifyBankCard");
                view.addAllObjects(map);
                return view;
            }
        }
        view.setViewName("bindBankCard");
        map.put("errorMsg", baseReturn.getMessageInfo());
        view.addAllObjects(map);
        return view;
    }

}
