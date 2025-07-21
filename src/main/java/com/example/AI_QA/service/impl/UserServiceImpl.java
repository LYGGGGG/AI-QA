package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.UserMapper;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        // 密码加密
        String encryptedPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encryptedPwd);
        user.setRole("user"); // 默认角色
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) return null;

        String encryptedPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        if (user.getPassword().equals(encryptedPwd)) {
            return user;
        }
        return null;
    }
}
