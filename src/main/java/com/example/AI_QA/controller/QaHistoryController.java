package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.vo.QuestionAnswerVO;
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
public class QaHistoryController {

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/history")
    public Result<Map<String, Object>> getUserHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Long userId = 1L; // ⚠️ 暂时固定用户 ID

        int offset = (page - 1) * size;
        List<QuestionAnswerVO> list = questionMapper.findUserQA(userId, size, offset);
        int total = questionMapper.countUserQA(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", total);

        return Result.success(result);
    }
}
