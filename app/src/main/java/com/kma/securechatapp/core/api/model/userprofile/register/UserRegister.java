package com.kma.securechatapp.core.api.model.userprofile.register;

public class UserRegister {
    public String email;
    public String phone;
    public String username;
    public String first_name;
    public String password;

    public UserRegister(String email, String phone, String user_name, String first_name, String password) {
        this.email = email;
        this.phone = phone;
        this.username = user_name;
        this.first_name = first_name;
        this.password = password;
    }
}
