package com.example.AI_QA.service;

import com.example.AI_QA.entity.User;

public interface UserService {
    User register(User user);

    User login(String username, String password);
}
