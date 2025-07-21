package com.example.AI_QA.controller;

import com.example.AI_QA.entity.User;
import com.example.AI_QA.service.UserService;
import com.example.AI_QA.util.JwtUtil;
import com.example.AI_QA.dto.LoginRequest;
import com.example.AI_QA.vo.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setRole("user");
        int result = userService.register(user);
        return result > 0 ? "注册成功" : "注册失败";
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/all")
    public List<User> listAll() {
        return userService.findAll();
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        User user = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (user != null) {
            String token = JwtUtil.generateToken(user.getId(), user.getUsername());
            return Result.success(token);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
