package com.hhn.hessian.product;

import com.hhn.util.BaseReturn;

/**
 * 后台修改标的状态
 * Created by hynpublic on 2015/1/13.
 */
public interface IModifyProductService {
    /**
     * 标的状态改为流标
     * @param product_id
     * @return
     */
    public BaseReturn modifyForStream(Integer product_id);

}
