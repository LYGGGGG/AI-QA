package com.example.AI_QA.controller;

import com.example.AI_QA.common.Result;
import com.example.AI_QA.util.ZhipuAiUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @PostMapping("/ask")
    public Result<String> ask(@RequestBody Map<String, String> param) {
        String question = param.get("question");
        try {
            String answer = ZhipuAiUtil.chat(question); // 实时调用 AI
            return Result.success(answer);
        } catch (IOException e) {
            return Result.error("调用失败：" + e.getMessage());
        }
    }
}
