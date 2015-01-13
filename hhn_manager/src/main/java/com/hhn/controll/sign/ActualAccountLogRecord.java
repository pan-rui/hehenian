package com.hhn.controll.sign;

import com.hhn.service.IActualAccountLogService;
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
 * Created by lenovo on 2014/12/8.
 */
@Controller
public class ActualAccountLogRecord extends BaseUserAction{
    @Autowired
    private IActualAccountLogService actualAccountLogService;

    @RequestMapping("/actualAccountLogRecordQuery.do")
    public ModelAndView actualAccountLogRecord(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            String user_name = request.getParameter("user_name");
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
            Map<String, Object> resultMap = actualAccountLogService.actualAccountLogRecordQuery(paraMap);
            view.addAllObjects(resultMap);
            view.setViewName("actualAccountLogList");
        }catch (Exception e){
            view.setViewName("error");
        }
        return view;
    }


}
