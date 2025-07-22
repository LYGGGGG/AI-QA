package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.vo.QuestionAnswerVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qa")
public class HistoryController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/history")
    public Result<Map<String, Object>> getUserHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        // 从 session 中获取当前用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }

        Long userId = user.getId();

        int offset = (page - 1) * size;
        List<QuestionAnswerVO> list = questionMapper.findUserQA(userId, size, offset);
        int total = questionMapper.countUserQA(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", total);

        return Result.success(result);
    }
}
