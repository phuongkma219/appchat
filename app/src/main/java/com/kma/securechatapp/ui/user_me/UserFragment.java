package com.kma.securechatapp.ui.user_me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.post.UserX;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.databinding.FragmentUserBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.repository.DataLocalManager;
import com.kma.securechatapp.ui.home.PostAdapter;
import com.kma.securechatapp.ui.home.profile.UserProfileViewModel;
import com.kma.securechatapp.ui.login.MySharePreferences;

import java.util.List;


public class UserFragment extends BaseFragment<FragmentUserBinding> implements View.OnClickListener {


    private UserViewModel userViewModel;
    private ProfileViewModel profileViewModel;
    private UserProfileViewModel userProfileViewModel;
    private NavController navController;
    private UserX user;
    private  PostAdapter postAdapter;
    private int currentPage = 0;
    private Boolean isCheckI = false;
    private boolean isLastPage = false;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void initBinding() {
        super.initBinding();
        CommonHelper.showLoading(this.getContext());
        navController = NavHostFragment.findNavController(this);
        user = DataLocalManager.getUser();
        binding.btnBack.setOnClickListener(v -> {
            navController.popBackStack();
        });
        userViewModel = new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user.getData());
                Log.d("link", user.getData().getAvatar());
            }
        });

        profileViewModel = new ProfileViewModel();
        profileViewModel.getProfile();
        profileViewModel.dataUserProfile.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if(profile!=null){
                    binding.setProfile(profile.getData());
                    isCheckI = false;
                }
                else {
                  binding.txtName.setText(user.full_name);
                  isCheckI = true;
                }

            }
        });
        userProfileViewModel = new UserProfileViewModel();
        postAdapter = new PostAdapter();
        binding.rcPost.setLayoutManager(new LinearLayoutManager(this.getContext()));
        Log.d("kkk", "initBinding: "+ user._id);
        userProfileViewModel.getUserPost(user._id,Integer.toString(0));
        userProfileViewModel.posts.observe(getViewLifecycleOwner(), new Observer<List<PostX>>() {
            @Override
            public void onChanged(List<PostX> postXES) {
                if(postXES!=null){
                    postAdapter.setItems(postXES);
                    binding.rcPost.setAdapter(postAdapter);
                }
                CommonHelper.hideLoading();

            }
        });

        final int[] visibleItemCount = {0};
        final int[] totalItemCount = {0};
        final int[] pastVisiblesItems = {0};
        binding.rcPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount[0] = binding.rcPost.getChildCount();
                    totalItemCount[0] = binding.rcPost.getLayoutManager().getItemCount();
                    pastVisiblesItems[0] = ((LinearLayoutManager) binding.rcPost.getLayoutManager()).findFirstVisibleItemPosition();
                    if (isLastPage) {
                        return;
                    } else {
                        if (visibleItemCount[0] + pastVisiblesItems[0] >= totalItemCount[0]) {
                            loadMore();
                        }
                    }
                }
            }
        });
        binding.tvMore.setOnClickListener(this);
        binding.btnCreat.setOnClickListener(this);

    }
    private void loadMore(){
        currentPage += 1;
        userProfileViewModel.getUserPostPage(user._id,Integer.toString(currentPage));
        userProfileViewModel.data.observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                if (post.data.posts.size() == 0) {
                    isLastPage = true;
                } else {
                    isLastPage = false;
                    for (int i = 0; i < post.data.posts.size(); i++) {
                        postAdapter.addItem(post.data.posts.get(i));
                    }
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMore: {
//                Bundle bundle=new Bundle();
//                bundle.putString("userId",);
                navController.navigate(R.id.action_user_fragment_to_profile_fragment);
                Log.d("kkk", "onClick: tvMore");

                break;
            }
            case R.id.btn_creat:{
                navController.navigate(R.id.action_user_fragment_to_createPostFragment);
                Log.d("kkk", "onClick: btn_creat");

                break;
            }

        }
    }
}
