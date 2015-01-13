package com.hhn.controll.sign;

import com.hhn.service.IInvestRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/9.
 */
@Controller
public class InvestRecord extends BaseUserAction {
    @Autowired
    private IInvestRecordService investRecordService;

    @RequestMapping("/investRecordQuery.do")
    public ModelAndView investRecord(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try{
            Map<String, Object> paraMap = new HashMap<String,Object>();
            String productId = request.getParameter("product_id");
            String productName = request.getParameter("product_name");
            String userName = request.getParameter("user_name");

            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");

            if(StringUtils.isNotEmpty(productId)){
                paraMap.put("product_id",productId);
            }
            if(StringUtils.isNotEmpty(productName)){
                paraMap.put("product_name", productName);
            }
            if(StringUtils.isNotEmpty(userName)){
                paraMap.put("user_name", userName);
            }

            if (StringUtils.isNotEmpty(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (StringUtils.isNotEmpty(endDate)){
                paraMap.put("endDate",endDate);
            }
            //添加投资包含的类型
            paraMap.put("trade_type", "INVESTMENT");
          //  paraMap = new HashMap<String, Object>();
            Map<String, Object> resultMap = investRecordService.investRecordList(paraMap);
            view.setViewName("investRecordList");
            view.addAllObjects(resultMap);
        }catch (Exception e){
            view.setViewName("error");
            e.printStackTrace();
        }
        return view;
    }

}
