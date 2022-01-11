package com.kma.securechatapp.ui.create_post;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.core.api.model.create_post.CreatePost;
import com.kma.securechatapp.core.api.model.create_post.PostCompelete;
import com.kma.securechatapp.core.api.model.upload_imgae.ImageUpload;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostViewModel extends ViewModel {
    private ApiService api;
    MutableLiveData<ImageUpload> imageUpload ;
    MutableLiveData<PostCompelete> postSucces ;
    private static final String TAG = "CreatePostViewModel";

    private String token ;
    CreatePostViewModel(){
     imageUpload   =new MutableLiveData();
        postSucces = new MutableLiveData();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        token = DataLocalManager.getToKen();
    }
    public MultipartBody.Part getPath(Uri uri){
        File file = new File(uri.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return  MultipartBody.Part.createFormData("images", file.getName(), reqFile);
    }
    public void uploadImage(ArrayList<MultipartBody.Part> list){
        api.uploadImage(Constant.PREFIX_TOKEN + token,list).enqueue(new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {
                if (response.body()==null || response.body().data== null){
                    imageUpload.postValue(null);
                }
                else {
                    imageUpload.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                imageUpload.postValue(null);
            }
        });
    }
    public ArrayList<String> getImageUpload(ImageUpload imageUpload){
        ArrayList<String> list =  new ArrayList<>();
        if (imageUpload==null){
            return null;
        }
        else{
            for (int i = 0; i <imageUpload.data.size() ; i++) {
                list.add(imageUpload.data.get(i).filename.toString());
            }

        }
        return list;
    }

    public void createPost(CreatePost createPost){
        api.creatPost(Constant.PREFIX_TOKEN + token,createPost).enqueue(new Callback<PostCompelete>() {
            @Override
            public void onResponse(Call<PostCompelete> call, Response<PostCompelete> response) {
                if (response.body()==null || response.body().data== null){
                    postSucces.postValue(null);
                    Log.d(TAG, "onResponse: null");
                }
                else {
                    postSucces.postValue(response.body());
                    Log.d(TAG, "onResponse: data");

                }
            }

            @Override
            public void onFailure(Call<PostCompelete> call, Throwable t) {
                postSucces.postValue(null);
                Log.d(TAG, "onFailure: null");
            }
        });
    }
}
