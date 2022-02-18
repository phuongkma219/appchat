package com.kma.securechatapp.core.api.model;

import com.google.gson.annotations.SerializedName;

public class UserRegistRequest {
    @SerializedName("name")
    public String name;

    @SerializedName("address")
    public  String address ;

    @SerializedName("publickey")
    public  String publicKey ;

    @SerializedName( "dob")
    public Long dob ;

    @SerializedName( "username")
    public String username;

    @SerializedName( "password")
    public String password;

    @SerializedName( "phonenumber")
    public String phonenumber;

}
