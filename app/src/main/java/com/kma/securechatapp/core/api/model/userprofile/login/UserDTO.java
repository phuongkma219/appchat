package com.kma.securechatapp.core.api.model.userprofile.login;

public class UserDTO {
   public String username;
    public String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
