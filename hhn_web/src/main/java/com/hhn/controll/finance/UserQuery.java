package com.hhn.controll.finance;

import com.hhn.hessian.invest.IInvestProductService;
import com.hhn.hessian.query.IQueryService;
import com.hhn.pojo.FundUserAccount;
import com.hhn.pojo.ProductRate;
import com.hhn.util.BaseLoginAction;
import com.hhn.util.BaseReturn;
import com.hhn.util.annotaion.AvoidSubmits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/9.
 */
@Controller
public class UserQuery extends BaseLoginAction {

    @Autowired
    private IInvestProductService investProductService;
    @Autowired
    private IQueryService queryService;

    /**
     * 查询用户账户余额
     * @return
     */
    @RequestMapping("/queryUserBalance.do")
    @ResponseBody
    public BaseReturn getUserBalance(HttpServletRequest request){
        String user_id = getUserId(request);
        Map<String, Object> map = new HashMap<String, Object>();
        BaseReturn balance = queryService.queryUserBalance(Integer.valueOf(user_id));
        FundUserAccount balanceInfo = (FundUserAccount)balance.getData();
        if (balance.getReturnCode()!=0 || balance.getData()==null) {
            map.put("ba",0);
        }else{
            map.put("ba", balanceInfo.getBalance_amount()==null?0:balanceInfo.getBalance_amount());
        }
        BaseReturn existProduct = queryService.queryPay();
        if (existProduct.getReturnCode()!=0 || existProduct.getData()==null){
            map.put("bt", 0);
        }else{
            BigDecimal canInvest = (BigDecimal)existProduct.getData();
            map.put("bt", canInvest);
        }
        return new BaseReturn(0, map);
    }
    /**
     * 查询定投列表
     * @return
     */
    @RequestMapping("/getFixInvestment.do")
    @ResponseBody
    public BaseReturn getFixInvestment(){
        List<ProductRate> productRate = (List<ProductRate>)(investProductService.getProductRateList().getData());
        return new BaseReturn(0, productRate, "查询成功！");
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
            if (pmap!=null && pmap.size()>0){
                mounth = ((String[])pmap.get("mounth"))[0];
                amount = ((String[])pmap.get("amount"))[0];
                source = ((String[])pmap.get("source"))[0];
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
                investAmount = investAmount.setScale(0, BigDecimal.ROUND_HALF_UP);
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
                view.setViewName("conductFinancial");
                view.addObject("errorMsg", "投资金额已超出可投范围，当前可投金额为0！");
                return view;
            }
            if (existProduct.getData()!=null){
                BigDecimal canInvest = (BigDecimal)existProduct.getData();
                if (new BigDecimal(amount).compareTo(canInvest)>0){
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
                if (investAmount.compareTo(balance) <= 0) {
                    //查询用户购买期间的设定利率
                    BaseReturn baseReturn = investProductService.getProductRate(map);
                    BigDecimal rate = (BigDecimal) (baseReturn.getData());
                    map.put("rate", rate.multiply(BigDecimal.valueOf(100)));
                    map.put("balance", balance.setScale(0, BigDecimal.ROUND_HALF_UP));
                    map.put("rateMoney", rate.multiply(investAmount).setScale(0, BigDecimal.ROUND_HALF_UP));
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
                    map.put("userName",realName);
                    map.put("phone",mobilePhone);
                    view.setViewName("payment"); //用户充值页面
                    map.put("balance", balance.setScale(0, BigDecimal.ROUND_HALF_UP));
                    map.put("mounth", Integer.valueOf(mounth));
                    map.put("amount", investAmount.subtract(balance).setScale(0, BigDecimal.ROUND_HALF_UP));
                    map.put("totalAmount", new BigDecimal(amount).setScale(0, BigDecimal.ROUND_HALF_UP));
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
                map.put("phone",mobilePhone);
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
            view.addObject("errorMsg", "数据解析出错啦！");
            return view;
        }
    }

    /**
     * PC端
     * 查询投资期限的利率
     * @return
     */
    @RequestMapping("/getIntPcRate.do")
    @AvoidSubmits(saveToken = true)
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
            map.put("balance", balance.setScale(0, BigDecimal.ROUND_HALF_UP));
        }else{
            map.put("balance", 0);
        }
        map.put("rateMoney", rate.multiply(new BigDecimal(totalAmount)).setScale(0, BigDecimal.ROUND_HALF_UP));
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
        view.setViewName("paymentMoney"); //用户充值页面
        view.addAllObjects(map);
        return view;
    }

}
