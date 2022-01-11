package com.kma.securechatapp.ui.create_post;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.create_post.CreatePost;
import com.kma.securechatapp.core.api.model.create_post.PostCompelete;
import com.kma.securechatapp.core.api.model.post.Content;
import com.kma.securechatapp.core.api.model.upload_imgae.ImageUpload;
import com.kma.securechatapp.core.api.model.upload_imgae.ItemImage;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.login.DataLogin;
import com.kma.securechatapp.databinding.FragmentCreatePostBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.ui.login.MySharePreferences;
import com.kma.securechatapp.ui.user_me.UserViewModel;
import com.kma.securechatapp.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MultipartBody;

public class CreatePostFragment extends BaseFragment<FragmentCreatePostBinding> implements View.OnClickListener {

    private ArrayList<MultipartBody.Part> images = new ArrayList<>();
    private CreatePostViewModel model;
    private ImagesAdapter2 adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_post;
    }

    @Override
    protected void initBinding() {
        CommonHelper.showLoading(this.getContext());
        model = new CreatePostViewModel();
//        binding.edtContent.requestFocus();
        adapter = new ImagesAdapter2();
        binding.rcImage.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.rcImage.setAdapter(adapter);
        onClickImage();
        initView();
    }

    private void initView() {
        binding.btnCreat.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
        binding.btnPhoto.setOnClickListener(this);
        UserViewModel userViewModel =  new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Utils.loadImage(binding.ivAvatar, user.dataUser.avatar);
                binding.tvName.setText(user.dataUser.full_name);
                CommonHelper.hideLoading();
            }
        });

        binding.edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.btnCreat.setEnabled(false);
                binding.btnCreat.setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    binding.btnCreat.setEnabled(false);
                    binding.btnCreat.setBackgroundResource(R.drawable.bg_shape_post);

                } else {
                    binding.btnCreat.setEnabled(true);
                    binding.btnCreat.setBackgroundResource(R.drawable.bg_shape_post_select);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_creat: {
                closeKeyboard();
                CommonHelper.showLoading(this.getContext());
                model.uploadImage(images);
                model.imageUpload.observe(getViewLifecycleOwner(), new Observer<ImageUpload>() {
                    @Override
                    public void onChanged(ImageUpload imageUpload) {
                        if (imageUpload == null) {
                            CreatePost createPost = new CreatePost(new Content(null, binding.edtContent.getText().toString()));
                            model.createPost(createPost);
                            model.postSucces.observe(getViewLifecycleOwner(), new Observer<PostCompelete>() {
                                @Override
                                public void onChanged(PostCompelete postCompelete) {
                                    if (postCompelete != null) {
                                        CommonHelper.hideLoading();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("post", postCompelete.data);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_dashboard, bundle);
                                    } else {
                                        CommonHelper.hideLoading();
                                        Toast.makeText(CreatePostFragment.this.getContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            CreatePost createPost = new CreatePost(new Content(model.getImageUpload(imageUpload), binding.edtContent.getText().toString()));
                            model.createPost(createPost);
                            model.postSucces.observe(getViewLifecycleOwner(), new Observer<PostCompelete>() {
                                @Override
                                public void onChanged(PostCompelete postCompelete) {
                                    if (postCompelete != null) {
                                        CommonHelper.hideLoading();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("post", postCompelete.data);
                                        Navigation.findNavController(binding.getRoot()).navigate(R.id.navigation_dashboard, bundle);
                                    } else {
                                        CommonHelper.hideLoading();
                                        Toast.makeText(CreatePostFragment.this.getContext(), "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });


                break;
            }

            case R.id.iv_close: {
                Navigation.findNavController(binding.getRoot()).popBackStack();
                break;
            }
            case R.id.btn_photo: {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker.with(requireActivity())
                                .setPeekHeight(1600)
                                .showTitle(false)
                                .setCompleteButtonText("Done")
                                .setEmptySelectionText("No Select")
                                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                                    @Override
                                    public void onImagesSelected(List<Uri> uriList) {
                                        for (int i = 0; i < uriList.size(); i++) {
                                            ItemImage image =new ItemImage(uriList.get(i).toString());
                                            adapter.addItem(image);
                                            images.add(model.getPath(uriList.get(i)));
                                        }

                                        if (images.size() > 0) {
                                            binding.btnCreat.setEnabled(true);
                                            binding.btnCreat.setBackgroundResource(R.drawable.bg_shape_post_select);
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(
                                requireActivity(),
                                "Permission Denied\n$deniedPermissions",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                };
                TedPermission.create()
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        )
                        .check();
                break;
            }


        }
    }
    private void onClickImage(){
        adapter.listener = new ImagesAdapter2.IMyImage(){
            @Override
            public void onItemRemove(ItemImage image, int position) {
                adapter.getItems().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
                images.remove(position);
            }
        };

    }

    private void closeKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
