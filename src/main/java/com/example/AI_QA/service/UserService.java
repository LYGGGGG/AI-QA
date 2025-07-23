package com.example.AI_QA.service;

import com.example.AI_QA.entity.User;

public interface UserService {
    User register(User user);

    User login(String username, String password);

    User findByUsername(String name);

    User updatePassWord(Long id, String newPassWord);
}
