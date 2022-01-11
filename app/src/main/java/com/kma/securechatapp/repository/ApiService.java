package com.kma.securechatapp.repository;

import com.kma.securechatapp.core.api.model.comment.Comment;
import com.kma.securechatapp.core.api.model.comment.PostComment;
import com.kma.securechatapp.core.api.model.create_post.CreatePost;
import com.kma.securechatapp.core.api.model.create_post.PostCompelete;
import com.kma.securechatapp.core.api.model.like.Like;
import com.kma.securechatapp.core.api.model.post.BaseApiRespone;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.upload_imgae.AvatarUpload;
import com.kma.securechatapp.core.api.model.upload_imgae.ImageUpload;
import com.kma.securechatapp.core.api.model.userprofile.DataUser;
import com.kma.securechatapp.core.api.model.userprofile.DataProfile;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.login.UserDTO;
import com.kma.securechatapp.core.api.model.userprofile.login.UserLogin;
import com.kma.securechatapp.core.api.model.userprofile.register.UserRegister;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/v1/posts")
    Call<Post> getData(@Header("Authorization") String token);

    @GET("/v1/posts")
    Call<Post> getPostsPage(@Header("Authorization") String token, @Query("page") String page);

    @GET("/v1/posts/{id}")
    Call<Post> getUserPosts(@Path("id") String id);

    @GET("/v1/posts/{id}")
    Call<Post> getUserPostsPage(@Path("id") String id,@Query("page") String page);

    @GET("/v1/posts/detail/{id}")
    Call<PostCompelete> getPostDetail(@Header("Authorization") String token, @Path("id") String id);

    @GET("/v1/posts/like/{id}")
    Call<Like> getLikePost(@Header("Authorization") String token, @Path("id") String id);

    @GET("/v1/posts/dislike/{id}")
    Call<Like> getDislikePost(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @POST("/v1/upload/images")
    Call<ImageUpload> uploadImage(@Header("Authorization") String token, @Part ArrayList<MultipartBody.Part> image);

    @POST("/v1/posts/upload")
    Call<PostCompelete> creatPost(@Header("Authorization") String token, @Body CreatePost createPost);

    @GET("/v1/comments/{id}")
    Call<Comment> getComments(@Header("Authorization") String token, @Path("id") String id);


    @POST("/v1/comments/upload")
    Call<Comment> postComment(@Header("Authorization") String token, @Body PostComment postComment);

    @GET("/v1/comments/{id}")
    Call<Comment> getCommentsPage(@Header("Authorization") String token,@Path("id") String id, @Query("page") String page);

    @POST("/auth/login")
    Call<UserLogin> login(@Body UserDTO user);

    @POST("/auth/register")
    Call<BaseApiRespone> userRegister(@Body UserRegister user);

    @GET("/v1/users/me")
    public Call<User> getUser(@Header("Authorization") String token);

    @GET("/v1/users/me/profile/")
    public Call<Profile> getProfile(@Header("Authorization") String token );

    @POST("/v1/tutors/update")
    public Call<Profile> updateProfile(@Header("Authorization") String token, @Body DataProfile dataProfile);

    @POST("/v1/users/me/update")
    public Call<User> updateUser(@Header("Authorization") String token, @Body DataUser dataUser);

    @GET("/v1/users/{id}")
    public Call<User> getProfileById(@Header("Authorization") String token,@Path("id")String userId );


    @GET("/v1/tutors/{id}")
    public Call<Profile> getMoreProfileById(@Header("Authorization") String token,@Path("id")String userId );

    @Multipart
    @POST("/v1/upload/avatar/{id}")
    public Call<AvatarUpload> uploadAvatar(@Header("Authorization") String token, @Path("id") String userId, @Part MultipartBody.Part image);


}
