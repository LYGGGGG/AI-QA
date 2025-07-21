package com.example.AI_QA.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionAnswerVO {
    private Long questionId;
    private String questionContent;
    private String answerContent;
    private LocalDateTime createTime;
}

