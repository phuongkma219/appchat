package com.kma.securechatapp.repository;

import android.content.Context;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.post.UserX;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.ui.login.MySharePreferences;

public class DataLocalManager {
    public static DataLocalManager instance;
    public MySharePreferences sharePreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.sharePreferences = new MySharePreferences(context);
    }
    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static DataLogin getData(){
          return DataLocalManager.getInstance().sharePreferences.getData();
    }
    public static UserX getUser(){
      if(DataLocalManager.getInstance().sharePreferences.getData() ==null){
          return UserX.emptyUser();
      }
      else {
          return DataLocalManager.getInstance().sharePreferences.getData().user;
      }
    }
    public static String getToKen(){
        if(DataLocalManager.getInstance().sharePreferences.getData() ==null){
            return Constant.TOKEN;
        }
        else {
           return DataLocalManager.getInstance().sharePreferences.getData().access_token;
        }
    }
    public static void setData(DataLogin dataLogin){
        DataLocalManager.getInstance().sharePreferences.putData(dataLogin);
    }
    public static void clearData(){
         DataLocalManager.getInstance().sharePreferences.clearSP();
    }
}
