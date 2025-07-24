package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.entity.User;
import com.example.AI_QA.mapper.QuestionMapper;
import com.example.AI_QA.vo.QuestionAnswerVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletRequest request) {

        // 从 session 中获取当前用户信息
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return Result.error("未登录");
        }

        Long userId = user.getId();

        int offset = (page - 1) * size;
        List<QuestionAnswerVO> list = questionMapper.findUserQA(userId, keyword, tag, startDate, endDate, size, offset);
        int total = questionMapper.countUserQA(userId, keyword, tag, startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", total);

        return Result.success(result);
    }

    @PostMapping("/star")
    public Result<?> updateStar(@RequestBody Map<String, Object> param) {
        Long id = ((Number) param.get("id")).longValue();
        boolean starred = Boolean.TRUE.equals(param.get("starred"));
        questionMapper.updateStarred(id, starred);
        return Result.success();
    }

    @PostMapping("/note")
    public Result<?> updateNote(@RequestBody Map<String, String> param) {
        Long id = Long.valueOf(param.get("id"));
        String note = param.get("note");
        questionMapper.updateNote(id, note);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestBody List<Long> ids) {
        questionMapper.deleteQuestions(ids);
        return Result.success();
    }

    @GetMapping("/summary/{id}")
    public Result<String> summary(@PathVariable Long id) {
        QuestionAnswerVO qa = questionMapper.findQAById(id);
        if (qa == null) return Result.error("记录不存在");
        String summary = qa.getSummary();
        if (summary == null || summary.isBlank()) {
            String answer = qa.getAnswerContent();
            if (answer == null) answer = "";
            summary = answer.length() > 100 ? answer.substring(0, 100) + "..." : answer;
        }
        return Result.success(summary);
    }
}
