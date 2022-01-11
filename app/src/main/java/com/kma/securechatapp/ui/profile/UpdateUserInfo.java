package com.kma.securechatapp.ui.profile;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.api.model.UserRegistRequest;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.ui.authentication.createaccount.CreateAccountUserNameFragment;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInfo extends AppCompatActivity {

    ApiInterface api = new ApiUtil().getChatApi();
    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.dob_input)
    TextInputEditText txtDob;

    @BindView(R.id.phonenumber_input)
    TextInputEditText txtPhoneNumber;

    @BindView(R.id.address_input)
    TextInputEditText txtAddress;

    @BindView(R.id.name_input)
    TextInputEditText txtFullname;

    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Cập nhập thông tin tài khoản");

        UserInfo currenInfo = AppData.getInstance().currentUser;

        txtFullname.setText(currenInfo.name);
        txtAddress.setText(currenInfo.address);
        txtPhoneNumber.setText(currenInfo.phone);

        if (currenInfo.dob > 0) {
            Date date = new Date(currenInfo.dob);
            myCalendar.setTime(date);
            int year = myCalendar.get(Calendar.YEAR);
            int month = myCalendar.get(Calendar.MONTH);
            int day = myCalendar.get(Calendar.DAY_OF_MONTH);
            updateDateOfBirthInput(year, month,
                    day);
        }

    }

    @OnClick(R.id.dob_input)
    public void onClickDob(View v) {
        // TODO Auto-generated method stub
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void updateDateOfBirthInput(int year, int monthOfYear,
                                int dayOfMonth) {
        txtDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
    }
    boolean validatePhoneNumber(String phoneNumber) {
        return true;
    }

    @OnClick(R.id.btn_next)
    public void onNext(View view) {


        if (!validatePhoneNumber(txtPhoneNumber.getText().toString())) {
            return;
        }

        CommonHelper.showLoading(this);
        UserInfo request = new UserInfo(AppData.getInstance().userUUID, "","" , 0l);


        request.address = txtAddress.getText().toString();
        request.dob = myCalendar.getTime().getTime();
        request.name = txtFullname.getText().toString();
        request.phone = txtPhoneNumber.getText().toString();

        api.updateUserInfo(request).enqueue(new Callback<ApiResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                CommonHelper.hideLoading();
                if (response.body() == null || response.body().data == null ){
                    Toast.makeText( UpdateUserInfo.this,"Cập nhập thông tin tài khoản thất bại",Toast.LENGTH_LONG).show();
                    return;
                }
                AppData.getInstance().currentUser = response.body().data;
                EventBus.getInstance().pushOnChangeProfile();
                Toast.makeText( UpdateUserInfo.this,"Cập nhập thông tin tài khoản thành công",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                CommonHelper.hideLoading();
                t.printStackTrace();
                Toast.makeText( UpdateUserInfo.this,"Cập nhập thông tin tài khoản thất bại",Toast.LENGTH_LONG).show();
            }
        });

    }

    }