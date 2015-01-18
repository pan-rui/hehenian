package com.hhn.dao;

import com.hhn.pojo.AccountUser;
import com.hhn.pojo.FundProduct;
import com.hhn.pojo.FundTrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundProductDao extends BaseDao<FundProduct> {

    public Integer existPerson(AccountUser accountUser);

    public int existFundProduct(FundProduct fundProduct);

    public int getAllCount(Map<String,Object> paraMap);

    public List<HashMap> findByPage(Map<String,Object> paraMap);

    public void updateSignStatus(FundProduct fundProduct);

    public void updateSignStatusByLoanId(FundProduct fundProduct);

    public List<FundProduct> queryProduct(@Param("money") BigDecimal money,@Param("month")Short month);
    public List<FundProduct> queryProduct2(@Param("money") BigDecimal money,@Param("month")Short month);
    public List<FundProduct> queryProduct3(@Param("money") BigDecimal money,@Param("month")Short month);
    public List<FundProduct> queryProduct4(@Param("money") BigDecimal money,@Param("month")Short month);

    public FundProduct queryByloanId(Integer loan_id);

    public List<FundProduct> queryByTradeId(Integer fund_trade_id);

    public Integer getWebProductCount(Map<String,Object> map);

    public List<FundTrade> getWebProductList(Map<String,Object> map);

    public HashMap queryDetail(int product_id);

    public List<HashMap> getPayBackProduct(Map<String, Object> map);
    //前台查询投资记录数
    public Integer getWebInvestmentCount(Map<String,Object> map);
    //前台查询投资记录列表
    public List<HashMap> getWebInvestmentList(Map<String,Object> map);

    public Integer updateProduct(@Param("params") Map<String,Object> params,@Param("product_id") Integer product_id,@Param("money") BigDecimal money);

    public List<HashMap> getWithdrawList(Map<String, Object> map);

}
