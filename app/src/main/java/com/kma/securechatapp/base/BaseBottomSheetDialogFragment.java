package com.kma.securechatapp.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseBottomSheetDialogFragment<T extends ViewDataBinding> extends BottomSheetDialogFragment {
    protected T binding;
    public BottomSheetBehavior<View> bottomSheetBehavior;

    public abstract int getLayoutId();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog  bottomsheet = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(requireActivity(), getLayoutId(), null);
        bottomsheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        setupFullHeight((View) view.getParent());
        bottomsheet.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        initBinding();
        return bottomsheet;

    }

    protected abstract void initBinding();

    private void setupFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }
}
