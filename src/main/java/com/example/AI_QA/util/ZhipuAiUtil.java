package com.example.AI_QA.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.AI_QA.dto.ChatMessage;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 智谱 AI 工具类，封装调用大语言模型的 HTTP 请求逻辑。
 *
 * 支持带上下文的多轮对话调用。使用 OkHttp + Fastjson 实现请求构建与响应解析。
 */
@Component
public class ZhipuAiUtil {

    /**
     * 智谱大模型 API Key，从 application.yml 中读取。
     * 示例配置：zhipu.api-key: Bearer xxx.yyy.zzz
     */
    @Value("${zhipu.api-key}")
    private String apiKey;

    /**
     * 智谱 GLM 模型 API 地址（可根据文档替换为其他模型地址）
     */
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    /**
     * 调用智谱 AI，进行单轮对话。
     * @param userQuestion 用户问题（如："什么是Java？"）
     * @return AI 回复文本
     * @throws IOException 网络异常或接口调用失败
     */
    public String chat(String userQuestion) throws IOException {
        return chat(List.of(new ChatMessage("user", userQuestion)));
    }

    /**
     * 调用智谱 AI，进行多轮对话（支持上下文）。
     * @param messages 聊天记录（包括 user / assistant 角色）
     * @return AI 返回的最新回复内容
     * @throws IOException 网络异常或接口调用失败
     */
    public String chat(List<ChatMessage> messages) throws IOException {
        // 1. 构造请求客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        // 2. 构建请求 JSON 体
        JSONObject payload = new JSONObject();
        payload.put("model", "glm-4-air-250414"); // 指定模型名称

        // 构造 messages 数组
        JSONArray msgArray = new JSONArray();
        for (ChatMessage m : messages) {
            JSONObject obj = new JSONObject();
            obj.put("role", m.getRole());       // 角色（user / assistant / system）
            obj.put("content", m.getContent()); // 消息内容
            msgArray.add(obj);
        }
        payload.put("messages", msgArray); // 加入到主请求体

        // 3. 构建 POST 请求体
        RequestBody body = RequestBody.create(
                payload.toJSONString(), // 将 JSON 对象序列化为字符串
                MediaType.get("application/json")
        );

        // 4. 构建 HTTP 请求
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", apiKey) // Bearer xxx 形式
                .post(body)
                .build();

        // 5. 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("智谱 API 调用失败: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();     // 响应 JSON 字符串
            JSONObject resJson = JSON.parseObject(responseBody); // 解析为 JSONObject

            // 提取返回的第一条 message 内容（choices -> message -> content）
            JSONArray choices = resJson.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");

            return message.getString("content"); // 最终 AI 回复文本
        }
    }

    /**
     * 使用大模型对给定文本进行总结，返回简短概括。
     */
    public String summarize(String text) throws IOException {
        String prompt = "请用中文简要概括以下内容，不超过80字：\n" + text;
        return chat(List.of(new ChatMessage("user", prompt)));
    }
}
