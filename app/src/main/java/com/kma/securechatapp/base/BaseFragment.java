package com.kma.securechatapp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.kma.securechatapp.MainActivity;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected T binding;
    protected LayoutInflater myInflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (myInflater == null) {
            myInflater = LayoutInflater.from(requireActivity());
        }
        binding = DataBindingUtil.inflate(myInflater, getLayoutId(), container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initBinding();
        return binding.getRoot();
    }

    protected void initBinding() {
    }
    //    protected MainActivity activityOwner = (MainActivity) requireActivity();
    public abstract int getLayoutId();
}
