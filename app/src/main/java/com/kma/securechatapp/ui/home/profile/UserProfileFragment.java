package com.kma.securechatapp.ui.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.databinding.FragmentProfileIdBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.ui.conversation.ConversationListFragment;
import com.kma.securechatapp.ui.conversation.InboxActivity;
import com.kma.securechatapp.ui.home.PostAdapter;
import com.kma.securechatapp.ui.post_details.PostDetailViewModel;
import com.kma.securechatapp.ui.profile.UserProfileViewModel;

import java.util.List;

public class UserProfileFragment extends BaseFragment<FragmentProfileIdBinding> implements View.OnClickListener {

    private boolean isLastPage = false;
    private int currentPage = 0;
    private UserPViewModel model;
    private PostAdapter postAdapter;
    private PostX post;
    private NavController navController;
    UserProfileViewModel userProfileViewModel;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile_id;
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        navController = NavHostFragment.findNavController(this);

        binding.btnBack.setOnClickListener(v -> {
            navController.popBackStack();
        });
        postAdapter = new PostAdapter();
        CommonHelper.showLoading(this.getContext());
        binding.rcPost.setLayoutManager(new LinearLayoutManager(this.getContext()));
        model = new UserPViewModel();
        if(getArguments()!=null){
             post = getArguments().getParcelable("profileDetail");
            model.getUserPost(post.user_id,Integer.toString(0));
            model.posts.observe(getViewLifecycleOwner(), new Observer<List<PostX>>() {
                @Override
                public void onChanged(List<PostX> postXES) {
                    postAdapter.setItems(postXES);
                    binding.rcPost.setAdapter(postAdapter);
                    CommonHelper.hideLoading();
                }
            });
        }


        model=new UserPViewModel();
        model.getDataById(post.user_id);
        model.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user.getData());
            }
        });

        model.getProfileById(post.user_id);
        model.dataProfile.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if(profile!=null){
                    binding.setProfile(profile.getData());
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
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("postDetail", postX);
                        navController.navigate(R.id.action_user_profile_to_postDetaillFragment, bundle);
                    }
                });

            }

            @Override
            public void onClickAvt(PostX item) {

            }
        };

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
    binding.btnMessage.setOnClickListener(this);
        userProfileViewModel =  ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileViewModel.trigerCheckHasContact(post.user_id);

        userProfileViewModel.getConversion().observe(this, conversation -> {
            Log.d("hihi", "onClick: ");
            if (conversation!= null) {
                Intent intent1 = new Intent(this.getActivity(), InboxActivity.class);
                intent1.putExtra("uuid", conversation.UUID);
                Log.d("hihi", "onClick: "+conversation.UUID);
                startActivity(intent1);
            }
        });
    }
    private void loadMore() {
        currentPage += 1;
        model.getUserPostPage(post.user_id,Integer.toString(currentPage));
        model.data.observe(getViewLifecycleOwner(), new Observer<Post>() {
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
        switch (v.getId()){
            case R.id.tvMore: {
                Bundle bundle = new Bundle();
                bundle.putString("more_profile", post.user_id);
                navController.navigate(R.id.action_user_profile_to_more_profile, bundle);
                break;
            }
            case R.id.btnMessage:{

                userProfileViewModel.trigerConversation(post.user_id);
                break;
            }

        }

    }
}
