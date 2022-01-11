package com.kma.securechatapp.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.userprofile.login.UserDTO;
import com.kma.securechatapp.core.api.model.userprofile.login.UserLogin;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginViewModel extends ViewModel {
    public MutableLiveData<UserLogin> user = new MutableLiveData<>();
    private ApiService api;
    private static final String TAG = "HomeViewModel";

    public LoginViewModel() {
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
    }

    public void loginUser(UserDTO userDTO){
        api.login(userDTO).enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                if (response.body()==null){
                    user.setValue(null);
                }
                else {
                    user.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                user.setValue(null);
            }
        });
    }

}
