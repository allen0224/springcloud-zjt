package com.zjt.dao.impl;

import com.zjt.annotation.ZjtTansacational;
import com.zjt.dao.AccountDao;
import com.zjt.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
public class IAccountDaoImpl implements AccountDao {
    /**
     * 通过@Resource注解引入JdbcTemplate对象。
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int add(Account account) {
        return jdbcTemplate.update("insert into account(name, money) values(?, ?)",
                account.getName(),account.getMoney());

    }
}
