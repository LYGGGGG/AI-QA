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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AI 问答控制器
 * 负责接收用户问题，调用 AI 模型生成回答，并将问答保存至数据库
 */
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

    /**
     * 用户向 AI 提问接口
     * @param param 请求参数，包含 question、conversationId
     * @param session 当前登录会话
     * @return 返回 AI 回答内容与 conversationId
     */
    @PostMapping("/ask")
    public Result<Map<String, String>> ask(@RequestBody Map<String, String> param, HttpSession session) {
        // 获取问题内容与对话 ID（可能为空）
        String questionText = param.get("question");
        String conversationId = param.get("conversationId");

        // 获取当前登录用户
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        Long userId = user.getId();

        // 如果没有传 conversationId，则新开一轮对话
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = conversationService.start(session);
        }

        // 1. 保存用户提问至数据库（状态为 waiting）
        Question question = new Question();
        question.setUserId(userId);
        question.setContent(questionText);
        question.setStatus("waiting");
        questionMapper.insert(question); // 自动回填 ID

        try {
            // 2. 构造上下文历史，调用 AI 获取回答
            var history = conversationService.appendUserMessage(session, conversationId, questionText);
            String aiAnswer = zhipuAiUtil.chat(history);  // 调用 AI 模型
            conversationService.appendAssistantMessage(session, conversationId, aiAnswer);

            // 3. 保存 AI 回答至数据库
            Answer answer = new Answer();
            answer.setQuestionId(question.getId());
            answer.setAnswerContent(aiAnswer);
            answer.setModelUsed("glm-4-air-250414"); // 可配置化
            answerMapper.insert(answer);

            // 4. 更新提问状态为 answered
            questionMapper.updateStatus(question.getId(), "answered");

            // 5. 构造响应结果：回答 + 对话 ID（便于继续多轮对话）
            Map<String, String> res = new HashMap<>();
            res.put("conversationId", conversationId);
            res.put("answer", aiAnswer);
            return Result.success(res);
        } catch (IOException e) {
            // 若调用 AI 出错，更新问题状态为 error
            questionMapper.updateStatus(question.getId(), "error");
            return Result.error("AI 调用失败: " + e.getMessage());
        }
    }
}
