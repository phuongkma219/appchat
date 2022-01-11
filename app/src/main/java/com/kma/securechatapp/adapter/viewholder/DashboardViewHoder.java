package com.kma.securechatapp.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.R;
import com.kma.securechatapp.core.api.model.Feed;
import com.kma.securechatapp.utils.common.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardViewHoder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_avatar)
    ImageView avatar;
    @BindView(R.id.item_name)
    TextView name;
    @BindView(R.id.item_content)
    TextView content;
    public DashboardViewHoder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
    public void bind(Feed feed){

        if (feed.user != null) {
            ImageLoader.getInstance().DisplayImage(ImageLoader.getUserAvatarUrl(feed.user.uuid, 80, 80), avatar);
            name.setText(feed.user.name);

        } else {
            name.setText("");
            avatar.setImageResource(R.drawable.app_icon);
        }

        if (feed.content != null && feed.content.text != null){
            content.setText(feed.content.text);
        } else {
            content.setText("");
        }

    }

}
