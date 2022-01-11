package com.kma.securechatapp.core.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T>{


    @SerializedName("code")
    public int error;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public T data;
    @SerializedName("transaction_id")
    public String transactionId;

    @SerializedName("last")
    public String last;


    public ApiResponse(int error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }


}