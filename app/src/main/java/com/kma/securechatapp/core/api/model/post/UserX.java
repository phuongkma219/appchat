package com.kma.securechatapp.core.api.model.post;

public class UserX {
    public int __v;
    public String _id;
    public String avatar;
    public String created_at;
    public String email;
    public String first_name;
    public String full_name;
    public String gender;
    public String id;
    public String last_name;
    public String updated_at;
    public String username;

    public UserX(int __v, String _id, String avatar, String created_at, String email, String first_name, String full_name, String gender, String id, String last_name, String updated_at, String username) {
        this.__v = __v;
        this._id = _id;
        this.avatar = avatar;
        this.created_at = created_at;
        this.email = email;
        this.first_name = first_name;
        this.full_name = full_name;
        this.gender = gender;
        this.id = id;
        this.last_name = last_name;
        this.updated_at = updated_at;
        this.username = username;
    }
    public UserX(int __v, String _id, String avatar, String created_at, String first_name, String full_name, String id, String last_name, String username) {
        this.__v = __v;
        this._id = _id;
        this.avatar = avatar;
        this.created_at = created_at;
        this.first_name = first_name;
        this.full_name = full_name;
        this.id = id;
        this.last_name = last_name;
        this.username = username;
    }
    public final static UserX emptyUser(){
        return new UserX(1,"sdfgh","a","a","a","a","a","a","a","a","a","a");
    }

}
