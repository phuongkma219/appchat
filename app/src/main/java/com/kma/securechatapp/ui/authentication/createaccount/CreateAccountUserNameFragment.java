package com.kma.securechatapp.ui.authentication.createaccount;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.AuthenResponse;
import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.api.model.UserRegistRequest;
import com.kma.securechatapp.core.service.DataService;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.ui.authentication.PinCodeFragment;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountUserNameFragment extends Fragment {

    ApiInterface api = new ApiUtil().getChatApi();
    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.dob_input)
    TextInputEditText txtDob;

    @BindView(R.id.account_input)
    TextInputEditText txtAccount;


    @BindView(R.id.phonenumber_input)
    TextInputEditText txtPhoneNumber;

    @BindView(R.id.address_input)
    TextInputEditText txtAddress;

    @BindView(R.id.name_input)
    TextInputEditText txtFullname;

    @BindView(R.id.error_account)
    TextView lbErrorAccount;


    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateOfBirthInput(year, monthOfYear,
                    dayOfMonth);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create_account_user_name, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @OnClick(R.id.dob_input)
    public void onClickDob(View v) {
        // TODO Auto-generated method stub
        new DatePickerDialog(CreateAccountUserNameFragment.this.getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_next)
    public void onNext(View view) {

        if (!validateUserName(txtAccount.getText().toString())) {
            return;
        }

        if (!validatePhoneNumber(txtPhoneNumber.getText().toString())) {
            return;
        }

        CommonHelper.showLoading(this.getContext());
        UserRegistRequest request = new UserRegistRequest();

        request.address = txtAddress.getText().toString();
        request.dob = myCalendar.getTime().getTime();
        request.username = txtAccount.getText().toString();
        request.name = txtFullname.getText().toString();
        request.phonenumber = txtPhoneNumber.getText().toString();

        api.registNewAccount(request).enqueue(new Callback<ApiResponse<AuthenResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenResponse>> call, Response<ApiResponse<AuthenResponse>> response) {
                CommonHelper.hideLoading();
                if (response.body() == null) {
                    Toast.makeText(CreateAccountUserNameFragment.this.getContext(), "C?? l???i x???y ra !!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body().error != 0) {
                    Toast.makeText(CreateAccountUserNameFragment.this.getContext(), response.body().message, Toast.LENGTH_SHORT).show();
                    return;
                }

                addSuccess(request, response.body().data);
                Toast.makeText(CreateAccountUserNameFragment.this.getContext(), "????ng k?? th??nh c??ng !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenResponse>> call, Throwable t) {
                CommonHelper.hideLoading();
                Toast.makeText(CreateAccountUserNameFragment.this.getContext(), "C?? l???i x???y ra !!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void addSuccess(UserRegistRequest request, AuthenResponse response) {

        AppData.getInstance().currentUser = response.user;
        AppData.getInstance().setToken(response.token);
        AppData.getInstance().setRefreshToken(response.refreshToken);
        AppData.getInstance().account = request.username;
        AppData.getInstance().userUUID = response.user.uuid;

        DataService.getInstance(this.getContext()).storeUserAccount( request.username ) ;
        DataService.getInstance(this.getContext()).storeToken(response.token,response.refreshToken);
        DataService.getInstance(this.getContext()).storeUserUuid( response.user.uuid );


        DataService.getInstance(this.getContext()).save();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft_rep = fm.beginTransaction();
        PinCodeFragment createAccountValidateFragment = new PinCodeFragment();
        Bundle bundle = new Bundle();

        bundle.putString("username",request.username);
        bundle.putString("access_token",response.token);

        createAccountValidateFragment.setArguments(bundle);
        ft_rep.replace(R.id.nav_create_account,createAccountValidateFragment);
        ft_rep.commit();



       /* NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.navigation_account); */

       // CommonHelper.showLoading(this.getContext());



    }

    void updateDateOfBirthInput(int year, int monthOfYear,
                                int dayOfMonth) {
        txtDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }

    @OnFocusChange(R.id.account_input)
    void onUserNameFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            validateUserName(txtAccount.getText().toString());
        }
    }

    boolean validatePhoneNumber(String phoneNumber) {
        return true;
    }

    boolean validatePassword(String password) {
        if (password.isEmpty()) {
            lbErrorAccount.setText("* M???t kh???u kh??ng ???????c tr???ng !");
            return false;
        }
        if (password.length() < 6 || password.length() > 13) {
            lbErrorAccount.setText("* M???t kh???u ph???i d??i t??? 6 ?????n 13 k?? t??? !");
            return false;
        }
        return true;
    }

    boolean validateUserName(String username) {
        if (username.isEmpty()) {
            return false;
        }
        if (username.length() < 6 || username.length() > 13) {
            lbErrorAccount.setText("* T??n ????ng nh???p ph???i d??i t??? 6 ?????n 13 k?? t??? !");
            return false;
        }

        api.userExist(username).enqueue(new Callback<ApiResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                if (response.body() != null && response.body().error != 1 && response.body().data != null) {
                    lbErrorAccount.setText("* T??n ????ng nh???p ???? t???n t???i !");
                } else {
                    lbErrorAccount.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {

            }
        });

        return true;
    }

}
