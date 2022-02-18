package com.kma.securechatapp.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.SocialApiInterface;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.Feed;
import com.kma.securechatapp.core.api.model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    ApiInterface api = ApiUtil.getChatApi();
    SocialApiInterface socialApi = ApiUtil.getSocialApi();
    private MutableLiveData<List<UserInfo>> users;
    private long lastTime = 0;
    private MutableLiveData<List<Feed>> feeds;

    public DashboardViewModel() {
        users = new  MutableLiveData<List<UserInfo>>();
        feeds = new  MutableLiveData<List<Feed>>();
    }

    public void triggerLoadSuggest(){
        this.api.getSuggestList().enqueue(new Callback<ApiResponse<List<UserInfo>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<UserInfo>>> call, Response<ApiResponse<List<UserInfo>>> response) {

                if (response.body() == null){
                    users.setValue(null);
                    return;
                }
                users.setValue(response.body().data);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<UserInfo>>> call, Throwable t) {
                users.setValue(null);
            }
        });
    }

    public void trigerLoadFeeds(){
        this.socialApi.getNewsFeeds(lastTime).enqueue(new Callback<ApiResponse<List<Feed>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Feed>>> call, Response<ApiResponse<List<Feed>>> response) {
                if (response.body() == null){
                    feeds.setValue(null);
                    return;
                }

                feeds.setValue(response.body().data);
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Feed>>> call, Throwable t) {

            }
        });
    }
    public LiveData<List<Feed>> getFeeds() {
        return feeds;
    }
    public LiveData<List<UserInfo>> getSuggest(){
        return users;
    }

 }