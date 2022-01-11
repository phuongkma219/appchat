package com.kma.securechatapp.ui.user_me;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.databinding.FragmentProfileBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.repository.DataLocalManager;


import java.io.File;
import java.io.IOException;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> implements View.OnClickListener {

    private UserViewModel userViewModel;
    private ProfileViewModel profileViewModel;
    public String userId ="";
    private NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initBinding() {
        super.initBinding();
        CommonHelper.showLoading(this.getContext());
        navController = NavHostFragment.findNavController(this);
        userId = DataLocalManager.getUser()._id;
        binding.tvUpdateProfile.setOnClickListener(this);

        binding.tvUpdateAvt.setOnClickListener(view -> {
            checkPermission();
        });

        binding.imgBack.setOnClickListener(view -> {
            navController.popBackStack();
        });

        userViewModel = new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.setUser(user.getData());
            }
        });

        profileViewModel = new ProfileViewModel();
        profileViewModel.getProfile();
        profileViewModel.dataUserProfile.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
//                    binding.tvHighSchool.setText(profile.getData().getHight_school().getName());
//                    binding.tvUniversity.setText(profile.getData().getUniversity().getName());
                if(profile!=null){
                    binding.setProfile(profile.getData());

                }
                CommonHelper.hideLoading();
            }
        });
    }


    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker.with(getActivity()).show(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setTitle("Thay Avatar");
                        builder.setMessage("Bạn Có Muốn Xác Nhận Thay Avatar?");
                        builder.setNegativeButton("No", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        });
                        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            binding.imgAvt.setImageBitmap(bitmap);
                            MultipartBody.Part multipartBody = getPath(uri);
                            userViewModel.uploadAvatar(userId, multipartBody);
                            AlertDialog.Builder builderSuccess = new AlertDialog.Builder(requireContext());
                            builderSuccess.setTitle("Thay Avatar");
                            builderSuccess.setMessage("Thay Avatar Thành Công");
                            builderSuccess.show();
                        });
                        builder.show();
                    }
                });


            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    public MultipartBody.Part getPath(Uri uri) {
        File file = new File(uri.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvUpdateProfile:{
                navController.navigate(R.id.action_profile_fragment_to_update_profile_fragment);
                break;
            }
        }

    }
}
