package com.kma.securechatapp.core.api.model;

import android.hardware.biometrics.BiometricPrompt;

import com.google.gson.annotations.SerializedName;

public class AuthenRequest{


    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;

    @SerializedName("token")
    public String token;

    @SerializedName("device")
    public Device device;
    @SerializedName("transactionId")
    public String transactionId;

    public AuthenRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public AuthenRequest(String username){
        this.username = username;
    }

}