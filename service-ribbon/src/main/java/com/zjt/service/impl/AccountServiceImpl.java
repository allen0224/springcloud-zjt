package com.zjt.service.impl;

import com.zjt.dao.AccountDao;
import com.zjt.entity.Account;
import com.zjt.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    AccountDao accountDao;
    @Transactional(readOnly = true)
    @Override
    public int add(Account account) {
        return accountDao.add(account);
    }
}
