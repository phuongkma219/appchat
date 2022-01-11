package com.kma.securechatapp.core.api.model.post;

import android.os.Parcel;
import android.os.Parcelable;

public class PostX implements Parcelable {
    public String _id;
    public Content content;
    public int total_like;
    public int total_comment;
    public String type;
    public String user_id;
    public String created_at;
    public String updated_at;
    public int __v;
    public Boolean isLike;
    public UserX user;

    public PostX(String _id, Content content, int total_like, int total_comment, String type, String user_id, String created_at, String updated_at, int __v, Boolean isLike, UserX user) {
        this._id = _id;
        this.content = content;
        this.total_like = total_like;
        this.total_comment = total_comment;
        this.type = type;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.__v = __v;
        this.isLike = isLike;
        this.user = user;
    }

    public PostX(String _id, Content content,String created_at,UserX userX) {
        this._id = _id;
        this.content = content;
        this.created_at = created_at;
        this.user = userX;
    }

    protected PostX(Parcel in) {
        _id = in.readString();
        total_like = in.readInt();
        total_comment = in.readInt();
        type = in.readString();
        user_id = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        __v = in.readInt();
        byte tmpIsLike = in.readByte();
        isLike = tmpIsLike == 0 ? null : tmpIsLike == 1;
    }

    public static final Creator<PostX> CREATOR = new Creator<PostX>() {
        @Override
        public PostX createFromParcel(Parcel in) {
            return new PostX(in);
        }

        @Override
        public PostX[] newArray(int size) {
            return new PostX[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public Content getContent() {
        return content;
    }

    public int getTotal_like() {
        return total_like;
    }

    public int getTotal_comment() {
        return total_comment;
    }

    public String getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int get__v() {
        return __v;
    }

    public Boolean getLike() {
        return isLike;
    }

    public UserX getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeInt(total_like);
        dest.writeInt(total_comment);
        dest.writeString(type);
        dest.writeString(user_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeInt(__v);
        dest.writeByte((byte) (isLike == null ? 0 : isLike ? 1 : 2));
    }

}
