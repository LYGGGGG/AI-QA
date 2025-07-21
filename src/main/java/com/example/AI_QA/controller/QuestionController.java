package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.Question;
import com.example.AI_QA.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/ask")
    public Result<?> ask(@RequestBody Question question) {
        return Result.success(questionService.askQuestion(question));
    }
}
