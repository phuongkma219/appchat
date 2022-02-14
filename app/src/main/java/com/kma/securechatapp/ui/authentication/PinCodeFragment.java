package com.kma.securechatapp.ui.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.UserKey;
import com.kma.securechatapp.core.api.model.VerifyRegister;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.core.security.AES;
import com.kma.securechatapp.core.security.RSAUtil;
import com.kma.securechatapp.core.service.DataService;
import com.kma.securechatapp.helper.CommonHelper;
import com.kma.securechatapp.ui.authentication.createaccount.CreateAccountValidateFragment;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinCodeFragment extends Fragment {


    public PinCodeFragment() {
        // Required empty public constructor
    }
    UserKey userKey ;
    ApiInterface api = ApiUtil.getChatApi();
    String uuid;
    String username;
    String token;

    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.input_opt)
    PinView txtOpt;

    @BindView(R.id.input_opt2)
    PinView txtOpt2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pin_code, container, false);
        Bundle bundle = this.getArguments();
        uuid = bundle.getString("uuid");
        username = bundle.getString("username");
        token = bundle.getString("token");
        ButterKnife.bind(this,root);
        return root;
    }
    @OnClick(R.id.btn_done)
    void onDone(View view){
        try {
            String text1 = txtOpt.getText().toString();
            String text2 = txtOpt2.getText().toString();

            if (text1 == null || text1.length() < 6) {
                Toast.makeText(this.getContext(),"Bạn cần nhập mã PIN có 6 ký tự", Toast.LENGTH_LONG).show();
                return;
            }
            if (!text1.equals(text2)){
                Toast.makeText(this.getContext(),"Mã pin xác nhận không đúng", Toast.LENGTH_LONG).show();
            }

            createKey(text1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(),"Có lỗi xảy ra xin thử lại!",Toast.LENGTH_SHORT).show();
        }
    }

    void createKey(String pass) throws NoSuchAlgorithmException {

        if ( pass == null ||  pass.length() < 6) {
            Toast.makeText(this.getContext(),"Bạn cần nhập mã PIN có 6 ký tự", Toast.LENGTH_LONG).show();
            return ;
        }

        String[] key = RSAUtil.generateKey();
        userKey = new UserKey(key[0],key[1]);
        AppData.getInstance().password = pass;
        String privateKey = userKey.privateKey; ;
        userKey.privateKey = ""; RSAUtil.base64Encode(AES.encrypt( RSAUtil.base64Decode(userKey.privateKey) , pass));
//        DataService.getInstance(this.getContext()).storePrivateKey(AppData.getInstance().currentUser.uuid, privateKey, pass);
        DataService.getInstance(this.getContext()).save();

        try {
            api.updateKey(userKey).execute();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(),"Có lỗi xảy ra xin thử lại!",Toast.LENGTH_SHORT).show();
        }
        userKey.privateKey = privateKey;
        AppData.getInstance().userKey = userKey;
//        EventBus.getInstance().pushOnLogin(AppData.getInstance().currentUser);
        this.getActivity().finishActivity(0);
        this.getActivity().finish();
    }
}