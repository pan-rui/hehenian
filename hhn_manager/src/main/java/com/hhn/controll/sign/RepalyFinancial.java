package com.hhn.controll.sign;

import com.hhn.pojo.FundTrade;
import com.hhn.service.IRepalyFinancialService;
import com.hhn.util.BaseReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.Bidi;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Controller
public class RepalyFinancial extends BaseUserAction {
    @Autowired
    private IRepalyFinancialService repalyFinancialService;

    /**
     * 查询提现列表
     * @param request
     * @return
     */
    @RequestMapping("/widthDrawList.do")
    public ModelAndView widthDrawList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map paraMap = new HashMap();
        try {
            String product_name = request.getParameter("product_name");
            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");
            if (product_name != null && !"".equals(product_name)) {
                paraMap.put("product_name", product_name);
            }
            if (beginDate != null && !"".equals(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (endDate != null && !"".equals(endDate)) {
                paraMap.put("endDate", endDate);
            }
            paraMap.put("product_status", 5);
            List<HashMap> list = repalyFinancialService.getRepalyList(paraMap);
            paraMap.put("repalyList", list);
            view.addAllObjects(paraMap);
            view.setViewName("repalyFinancial");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg", e.getMessage());
        }
        return view;
    }

    /**
     * 提现查询
     * @param request
     * @return
     */
    @RequestMapping("/widthDrawDetail.do")
    public ModelAndView widthDrawDetail(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        String productId = request.getParameter("product_id");
        if(productId==null || "".equals(productId)){
            view.addObject("errorMsg", "标的ID不能为空！");
            view.setViewName("repalyFinancial");
            return view;
        }
        Integer product_id = Integer.valueOf(productId);
        Map bankMap = repalyFinancialService.getUserBankDetail(Integer.valueOf(product_id));
        view.setViewName("repalyFinancialDetail");
        view.addObject("cardDetail", bankMap);
        return view;
    }
    /**
     * 提现处理
     * @param request
     * @return
     */
    @RequestMapping("/widthDrawProcess.do")
    @ResponseBody
    public BaseReturn widthDrawProcess(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = request.getParameter("userId");
        String amount = request.getParameter("amount");
        if (userId==null || "".equals(userId)){
            return new BaseReturn(1,"用户ID不能为空！");
        }else{
            map.put("userId", Integer.valueOf(userId));
        }
        if (amount==null || "".equals(amount)){
            return new BaseReturn(1,"提现金额不能为空！");
        }else{
            map.put("amount", new BigDecimal(amount));
        }
        return repalyFinancialService.widthDrawTrade(map);
    }

}
