package com.kma.securechatapp.core.api;

import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.AuthenRequest;
import com.kma.securechatapp.core.api.model.AuthenResponse;
import com.kma.securechatapp.core.api.model.Feed;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SocialApiInterface {

    @POST("/post_feed")
    Call<ApiResponse<Feed>> postFeeds(Feed feed);

    @GET("/my_feeds")
    Call<ApiResponse<List<Feed>>> getMyFeeds(@Query("last") long last);

    @GET("/user/feeds")
    Call<ApiResponse<List<Feed>>> getNewsFeeds(@Query("last") long last);

}
