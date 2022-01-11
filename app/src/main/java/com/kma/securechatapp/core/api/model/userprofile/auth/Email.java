package com.kma.securechatapp.core.api.model.userprofile.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email {
    @SerializedName("verified")
    @Expose
    public Boolean verified;

    public Email() {
    }

    public Email(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
