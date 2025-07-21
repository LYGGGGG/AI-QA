package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.Answer;
import com.example.AI_QA.entity.Question;
import com.example.AI_QA.mapper.AnswerMapper;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.service.QuestionService;
import com.example.AI_QA.util.ZhipuAiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public Question askQuestion(Question question) {
        question.setStatus("answered");
        questionMapper.insert(question);

        String answerText = "默认回答";
        try {
            answerText = ZhipuAiUtil.chat(question.getContent());
        } catch (Exception e) {
            question.setStatus("error");
            questionMapper.insert(question);
            answerText = "AI服务调用失败：" + e.getMessage();
        }

        Answer answer = new Answer();
        answer.setQuestionId(question.getId());
        answer.setAnswerContent(answerText);
        answer.setModelUsed("glm-4");
        answerMapper.insert(answer);

        return question;
    }
}
