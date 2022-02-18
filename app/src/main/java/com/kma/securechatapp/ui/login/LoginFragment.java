package com.kma.securechatapp.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.api.model.userprofile.login.UserDTO;
import com.kma.securechatapp.core.api.model.userprofile.login.UserLogin;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.databinding.LayoutLoginBinding;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;
import com.kma.securechatapp.repository.DataLocalManager;
import com.kma.securechatapp.ui.home.EventSendData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends BaseFragment<LayoutLoginBinding> implements View.OnClickListener {
    private ApiService api;
    private NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.layout_login;
    }

    @Override
    protected void initBinding() {
        super.initBinding();
        navController = NavHostFragment.findNavController(this);
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        initEdt();
        binding.btnRegister.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    private void initEdt() {
        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    binding.btnLogin.setEnabled(false);
                    binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post);

                } else {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post_select);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    binding.btnLogin.setEnabled(false);
                    binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post);

                } else {
                    binding.btnLogin.setEnabled(true);
                    binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post_select);
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
            case R.id.btn_register: {
                navController.navigate(R.id.action_navigation_login_to_navigation_register);
                break;
            }
            case R.id.btn_login: {
                CommonHelper.showLoading(this.getActivity());
                binding.tvEr.setVisibility(View.VISIBLE);

                UserDTO userDTO = new UserDTO(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
                api.login(userDTO).enqueue(new Callback<UserLogin>() {
                    @Override
                    public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                        if (response.body() == null) {
                            CommonHelper.hideLoading();
                            binding.tvEr.setVisibility(View.VISIBLE);
                            binding.tvEr.setText("Tài khoản hoặc mật khẩu không chính xác. Vui lòng thử lại.!");
                        } else {
                            EventSendData.getInstance().pushOnLogin(response.body().data);
                            DataLocalManager.setData(response.body().data);
                            AppData.getInstance().setToken(response.body().data.access_token);
                            AppData.getInstance().account = response.body().data.user.username;
                            AppData.getInstance().setUserUUID(response.body().data.user._id);
                            EventBus.getInstance().pushOnLogin(new UserInfo(response.body().data.user._id,response.body().data.user.username, "",0l ));
                            onLoginSuccess();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLogin> call, Throwable t) {
                        CommonHelper.hideLoading();
                        t.printStackTrace();
                        binding.tvEr.setVisibility(View.VISIBLE);
                        binding.tvEr.setText("Đăng nhập thất bại..");
                    }
                });

                break;
            }
        }
    }

    void onLoginSuccess() {
        CommonHelper.hideLoading();
        if (this.getActivity() == null) {
            return;
        }

//        EventBus.getInstance().pushOnLogin(AppData.getInstance().currentUser);
//        this.getActivity().finishActivity(0);
//        this.getActivity().finish();
        Intent intent = new Intent(this.getActivity(),MainActivity.class);
        startActivity(intent);
    }
}
