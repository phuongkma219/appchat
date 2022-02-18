package com.kma.securechatapp.ui.user_me;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.upload_imgae.AvatarUpload;
import com.kma.securechatapp.core.api.model.userprofile.DataUser;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {

    private ApiService api;
    public MutableLiveData<User> dataUser;
    public MutableLiveData<AvatarUpload> dataAvatar;
    private String token;

    public UserViewModel() {
        dataAvatar = new MutableLiveData<AvatarUpload>();
        dataUser = new MutableLiveData<>();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        token = DataLocalManager.getToKen();
    }

    public void getData() {
        api.getUser(Constant.PREFIX_TOKEN  + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null || response.body().getData() == null) {
                    dataUser.postValue(null);
                } else {
                    dataUser.postValue(response.body());
                    Log.d("data", String.valueOf(dataUser));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", "Fail: ${response.code()}");
            }
        });
    }

    public void updateDataUser(DataUser data) {
        api.updateUser(Constant.PREFIX_TOKEN  + token, data).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dataUser.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", "Fail: ${response.code()}");
            }
        });
    }

    public void uploadAvatar(String userId, MultipartBody.Part image) {
        api.uploadAvatar(Constant.PREFIX_TOKEN  + token, userId, image).enqueue(new Callback<AvatarUpload>() {
            @Override
            public void onResponse(Call<AvatarUpload> call, Response<AvatarUpload> response) {
                if (response.body() == null) {
                    dataAvatar.setValue(null);
                } else {
                    dataAvatar.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<AvatarUpload> call, Throwable t) {
                Log.d("Error", "Fail: ${response.code()}");
            }
        });
    }
}
