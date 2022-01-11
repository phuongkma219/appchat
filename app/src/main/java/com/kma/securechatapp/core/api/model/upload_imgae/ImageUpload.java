package com.kma.securechatapp.core.api.model.upload_imgae;

import java.util.List;

public class ImageUpload {
    public Boolean success;
    public String message;
    public List<Data> data;
    public int statusCode;


    @Override
    public String toString() {
        return "ImageUpload{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", statusCode=" + statusCode +
                '}';
    }
}
