package com.hhn.controll.sign;

import com.hhn.pojo.FundProduct;
import com.hhn.service.ISignQueryService;
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
 * Created by lenovo on 2014/12/5.
 */
@Controller
public class SignQuery extends BaseUserAction {

    @Autowired
    private ISignQueryService signQueryService;

    //标的查询
    @RequestMapping("/signQuery.do")
    public ModelAndView signQuery(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String product_name = request.getParameter("product_name");
            //product_name = new String(product_name.getBytes("iso-8859-1"),"UTF-8");
            //String loan_period = request.getParameter("loan_period");
            //String product_status = request.getParameter("product_status");
            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");
            if (product_name != null && !"".equals(product_name)) {
                paraMap.put("product_name", product_name);
            }
//            if (loan_period != null && !"".equals(loan_period)) {
//                paraMap.put("loan_period", Integer.valueOf(loan_period));
//            }
//            if (product_status != null && !"".equals(product_status)) {
//                paraMap.put("product_status", Integer.valueOf(product_status));
//            }
            if (beginDate != null && !"".equals(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (endDate != null && !"".equals(endDate)) {
                paraMap.put("endDate", endDate);
            }
            paraMap.put("product_status", 1);
            List<HashMap> list = signQueryService.findFundByPage(paraMap);
            paraMap.put("signList", list);
            view.addAllObjects(paraMap);
            view.setViewName("signQuery");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg",e.getMessage());
        }
        return view;
    }
    //标的明细
    @RequestMapping("/productDetail.do")
    public ModelAndView findProductDetail(Integer product_id){
        ModelAndView view = new ModelAndView("signDetail");
        FundProduct product = signQueryService.query(product_id);
        view.addObject("productDetail", product);
        return view;
    }
    //放款查询
    @RequestMapping("/putMoney.do")
    public ModelAndView putMoney(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String product_name = request.getParameter("product_name");
            //product_name = new String(product_name.getBytes("iso-8859-1"), "UTF-8");
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
            paraMap.put("product_status", 3);
            List<HashMap> list = signQueryService.findFundByPage(paraMap);
            paraMap.put("productList", list);
            view.addAllObjects(paraMap);
            view.setViewName("putMoney");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg",e.getMessage());
        }
        return view;
    }
    //放款明细
    @RequestMapping("/putMoneyConfirm.do")
    public ModelAndView putMoneyConfirm(Integer product_id){
        ModelAndView view = new ModelAndView("putMoneyConfirm");
        HashMap productMap = signQueryService.queryDetail(product_id);
        view.addObject("productDetail", productMap);
        return view;
    }
    //筹标中查询
    @RequestMapping("/productProcess.do")
    public ModelAndView productProcess(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String product_name = request.getParameter("product_name");
            //product_name = new String(product_name.getBytes("iso-8859-1"), "UTF-8");
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
            paraMap.put("product_status", 2);
            List<HashMap> list = signQueryService.findFundByPage(paraMap);
            paraMap.put("productList", list);
            view.addAllObjects(paraMap);
            view.setViewName("choubiaozhong");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg",e.getMessage());
        }
        return view;
    }
    //筹标中明细
    @RequestMapping("/productProcessConfirm.do")
    public ModelAndView productProcessConfirm(Integer product_id){
        ModelAndView view = new ModelAndView("choubiaozhongconfirm");
        HashMap productMap = signQueryService.queryDetail(product_id);
        view.addObject("productDetail", productMap);
        return view;
    }

    //还款查询
    @RequestMapping("/backMoney.do")
    public ModelAndView backMoney(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String product_name = request.getParameter("product_name");
            //product_name = new String(product_name.getBytes("iso-8859-1"), "UTF-8");
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
            List<HashMap> list = signQueryService.getPayBackProduct(paraMap);
            paraMap.put("productList", list);
            view.addAllObjects(paraMap);
            view.setViewName("huankuanzhong");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg",e.getMessage());
        }
        return view;
    }

    //提前还款查明细
    @RequestMapping("/aheadMoneyDetail.do")
    public ModelAndView aheadMoneyDetail(Integer product_id){
        ModelAndView view = new ModelAndView("aheadMoneyDetail");
        List<HashMap> repayList = signQueryService.getAheadMoneyList(product_id);
        view.addObject("repayList", repayList);
        return view;
    }

    //还款查明细
    @RequestMapping("/backMoneyDetail.do")
    public ModelAndView backMoneyDetail(Integer product_id){
        ModelAndView view = new ModelAndView("backMoneyDetail");
        List<HashMap> repayList = signQueryService.getBackMoneyList(product_id);
        view.addObject("repayList", repayList);
        return view;
    }

    //根据状态查询所有标的
    @RequestMapping("/signQueryAll.do")
    public ModelAndView signQueryAll(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        Map<String, Object> paraMap = new HashMap<String, Object>();
        try {
            String product_name = request.getParameter("product_name");
            //product_name = new String(product_name.getBytes("iso-8859-1"), "UTF-8");
            String product_status = request.getParameter("product_status");
            String beginDate = request.getParameter("beginDate");
            String endDate = request.getParameter("endDate");
            if (product_name != null && !"".equals(product_name)) {
                paraMap.put("product_name", product_name);
            }
            if (product_status!=null && !"".equals(product_status)){
                paraMap.put("product_status",Integer.valueOf(product_status));
            }
            if (beginDate != null && !"".equals(beginDate)) {
                paraMap.put("beginDate", beginDate);
            }
            if (endDate != null && !"".equals(endDate)) {
                paraMap.put("endDate", endDate);
            }
            List<HashMap> list = signQueryService.findFundByPage(paraMap);
            paraMap.put("productList", list);
            view.addAllObjects(paraMap);
            view.setViewName("signQueryAll");
        }catch (Exception e){
            logger.error("error",e);
            view.setViewName("error");
            view.addObject("errorMsg",e.getMessage());
        }
        return view;
    }

}