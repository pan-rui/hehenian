package com.hhn.util;

import com.hhn.dao.BaseDao;
import org.springframework.cache.annotation.Cacheable;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Created by lenovo on 2014/12/6.
 */
public class BaseService<T> extends Base {
    protected javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public String className = "";
    public BaseService() {
//        Class clazz = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()
        Type type = getClass().getGenericSuperclass();
        Class clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            className = clazz.getSimpleName();
        } else {
            String namee = ((Class) type).getSimpleName();
            className = namee.substring(0, namee.indexOf("Service")==-1?namee.length():namee.indexOf("Service"));
        }
//           System.out.println("***********\t"+className);
    }

    //    @Resource
//    protected Validator validator;
    public List<T> getAll(BaseDao<T> baseDao) {
        return baseDao.queryAll();
    }

    public List<T> getAll(BaseDao<T> baseDao, String key) {
        return baseDao.queryAll();
    }

    public List<T> getPage(BaseDao<T> baseDao, int size) {
        return baseDao.queryForPage(size);
    }

    public List<T> getPage(BaseDao<T> baseDao, int size, String key) {
        return baseDao.queryForPage(size);
    }

    public T getById(BaseDao<T> baseDao, int id) {
        return baseDao.query(id);
    }

    public T getById(BaseDao<T> baseDao, int id, String key) {
        return baseDao.query(id);
    }


/*    @ExceptionHandler
    public BaseReturn exp(Exception ex) throws SQLException{  //TODO:可删除
        BaseReturn baseReturn=new BaseReturn();
        ex.printStackTrace();
        if (ex instanceof HttpMessageNotReadableException){
            baseReturn.setReturnCode(BaseReturn.Err_data_inValid);
            baseReturn.setMessageInfo("data.inValid");
        }else if(ex instanceof SQLException) {
            ex.printStackTrace();
            throw new SQLException("sql执行错误");
//            return new BaseReturn()
        } else{
            baseReturn.setReturnCode(BaseReturn.Err_system_error);
            baseReturn.setMessageInfo(ex.getMessage());
        }
        logger.error(ex.getMessage());
        return baseReturn;
    }*/

    public <E> BaseReturn validAndReturn(E t) {
        Set<ConstraintViolation<E>> errs = validator.validate(t);
        if (errs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<E> cons : errs)
                sb.append(cons.getPropertyPath() + ":" + cons.getInvalidValue() + "===>" + cons.getMessage() + "\r\n");
            return new BaseReturn(1, sb.toString());
        } else return new BaseReturn(0, null);
    }
}
