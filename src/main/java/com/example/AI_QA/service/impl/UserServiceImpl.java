package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.UserMapper;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        // 不加密密码（明文存储）
        user.setRole("user"); // 默认角色
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) return null;

        // 直接比对明文密码
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User findByUsername(String name) {
        return userMapper.findByUsername(name);
    }
}
