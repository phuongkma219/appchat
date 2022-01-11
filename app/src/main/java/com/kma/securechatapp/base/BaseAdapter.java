package com.kma.securechatapp.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> items = new ArrayList<>();
    private LayoutInflater inflater;
    private final int layout;


    public BaseListener listener;

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ViewDataBinding binding = onCreateBinding(inflater, layout, parent);
        return new BaseViewHolder(binding);
    }

    public BaseAdapter(int layout) {
        this.layout = layout;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.binding.setVariable(BR.item, items.get(position));
        holder.binding.setVariable(BR.itemPosition, position);
        holder.binding.setVariable(BR.itemListener, listener);
        holder.binding.setLifecycleOwner((LifecycleOwner) holder.binding.getRoot().getContext());
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    private ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        return DataBindingUtil.inflate(inflater, layoutId, viewGroup, false);
    }

    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    public void addItem(T item){
        this.items.add(item);
        notifyDataSetChanged();    }
    public void addItemFirst(T item){
        this.items.add(0,item);
        notifyDataSetChanged();

    }

    public List<T> getItems() {
        return items;
    }

}
