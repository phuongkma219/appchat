package com.kma.securechatapp.ui.home.profile;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.databinding.FragmentMoreProfileByIdBinding;
import com.kma.securechatapp.helper.CommonHelper;

import java.util.List;

public class MoreProfileFragment extends BaseFragment<FragmentMoreProfileByIdBinding> {


    private UserProfileViewModel model;
    private String id;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more_profile_by_id;
    }
    private NavController navController;


    @Override
    public void initBinding() {
        super.initBinding();
        navController = NavHostFragment.findNavController(this);
        if (getArguments() != null) {
            id = getArguments().getString("more_profile");

        }

        binding.imgBack.setOnClickListener(view -> {
            navController.popBackStack();
        });


        model = new UserProfileViewModel();
        model.getDataById(id);
        model.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user.getData());
            }
        });

        model.getProfileById(id);
        model.dataProfile.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                binding.setProfile(profile.getData());
            }
        });
    }
}


