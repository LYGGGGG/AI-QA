package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.dto.LoginRequest;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        return Result.success(userService.register(user));
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) return Result.error("用户名或密码错误");
        return Result.success(user);
    }
}
