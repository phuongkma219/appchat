package com.kma.securechatapp.core.api.model.comment;

import com.kma.securechatapp.core.api.model.post.PostX;

import java.util.List;

public class DataComment {
    public List<Object> data;
    public String next_page;
    public String prev_page;
    public List<PostX> comments;
}
