package com.hhn.controll.sign;

import com.hhn.pojo.FundTradeDetail;
import com.hhn.service.ILoanTransferService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/10.
 */
@Controller
public class LoanTransfer extends BaseUserAction {

    @Autowired
    private ILoanTransferService loanTransferService;
    /**
     * 债权转让查询
      * @param request
     * @return
     */
    @RequestMapping("/doLoanTransfer.do")
    public ModelAndView loanTransfer(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            String user_name = request.getParameter("user_name");
          //  user_name = new String(user_name.getBytes("iso-8859-1"),"UTF-8");
            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");
            Map<String, Object> paraMap = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(user_name)){
                paraMap.put("user_name", user_name);
            }
            if (StringUtils.isNotEmpty(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (StringUtils.isNotEmpty(endDate)){
                paraMap.put("endDate",endDate);
            }
            //债权转让类型
           // paraMap.put("trade_type", "CLAIM_FEE");
            Map<String, Object> resultMap = loanTransferService.getLoanTransferList(paraMap);
            view.setViewName("loanTransfer");
            view.addAllObjects(resultMap);
        }catch (Exception e){
            view.setViewName("error");
        }
        return view;
    }

    @RequestMapping("/getTradeDetail.do")
    public ModelAndView getTradeDetail(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            int trade_id = Integer.valueOf(request.getParameter("trade_id")).intValue();
            List<FundTradeDetail> detailList = loanTransferService.getTradeDetailList(trade_id);
            view.setViewName("tradeDetail");
            view.addObject("detailList", detailList);
        }catch (Exception e){
            view.setViewName("error");
        }
        return view;
    }

}
