package com.kma.securechatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.R;
import com.kma.securechatapp.adapter.viewholder.DashboardViewHoder;
import com.kma.securechatapp.adapter.viewholder.SuggestViewHolder;
import com.kma.securechatapp.core.api.model.Feed;

import java.util.List;

public class DashboardAdapter extends   RecyclerView.Adapter {
    public List<Feed> feeds;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);
        return new DashboardViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DashboardViewHoder)holder).bind(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        if (feeds == null){
            return 0;
        }
        return feeds.size();
    }

    public void setFeeds(List<Feed> feeds){
        this.feeds = feeds;
        this.notifyDataSetChanged();
    }
}
