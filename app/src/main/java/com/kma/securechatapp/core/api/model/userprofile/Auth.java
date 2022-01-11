package com.kma.securechatapp.core.api.model.userprofile;


import com.kma.securechatapp.core.api.model.userprofile.auth.Email;
import com.kma.securechatapp.core.api.model.userprofile.auth.Phone;

public class Auth {

    public Email email;
    public Phone phone_number;

    public Auth() {
    }

    public Auth(Email email, Phone phone_number) {
        this.email = email;
        this.phone_number = phone_number;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Phone getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Phone phone_number) {
        this.phone_number = phone_number;
    }
}
