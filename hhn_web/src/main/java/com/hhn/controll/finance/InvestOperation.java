package com.hhn.controll.finance;

import com.hehenian.biz.common.dqlc.IDqlcService;
import com.hhn.dao.IFundTradeDao;
import com.hhn.hessian.cardverify.ICardVerifyService;
import com.hhn.hessian.invest.IFundInvestService;
import com.hhn.hessian.query.IQueryService;
import com.hhn.hessian.recharge.IRechargeService;
import com.hhn.hessian.withdraw.IWithdrawService;
import com.hhn.pojo.FundBankCard;
import com.hhn.pojo.Invest;;
import com.hhn.util.BaseLoginAction;
import com.hhn.util.BaseReturn;
import com.hhn.util.Constants;
import com.hhn.util.annotaion.AvoidSubmits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/13.
 * 投资用户专用类
 */
@Controller
public class InvestOperation extends BaseLoginAction {

    @Autowired
    private IFundInvestService fundInvestmentService;
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IQueryService queryService;
    @Autowired
    private IDqlcService dqlcService;
    @Autowired
    private ICardVerifyService cardVerifyService;
    /**
     * PC端
     * 用户投资充值
     * @param request
     * @return
     */
    @RequestMapping("/doChargeMoney.do")
    @ResponseBody
    public BaseReturn chargeMoney(HttpServletRequest request) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String user_name = request.getParameter("userName");//用户姓名
            String bankCode = request.getParameter("bankCode");//银行类型
            String account_no = request.getParameter("userAccount");//银行帐号
            String password = request.getParameter("password");//登录密码
            String mounth = request.getParameter("mounth");//购买期限
            String amount = request.getParameter("amount"); //充值金额
            String totalAmount = request.getParameter("totalAmount");//总金额
            String phone = request.getParameter("phone");//手机号
            String verfiyCode = request.getParameter("verfiyCode");//验证码
            String user_id = getUserId(request); //用户ID
            paraMap.put("user_id", user_id);
            if (password != null && !"".equals(password)) {
                if (verfiyCode ==null || "".equals(verfiyCode)) {
                    return new BaseReturn(1, "验证码不能为空！");
                }
                Integer userId = Integer.valueOf(user_id);
                //查询账户名和手机号
                BaseReturn userPhone = queryService.queryPhone(userId);
                Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
                String mobilePhone = (String)userMap.get("mobilePhone");
                if (mobilePhone.startsWith("-")){
                    mobilePhone = mobilePhone.substring(1);
                }
                String realName = (String)userMap.get("realName");
                logger.info("账户名：" + realName + ",手机号:" + mobilePhone);
                paraMap.put("ACCOUNT_NAME", realName);//帐户名
                Map<String,Boolean> validMap = dqlcService.checkPhoneVerifyCodeAndPwd(userId.longValue(),password,mobilePhone,verfiyCode);
                if (!validMap.get("pwd")){
                    return new BaseReturn(1, "登录密码不正确！");
                }
                if (!validMap.get("phone")) {
                    return new BaseReturn(1, "验证码不正确！");
                }
            } else {
                return new BaseReturn(1, "登录密码不能为空！");
            }
            if (bankCode!=null && !"".equals(bankCode)){
                paraMap.put("BANK_CODE", bankCode);
            }else{
                return new BaseReturn(1, "未选择银行！");
            }
            if (account_no != null && !"".equals(account_no)) {
                paraMap.put("ACCOUNT_NO", account_no.replaceAll(" ", ""));
            } else {
                return new BaseReturn(1, "用户ID不能为空！");
            }
            if (amount != null && !"".equals(amount)) {
                paraMap.put("AMOUNT", amount);
            } else {
                return new BaseReturn(1, "充值金额不能为空！");
            }
            BaseReturn baseReturn = rechargeService.recharge(paraMap);
            if (baseReturn.getReturnCode() == 0) {
                if (mounth != null && !"".equals(mounth)) {
                    paraMap.put("mounth", mounth);
                }
                if (totalAmount != null && !"".equals(totalAmount)) {
                    paraMap.put("totalAmount", totalAmount);
                }
                baseReturn.setData(paraMap);
            }
            return baseReturn;
        } catch (Exception e) {
            logger.error("error",e);
            return new BaseReturn(1, "系统正忙请稍后重试！");
        }
    }
    /**
     * PC端和移动端
     * 用户投资申请
     * @param request
     * @return
     */
    @RequestMapping("/doInvest.do")
    @ResponseBody
    @AvoidSubmits(removeToken = true)
    public BaseReturn investment(HttpServletRequest request) {
        try {
            Invest invest = new Invest();
            String mounth = request.getParameter("mounth");//投资几月
            String amount = request.getParameter("amount"); //提现金额
            String source = request.getParameter("source"); //来源
            invest.setUser_id(Integer.valueOf(getUserId(request)));
            if (mounth == null || "".equals(mounth)) {
                return new BaseReturn(1, "购买期限不能为空！");
            } else {
                invest.setMonth(Integer.valueOf(mounth));
            }
            if (amount == null || "".equals(amount)) {
                return new BaseReturn(1, "购买金额不能为空！");
            } else {
                BigDecimal withdraw_amount = new BigDecimal(amount);
                invest.setMoney(withdraw_amount);
            }
            if ("PC".equals(source)){
                invest.setTargetType(IFundTradeDao.TargetType.PC);
            }else if("ANDROID".equals(source)){
                invest.setTargetType(IFundTradeDao.TargetType.ANDROID);
            }else if("ISO".equals(source)){
                invest.setTargetType(IFundTradeDao.TargetType.IOS);
            }else if ("WP".equals(source)){
                invest.setTargetType(IFundTradeDao.TargetType.WP);
            }else {
                invest.setTargetType(IFundTradeDao.TargetType.OTHER);
            }
            logger.info("调投资接口开始..................start.");
            BaseReturn baseReturn = fundInvestmentService.investment(invest);
            logger.info("调投资接口返回......................end");
            return baseReturn;
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"系统正忙请稍后重试！");
        }
    }

    /**
     * PC端
     * 用户充值
     * @param request
     * @return
     */
    @RequestMapping("/doCharge.do")
    @ResponseBody
    public BaseReturn doCharge(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String user_name = request.getParameter("userName");//用户姓名
            String bankCode = request.getParameter("bankCode");//银行类型
            String account_no = request.getParameter("userAccount");//银行帐号
            //String password = request.getParameter("password");//登录密码
            String amount = request.getParameter("amount"); //充值金额
            String phone = request.getParameter("phone");//手机号
            String verfiyCode = request.getParameter("verfiyCode");//验证码
            String user_id = getUserId(request); //用户ID
            if (bankCode!=null && !"".equals(bankCode)){
                map.put("BANK_CODE", bankCode);
            }else{
                return new BaseReturn(1, "未选择银行！");
            }
            if (account_no==null || "".equals(account_no)){
                return new BaseReturn(1,"卡号不能为空！");
            }else {
                map.put("ACCOUNT_NO", account_no);
            }
            if (amount == null || "".equals(amount)) {
                return new BaseReturn(1, "充值金额不能为空！");
            } else {
                BigDecimal withdraw_amount = new BigDecimal(amount);
                map.put("AMOUNT", withdraw_amount.toString());
            }
            if (verfiyCode==null || "".equals(verfiyCode)){
                return new BaseReturn(1, "验证码不能为空！");
            }
            if (user_id!=null && !"".equals(user_id)){
                Integer userId = Integer.valueOf(user_id);
                //查询账户名和手机号
                BaseReturn userPhone = queryService.queryPhone(userId);
                Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
                String mobilePhone = (String)userMap.get("mobilePhone");
                if (mobilePhone.startsWith("-")){
                    mobilePhone = mobilePhone.substring(1);
                }
                String realName = (String)userMap.get("realName");
                logger.info("账户名："+realName+",手机号:"+mobilePhone);
                map.put("ACCOUNT_NAME", realName);//帐户名
                map.put("user_id", user_id); //userId
                boolean flag = dqlcService.checkPhoneVerifyCode(mobilePhone,verfiyCode);
                if (!flag){
                    return new BaseReturn(1, "验证码不正确！");
                }
            }else{
                return new BaseReturn(2,getHhn_login(),"请先登录!");
            }
            BaseReturn baseReturn = rechargeService.recharge(map);
            return baseReturn;
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1, "系统正忙请稍后重试！");
        }
    }

    /**
     * 用户提现申请
     * @param request
     * @return
     */
    @RequestMapping("/doWithdraw.do")
    @ResponseBody
    public BaseReturn doWithdraw(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String amount = request.getParameter("amount"); //提现金额
            map.put("user_id", getUserId(request));
            if (amount == null || "".equals(amount)) {
                return new BaseReturn(1, "购买金额不能为空！");
            } else {
                BigDecimal withdraw_amount = new BigDecimal(amount);
                map.put("amount", withdraw_amount.toString());
            }
            BaseReturn baseReturn = withdrawService.withdraw(map);
            return baseReturn;
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"系统正忙请稍后重试！");
        }
    }

    /**
     * 绑定银行卡
     * @param request
     * @return
     */
    @RequestMapping("/bindBankCard.do")
    public ModelAndView bindBank(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Map<String, String> param = new HashMap<String, String>();
            String bankCode = request.getParameter("bankCode");
            String userAccount = request.getParameter("userAccount");
            userAccount = userAccount.replaceAll(" ", "");
            //String verifyCode = request.getParameter("verifyCode");
            String userId = getUserId(request);
            if (bankCode == null || "".equals(bankCode)) {
                view.setViewName("bindBankCard");
                view.addObject("errorMsg", "银行代码不能为空！");
                return view;
            } else {
                param.put("bankCode", bankCode);
            }
            if (userAccount == null || "".equals(userAccount)) {
                view.setViewName("bindBankCard");
                view.addObject("errorMsg", "银行卡号不能为空！");
                return view;
            } else {
                param.put("bankNo", userAccount);
            }
            param.put("userId", userId);
            BaseReturn baseReturn = cardVerifyService.sendBankIdentifyCode(param);
            //BaseReturn baseReturn = new BaseReturn(0,"成功！");
            if (baseReturn != null && baseReturn.getReturnCode() == 0) {
                queryUserInfo(userId, bankCode, userAccount, map);
                map.put("awardPage", 1);
                view.setViewName("modifyBankCard");
                view.addAllObjects(map);
                return view;
            } else {
                queryUserInfo(userId,bankCode,userAccount,map);
                map.put("errorMsg", baseReturn.getMessageInfo());
                view.setViewName("bindBankCard");
                view.addAllObjects(map);
                return view;
            }
        }catch (Exception e){
            logger.error("error", e);
            view.setViewName("bindBankCard");
            view.addObject("errorMsg", "系统正忙请稍后重试！");
            return view;
        }
    }

    /**
     * 验证银行卡
     * @param request
     * @return
     */
    @RequestMapping("/verifyBankCard.do")
    public ModelAndView verifyBindBank(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            Map<String, String> map = new HashMap<String, String>();
            String money = request.getParameter("money");
            String bankCode = request.getParameter("bankCode");
            String bankNo = request.getParameter("codeNo");
            HttpSession session = request.getSession();
            String userId = getUserId(request);
            map.put("userId", userId);
            map.put("amount", money);
            logger.debug("checkBankIdentifyCode::userId:::"+userId+"::amount::"+money);
            BaseReturn baseReturn = cardVerifyService.checkBankIdentifyCode(map);
            logger.debug("return checkBankIdentifyCode:"+baseReturn.getReturnCode());
            //BaseReturn baseReturn = new BaseReturn(0, "成功!");
            if (baseReturn != null && baseReturn.getReturnCode() == 0) {
                String fromUrl = (String)session.getAttribute("fromUrl");
                logger.debug("fromUrl:::::::"+fromUrl);
                if (fromUrl!=null && !"".equals(fromUrl)){
                    return new ModelAndView("forward:"+fromUrl);
                }else {
                    view.setViewName("bindCardSuccess");
                    return view;
                }
            } else {
                queryUserInfo(userId, bankCode, bankNo, param);
                param.put("awardPage", 1);
                param.put("errorMsg", baseReturn.getMessageInfo());
                view.setViewName("modifyBankCard");
                view.addAllObjects(param);
                return view;
            }
        }catch (Exception e){
            logger.error(e);
            view.setViewName("modifyBankCard");
            view.addObject("errorMsg", "系统正忙请稍后重试！");
            return view;
        }
    }

    /**
     * 修改绑定银行卡
     * @param request
     * @return
     */
    @RequestMapping("/modifyBankCard.do")
    public ModelAndView modifyBindBank(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> param = new HashMap<String, Object>();
        try {
            Map<String, String> map = new HashMap<String, String>();
            String bankCode = request.getParameter("bankCode");
            String userAccount = request.getParameter("userAccount");
            userAccount = userAccount.replaceAll(" ", "");
            String userId = getUserId(request);
            if (bankCode != null && !"".equals(bankCode)) {
                map.put("bankCode", bankCode);
            } else {
                view.setViewName("modifyBankCard");
                view.addObject("errorMsg", "银行不能为空！");
                return view;
            }
            if (userAccount != null && !"".equals(userAccount)) {
                map.put("bankNo", userAccount);
            } else {
                view.setViewName("modifyBankCard");
                view.addObject("errorMsg", "银行卡号不能为空！");
                return view;
            }
            map.put("userId", userId);
            BaseReturn baseReturn = cardVerifyService.updateUserBindingCard(map);
            if (baseReturn != null && baseReturn.getReturnCode() == 0) {
                queryUserInfo(userId,bankCode,userAccount,param);
                param.put("awardPage", 1);
                view.setViewName("modifyBankCard");
                view.addAllObjects(param);
                return view;
            } else {
                queryUserInfo(userId,bankCode,userAccount, param);
                param.put("awardPage", 0);
                param.put("errorMsg", baseReturn.getMessageInfo());
                view.setViewName("modifyBankCard");
                view.addAllObjects(param);
                return view;
            }
        }catch (Exception e){
            logger.error("error", e);
            view.setViewName("modifyBankCard");
            view.addObject("errorMsg", "系统正忙请稍后重试！");
            return view;
        }
    }

    private void queryUserInfo(String userId,String bankCode, String bankNo, Map map){
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
        HashMap<String, String> mapp = new HashMap<String, String>();
        mapp.put("userId", userId);
        FundBankCard fundBankCard = new FundBankCard();
        fundBankCard.setUser_id(Integer.valueOf(userId));
        fundBankCard.setBank_code(bankCode);
        fundBankCard.setCard_no(bankNo);
        map.put("bankCard", fundBankCard);
    }

}
