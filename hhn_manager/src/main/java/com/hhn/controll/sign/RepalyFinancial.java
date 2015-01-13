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
    @RequestMapping("/repalyList.do")
    public ModelAndView repalyList(HttpServletRequest request) {
        ModelAndView view = new ModelAndView();
        try {
            String user_name = request.getParameter("user_name");
            //user_name = new String(user_name.getBytes("iso-8859-1"),"UTF-8");
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
            List<FundTrade> list = repalyFinancialService.getRepalyList(paraMap);
            paraMap.put("repalyList", list);
            view.addAllObjects(paraMap);
            view.setViewName("repalyFinancial");
        }catch (Exception e){
            view.setViewName("error");
        }
        return view;
    }

    /**
     * 提现审核(放款)
     * @param request
     * @return
     */
    @RequestMapping("/repalyStatus.do")
    @ResponseBody
    public BaseReturn repalyStatus(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String repalyId = request.getParameter("repalyId");
        if(repalyId!=null && !"".equals(repalyId)){
            map.put("repalyId", repalyId);
        }
        BaseReturn baseReturn = repalyFinancialService.repalyStatus(map);
        return baseReturn;
    }

}
