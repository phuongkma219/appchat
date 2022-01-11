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

import com.kma.securechatapp.R;

import java.util.List;

public class BaseMultiItemAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> items;
    private LayoutInflater inflater;
    private final int layout;

    public BaseListener listener;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_LOADING = 2;
    private boolean isLoadingAdd;

    @Override
    public int getItemViewType(int position) {
        if (items != null && position == items.size() - 1 && isLoadingAdd){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        if (TYPE_ITEM ==viewType){
            ViewDataBinding binding = onCreateBinding(inflater, layout, parent);
            return new BaseViewHolder(binding);

        }
        else {
            ViewDataBinding binding = onCreateBinding(inflater, R.layout.item_loading, parent);
            return new BaseViewHolder(binding);
        }

    }

    public BaseMultiItemAdapter(int layout) {
        this.layout = layout;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
       if (TYPE_ITEM==holder.getItemViewType()){
           holder.binding.setVariable(BR.item, items.get(position));
           holder.binding.setVariable(BR.itemPosition, position);
           holder.binding.setVariable(BR.itemListener, listener);
           holder.binding.setLifecycleOwner((LifecycleOwner) holder.binding.getRoot().getContext());
           holder.binding.executePendingBindings();
       }
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
        this.items = items;
        this.notifyDataSetChanged();
    }

    public List<T> getItems() {
        return items;
    }

    public boolean isLoadingAdd() {
        return isLoadingAdd;
    }

    public void setLoadingAdd(boolean loadingAdd) {
        isLoadingAdd = loadingAdd;
    }
}
