package com.kma.securechatapp.ui.user_me;

import android.app.AlertDialog;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.userprofile.DataProfile;
import com.kma.securechatapp.core.api.model.userprofile.DataUser;
import com.kma.securechatapp.core.api.model.userprofile.Profile;
import com.kma.securechatapp.core.api.model.userprofile.User;
import com.kma.securechatapp.core.api.model.userprofile.school.HighSchool;
import com.kma.securechatapp.core.api.model.userprofile.school.University;
import com.kma.securechatapp.databinding.FragmentUpdateProfileBinding;


public class UpdateProfileFragment extends BaseFragment<FragmentUpdateProfileBinding> {

    private UserViewModel userViewModel;
    private ProfileViewModel profileViewModel;
    private NavController navController;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_update_profile;
    }



    @Override
    public void initBinding(){
        super.initBinding();
        navController = NavHostFragment.findNavController(this);
        binding.imgBack.setOnClickListener(view -> {
            navController.popBackStack();
        });


        userViewModel=new UserViewModel();
        userViewModel.getData();
        userViewModel.dataUser.observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                binding.setUser(user.getData());
//                Log.d("link",user.getData().getAvatar());

            }
        });

        profileViewModel=new ProfileViewModel();
        profileViewModel.getProfile();
        profileViewModel.dataUserProfile.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {

                binding.setProfile(profile.getData());
            }
        });

        binding.btnUpdate.setOnClickListener(view -> {
            String firstName=binding.edtFirstName.getText().toString();
            String lastName=binding.edtLastName.getText().toString();
            String gender=binding.edtGender.getText().toString();

            DataUser dataUser =new DataUser(firstName,lastName,gender);

            String highSchoolText=binding.edtHighSchool.getText().toString();
            String universityText=binding.edtUniversity.getText().toString();

            HighSchool highSchool=new HighSchool(highSchoolText,null);
            University university=new University(universityText,null);
            DataProfile dataProfile=new DataProfile(highSchool,university);

            AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
            builder.setTitle("Sửa Thông Tin");
            builder.setMessage("Bạn Có Muốn Xác Nhận Sửa?");
            builder.setNegativeButton("No",(dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.setPositiveButton("Yes",(dialogInterface, i) -> {
                userViewModel.updateDataUser(dataUser);
                profileViewModel.updateDataProfile(dataProfile);
                dialogInterface.dismiss();
                AlertDialog.Builder builderSuccess = new AlertDialog.Builder(requireContext());
                builderSuccess.setTitle("Sửa thông tin");
                builderSuccess.setMessage("Sửa thông tin thành công");
                builderSuccess.show();
                navController.popBackStack();

            });
            builder.show();
        });
    }


}

