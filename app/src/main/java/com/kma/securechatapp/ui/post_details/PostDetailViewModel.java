package com.kma.securechatapp.ui.post_details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.comment.Comment;
import com.kma.securechatapp.core.api.model.comment.PostComment;
import com.kma.securechatapp.core.api.model.create_post.PostCompelete;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailViewModel extends ViewModel {
    public MutableLiveData<List<PostX>> comments;
    public MutableLiveData<List<PostX>> commentsPage;
    public MutableLiveData<Boolean> isCheckCmt;
    public MutableLiveData<PostX> post;
    private ApiService api;
    private String token ;
    private static final String TAG = "PostDetailViewModel";

    public PostDetailViewModel() {
        comments = new MutableLiveData<>();
        commentsPage= new MutableLiveData<>();
        isCheckCmt = new MutableLiveData<>();
        post = new MutableLiveData<>();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        token = DataLocalManager.getToKen();

    }
    public void getPostDetail(String id){
        api.getPostDetail(Constant.PREFIX_TOKEN + token ,id).enqueue(new Callback<PostCompelete>() {
            @Override
            public void onResponse(Call<PostCompelete> call, Response<PostCompelete> response) {
                if (response.body()==null){
                    post.setValue(null);
                }
                else{
                    post.setValue(response.body().data);
                }
            }

            @Override
            public void onFailure(Call<PostCompelete> call, Throwable t) {
                post.setValue(null);
            }
        });
    }
    public void getComments(String id,String page){
        api.getCommentsPage(Constant.PREFIX_TOKEN + token,id,page).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.body() ==null){
                    comments.setValue(null);
                }
                else {
                    comments.setValue(response.body().data.comments);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                comments.setValue(null);
            }
        });
    }
    public void getCommentsPage(String id,String page){
        commentsPage = new MutableLiveData<>();
        api.getCommentsPage(Constant.PREFIX_TOKEN + token,id,page).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.body() ==null){
                    commentsPage.setValue(null);
                }
                else {
                    commentsPage.setValue(response.body().data.comments);
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                commentsPage.setValue(null);
            }
        });
    }

    public void postComment(PostComment comment){
        api.postComment(Constant.PREFIX_TOKEN +token,comment).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.body().message.equals("COMMENT.CREATE.SUCCESS")){
                    isCheckCmt.setValue(true);
                }
                else {
                    isCheckCmt.setValue(false);
                }

            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                isCheckCmt.setValue(false);
                Log.d(TAG, "onResponse: deo vao day");
            }
        });
    }
}
