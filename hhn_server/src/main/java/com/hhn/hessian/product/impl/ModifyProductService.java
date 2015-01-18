package com.hhn.hessian.product.impl;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.product.IModifyProductService;
import com.hhn.pojo.FundProduct;
import com.hhn.service.impl.ModifyProductServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * Created by hynpublic on 2015/1/13.
 */
@Controller
public class ModifyProductService extends BaseService<FundProduct> implements IModifyProductService {
    @Resource
    private ModifyProductServiceImpl modifyProductServiceImpl;
@Resource
private DqlcConfig processInfo;
    @Override
    public BaseReturn modifyForStream(Integer product_id) {
        try {
            return modifyProductServiceImpl.modifyForStream(product_id);
        } catch (Exception rex) {
            rex.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }
}
