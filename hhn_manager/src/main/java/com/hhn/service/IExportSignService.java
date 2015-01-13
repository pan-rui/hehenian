package com.hhn.service;

import com.hhn.pojo.AccountUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lenovo on 2014/12/4.
 */
@Transactional
public interface IExportSignService {

    public List<String> ExportSign(List<AccountUser> userList) throws Exception;


}
