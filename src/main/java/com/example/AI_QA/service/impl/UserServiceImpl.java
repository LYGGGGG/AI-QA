package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.UserMapper;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public int register(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }

    @Override
    public int delete(Long id) {
        return userMapper.delete(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
