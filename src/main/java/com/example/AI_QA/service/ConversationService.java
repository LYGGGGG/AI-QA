package com.example.AI_QA.service;

import com.example.AI_QA.dto.ChatMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ConversationService 用于管理每个用户在会话中的上下文历史（如用户问、AI答的记录）。
 * 会话数据存在 HttpSession 中，不存入数据库，适合轻量临时缓存。
 */
@Service
public class ConversationService {

    // HttpSession 中存储会话的键名
    private static final String SESSION_KEY = "CONVERSATIONS";

    // 每个会话最多保留的消息数，超过将自动移除最早的一条
    private static final int MAX_MESSAGES = 20;

    /**
     * 从 session 中获取所有会话的 Map，若不存在则创建。
     * 每个用户 session 中会有多个 conversationId -> ChatMessage 队列的映射。
     */
    @SuppressWarnings("unchecked")
    private Map<String, Deque<ChatMessage>> getMap(HttpSession session) {
        Object obj = session.getAttribute(SESSION_KEY);
        if (obj instanceof Map<?, ?> map) {
            return (Map<String, Deque<ChatMessage>>) map;
        }
        Map<String, Deque<ChatMessage>> map = new HashMap<>();
        session.setAttribute(SESSION_KEY, map);
        return map;
    }

    /**
     * 启动一个新的会话，并生成唯一的 conversationId。
     * 创建一个新的空队列，用于记录该会话的历史。
     * @return conversationId 会话唯一标识
     */
    public String start(HttpSession session) {
        String id = UUID.randomUUID().toString();
        getMap(session).put(id, new ArrayDeque<>());
        return id;
    }

    /**
     * 添加用户的一条提问消息，并返回当前对话历史（包括用户消息）
     * @param id 会话 ID
     * @param content 用户的问题内容
     */
    public List<ChatMessage> appendUserMessage(HttpSession session, String id, String content) {
        Deque<ChatMessage> list = getMap(session).computeIfAbsent(id, k -> new ArrayDeque<>());
        list.addLast(new ChatMessage("user", content));
        trim(list);
        return new ArrayList<>(list);
    }

    /**
     * 添加 AI 的一条回答，不返回对话历史（通常配合用户消息后单独插入）
     */
    public void appendAssistantMessage(HttpSession session, String id, String content) {
        Deque<ChatMessage> list = getMap(session).computeIfAbsent(id, k -> new ArrayDeque<>());
        list.addLast(new ChatMessage("assistant", content));
        trim(list);
    }

    /**
     * 获取指定会话的全部历史（包括用户和AI消息）
     */
    public List<ChatMessage> getHistory(HttpSession session, String id) {
        Deque<ChatMessage> list = getMap(session).get(id);
        if (list == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(list);
    }

    /**
     * 超过上限时，自动移除最早的消息，保持 MAX_MESSAGES 条以内
     */
    private void trim(Deque<ChatMessage> list) {
        while (list.size() > MAX_MESSAGES) {
            list.removeFirst();
        }
    }
}
