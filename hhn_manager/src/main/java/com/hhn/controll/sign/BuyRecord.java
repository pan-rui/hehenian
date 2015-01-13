package com.hhn.controll.sign;

import com.hhn.service.IBuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
@Controller
public class BuyRecord extends BaseUserAction{
    @Autowired
    private IBuyRecordService buyRecordService;

    @RequestMapping("/buyRecordQuery.do")
    public ModelAndView exportSign(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            String user_name = request.getParameter("user_name");
           // user_name = new String(user_name.getBytes("iso-8859-1"),"UTF-8");
            String period = request.getParameter("period");
            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");
            Map<String, Object> paraMap = new HashMap<String, Object>();
            if (user_name!=null && !"".equals(user_name)){
                paraMap.put("user_name", user_name);
            }
            if (period!=null && !"".equals(period)){
                paraMap.put("period", period);
            }
            if (beginDate != null && !"".equals(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (endDate!=null && !"".equals(endDate)){
                paraMap.put("endDate",endDate);
            }
            Map<String, Object> resultMap = buyRecordService.buyRecordQuery(paraMap);
            view.addAllObjects(resultMap);
            view.setViewName("buyRecordList");
        }catch (Exception e){
            view.setViewName("error");
        }
        return view;
    }


}
