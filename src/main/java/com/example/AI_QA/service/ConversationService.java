package com.example.AI_QA.service;

import com.example.AI_QA.dto.ChatMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Manage conversation history in HttpSession.
 */
@Service
public class ConversationService {
    private static final String SESSION_KEY = "CONVERSATIONS";
    private static final int MAX_MESSAGES = 20;

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

    public String start(HttpSession session) {
        String id = UUID.randomUUID().toString();
        getMap(session).put(id, new ArrayDeque<>());
        return id;
    }

    public List<ChatMessage> appendUserMessage(HttpSession session, String id, String content) {
        Deque<ChatMessage> list = getMap(session).computeIfAbsent(id, k -> new ArrayDeque<>());
        list.addLast(new ChatMessage("user", content));
        trim(list);
        return new ArrayList<>(list);
    }

    public void appendAssistantMessage(HttpSession session, String id, String content) {
        Deque<ChatMessage> list = getMap(session).computeIfAbsent(id, k -> new ArrayDeque<>());
        list.addLast(new ChatMessage("assistant", content));
        trim(list);
    }

    public List<ChatMessage> getHistory(HttpSession session, String id) {
        Deque<ChatMessage> list = getMap(session).get(id);
        if (list == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(list);
    }

    private void trim(Deque<ChatMessage> list) {
        while (list.size() > MAX_MESSAGES) {
            list.removeFirst();
        }
    }
}