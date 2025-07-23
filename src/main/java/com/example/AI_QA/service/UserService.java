package com.example.AI_QA.service;

import com.example.AI_QA.entity.User;

public interface UserService {
    User register(User user);

    User login(String username, String password);

    User findByUsername(String name);

    /**
     * Update the user's password.
     *
     * @param id         user id
     * @param newPassWord new encoded password
     */
    void updatePassWord(Long id, String newPassWord);
}
