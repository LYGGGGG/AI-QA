package com.example.AI_QA.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/qa")
    public String qaPage() {
        return "qa"; // 对应 templates/qa.html
    }
}
