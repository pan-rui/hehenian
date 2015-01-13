package com.hhn.util;

import com.hhn.dao.BaseDao;
import com.hhn.util.annotaion.KeyInTable;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;

/**
 * Created by lenovo on 2014/12/8.
 */
public class KeyInTableCheck extends Base implements ConstraintValidator<KeyInTable,Serializable> {
    private KeyInTable keyInTable;
    private BaseDao baseDao;
    private String column;
    @Override
    public void initialize(KeyInTable keyInTable) {
        this.keyInTable=keyInTable;
//        System.out.println("application========================>"+applicationContext);
        this.baseDao = (BaseDao)applicationContext.getBean(keyInTable.value());
        this.column=keyInTable.column();
    }

    @Override
    public boolean isValid(Serializable serializable, ConstraintValidatorContext constraintValidatorContext) {
        if(serializable==null) return false;
        if(StringUtils.isEmpty(column))
          return baseDao.query((Integer)serializable)!=null;
        else
            return baseDao.queryByProperties(column,serializable).size()>0;
    }
}
