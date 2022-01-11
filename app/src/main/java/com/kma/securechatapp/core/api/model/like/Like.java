package com.kma.securechatapp.core.api.model.like;

public class Like {
    public String _id;
    public String user_id;
    public String post_id;
    public String created_at;
    public String updated_at;
    public int __v;
    public String id;

    @Override
    public String toString() {
        return "Like{" +
                "_id='" + _id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", post_id='" + post_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", __v=" + __v +
                ", id='" + id + '\'' +
                '}';
    }
}
