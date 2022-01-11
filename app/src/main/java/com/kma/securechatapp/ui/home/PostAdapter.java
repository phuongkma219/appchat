package com.kma.securechatapp.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseAdapter;
import com.kma.securechatapp.base.BaseListener;
import com.kma.securechatapp.base.BaseViewHolder;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.databinding.ItemPostBinding;
import com.kma.securechatapp.ui.home.HomeViewModel;
import com.kma.securechatapp.ui.login.MySharePreferences;

public class PostAdapter extends BaseAdapter<PostX> {
    public PostAdapter() {
        super(R.layout.item_post);
    }

    public interface IPost extends BaseListener {
        void onItemClick(PostX item, Integer position);
        void onClickAvt(PostX item);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);
        ItemPostBinding item = (ItemPostBinding) holder.binding;
        item.tvContent.resetState(false);
        final Boolean[] isLike = {getItems().get(position).isLike};

        item.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLike[0]) {
                    item.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_svgrepo_com, 0, 0, 0);
                    isLike[0] = false;
                } else {
                    item.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_true, 0, 0, 0);
                    isLike[0] = true;

                }
                HomeViewModel model = new HomeViewModel();
                if (isLike[0]) {
                    if (getItems().get(position).total_like - 1 == 0) {
                        item.tvLike.setText(Integer.toString(1));
                    } else if(getItems().get(position).total_like == 0){
                        item.tvLike.setText(Integer.toString(getItems().get(position).total_like + 1));

                    }
                    else {
                        item.tvLike.setText(Integer.toString(getItems().get(position).total_like ));

                    }
                    model.getLikePost(getItems().get(position)._id);
                    item.tvLike.setVisibility(View.VISIBLE);
                } else {
                    model.getDislikePost(getItems().get(position)._id);
                    if (getItems().get(position).total_like == 0) {
                        item.tvLike.setText("0");
                        item.tvLike.setVisibility(View.INVISIBLE);
                    } else if (getItems().get(position).total_like - 1 == 0) {
                        item.tvLike.setVisibility(View.INVISIBLE);
                        item.tvLike.setText("0");
                    } else if (getItems().get(position).total_like > 0) {
                        item.tvLike.setVisibility(View.VISIBLE);
                        item.tvLike.setText(Integer.toString(getItems().get(position).total_like - 1));
                    }
                }
            }
        });

    }
}
