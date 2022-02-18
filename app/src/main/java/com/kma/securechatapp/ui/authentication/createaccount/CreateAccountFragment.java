package com.kma.securechatapp.ui.authentication.createaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kma.securechatapp.R;


public class CreateAccountFragment extends Fragment {

    FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_account, container, false);
        fm = getFragmentManager();
        return root;
    }


}
