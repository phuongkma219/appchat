package com.kma.securechatapp.ui.login.create_account;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.kma.securechatapp.Constant;
import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseFragment;
import com.kma.securechatapp.core.api.model.post.BaseApiRespone;
import com.kma.securechatapp.core.api.model.userprofile.register.ResponseRegister;
import com.kma.securechatapp.core.api.model.userprofile.register.UserRegister;
import com.kma.securechatapp.databinding.LayoutRegisterBinding;
import com.kma.securechatapp.repository.ApiRetrofit;
import com.kma.securechatapp.repository.ApiService;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends BaseFragment<LayoutRegisterBinding> implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.layout_register;
    }
    private ApiService api;
    private Boolean isCheck = false;
    private String MATCHER_PATTERN = "((?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=\\S+$).{6,20})";
    private Pattern PASSWORD_PATTERN = Pattern.compile(MATCHER_PATTERN);
    private String edtPassWord = "";
    NavController navController;
    @Override
    protected void initBinding() {
        super.initBinding();
        api = (ApiService) ApiRetrofit.createRetrofit(Constant.BASE_URL, ApiService.class);
        navController = NavHostFragment.findNavController(this);

        binding.edtName.setOnFocusChangeListener(this);
        binding.edtPass.setOnFocusChangeListener(this);
        binding.edtEmail.setOnFocusChangeListener(this);
        binding.edtEnterPassword.setOnFocusChangeListener(this);
        binding.checkbox.setOnCheckedChangeListener(this);
        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post);
        binding.btnBack.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && isCheck) {
            binding.btnLogin.setEnabled(true);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post_select);
        } else {
            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post);
        }
    }

    private Boolean validateName() {
        String name = binding.edtName.getText().toString().trim();
        if (name.isEmpty()) {
            binding.inputName.setErrorEnabled(true);
            binding.inputName.setError("Nhập tên người dùng");
            return false;
        } else {
            binding.inputName.setError(null);
            binding.inputName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String mail = binding.edtEmail.getText().toString().trim();
        if (mail.isEmpty()) {
            binding.inputEmail.setErrorEnabled(true);
            binding.inputEmail.setError("Nhập email người dùng");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            binding.inputEmail.setErrorEnabled(true);
            binding.inputEmail.setError("Nhập email không đúng định dạng");
            return false;
        } else {
            binding.inputEmail.setError(null);
            binding.inputEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassWord() {
        if (edtPassWord.isEmpty()) {
            binding.inputPass.setErrorEnabled(true);
            binding.inputPass.setError("Nhập mật khẩu người dùng");
            return false;
        } else if (edtPassWord.length() < 8) {
            binding.inputPass.setErrorEnabled(true);
            binding.inputPass.setError("Nhập mật khẩu phải có 8 ký tự");
            return false;
        } else {
            binding.inputPass.setError(null);
            binding.inputPass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEtPassWord() {
        Log.d("", "validateEtPassWord: dhs");
        String edtEtPassWord = binding.edtEnterPassword.getText().toString();
        if (edtEtPassWord.isEmpty()) {
            binding.inputEnterPassword.setErrorEnabled(true);
            binding.inputEnterPassword.setError("Nhập lại mật khẩu");
            return false;
        } else if (!edtPassWord.equals(edtEtPassWord)) {
            binding.inputEnterPassword.setErrorEnabled(true);
            binding.inputEnterPassword.setError("Mật khẩu không khớp");
            return false;
        } else {
            binding.inputEnterPassword.setError(null);
            binding.inputEnterPassword.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePhone() {
        String phone = binding.edtPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            binding.inputPhone.setErrorEnabled(true);
            binding.inputPhone.setError("Nhập số điện thoại");
            return false;
        } else {
            if (phone.length() != 10) {
                binding.inputPhone.setErrorEnabled(true);
                binding.inputPhone.setError("Nhập lại số điện thoại");
                return false;
            }
            binding.inputPhone.setError(null);
            binding.inputPhone.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edt_enter_password: {
                validateEtPassWord();
                break;
            }
            case R.id.edt_email: {
                validateEmail();
                break;
            }
            case R.id.edt_name: {
//
                validateName();
                break;
            }
            case R.id.edt_phone: {
                validatePhone();
                break;
            }
            case R.id.edt_pass: {
                edtPassWord = binding.edtPass.getText().toString();
                validatePassWord();

                break;
            }


        }
        if (validateName() && validateEmail() && validatePhone() && validateEtPassWord()) {
            binding.btnLogin.setEnabled(true);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post_select);
            isCheck = true;


        } else {
            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setBackgroundResource(R.drawable.bg_shape_post);
            isCheck = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            String first_name = binding.edtName.getText().toString();
            String email  = binding.edtEmail.getText().toString();
            String phone = binding.edtPhone.getText().toString();
            String user_name = email;
             String pass = binding.edtEnterPassword.getText().toString();
            UserRegister userRegister = new UserRegister(email,phone,user_name,first_name,pass);
            api.userRegister(userRegister).enqueue(new Callback<BaseApiRespone>() {
                @Override
                public void onResponse(Call<BaseApiRespone> call, Response<BaseApiRespone> response) {
                    if(response.body().success){
                        Toast.makeText(RegisterFragment.this.getContext(), "Đăng ký thành công.", Toast.LENGTH_SHORT).show();

                        navController.popBackStack();
                    }
                    else {
                        if (response.body().message.equals("AUTH.REGISTER.CONFLICT_USERNAME")) {
                            Toast.makeText(RegisterFragment.this.getContext(), "Email đã đăng ký.Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("kkk", "onResponse: "+ response.body().toString());
                            Toast.makeText(RegisterFragment.this.getContext(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseApiRespone> call, Throwable t) {
                    t.printStackTrace();
                        Toast.makeText(RegisterFragment.this.getContext(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                        Log.d("kkk", "onFailure: ");
                }
            });
        }
        if(v.getId()== R.id.btn_back){
            navController.popBackStack();
        }
    }
}
