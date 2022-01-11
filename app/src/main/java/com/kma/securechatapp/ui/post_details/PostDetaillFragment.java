package com.kma.securechatapp.ui.post_details;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.comment.PostComment;
import com.kma.securechatapp.core.api.model.post.Content;
import com.kma.securechatapp.core.api.model.post.Post;
import com.kma.securechatapp.core.api.model.post.PostX;
import com.kma.securechatapp.core.api.model.post.UserX;
import com.kma.securechatapp.core.api.model.upload_imgae.ItemImage;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.databinding.FragmentPostDetailBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.repository.DataLocalManager;
import com.kma.securechatapp.ui.home.HomeViewModel;
import com.kma.securechatapp.ui.login.MySharePreferences;
import com.kma.securechatapp.ui.user_me.UserViewModel;
import com.kma.securechatapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PostDetaillFragment extends BaseFragment<FragmentPostDetailBinding> implements View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_post_detail;
    }

    private PostDetailViewModel model;
    private ImageAdapter imageAdapter;
    private CommentsAdapter commentsAdapter;
    private PostX post;
    private Boolean isLike = false;
    private UserX userX;
    private NavController navController;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private Boolean isCheckI = false;

    @Override
    protected void initBinding() {
        super.initBinding();
        navController = NavHostFragment.findNavController(this);
        model = new PostDetailViewModel();
        imageAdapter = new ImageAdapter();
        CommonHelper.showLoading(this.getContext());
        binding.rvImage.setLayoutManager(new LinearLayoutManager(this.getContext()));
        List<ItemImage> images = new ArrayList<>();
        if (getArguments() != null) {
            post = getArguments().getParcelable("postDetail");
            binding.setItem(post);

        }
        userX = DataLocalManager.getUser();
        UserViewModel userViewModel = new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Utils.loadImage(binding.ivAvatar, user.dataUser.avatar);
                binding.tvName.setText(user.dataUser.full_name);
                CommonHelper.hideLoading();
            }
        });

        isLike = post.isLike;
        for (int i = 0; i < post.content.image.size(); i++) {
            images.add(new ItemImage(post.content.image.get(i)));
        }
        imageAdapter.setItems(images);
        binding.rvImage.setAdapter(imageAdapter);
        commentsAdapter = new CommentsAdapter();
        binding.rvCmt.setLayoutManager(new LinearLayoutManager(this.getContext()));
        init();
        binding.rvCmt.setAdapter(commentsAdapter);
        loadComment();

    }

    private void init() {
        binding.btnSend.setOnClickListener(this);
        binding.btnLike.setOnClickListener(this);
        binding.btnBefore.setOnClickListener(this);
    }

    private void loadComment() {
        model.getComments(post._id, Integer.toString(0));

        model.comments.observe(getViewLifecycleOwner(), new Observer<List<PostX>>() {
            @Override
            public void onChanged(List<PostX> postXES) {
                if (postXES != null) {
                    commentsAdapter.setItems(postXES);
                    CommonHelper.hideLoading();
                }
            }
        });
        final int[] visibleItemCount = {0};
        final int[] totalItemCount = {0};
        final int[] pastVisiblesItems = {0};
        binding.rvCmt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount[0] = binding.rvCmt.getChildCount();
                    totalItemCount[0] = binding.rvCmt.getLayoutManager().getItemCount();
                    pastVisiblesItems[0] = ((LinearLayoutManager) binding.rvCmt.getLayoutManager()).findFirstVisibleItemPosition();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_like: {

                if (isLike) {
                    binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_svgrepo_com, 0, 0, 0);
                    isLike = false;
                } else {
                    binding.btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_true, 0, 0, 0);
                    isLike = true;

                }
                HomeViewModel model = new HomeViewModel();
                if (isLike) {
                    if (post.total_like - 1 == 0) {
                        binding.tvLike.setText(Integer.toString(1));
                    } else if (post.total_like == 0) {
                        binding.tvLike.setText(Integer.toString(post.total_like + 1));

                    } else {
                        binding.tvLike.setText(Integer.toString(post.total_like));

                    }
                    model.getLikePost(post._id);
                    binding.tvLike.setVisibility(View.VISIBLE);
                } else {
                    model.getDislikePost(post._id);
                    if (post.total_like == 0) {
                        binding.tvLike.setText("0");
                        binding.tvLike.setVisibility(View.INVISIBLE);
                    } else if (post.total_like - 1 == 0) {
                        binding.tvLike.setVisibility(View.INVISIBLE);
                        binding.tvLike.setText("0");
                    } else if (post.total_like > 0) {
                        binding.tvLike.setVisibility(View.VISIBLE);
                        binding.tvLike.setText(Integer.toString(post.total_like - 1));
                    }
                }

                break;
            }
            case R.id.btn_before: {
                navController.navigate(R.id.navigation_dashboard);

                break;
            }
            case R.id.btn_send: {
                if (!binding.edtCmt.getText().toString().trim().equals("")) {
                    setComments(binding.edtCmt.getText().toString());
                } else {
                    Toast.makeText(getContext(), "Nhập nội dung để bình luận.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void setComments(String contentX) {
        PostComment postComment = new PostComment(new Content(contentX), post._id);
        model.postComment(postComment);
        PostX postX = new PostX(post._id, new Content(contentX), "vừa xong", userX);
        model.isCheckCmt.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (!binding.edtCmt.getText().toString().equals("")) {
                        commentsAdapter.addItemFirst(postX);
                        commentsAdapter.notifyItemInserted(commentsAdapter.getItems().size() - 1);
                        binding.tvComment.setText(Integer.toString(commentsAdapter.getItems().size()) + " bình luận");
                        binding.edtCmt.setText("");
                        closeKeyboard();
                    }

                } else {
                    Log.d("kkk", "onChanged: false");
                }
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void loadMore() {
        currentPage += 1;
        model.getCommentsPage(post._id, Integer.toString(currentPage));
        model.commentsPage.observe(getViewLifecycleOwner(), new Observer<List<PostX>>() {
            @Override
            public void onChanged(List<PostX> postXES) {
                isLastPage = false;
                if (postXES != null) {
                    Log.d("kkk", "onChanged: "+postXES.size());
                    for (int i = 0; i < postXES.size(); i++) {
                        commentsAdapter.addItem(postXES.get(i));
                    }
                    CommonHelper.hideLoading();
                    isCheckI = false;
                    isLastPage = false;

                } else {
                    isLastPage = true;
                    CommonHelper.hideLoading();
                    ((MainActivity) getActivity()).showStatus();
                    isCheckI = true;
                }
            }
        });

    }
}
