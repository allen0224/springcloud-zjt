package com.zjt.servicezjt.dao.impl;

import com.zjt.servicezjt.dao.UserDao;
import com.zjt.servicezjt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    /**
     * 通过@Resource注解引入JdbcTemplate对象。
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int addUser(User user) {
        // 1. 定义一个sql语句
        String execSQL = "INSERT into user (username, name, age, balance) values (?, ?, ?, ?)";

        // 2. 执行查询方法
        int result = jdbcTemplate.update(execSQL,
                new Object[]{user.getUsername(), user.getName(), user.getAge(), user.getBalance()});

        return result;
    }
}
