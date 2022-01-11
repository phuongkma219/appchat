package com.kma.securechatapp;

import androidx.lifecycle.MutableLiveData;

import com.kma.securechatapp.core.api.model.post.Data;
import com.kma.securechatapp.core.api.model.post.PostX;

import org.jetbrains.annotations.NotNull;

public final class SendData {
    private MutableLiveData<PostX> post = new MutableLiveData();
    private static final SendData mInstance = new SendData();

    @NotNull
    public final MutableLiveData<PostX> getLivePost() {
        return this.post;
    }
    @NotNull
    public final synchronized SendData getInstance() {
        return SendData.mInstance;
    }

}
