package com.hhn.hessian.query;

import com.hhn.util.BaseReturn;

import java.math.BigDecimal;

/**
 * 查询可用余额,投资金额,赎回金额,已赚利息,待收利息,其它收益
 * Created by hynpublic on 2014/12/26.
 */
public interface IQueryService {
    /**
     * 用户余额
     * @param userId
     * @return
     */
    public BaseReturn queryUserBalance(Integer userId);

    /**
     * 可投金额
     * @return
     */
    public BaseReturn queryPay();

    /**
     * 投资金额
     * @param userId
     * @return
     */
    public BaseReturn queryTotalInvest(Integer userId);

    /**
     * 赎回金额
     * @param userId
     * @return
     */

    public BaseReturn queryRound(Integer userId);

    /**
     * 已赚利息
     * @param userId
     * @return
     */

    public BaseReturn queryInterested(Integer userId);

    /**
     * 代收利息
     * @param userId
     * @return
     */

    public BaseReturn queryInterest(Integer userId);

    /**
     * 其它收益
     * @param userId
     * @return
     */

    public BaseReturn queryOtherInterest(Integer userId);

    /**
     * 查询手机号
     * @param userId
     * @return
     */

    public BaseReturn queryPhone(Integer userId);

    /**
     * 查询银行卡号
     * @param userId
     * @return
     */
    public BaseReturn queryBankCard(Integer userId);

}
