package com.kma.securechatapp.ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.AuthenRequest;
import com.kma.securechatapp.core.api.model.AuthenResponse;
import com.kma.securechatapp.core.api.model.Device;
import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.api.model.UserKey;
import com.kma.securechatapp.core.security.RSAUtil;
import com.kma.securechatapp.core.service.DataService;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.utils.common.Utils;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    @BindView(R.id.account_input)
    TextInputEditText text_account;
    @BindView(R.id.img_scane)
    ImageView imgBtnScane ;
    @BindView(R.id.input_pin)
    PinView pinView ;


    ApiInterface api = ApiUtil.getChatApi();
    NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        navController = NavHostFragment.findNavController(this);
        if ( !AppData.getInstance().opened) {
            if (AppData.getInstance().account != null && AppData.getInstance().userUUID != null) {

                navController.navigate(R.id.navigation_password);
            }
            AppData.getInstance().opened = true;
        }
        return root;

    }

    @OnClick(R.id.btn_signup)
    public void buttonSignUpClick(View view){

        navController.navigate(R.id.navigation_create_account);

    }
    @OnClick(R.id.img_scane)
    public void imgBtnScaneClick(View view){
        Intent intent = new Intent(AccountFragment.this.getContext(), LoginQrScane.class);
        startActivityForResult(intent,10);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 ){
            if (AppData.getInstance().userUUID != null) {
               // this.getActivity().finishActivity(0);
               // this.getActivity().finish();
                navController.navigate(R.id.navigation_password);
            }
        }

    }
    @OnClick(R.id.btn_login)
    public void continueLogin(View view) {
       // Toast.makeText(this.getContext(),text_account.getText().toString(),Toast.LENGTH_SHORT).show();
        CommonHelper.showLoading(this.getContext());
        AppData.getInstance().account = text_account.getText().toString();
        String pin = pinView.getText().toString();


        api.userExist( AppData.getInstance().account).enqueue(new Callback<ApiResponse<UserInfo>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserInfo>> call, Response<ApiResponse<UserInfo>> response) {
                CommonHelper.hideLoading();
                if (response.body() == null){
                    Toast.makeText(AccountFragment.this.getContext(),"Tài khoản này không tồn tai, xin kiểm tra lại",Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (response.body().error != 0){
                    Toast.makeText(AccountFragment.this.getContext(),"Tài khoản này không tồn tai, xin kiểm tra lại",Toast.LENGTH_SHORT).show();
                    return ;
                }

                //AppData.getInstance().currentUser = response.body().data;
                AppData.getInstance().userUUID = response.body().data.uuid;
                AppData.getInstance().currentTransactionId = response.body().transactionId;
                DataService.getInstance(null).storeUserAccount( AppData.getInstance().account) ;

                //navController.navigate(R.id.navigation_password);
                loginWithPin( pin );
            }

            @Override
            public void onFailure(Call<ApiResponse<UserInfo>> call, Throwable t) {
                CommonHelper.hideLoading();
                Toast.makeText(AccountFragment.this.getContext(),"Có lỗi xảy ra : "+t.toString(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    void loginWithPin( String password ){

        CommonHelper.showLoading(this.getContext());
        AuthenRequest auth = new AuthenRequest(AppData.getInstance().account,password);
        auth.device = new Device();
        auth.device.deviceCode = AppData.getInstance().deviceId;
        auth.device.deviceOs = "android";
        auth.transactionId = AppData.getInstance().currentTransactionId;

        String privateKey = DataService.getInstance(this.getContext()).getPrivateKey(AppData.getInstance().userUUID,password);

        if (privateKey == null || privateKey.isEmpty()) {
            CommonHelper.hideLoading();
            Toast.makeText(this.getContext(), "Bạn không có khoá bí mật để đăng nhập vào tài khoản này", Toast.LENGTH_LONG).show();
            navController.navigate(R.id.navigation_password);
            return;
        }

        UserKey userKey  =  new UserKey(null,null);
        userKey.privateKey = privateKey ;
        try{
            PrivateKey objectPrivateKey = RSAUtil.stringToPrivateKey(privateKey);
            PublicKey publicKey = RSAUtil.getPublicKey(objectPrivateKey);
            userKey.publicKey =  RSAUtil.base64Encode(publicKey.getEncoded());
            byte[] data = RSAUtil.RSAEncrypt("Helloworld",publicKey);
            String data2 = RSAUtil.RSADecrypt(data,objectPrivateKey );
            if (data2.endsWith("Helloworld"))
            {
                //  AppData.getInstance().userKey = userKey;
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey secretKey = keyGen.generateKey();
                byte[] buffKey = secretKey.getEncoded();

                auth.password = RSAUtil.bytesToHex(buffKey);
                auth.transactionId = RSAUtil.base64Encode(RSAUtil.RSASignData(auth.password.getBytes("UTF-8"), objectPrivateKey));
                AppData.getInstance().userKey =  userKey;
            }
            else{
                CommonHelper.hideLoading();
                Toast.makeText(this.getContext(),"Mật khẩu của khoá bí mật không đúng",Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.navigation_password);
                return;
            }

        } catch ( Exception e) {
            e.printStackTrace();
            CommonHelper.hideLoading();
            Toast.makeText(this.getContext(),"Mật khẩu của khoá bí mật không đúng",Toast.LENGTH_SHORT).show();
            navController.navigate(R.id.navigation_password);
            return;
        }

        AppData.getInstance().password = password;

        if ( Utils.haveNetworkConnection(this.getContext())  ) {
            onlineLogin(auth);
        } else {
            onLoginSuccess();

        }

    }


    void onlineLogin(AuthenRequest auth){

        api.login(auth).enqueue(new Callback<ApiResponse<AuthenResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthenResponse>> call, Response<ApiResponse<AuthenResponse>> response) {
                CommonHelper.hideLoading();
                if (response.body() == null || response.body().error != 0 ){
                    if (response.body()  == null){
                        Toast.makeText(AccountFragment.this.getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    return;
                }
                // login success
                AppData.getInstance().setToken(response.body().data.token);
                AppData.getInstance().setRefreshToken(response.body().data.refreshToken);
//                try {
//                   // AppData.getInstance().currentUser = api.getCurrenUserInfo().execute().body().data;
//                } catch (IOException e) {
//                    return;
//                }
                DataService.getInstance(AccountFragment.this.getContext()).storeToken(response.body().data.token,response.body().data.refreshToken);
                //DataService.getInstance(null).storeUserUuid( AppData.getInstance().currentUser.uuid);
                //end screen
                DataService.getInstance(null).save();

                onLoginSuccess();
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenResponse>> call, Throwable t) {
                CommonHelper.hideLoading();
                t.printStackTrace();
                Toast.makeText(AccountFragment.this.getContext(),"Đăng nhập thấy bại",Toast.LENGTH_SHORT).show();
            }
        });

    }

    void onLoginSuccess(){
        CommonHelper.hideLoading();
        if (this.getActivity() == null ){
            return;
        }
        Log.d("kkk", "onLoginSuccess: vào đay");
//        EventBus.getInstance().pushOnLogin(AppData.getInstance().currentUser);
        this.getActivity().finishActivity(0);
        this.getActivity().finish();
    }
}
