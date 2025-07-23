package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.UserMapper;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /** Password encoder for hashing and validating passwords */
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User register(User user) {
        // 加密密码
        user.setRole("user"); // 默认角色
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);

        if (user == null) return null;

        // 比对加密密码
        /*if (user.getPassword().equals(password)) {*/
        if (passwordEncoder.matches(password, user.getPassword())){
            return user;
        }else if (password.equals(user.getPassword())){
            String encoded = passwordEncoder.encode(password);
            userMapper.updatePassWord(user.getId(),encoded);
            user.setPassword(encoded);
            return user;
        }

        return null;
    }

    @Override
    public User findByUsername(String name) {
        return userMapper.findByUsername(name);
    }

    @Override
    public void updatePassWord(Long id, String newPassWord) {
        String encoded = passwordEncoder.encode(newPassWord);
        userMapper.updatePassWord(id, encoded);
    }
}
