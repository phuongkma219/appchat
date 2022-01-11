package com.kma.securechatapp.core.api.model.userprofile;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kma.securechatapp.core.api.model.userprofile.school.HighSchool;
import com.kma.securechatapp.core.api.model.userprofile.school.University;

public class DataProfile {
    @SerializedName("hight_school")
    @Expose
    public HighSchool hight_school;
    @SerializedName("university")
    @Expose
    public University university;
    @SerializedName("_id")
    @Expose
    public String _id;
    @SerializedName("user_id")
    @Expose
    public  String user_id;
    @SerializedName("prize")
    @Expose
    public Object prize;
    @SerializedName("specialized")
    @Expose
    public  Object specialized;
    @SerializedName("skill")
    @Expose
    public  Object skill;
    @SerializedName("created_at")
    @Expose
    public  String created_at;
    @SerializedName("updated_at")
    @Expose
    public  String updated_at;
    @SerializedName("__v")
    @Expose
    public  Integer __v;

    public DataProfile(HighSchool hight_school, University university, String _id, String user_id, Object prize, Object specialized, Object skill, String created_at, String updated_at, Integer __v) {
        this.hight_school = hight_school;
        this.university = university;
        this._id = _id;
        this.user_id = user_id;
        this.prize = prize;
        this.specialized = specialized;
        this.skill = skill;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.__v = __v;
    }

    public DataProfile() {
    }

    public DataProfile(HighSchool hight_school, University university) {
        this.hight_school = hight_school;
        this.university = university;
    }

    public HighSchool getHight_school() {
        return hight_school;
    }

    public void setHight_school(HighSchool hight_school) {
        this.hight_school = hight_school;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Object getPrize() {
        return prize;
    }

    public void setPrize(Object prize) {
        this.prize = prize;
    }

    public Object getSpecialized() {
        return specialized;
    }

    public void setSpecialized(Object specialized) {
        this.specialized = specialized;
    }

    public Object getSkill() {
        return skill;
    }

    public void setSkill(Object skill) {
        this.skill = skill;
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

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }
}
