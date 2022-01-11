package com.kma.securechatapp.core.api.model.userprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User  {
    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public DataUser dataUser;
    @SerializedName("statusCode")
    @Expose
    public Integer statusCode;


    public User() {
    }

    public User(Boolean success, String message, DataUser dataUser, int statusCode) {
        this.success = success;
        this.message = message;
        this.dataUser = dataUser;
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataUser getData() {
        return dataUser;
    }

    public void setData(DataUser dataUser) {
        this.dataUser = dataUser;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
