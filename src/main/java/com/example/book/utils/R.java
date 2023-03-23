package com.example.book.utils;

import lombok.Builder;
import lombok.Data;

/**
 * global response struct for json
 */
@Data
@Builder
public class R {

    private int code;

    private String message;

    private Object data;

    private Object err;

    public static R success() {
        return R.builder().code(1).build();
    }

    public static R success(String message) {
        return R.builder()
                .code(1)
                .message(message)
                .build();
    }

    public static R success(String message, Object data) {
        return R.builder()
                .code(1)
                .message(message)
                .data(data)
                .build();
    }

    public static R failed(String message) {
        return R.builder()
                .code(0)
                .message(message)
                .build();
    }

    public static R failed(String message, Exception e) {
        return R.builder()
                .code(0)
                .message(message)
                .err(e)
                .build();
    }
}
