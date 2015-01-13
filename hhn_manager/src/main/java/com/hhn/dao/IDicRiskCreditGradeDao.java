package com.hhn.dao;

import com.hhn.pojo.DicRiskCreditGrade;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IDicRiskCreditGradeDao {
    public int save(DicRiskCreditGrade dicRiskCreditGrade);

    public DicRiskCreditGrade query(int id);

    public int delete(int id);

    public int update(DicRiskCreditGrade dicRiskCreditGrade);

}
