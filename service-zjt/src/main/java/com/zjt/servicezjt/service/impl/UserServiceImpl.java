package com.zjt.servicezjt.service.impl;

import com.zjt.servicezjt.dao.UserDao;
import com.zjt.servicezjt.entity.User;
import com.zjt.servicezjt.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class UserServiceImpl implements UserService {
    @Resource
    UserDao userdao;
    @Transactional(readOnly = true)
    @Override
    public int addUser(User user) {
        return 0;
    }
}
