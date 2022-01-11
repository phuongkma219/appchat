package com.kma.securechatapp.base;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    @NotNull
    public ViewDataBinding binding;


    public BaseViewHolder(@NotNull ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
