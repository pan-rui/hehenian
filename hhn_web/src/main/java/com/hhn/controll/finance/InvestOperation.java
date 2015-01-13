package com.hhn.controll.finance;

import com.hehenian.biz.common.dqlc.IDqlcService;
import com.hhn.dao.IFundTradeDao;
import com.hhn.hessian.invest.IFundInvestService;
import com.hhn.hessian.query.IQueryService;
import com.hhn.hessian.recharge.IRechargeService;
import com.hhn.hessian.repay.IRepaymentService;
import com.hhn.hessian.withdraw.IWithdrawService;
import com.hhn.pojo.Invest;
import com.hhn.util.BankCodeUtil;
import com.hhn.util.BaseLoginAction;
import com.hhn.util.BaseReturn;
import com.hhn.util.annotaion.AvoidSubmits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                Integer code = Integer.valueOf(bankCode);
                String bc = BankCodeUtil.getBankCode(code);
                if (bc!=null && !"".equals(bc)){
                    paraMap.put("BANK_CODE", bc);
                }else {
                    return new BaseReturn(1, "选择银行错误，请重新选择！");
                }
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
            return new BaseReturn(1, "数据解析失败！");
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
                withdraw_amount = withdraw_amount.setScale(0, BigDecimal.ROUND_HALF_UP);
                invest.setMoney(withdraw_amount);
            }
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
            logger.info("调投资接口开始..................start.");
            BaseReturn baseReturn = fundInvestmentService.investment(invest);
            logger.info("调投资接口返回......................end");
            return baseReturn;
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"数据解析失败！");
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
            if (account_no==null || "".equals(account_no)){
                return new BaseReturn(1,"卡号不能为空！");
            }else {
                map.put("ACCOUNT_NO", account_no);
            }
            if (amount == null || "".equals(amount)) {
                return new BaseReturn(1, "充值金额不能为空！");
            } else {
                BigDecimal withdraw_amount = new BigDecimal(amount);
                withdraw_amount = withdraw_amount.setScale(0, BigDecimal.ROUND_HALF_UP);
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
            return new BaseReturn(1, "数据解析失败！");
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
                withdraw_amount = withdraw_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
                map.put("amount", withdraw_amount.toString());
            }
            BaseReturn baseReturn = withdrawService.withdraw(map);
            return baseReturn;
        }catch (Exception e){
            logger.error("error",e);
            return new BaseReturn(1,"数据解析失败！");
        }
    }

}
