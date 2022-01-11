package com.kma.securechatapp.core.api.model.userprofile.login;

public class UserLogin {
    public Boolean success;
    public String message;
    public DataLogin data;
    public Integer statusCode;

    public UserLogin(Boolean success, String message, DataLogin data, Integer statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}
