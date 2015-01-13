package com.hhn.service;

import com.hhn.pojo.FundProduct;
import com.hhn.pojo.LoanDetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/5.
 */
public interface ISignQueryService {
    /**
     * 查询标记录数
     * @param paraMap
     * @return
     */
    public int getLoanCount(Map<String,Object> paraMap);
    /**
     * 分頁查询标
     * @param paraMap
     * @return
     */
    public List<LoanDetail> findLoanByPage(Map<String,Object> paraMap);
    /**
     * 查询标明细
     * @param product_id
     * @return
     */
    public FundProduct query(int product_id);
    /**
     * 查询投资记录数
     * @param paraMap
     * @return
     */
    public int getFundCount(Map<String,Object> paraMap);
    /**
     * 分页查询投资记录
     * @param paraMap
     * @return
     */
    public List<HashMap> findFundByPage(Map<String, Object> paraMap);

    /**
     * 查询满标明细
     * @param product_id
     * @return
     */
    public HashMap queryDetail(int product_id);

    /**
     * 还款查询
     * @param paraMap
     * @return
     */
    public List<HashMap> getPayBackProduct(Map<String, Object> paraMap);

    /**
     * 查询提前还款明细
     * @param product_id
     * @return
     */
    public List<HashMap> getAheadMoneyList(Integer product_id);

    /**
     * 查询还款明细
     * @param product_id
     * @return
     */
    public List<HashMap> getBackMoneyList(Integer product_id);

}
