package com.example.AI_QA.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回封装类，统一前后端交互数据结构
 * @param <T> 数据泛型类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /** 业务状态码：0 表示成功，非 0 表示失败 */
    private Integer code;

    /** 提示信息 */
    private String msg;

    /** 返回的数据 */
    private T data;

    // ======================== 成功 ========================

    /** 成功：带返回数据 */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }

    /** 成功：不带数据 */
    public static Result<?> success() {
        return new Result<>(0, "操作成功", null);
    }

    // ======================== 失败 ========================

    /** 失败：仅返回消息 */
    public static <T> Result<T> error(String msg) {
        return new Result<>(1, msg, null);
    }

    /** 失败：自定义 code 和消息 */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
