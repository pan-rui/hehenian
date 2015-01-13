package com.hhn.service.impl;

import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundRepaymentDao;
import com.hhn.dao.ILoanDetailDao;
import com.hhn.pojo.FundProduct;
import com.hhn.pojo.LoanDetail;
import com.hhn.service.ISignQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/5.
 */
@Service
public class SignQueryServiceImpl implements ISignQueryService {

    @Autowired
    private ILoanDetailDao loanDetailDao;
    @Autowired
    private IFundProductDao fundProductDao;
    @Autowired
    private IFundRepaymentDao fundRepaymentDao;
    /**
     * 查询标记录数
     * @param paraMap
     * @return
     */
    public int getLoanCount(Map<String,Object> paraMap){
        return loanDetailDao.getAllCount(paraMap);
    }
    /**
     * 分頁查询标
     * @param paraMap
     * @return
     */
    public List<LoanDetail> findLoanByPage(Map<String,Object> paraMap){
        return loanDetailDao.findByPage(paraMap);
    }
    /**
     * 查询标明细
     * @param product_id
     * @return
     */
    public FundProduct query(int product_id){ return fundProductDao.query(product_id); }

    /**
     * 查询投资记录数
     * @param paraMap
     * @return
     */
    public int getFundCount(Map<String,Object> paraMap){
        return fundProductDao.getAllCount(paraMap);
    }

    /**
     * 分页查询投资记录
     * @param paraMap
     * @return
     */
    public List<HashMap> findFundByPage(Map<String, Object> paraMap){
        return fundProductDao.findByPage(paraMap);
    }
    /**
     * 查询满标明细
     * @param product_id
     * @return
     */
    public HashMap queryDetail(int product_id){
        return fundProductDao.queryDetail(product_id);
    }
    /**
     * 还款查询
     * @param paraMap
     * @return
     */
    public List<HashMap> getPayBackProduct(Map<String, Object> paraMap){
        return fundProductDao.getPayBackProduct(paraMap);
    }
    /**
     * 查询提前还款明细
     * @param product_id
     * @return
     */
    public List<HashMap> getAheadMoneyList(Integer product_id){
        return fundRepaymentDao.getReymentDetailList(product_id);
    }


    /**
     * 查询还款明细
     * @param product_id
     * @return
     */
    public List<HashMap> getBackMoneyList(Integer product_id){
        return fundRepaymentDao.getReymentDetailList(product_id);
    }

}
