package com.example.AI_QA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    /**
     * 访问根路径时跳转到静态的 index.html
     */
    @GetMapping("/")
    public String index() {
        // redirect 到 static 目录下的 index.html
        return "redirect:/index.html";
    }

    @GetMapping("/qa")
    public String qaPage() {
        return "qa"; // 对应 templates/qa.html
    }
}
