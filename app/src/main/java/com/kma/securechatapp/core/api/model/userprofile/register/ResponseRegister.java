package com.kma.securechatapp.core.api.model.userprofile.register;

public class ResponseRegister {
    public Boolean success;
    public String message;
    public int statusCode;

    public ResponseRegister(Boolean success, String message, int statusCode) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
    }
}
