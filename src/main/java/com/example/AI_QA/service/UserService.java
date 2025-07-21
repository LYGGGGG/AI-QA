package com.example.AI_QA.service;

import com.example.AI_QA.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    int register(User user);
    int update(User user);
    int delete(Long id);
    List<User> findAll();

}
