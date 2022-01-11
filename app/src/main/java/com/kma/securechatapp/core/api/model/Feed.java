package com.kma.securechatapp.core.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Feed  implements Serializable {
    @SerializedName( "user" )
    public UserInfo user;
    @SerializedName( "content" )
    public FeedContent content;
  /*  @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;*/
}
