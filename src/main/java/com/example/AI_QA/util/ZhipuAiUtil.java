package com.example.AI_QA.util;

import okhttp3.*; // 引入 OkHttp 发送 HTTP 请求
import com.alibaba.fastjson.JSONArray;   // 引入 Fastjson 的 JSONArray 类
import com.alibaba.fastjson.JSONObject;  // 引入 Fastjson 的 JSONObject 类
import com.alibaba.fastjson.JSON;        // Fastjson 的 JSON 工具类

import java.io.IOException;

public class ZhipuAiUtil {

    // 替换为你自己的智谱 API Token
    private static final String API_KEY = "1efd0879c8b84cc69b94a708870ff16b.jcFo2QTRwx6N0Nrh";

    // 智谱大模型的调用接口地址
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    /**
     * 向智谱大模型发送用户提问并返回 AI 回复内容
     *
     * @param userQuestion 用户输入的问题
     * @return AI 返回的回答文本
     * @throws IOException 接口调用失败时抛出异常
     */
    public static String chat(String userQuestion) throws IOException {
        // 创建 OkHttp 客户端
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)   // 连接超时
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)      // 读取超时（模型响应慢时用）
                .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)     // 写入超时
                .build();

        // 创建请求参数 JSON 对象
        JSONObject json = new JSONObject();
        json.put("model", "glm-4-air-250414"); // 指定使用的模型，可换成 "chatglm_turbo"

        // 构建 messages 数组，格式为：[{ "role": "user", "content": "..." }]
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");           // 角色为用户
        userMessage.put("content", userQuestion);  // 用户的问题内容
        messages.add(userMessage);                 // 添加到数组中

        // 将 messages 加入总请求体
        json.put("messages", messages);

        // 构建 POST 请求体，设置为 application/json
        RequestBody body = RequestBody.create(
                json.toJSONString(),                           // Fastjson 序列化为字符串
                MediaType.get("application/json")              // 设置 MIME 类型
        );

        // 构建完整的 HTTP 请求
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", API_KEY) // 添加鉴权 header
                .post(body)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("调用失败: " + response.code() + " " + response.message());
            }

            // 获取响应体字符串
            String responseBody = response.body().string();

            // 解析 JSON 字符串为 JSONObject（Fastjson 解析）
            JSONObject resJson = JSON.parseObject(responseBody);

            // 提取返回的 message 内容
            JSONArray choices = resJson.getJSONArray("choices");           // 获取 choices 数组
            JSONObject message = choices.getJSONObject(0)                  // 获取第一个 choice
                    .getJSONObject("message");                             // 获取 message 对象

            return message.getString("content");                           // 返回 content 字段
        }
    }
}
