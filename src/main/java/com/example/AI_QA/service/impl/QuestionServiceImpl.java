package com.example.AI_QA.service.impl;

import com.example.AI_QA.entity.Answer;
import com.example.AI_QA.entity.Question;
import com.example.AI_QA.mapper.AnswerMapper;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.service.QuestionService;
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
        question.setStatus("answered"); // 模拟已回答
        questionMapper.insert(question);

        // 模拟AI生成回答
        Answer answer = new Answer();
        answer.setQuestionId(question.getId());
        answer.setAnswerContent("这是AI的回答（模拟内容）");
        answer.setModelUsed("Mock-GPT");
        answerMapper.insert(answer);

        return question;
    }
}
