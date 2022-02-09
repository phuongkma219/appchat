package com.kma.securechatapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.post.UserX;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.databinding.FragmentHomeBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.repository.DataLocalManager;
import com.kma.securechatapp.ui.login.LoginFragment;
import com.kma.securechatapp.ui.login.MySharePreferences;
import com.kma.securechatapp.ui.post_details.PostDetailViewModel;
import com.kma.securechatapp.ui.user_me.UserViewModel;
import com.kma.securechatapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener {
    private boolean isLastPage = false;
    private int currentPage = 0;
    private HomeViewModel model;
    private PostAdapter postAdapter;
    private NavController navController;
    private Boolean isCheckI = false;
    private PostX post;
    private UserX userX;
    private List<PostX> posts = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        CommonHelper.showLoading(this.getContext());
        navController = NavHostFragment.findNavController(this);
        postAdapter = new PostAdapter();
        binding.rcPost.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rcPost.setAnimation(null);
        model = new HomeViewModel();
        userX = DataLocalManager.getUser();
        model.getPostFirst(Integer.toString(currentPage));
        if (getArguments() != null) {
            post = getArguments().getParcelable("post");
        }
        model.posts.observe(getViewLifecycleOwner(), new Observer<List<PostX>>() {
            @Override
            public void onChanged(List<PostX> postXES) {
                posts = new ArrayList<>();
                if (postXES != null) {
                    ((MainActivity) getActivity()).hideStatus();
                    for (int i = 0; i < postXES.size(); i++) {
                        if (post != null) {
                            if (post._id.equals(postXES.get(i)._id)) {
                                posts.add(0, postXES.get(i));
                            } else {
                                posts.add(postXES.get(i));
                            }
                        } else {
                            posts.add(postXES.get(i));
                        }
                    }
                    postAdapter.setItems(posts);
                    CommonHelper.hideLoading();
                    isCheckI = false;
                } else {
                    ((MainActivity) getActivity()).showStatus();
                    CommonHelper.hideLoading();
                    isCheckI = true;
                }
            }
        });
        binding.ivAvatar.setOnClickListener(this);
        binding.tvCreatPost.setOnClickListener(this);
        UserViewModel userViewModel =  new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user!=null){
                    Utils.loadImage(binding.ivAvatar, user.dataUser.avatar);

                }
                else {
//                    userX = DataLocalManager.getUser();
//                    Utils.loadImage(binding.ivAvatar, userX.avatar);
                }

            }
        });
        postAdapter.listener = new PostAdapter.IPost() {

            @Override
            public void onItemClick(PostX item, Integer position) {
                PostDetailViewModel model = new PostDetailViewModel();
                model.getPostDetail(item._id);
                model.post.observe(getViewLifecycleOwner(), new Observer<PostX>() {
                    @Override
                    public void onChanged(PostX postX) {
                        if (postX != null) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("postDetail", postX);
                            navController.navigate(R.id.action_navigation_dashboard_to_postDetaillFragment, bundle);
                            posts.clear();
                        }
                    }
                });

            }

            @Override
            public void onClickAvt(PostX item) {
                if (!isCheckI) {
                    if(item.user_id.equals(userX.id)){
                        navController.navigate(R.id.action_navigation_dashboard_to_user_fragment2);
                    }
                    else {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("profileDetail", item);
                        navController.navigate(R.id.action_navigation_dashboard_to_user_profile, bundle);
                    }

                }
            }
        };

        binding.rcPost.setAdapter(postAdapter);
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

    }

    private void loadMore() {
        currentPage += 1;
        model.getPostsPage(Integer.toString(currentPage));
        model.data.observe(getViewLifecycleOwner(), new Observer<Post>() {
            @Override
            public void onChanged(Post post) {
                isLastPage = false;
                if (post != null) {
                    if (post.data.posts.size() == 0) {
                        isLastPage = true;
                    } else {
                        binding.tvError.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).hideStatus();
                        for (int i = 0; i < post.data.posts.size(); i++) {
                            postAdapter.addItem(post.data.posts.get(i));
                        }
                        CommonHelper.hideLoading();
                        isCheckI = false;
                    }
                } else {
                    CommonHelper.hideLoading();
                    binding.tvError.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).showStatus();
                    isCheckI = true;
                }


            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_creat_post: {
                if (!isCheckI) {
                    navController.navigate(R.id.action_navigation_dashboard_to_createPostFragment);
                }
                break;
            }
            case R.id.iv_avatar: {
                navController.navigate(R.id.action_navigation_dashboard_to_user_fragment2);
                break;
            }
        }
    }


}
