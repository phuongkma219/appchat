package com.kma.securechatapp.core.api.model.comment;

import com.kma.securechatapp.core.api.model.post.Content;

public class PostComment {
    public Content content;
    public String post_id;

    public PostComment(Content content, String post_id) {
        this.content = content;
        this.post_id = post_id;
    }
}
