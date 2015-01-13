package com.hhn.controll.finance;

import com.hehenian.biz.common.dqlc.IDqlcService;
import com.hhn.dao.IFundTradeDao;
import com.hhn.hessian.invest.IFundInvestService;
import com.hhn.hessian.invest.IInvestProductService;
import com.hhn.hessian.query.IQueryService;
import com.hhn.hessian.recharge.IRechargeService;
import com.hhn.pojo.FundUserAccount;
import com.hhn.pojo.Invest;
import com.hhn.util.BankCodeUtil;
import com.hhn.util.BaseLoginAction;
import com.hhn.util.BaseReturn;
import com.hhn.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/1/9.
 */
@Controller
public class PhoneOperation extends BaseLoginAction {

    @Autowired
    private IInvestProductService investProductService;
    @Autowired
    private IQueryService queryService;
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private IFundInvestService fundInvestmentService;
    @Autowired
    private IDqlcService dqlcService;

    /**
     * 移动端
     * 查询投资期限的利率
     * @return
     */
    @RequestMapping("/getInvestRate.do")
    @ResponseBody
    public BaseReturn getInvestRate(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            String mounth = request.getParameter("mounth");
            if (mounth == null || "".equals(mounth)) {
                return new BaseReturn(1, "投资期限不能为空！");
            } else {
                //查询用户余额
                BigDecimal balanceAmount = new BigDecimal(0);
                String user_id = getUserId(request);
                BaseReturn balance = queryService.queryUserBalance(Integer.valueOf(user_id));
                if (balance.getData()!=null) {
                    FundUserAccount balanceInfo = (FundUserAccount) balance.getData();
                    if (balanceInfo != null) {
                        balanceAmount = balanceInfo.getBalance_amount() == null ? new BigDecimal(0) : balanceInfo.getBalance_amount();
                    }
                }
                //查询当前利率
                map.put("mounth", Integer.valueOf(mounth));
                BaseReturn baseReturn = investProductService.getProductRate(map);
                map.put("balance", balanceAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
                map.put("rate", baseReturn.getData());
                baseReturn.setData(map);
                return baseReturn;
            }
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"数据解析失败!");
        }
    }

    /**
     * 移动端
     * 用户购买
     * @return
     */
    @RequestMapping("/buyFunancial.do")
    @ResponseBody
    public BaseReturn buyFunancial(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>();
        String mounth = request.getParameter("mounth");
        String amount = request.getParameter("amount");
        String source = request.getParameter("source");
        if (mounth != null && !"".equals(mounth)) {
            map.put("mounth", Integer.valueOf(mounth));
        } else {
            return new BaseReturn(1, "投资期限不能为空！");
        }
        if (amount != null && !"".equals(amount)) {
            BigDecimal investAmount = new BigDecimal(amount);
            investAmount = investAmount.setScale(0, BigDecimal.ROUND_HALF_UP);
            map.put("amount", investAmount);
        } else {
            return new BaseReturn(1, "投资金额不能为空！");
        }
        if (source != null && !"".equals(source)) {
            map.put("source", source);
        } else {
            return new BaseReturn(1, "投资来源不能为空！");
        }
        String userId = getUserId(request);
        //判断投资金额与账户余额比较
        BaseReturn baseBalance = queryService.queryUserBalance(Integer.valueOf(userId));
        FundUserAccount balanceAmount = (FundUserAccount)baseBalance.getData();
        if (balanceAmount!=null) {
            BigDecimal balance = balanceAmount.getBalance_amount();
            balance = balance==null?new BigDecimal(0):balance;
            BigDecimal investAmount = new BigDecimal(amount);
            //投资金额小于或等于余额时
            if (investAmount.compareTo(balance) <= 0) {
                //余额够用，调投资接口
                BaseReturn baseReturn = doPhoneInvestment(userId,mounth,amount,source);
                map.put("data",baseReturn.getData());
                map.put("messageInfo",baseReturn.getMessageInfo());
                map.put("tradeTime", DateUtil.format(new Date()));
                baseReturn.setData(map);
                return baseReturn;
            }else{
                //先充值，后投资
                BaseReturn userPhone = queryService.queryPhone(Integer.valueOf(userId));
                Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
                String mobilePhone = (String)userMap.get("mobilePhone");
                if (mobilePhone.startsWith("-")){
                    mobilePhone = mobilePhone.substring(1);
                }
                String realName = (String)userMap.get("realName");
                map.put("userName",realName);
                map.put("phone",mobilePhone);
                map.put("balance",balance.setScale(0,BigDecimal.ROUND_HALF_UP));
                BigDecimal phAmount = investAmount.subtract(balance).setScale(0, BigDecimal.ROUND_HALF_UP);
                map.put("amount", phAmount);
                BigDecimal phTotalAmount = new BigDecimal(amount).setScale(0, BigDecimal.ROUND_HALF_UP);
                map.put("totalAmount", phTotalAmount);
                HttpSession session = request.getSession();
                session.setAttribute("phMounth",mounth);
                session.setAttribute("phAmount",phAmount);
                session.setAttribute("phTotalAmount", phTotalAmount);
                BaseReturn baseReturn = new BaseReturn();
                baseReturn.setReturnCode(2);//2-先充值
                baseReturn.setData(map);
                return baseReturn;
            }
        }else {
            BaseReturn userPhone = queryService.queryPhone(Integer.valueOf(userId));
            Map<String,Object> userMap = (HashMap<String,Object>)userPhone.getData();
            String mobilePhone = (String)userMap.get("mobilePhone");
            if (mobilePhone.startsWith("-")){
                mobilePhone = mobilePhone.substring(1);
            }
            String realName = (String)userMap.get("realName");
            map.put("userName",realName);
            map.put("phone",mobilePhone);
            map.put("balance", 0);
            map.put("amount", map.get("amount"));
            map.put("totalAmount", map.get("amount"));
            HttpSession session = request.getSession();
            session.setAttribute("phMounth",mounth);
            session.setAttribute("phAmount",map.get("amount"));
            session.setAttribute("phTotalAmount", map.get("amount"));
            BaseReturn baseReturn = new BaseReturn();
            baseReturn.setReturnCode(2);//2-先充值
            baseReturn.setData(map);
            return baseReturn;
        }
    }
    /**
     * 移动端
     * 用户投资充值
     * @return
     */
    @RequestMapping("/chargeMoneyPhone.do")
    @ResponseBody
    public BaseReturn chargeMoneyPhone(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            String user_name = request.getParameter("userName");//用户姓名
            String bankCode = request.getParameter("bankCode");//银行代码
            String account_no = request.getParameter("userAccount");//银行帐号
            String password = request.getParameter("passwd");//登录密码
            String mounth = request.getParameter("mounth");//购买期限
            String amount = request.getParameter("amount"); //充值金额
            String totalAmount = request.getParameter("totalAmount");//总金额
            String phone = request.getParameter("phone");//手机号
            String source = request.getParameter("source");//来源
            String verfiyCode = request.getParameter("verfiyCode");//验证码
            String user_id = getUserId(request); //用户ID
            map.put("user_id", user_id);
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
                map.put("ACCOUNT_NAME", realName);//帐户名
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
                Integer code = Integer.valueOf(bankCode);
                String bc = BankCodeUtil.getBankCode(code);
                if (bc!=null && !"".equals(bc)){
                    map.put("BANK_CODE", bc);
                }else {
                    return new BaseReturn(1, "选择银行错误，请重新选择！");
                }
            }else{
                return new BaseReturn(1, "未选择银行！");
            }
            if (account_no != null && !"".equals(account_no)) {
                map.put("ACCOUNT_NO", account_no.replaceAll(" ", ""));
            } else {
                return new BaseReturn(1, "用户ID不能为空！");
            }
            HttpSession session = request.getSession();
            if (amount != null && !"".equals(amount)) {
                map.put("AMOUNT", session.getAttribute("phAmount"));
            } else {
                return new BaseReturn(1, "充值金额不能为空！");
            }
            BaseReturn baseReturn = rechargeService.recharge(map);
            if (baseReturn.getReturnCode() == 0) {
                String phmounth = (String)session.getAttribute("phMounth");
                String phAmount = (String)session.getAttribute("phAmount");
                String phTotalAmount = (String)session.getAttribute("phTotalAmount");
                //调投资接口
                BaseReturn baseReturn1 = doPhoneInvestment(user_id,phmounth,phAmount,phTotalAmount);
                map.put("data",baseReturn.getData());
                map.put("messageInfo",baseReturn.getMessageInfo());
                map.put("tradeTime", DateUtil.format(new Date()));
                baseReturn1.setData(map);
                return baseReturn1;
            }else{
                logger.info("手机端充值失败。。。。。。。。。。。。。。");
                logger.info(baseReturn.getMessageInfo());
                logger.info(baseReturn.getData());
                return baseReturn;
            }
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"数据解析失败!");
        }
    }

    /**
     * 移动端
     * 用户购买记录查询
     * @return
     */
    @RequestMapping("/getTradeForPhone.do")
    @ResponseBody
    public BaseReturn getTradeList(HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", getUserId(request));
        return  investProductService.getWebTradeList(map);
    }

    //手机用户投资
    private BaseReturn doPhoneInvestment(String userId,String mounth,String amount,String source){
        Invest invest = new Invest();
        invest.setUser_id(Integer.valueOf(userId));
        invest.setMonth(Integer.valueOf(mounth));
        BigDecimal withdraw_amount = new BigDecimal(amount);
        withdraw_amount = withdraw_amount.setScale(0, BigDecimal.ROUND_HALF_UP);
        invest.setMoney(withdraw_amount);
        if ("pc".equals(source)){
            invest.setTargetType(IFundTradeDao.TargetType.PC);
        }else if("android".equals(source)){
            invest.setTargetType(IFundTradeDao.TargetType.ANDROID);
        }else if("iphone".equals(source)){
            invest.setTargetType(IFundTradeDao.TargetType.IOS);
        }else if ("winphone".equals(source)){
            invest.setTargetType(IFundTradeDao.TargetType.WP);
        }else {
            invest.setTargetType(IFundTradeDao.TargetType.OTHER);
        }
        logger.info("手机端调投资接口开始..................start.");
        BaseReturn baseReturn = fundInvestmentService.investment(invest);
        logger.info("手机端调投资接口返回......................end.");
        return baseReturn;
    }

}
