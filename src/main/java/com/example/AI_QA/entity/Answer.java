package com.example.AI_QA.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Answer {
    private Long id;
    private Long questionId;
    private String answerContent;
    private LocalDateTime createTime;
    private String modelUsed;
}
