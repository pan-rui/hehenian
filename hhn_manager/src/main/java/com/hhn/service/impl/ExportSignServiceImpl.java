package com.hhn.service.impl;

import com.hhn.dao.*;
import com.hhn.pojo.AccountUser;
import com.hhn.pojo.FundProduct;
import com.hhn.pojo.FundUserAccount;
import com.hhn.pojo.LoanDetail;
import com.hhn.service.IExportSignService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2014/12/4.
 */
@Service
@Transactional
public class ExportSignServiceImpl implements IExportSignService {
    @Autowired
    private ILoanDetailDao loanDetailDao;
    @Autowired
    private IFundProductDao fundProductDao;
    @Autowired
    private IFundUserAccountDao fundUserAccountDao;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    private Logger logger = Logger.getLogger(this.getClass());

    public List<String> ExportSign(List<AccountUser> userList) {
        List<String> existList = new ArrayList<String>();
        if (userList.size() > 0) {
            DefaultTransactionDefinition definition = new DefaultTransactionDefinition();//事务定义类
            definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            for (int i = 0; i < userList.size(); i++) {
                AccountUser user = (AccountUser)userList.get(i);
                Integer userId = fundProductDao.existPerson(user);
                if (userId!=null && userId>0){
                    TransactionStatus status = transactionManager.getTransaction(definition);//返回事务对象
                    try {
                        user.setUser_id(userId);
                        addOneRow(user);
                        transactionManager.commit(status);
                    }catch (Exception e) {
                        transactionManager.rollback(status);
                        logger.error("error",e);
                    }
                }else{
                    existList.add("系统不存在借款人:" + user.getUser_name() + ",身份证号:" + user.getId_mumber()+",请先添加借款人！");
                }
            }
        }
        return existList;
    }
    /**
     * 插入一行借款信息，标的记录
     * 异常则回滚当前行记录
     * @param user
     * @throws Exception
     */
    private void addOneRow(AccountUser user) throws Exception {
        Integer user_id = user.getUser_id();
        LoanDetail detail = user.getLoanDetail();
        detail.setUser_id(user_id);
        int loanId = loanDetailDao.save(detail);//插入借款信息
        FundProduct product = user.getFundProduct();
        product.setUser_id(user_id);
        product.setLoan_id(detail.getLoan_id());
        int productId = fundProductDao.save(product);//插入投资信息
        //判断是否存在虚拟资金账户
        int flag = fundUserAccountDao.existUserAccount(user_id);
        if (flag==0) {
            //如果不存在，则插入虚拟账户
            FundUserAccount userAccount = new FundUserAccount();
            userAccount.setUser_id(user_id);
            userAccount.setBalance_amount(new BigDecimal(0));
            userAccount.setCreate_time(new Date());
            fundUserAccountDao.save(userAccount);
        }
    }

}
