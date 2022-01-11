package com.kma.securechatapp.core.api.model.userprofile.school;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighSchool {
    @SerializedName("name")
    @Expose
    public  String name;
    @SerializedName("date_graduation")
    @Expose
    public  String date_graduation;

    public HighSchool() {
    }

    public HighSchool(String name, String date_graduation) {
        this.name = name;
        this.date_graduation = date_graduation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_graduation() {
        return date_graduation;
    }

    public void setDate_graduation(String date_graduation) {
        this.date_graduation = date_graduation;
    }
}
