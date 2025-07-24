package com.example.AI_QA.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Question {
    private Long id;
    private Long userId;
    private String content;
    private String tag;
    private Boolean starred;
    private String note;
    private String summary;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
