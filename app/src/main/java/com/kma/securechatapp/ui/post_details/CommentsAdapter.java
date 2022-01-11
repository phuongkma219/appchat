package com.kma.securechatapp.ui.post_details;

import androidx.annotation.NonNull;

import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseAdapter;
import com.kma.securechatapp.base.BaseViewHolder;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.databinding.ItemCommentBinding;

public class CommentsAdapter extends BaseAdapter<PostX> {
    public CommentsAdapter() {
        super(R.layout.item_comment);
    }


}
