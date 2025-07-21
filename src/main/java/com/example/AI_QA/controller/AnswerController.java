package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.Answer;
import com.example.AI_QA.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private AnswerMapper answerMapper;

    @GetMapping("/byQuestion/{questionId}")
    public Result<?> getAnswerByQuestion(@PathVariable Long questionId) {
        Answer answer = answerMapper.findByQuestionId(questionId);
        if (answer == null) {
            return Result.error("该问题暂无回答");
        }
        return Result.success(answer);
    }
}
