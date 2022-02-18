package com.kma.securechatapp.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;

public class MySharePreferences {
    private String MY_SHARE_PREFERENCES ="MY_SHARE_PREFERENCES";
    private String KEY ="APP_CHAT";
    private Context mContext;
    public   MySharePreferences( Context context){
        mContext = context;
    }
    public void putData(DataLogin dataLogin){
        SharedPreferences sharePreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataLogin);
        editor.putString(KEY,json);
        editor.apply();
    }
    public DataLogin getData(){
        SharedPreferences sharePreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharePreferences.getString(KEY,null);
        return gson.fromJson(json,DataLogin.class);
    }
    public void clearSP(){
        SharedPreferences sharePreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreferences.edit();
        editor.clear();
        editor.apply();
    }
}
