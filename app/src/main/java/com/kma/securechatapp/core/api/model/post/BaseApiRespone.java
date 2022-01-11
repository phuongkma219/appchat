package com.kma.securechatapp.core.api.model.post;

public class BaseApiRespone<T> {
    public Boolean success;
    public String message;
    public T data;
    public int statusCode;

    public BaseApiRespone(Boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
    public BaseApiRespone(Boolean success, String message ,int statusCode) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }
}
