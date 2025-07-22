package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.Answer;
import com.example.AI_QA.entity.Question;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.AnswerMapper;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.util.ZhipuAiUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private ZhipuAiUtil zhipuAiUtil;

    @PostMapping("/ask")
    public Result<String> ask(@RequestBody Map<String, String> param, HttpSession session) {
        String questionText = param.get("question");

        // ⚠ 这里暂时写死 userId = 1，后续可从 session 中获取
//        Long userId = 1L;
        User user = (User) session.getAttribute("user");
        Long userId = user.getId();


        // 1. 保存问题
        Question question = new Question();
        question.setUserId(userId);
        question.setContent(questionText);
        question.setStatus("waiting");
        questionMapper.insert(question); // 插入后 ID 自动回填

        try {
            // 2. 调用 AI 获取答案
            String aiAnswer = zhipuAiUtil.chat(questionText);

            // 3. 保存回答
            Answer answer = new Answer();
            answer.setQuestionId(question.getId());
            answer.setAnswerContent(aiAnswer);
            answer.setModelUsed("glm-4-air-250414");
            answerMapper.insert(answer);

            // 4. 更新问题状态
            questionMapper.updateStatus(question.getId(), "answered");

            return Result.success(aiAnswer);
        } catch (IOException e) {
            // 调用失败，标记问题状态
            questionMapper.updateStatus(question.getId(), "error");
            return Result.error("AI 调用失败: " + e.getMessage());
        }
    }
}

