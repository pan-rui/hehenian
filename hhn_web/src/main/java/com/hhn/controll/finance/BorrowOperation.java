package com.hhn.controll.finance;

import com.hhn.hessian.loan.ILoanDetailService;
import com.hhn.pojo.LoanDetail;
import com.hhn.util.BaseAction;
import com.hhn.util.BaseReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/9.
 * 借款专用类
 */
@Controller
public class BorrowOperation extends BaseAction {

    @Autowired
    private ILoanDetailService loanDetailService;

    /**
     * 用户借款申请
     *
     * @param request
     * @return
     */
    @RequestMapping("/doLoanDetail.do")
    @ResponseBody
    public BaseReturn loanDetail(HttpServletRequest request) {
//        LoanPerson person = new LoanPerson();
//        person.setUser_id(698);
//        person.setEducation((byte)0);
//        person.setGraduate_school("北京大学");
//        person.setGraduate_year(2013);
//        person.setResidence_type((byte)1);
//        person.setFamily_phone("075589890102");
//        person.setMarriaged((byte)1);
//        person.setHas_children((byte)0);
//        person.setHas_house((byte)0);
//        person.setHas_house_loan((byte)0);
//        person.setHas_car((byte)1);
//        person.setCreate_time(new Date());
//        LoanPersonWork personWork = new LoanPersonWork();
//        personWork.setUser_id(698);
//        personWork.setCom_full_name("和合年咨询有限公司");
//        personWork.setDepartment("研发部");
//        personWork.setPosition((byte)0);
//        personWork.setIncome((byte)2);
//        personWork.setEmail("jhe@jc-yt.com");
//        personWork.setCom_type((byte)2);
//        personWork.setIndustry((byte)2);
//        personWork.setScale((byte)2);
//        personWork.setWork_year((short)1);
//        personWork.setCreate_time(new Date());

        LoanDetail detail = new LoanDetail();
        detail.setUser_id(728);
        detail.setLoan_title("房贷" + "000001");
        BigDecimal loan_amount = new BigDecimal("2500");
        loan_amount = loan_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        detail.setLoan_amount(loan_amount);
        detail.setLoan_usage("6");
        detail.setLoan_period((short) 10);
        BigDecimal annual_rate = new BigDecimal("0.7");
        annual_rate = annual_rate.setScale(2, BigDecimal.ROUND_HALF_UP);
        detail.setAnnual_rate(annual_rate);
        detail.setRepay_period((short) 10);
        detail.setRepay_type((byte) 0);
        detail.setTender_day((short) 5);
        detail.setLoan_desc("房贷");
        detail.setLoan_status((byte) 0);
        detail.setBorrower_type((byte) 0);
        detail.setCreate_time(new Date());
        BigDecimal loan_rate = new BigDecimal("0.71");
        loan_rate = loan_rate.setScale(2, BigDecimal.ROUND_HALF_UP);
        detail.setLoan_rate(loan_rate);
//        BaseReturn msg = loanPersonService.apply(person, personWork);
        BaseReturn msg = loanDetailService.apply(detail);
        System.out.println(msg.getReturnCode() + "==========" + msg.getMessageInfo());
        return msg;
    }

    /**
     * 借款审核
     *
     * @param request
     * @return
     */
    @RequestMapping("/doLoanAudit.do")
    @ResponseBody
    public BaseReturn doLoanAudit(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String user_id = request.getParameter("userId");//用户ID
        String amount = request.getParameter("amount"); //提现金额
        //BigDecimal withdraw_amount = new BigDecimal(amount);
        //withdraw_amount = withdraw_amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        //int loanDetailId = Integer.valueOf(request.getParameter("loanDetailId"));
        int loanDetailId = 159;
        BaseReturn baseReturn = loanDetailService.verify(loanDetailId);
        //借款审核成功
        return baseReturn;
    }


}
