package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.Answer;
import com.example.AI_QA.entity.Question;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.AnswerMapper;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.service.ConversationService;
import com.example.AI_QA.util.ZhipuAiUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
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

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/ask")
//    public Result<String> ask(@RequestBody Map<String, String> param, HttpSession session) {
    public Result<Map<String, String>> ask(@RequestBody Map<String, String> param, HttpSession session) {
        String questionText = param.get("question");
        String conversationId = param.get("conversationId");
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = conversationService.start(session);
        }

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
//            String aiAnswer = zhipuAiUtil.chat(questionText);
            // 构建对话历史并调用 AI
            var history = conversationService.appendUserMessage(session, conversationId, questionText);
            String aiAnswer = zhipuAiUtil.chat(history);
            conversationService.appendAssistantMessage(session, conversationId, aiAnswer);
            // 3. 保存回答
            Answer answer = new Answer();
            answer.setQuestionId(question.getId());
            answer.setAnswerContent(aiAnswer);
            answer.setModelUsed("glm-4-air-250414");
            answerMapper.insert(answer);

            // 4. 更新问题状态
            questionMapper.updateStatus(question.getId(), "answered");

//            return Result.success(aiAnswer);
            Map<String, String> res = new HashMap<>();
            res.put("conversationId", conversationId);
            res.put("answer", aiAnswer);
            return Result.success(res);
        } catch (IOException e) {
            // 调用失败，标记问题状态
            questionMapper.updateStatus(question.getId(), "error");
            return Result.error("AI 调用失败: " + e.getMessage());
        }
    }
}

