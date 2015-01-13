package com.hhn.controll.sign;

import com.hhn.pojo.FundProduct;
import com.hhn.pojo.FundProductAudit;
import com.hhn.service.ISignAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by lenovo on 2014/12/8.
 */
@Controller
public class SignAudit extends BaseUserAction {

    @Autowired
    private ISignAuditService signAuditService;

    /**
     * 标的发布审核
     * @param productId
     * @return
     */
    @RequestMapping("/signAudit.do")
    @ResponseBody
    public boolean signAudit(HttpServletRequest request, Integer productId){
        try {
            FundProduct fundProduct = new FundProduct();
            fundProduct.setProduct_id(productId);
            String userId = getUserId(request);
            fundProduct.setUser_id(Integer.valueOf(userId));
            String userName = getUserName(request);
            fundProduct.setProduct_status((byte)2);
            fundProduct.setUpdate_time(new Date());
            signAuditService.updateSignStatus(fundProduct, userName);
            return true;
        }catch (Exception e){
            logger.error("error",e);
            return false;
        }
    }

    /**
     * 放弃发布标的
     * @param productId
     * @return
     */
    @RequestMapping("/dropAuditById.do")
    @ResponseBody
    public boolean signAuditById(HttpServletRequest request, Integer productId){
        try{
            FundProduct fundProduct = new FundProduct();
            fundProduct.setProduct_id(productId);
            String userId = getUserId(request);
            fundProduct.setUser_id(Integer.valueOf(userId));
            String userName = getUserName(request);
            fundProduct.setProduct_status((byte)8);
            fundProduct.setUpdate_time(new Date());
            signAuditService.updateSignStatus(fundProduct,userName);
            return true;
        }catch (Exception e){
            logger.error("error",e);
            return false;
        }
    }

    /**
     * 流标、冻结
     * @param productId
     * @return
     */
    @RequestMapping("/processConfirm.do")
    @ResponseBody
    public boolean processConfirm(HttpServletRequest request, Integer productId, Integer status){
        try {
            FundProduct fundProduct = new FundProduct();
            fundProduct.setProduct_id(productId);
            String userId = getUserId(request);
            fundProduct.setUser_id(Integer.valueOf(userId));
            String userName = getUserName(request);
            fundProduct.setProduct_status(status.byteValue());
            fundProduct.setUpdate_time(new Date());
            signAuditService.updateSignStatus(fundProduct,userName);
            return true;
        }catch (Exception e){
            logger.error("error",e);
            return false;
        }
    }

    /**
     * 放款审核
     * @param productId
     * @return
     */
    @RequestMapping("/signPutMoney.do")
    @ResponseBody
    public boolean signPutMoney(HttpServletRequest request, Integer productId){
        try {
            FundProduct fundProduct = new FundProduct();
            fundProduct.setProduct_id(productId);
            String userId = getUserId(request);
            fundProduct.setUser_id(Integer.valueOf(userId));
            String userName = getUserName(request);
            fundProduct.setProduct_status((byte)5);
            fundProduct.setUpdate_time(new Date());
            return signAuditService.putMoneyAudit(fundProduct,userName);
        }catch (Exception e){
            logger.error("error",e);
            return false;
        }
    }


}
