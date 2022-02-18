package com.kma.securechatapp.ui.authentication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.kma.securechatapp.utils.common.GFingerPrint;
import com.kma.securechatapp.utils.common.ImageLoader;
import com.kma.securechatapp.utils.common.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PasswordFragment extends Fragment {

    ApiInterface api = ApiUtil.getChatApi();

    @BindView(R.id.password_input)
    PinView text_password;
    @BindView(R.id.opt_layout)
    LinearLayout optLayout;
    @BindView(R.id.opt_input)
    TextInputEditText optInput;
    @BindView(R.id.login_avatar)
    ImageView loginAvatar;
    @BindView(R.id.login_name)
    TextView loginName;
    @BindView(R.id.fingerprint_image)
    ImageView fingerprint;
    private Executor executor;
    private androidx.biometric.BiometricPrompt biometricPrompt;
    private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;
    NavController navController;
    boolean checkopt = false;
    GFingerPrint gFingerPrint ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login2, container, false);
        ButterKnife.bind(this, root);
        optLayout.setVisibility(View.GONE);
        fingerprint.setVisibility(View.VISIBLE);
        NavController navController = NavHostFragment.findNavController(this);

            ImageLoader.getInstance().DisplayImage(ImageLoader.getUserAvatarUrl(AppData.getInstance().userUUID,200,200),loginAvatar);
            loginName.setText(AppData.getInstance().account);

        //finger print
        SettingFingerPrint();
        return root;
    }

    public void SettingFingerPrint(){
        gFingerPrint = new GFingerPrint(this.getContext());
        executor = ContextCompat.getMainExecutor(getContext());
        biometricPrompt = new BiometricPrompt(this , executor, new BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getContext(),
                        "Lỗi vân tay: " + errString, Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                String password = null;
                //Toast.makeText(getContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                CommonHelper.showLoading(getContext());
                String encryptPassword = DataService.getInstance(getContext()).getFingerSaved(AppData.getInstance().userUUID);
                Cipher cipher =  result.getCryptoObject().getCipher();
                try {
                    password =   gFingerPrint.decipher(cipher,encryptPassword );
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                AuthenRequest auth = new AuthenRequest(AppData.getInstance().account,password);
                auth.token = optInput.getText().toString();
                auth.device = new Device();
                auth.device.deviceCode = AppData.getInstance().deviceId;
                auth.device.deviceOs = "android";
                AppData.getInstance().password = password;
                if ( Utils.haveNetworkConnection(getContext())  ) {
                    onlineLogin(auth);
                } else {
                    onLoginSuccess();

                }
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Sử dụng vân tay để đăng nhập")
                .setNegativeButtonText("Không sử dụng vân tay")
                .build();


        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        Cipher cipher = gFingerPrint.createCipher(Cipher.DECRYPT_MODE);
                        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (gFingerPrint.initKeyStore()){
                if (DataService.getInstance(this.getContext()).getFingerSaved(AppData.getInstance().userUUID) != null){
                    fingerprint.setVisibility(View.VISIBLE);
                } else {
                    fingerprint.setVisibility(View.GONE);
                }
            } else {
                fingerprint.setVisibility(View.GONE);
            }
        } else {
            fingerprint.setVisibility(View.GONE);
        }
    }

    public void CheckOpt() {
            if (checkopt )
                return;
            checkopt = true;
            CommonHelper.showLoading(this.getContext());
            try {

                Response<ApiResponse<UserInfo>> data = api.preLogin(AppData.getInstance().account,  AppData.getInstance().deviceId).execute();
                if (data.body().error!= 0){
                    showOpt();
                }
                else{
                    optLayout.setVisibility(View.GONE);
                }
                AppData.getInstance().currentTransactionId = data.body().transactionId;
            } catch (IOException e) {
                CommonHelper.hideLoading();
                navController.navigate(R.id.navigation_account);
                Toast.makeText(PasswordFragment.this.getContext(),"Lỗi đăng nhập, xin kiểm tra lại mã đã nhập",Toast.LENGTH_SHORT).show();

                return;
            }
            CommonHelper.hideLoading();


    }

    public void showOpt(){
        fingerprint.setVisibility(View.GONE);
        optLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_login2)
    public void buttonLoginClick (View view)   {
        String password = text_password.getText().toString();
        CommonHelper.showLoading(this.getContext());
        AuthenRequest auth = new AuthenRequest(AppData.getInstance().account,password);
        auth.token = optInput.getText().toString();
        auth.device = new Device();
        auth.device.deviceCode = AppData.getInstance().deviceId;
        auth.device.deviceOs = "android";
        auth.transactionId = AppData.getInstance().currentTransactionId;

        String privateKey = DataService.getInstance(this.getContext()).getPrivateKey(AppData.getInstance().userUUID,password);

        if (privateKey == null || privateKey.isEmpty()) {
            CommonHelper.hideLoading();
            Toast.makeText(this.getContext(), "Bạn không có khoá bí mật để đăng nhập vào tài khoản này", Toast.LENGTH_LONG).show();
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
                    return;
                }

        } catch ( Exception e) {
            e.printStackTrace();
            CommonHelper.hideLoading();
            Toast.makeText(this.getContext(),"Mật khẩu của khoá bí mật không đúng",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PasswordFragment.this.getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                   // AppData.getInstance().currentTransactionId = response.body().transactionId;

                    if (response.body() == null || response.body().error == 1) {
                        Toast.makeText(PasswordFragment.this.getContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                    }else{
                        if (optLayout.getVisibility() == View.GONE){
                            optLayout.setVisibility(View.GONE);
                        }
                        Toast.makeText(PasswordFragment.this.getContext(), "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
                // login success
                AppData.getInstance().setToken(response.body().data.token);
                AppData.getInstance().setRefreshToken(response.body().data.refreshToken);
//                try {
////                    AppData.getInstance().currentUser = api.getCurrenUserInfo().execute().body().data;
//                } catch (IOException e) {
//                    return;
//                }
                DataService.getInstance(PasswordFragment.this.getContext()).storeToken(response.body().data.token,response.body().data.refreshToken);
//                DataService.getInstance(null).storeUserUuid( AppData.getInstance().currentUser.uuid);
                //end screen
                DataService.getInstance(null).save();

                onLoginSuccess();
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthenResponse>> call, Throwable t) {
                CommonHelper.hideLoading();
                t.printStackTrace();
                Toast.makeText(PasswordFragment.this.getContext(),"Đăng nhập thấy bại",Toast.LENGTH_SHORT).show();
            }
        });

    }

    void onLoginSuccess(){
        if (PasswordFragment.this.getActivity() == null ){
            return;
        }
//        EventBus.getInstance().pushOnLogin(AppData.getInstance().currentUser);
        PasswordFragment.this.getActivity().finishActivity(0);
        PasswordFragment.this.getActivity().finish();
    }


    @OnClick(R.id.btn_forgot)
    void clickImport(View view ) {
        if (ContextCompat.checkSelfPermission(this.getContext(), READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]
                    {
                            READ_EXTERNAL_STORAGE,
                            WRITE_EXTERNAL_STORAGE

                    }, 1);

        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(intent, "Chọn file tải lên"),
                1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Uri uri = data.getData();
            InputStream br;
            FileOutputStream os;
            try {
                br = this.getActivity().getContentResolver().openInputStream(uri);
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                for (int length; (length = br.read(buffer)) != -1; ) {
                    result.write(buffer, 0, length);
                }
                String text = result.toString("UTF-8");
                DataService.getInstance(this.getContext()).storePrivateKey(AppData.getInstance().userUUID,text,null);
                DataService.getInstance(null).save();
                Toast.makeText(this.getContext(),"Import key cho tài khoản " + this.loginName.getText().toString() +" thành công!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
