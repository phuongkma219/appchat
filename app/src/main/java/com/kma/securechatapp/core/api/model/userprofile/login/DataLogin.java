package com.kma.securechatapp.core.api.model.userprofile.login;

import com.kma.securechatapp.core.api.model.post.UserX;

public class DataLogin {
    public String access_token;
    public int expires_in;
    public UserX user;

    public DataLogin(String access_token, int expires_in, UserX user) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.user = user;
    }

    public final static DataLogin emptyData(){
        return new DataLogin("sdfghjkl",1,UserX.emptyUser());
    }
}
