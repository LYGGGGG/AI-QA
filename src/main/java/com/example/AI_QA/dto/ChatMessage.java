package com.example.AI_QA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChatMessage 是对话消息的数据结构，
 * 用于构造与 AI 模型（如 ChatGLM、GPT 等）交互的上下文。
 *
 * role 表示消息发送者的身份：如 "user"、"assistant"、"system"
 * content 是对应的消息文本内容
 */
@Data
@NoArgsConstructor  // 生成无参构造
@AllArgsConstructor // 生成全参构造
public class ChatMessage {

    /**
     * 消息发送方角色：user / assistant / system
     */
    private String role;

    /**
     * 消息正文内容
     */
    private String content;
}
