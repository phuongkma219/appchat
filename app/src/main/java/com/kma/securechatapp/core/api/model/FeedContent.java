package com.kma.securechatapp.core.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedContent  implements Serializable {
    @SerializedName( "text" )
    public String text;
    @SerializedName( "images" )
    public String[]  images;
    @SerializedName( "videos" )
    public String  videos;
}
