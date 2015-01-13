package com.hhn.hessian.product.impl;

import com.hhn.hessian.product.IModifyProductService;
import com.hhn.pojo.FundProduct;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.ModifyProductServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created by hynpublic on 2015/1/13.
 */
@Controller
public class ModifyProductService extends BaseService<FundProduct> implements IModifyProductService {
    @Resource
    private ModifyProductServiceImpl modifyProductServiceImpl;
@Resource
private ProcessInfo processInfo;
    @Override
    public BaseReturn modifyForStream(Integer product_id) {
        try {
            return modifyProductServiceImpl.modifyForStream(product_id);
        } catch (Exception rex) {
            rex.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }
}
