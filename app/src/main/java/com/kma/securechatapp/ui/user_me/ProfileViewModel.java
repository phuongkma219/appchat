package com.kma.securechatapp.ui.user_me;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.userprofile.DataProfile;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private ApiService api;
    public  MutableLiveData<Profile> dataUserProfile;
    String token;

    public ProfileViewModel() {
        dataUserProfile=new MutableLiveData<>();
        api= (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL,ApiService.class);
        token = DataLocalManager.getToKen();
    }
    public void getProfile(){
        api.getProfile(Constant.PREFIX_TOKEN +token).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.body()==null || response.body().getData()==null){
                    dataUserProfile.postValue(null);
                    Log.d("abc","vao day1");
                }else {
                    dataUserProfile.postValue(response.body());
                    Log.d("abc","vao day2");

                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dataUserProfile.postValue(null);
                Log.d("abc","vao day3");

            }
        });
    }

    public void updateDataProfile(DataProfile dataProfile){
        api.updateProfile(Constant.PREFIX_TOKEN +token,dataProfile).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dataUserProfile.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d("Error","Fail: ${response.code()}");
            }
        });
    }

}

