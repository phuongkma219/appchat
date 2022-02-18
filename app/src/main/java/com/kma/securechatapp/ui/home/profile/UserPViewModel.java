package com.kma.securechatapp.ui.home.profile;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.upload_imgae.AvatarUpload;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPViewModel extends ViewModel {
    public MutableLiveData<Post> data;
    public MutableLiveData<List<PostX>> posts;
    private ApiService api;
    public MutableLiveData<User> dataUser;
    public MutableLiveData<Profile> dataProfile;
    private MutableLiveData<AvatarUpload> dataAvatar;
    private static final String TAG = "UserProfileViewModel";
    private String token;

    public UserPViewModel() {
        dataAvatar = new MutableLiveData<AvatarUpload>();
        dataUser = new MutableLiveData<>();
        dataProfile = new MutableLiveData<>();
        posts = new MutableLiveData<>();
        data = new MutableLiveData<>();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        token = DataLocalManager.getToKen();
    }

    public void getUserPost(String id, String page) {
        api.getUserPostsPage(id, page).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.body() == null) {
                    posts.setValue(null);
                } else {
                    posts.setValue(response.body().data.posts);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("kkk", "onFailure: " + t.getMessage());
                posts.setValue(null);
            }
        });
    }

    public void getUserPostPage(String id, String page) {
        data = new MutableLiveData<>();
        api.getUserPostsPage(id, page).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.body() == null) {
                    data.setValue(null);
                } else {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                data.setValue(null);
            }
        });
    }

    public void uploadAvatar(String userId, MultipartBody.Part image) {
        api.uploadAvatar("Bearer " + token, userId, image).enqueue(new Callback<AvatarUpload>() {
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

    public void getDataById(String userId) {
        api.getProfileById(Constant.PREFIX_TOKEN  + token, userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
                    dataUser.postValue(null);
                } else {
                    dataUser.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Error", "Fail: ${response.code()}");
            }
        });
    }

    public void getProfileById(String userId) {
        api.getMoreProfileById(Constant.PREFIX_TOKEN  + token, userId).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.body() == null || response.body().getData() == null) {
                    dataProfile.postValue(null);
//                    Log.d("abc",""+ response.body().getData().toString());
                } else {
                    dataProfile.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d("Error", "Fail: ${response.code()}");
            }
        });
    }


}
