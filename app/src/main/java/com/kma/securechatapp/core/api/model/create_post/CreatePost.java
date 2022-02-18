package com.kma.securechatapp.core.api.model.create_post;

import com.kma.securechatapp.core.api.model.post.Content;

public class CreatePost {
    Content content;
    String type = "POST";

    public CreatePost(Content content) {
        this.content = content;
        this.type = "POST";
    }


}
