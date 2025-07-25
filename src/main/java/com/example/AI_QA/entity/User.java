package com.example.AI_QA.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

