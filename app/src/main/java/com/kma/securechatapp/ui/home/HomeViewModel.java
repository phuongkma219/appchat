package com.kma.securechatapp.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.like.Like;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    public MutableLiveData<List<PostX>> posts;
    public MutableLiveData<Post> data = new MutableLiveData<>();
    private ApiService api;
    private static final String TAG = "HomeViewModel";
    private String token ;

    public HomeViewModel() {
        posts = new MutableLiveData<>();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        token = DataLocalManager.getToKen();
    }

//    public void getData() {
//        api.getData(Constant.PREFIX_TOKEN + token).enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                if (response.body() == null || response.body().data == null) {
//                    posts.postValue(null);
//                } else {
//                    posts.postValue(response.body().data.posts);
//                    Log.d(TAG, "onResponse: ");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                posts.postValue(null);
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//    }

    public void getPostFirst(String page) {
        api.getPostsPage(Constant.PREFIX_TOKEN  + token, page).enqueue(new Callback<Post>() {
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
                posts.postValue(null);
            }
        });
    }

    public void getPostsPage(String page) {
        data = new MutableLiveData<>();
        api.getPostsPage(Constant.PREFIX_TOKEN + token, page).enqueue(new Callback<Post>() {
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

    public void getLikePost(String id) {
        api.getLikePost(Constant.PREFIX_TOKEN + token, id).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.body() == null) {
                    Log.d(TAG, "onResponse: NULL");
                } else {
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    public void getDislikePost(String id) {
        api.getDislikePost(Constant.PREFIX_TOKEN  + token, id).enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                if (response.body() == null) {
                    Log.d(TAG, "onResponse: NULL");
                } else {
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}
