package com.example.AI_QA.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(true, "成功", data);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(false, msg, null);
    }
}

