package com.kma.securechatapp.core.api.model.userprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataUser {
    @SerializedName("first_name")
    @Expose
    public String first_name;
    @SerializedName("last_name")
    @Expose
    public String last_name;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("auth")
    @Expose
    public Auth auth;
    @SerializedName("roles")
    @Expose
    public List roles;
    @SerializedName("avatar")
    @Expose
    public String avatar;
    @SerializedName("point")
    @Expose
    public Integer point;
    @SerializedName("_id")
    @Expose
    public String _id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
    @SerializedName("__v")
    @Expose
    public Integer __v;
    @SerializedName("full_name")
    @Expose
    public String full_name;
    @SerializedName("total_followers")
    @Expose
    public Integer total_followers;
    @SerializedName("total_followings")
    @Expose
    public Integer total_followings;
    @SerializedName("id")
    @Expose
    public String id;

    public DataUser() {
    }


    public DataUser(String first_name, String last_name, String gender) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
    }

    public DataUser(String first_name, String last_name, String gender, String address, Auth auth, List roles, String avatar, int point, String _id, String username, String email, String password, String created_at, String updated_at, int __v, String full_name, int total_followers, int total_followings, String id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.address = address;
        this.auth = auth;
        this.roles = roles;
        this.avatar = avatar;
        this.point = point;
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.__v = __v;
        this.full_name = full_name;
        this.total_followers = total_followers;
        this.total_followings = total_followings;
        this.id = id;
    }



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public List getRoles() {
        return roles;
    }

    public void setRoles(List roles) {
        this.roles = roles;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getTotal_followers() {
        return total_followers;
    }

    public void setTotal_followers(int total_followers) {
        this.total_followers = total_followers;
    }

    public int getTotal_followings() {
        return total_followings;
    }

    public void setTotal_followings(int total_followings) {
        this.total_followings = total_followings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
