package com.kma.securechatapp.core.api.model.upload_imgae;

public class AvatarUpload {
    public String success;
    public String message;
    public Data data;
    public Integer statuscode;

    public AvatarUpload(String success, String message, Data data, Integer statuscode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statuscode = statuscode;
    }

    public AvatarUpload() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }
}

