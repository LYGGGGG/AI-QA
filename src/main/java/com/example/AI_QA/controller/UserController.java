package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user, HttpSession session) {
        User dbUser = userService.findByUsername(user.getUsername());
        if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) {
            return Result.error("用户名或密码错误");
        }
        session.setAttribute("user", dbUser); // 保存 session
        return Result.success(dbUser);
    }

    // 用户注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        userService.register(user);
        return Result.success("注册成功");
    }

    // 用户退出
    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.invalidate();
        return Result.success("已退出登录");
    }

    // 获取当前登录用户
    @GetMapping("/me")
    public Result<?> currentUser(HttpSession session) {
        Object user = session.getAttribute("user");
        return Result.success(user);
    }
}
