package com.hhn.hessian.invest;

import com.hhn.pojo.Invest;
import com.hhn.util.BaseReturn;

/**
 * 投资处理
 * Created by lenovo on 2014/12/12.
 */
public interface IFundInvestService {
    /**
     * 投资
     * @param invest
     * @return
     */
    public BaseReturn investment(Invest invest);


}
