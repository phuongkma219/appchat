package com.kma.securechatapp.ui.authentication.changepin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.chaos.view.PinView;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.model.UserKey;
import com.kma.securechatapp.core.security.RSAUtil;
import com.kma.securechatapp.core.service.DataService;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ChangeOldPinFragment extends Fragment {
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.input_opt)
    PinView txtOpt;

    NavController navController;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_olpin_input, container, false);
         ButterKnife.bind(this, root);
        navController = NavHostFragment.findNavController(this);
        return root;
    }

    @OnClick(R.id.btn_done)
    void onDone(View view){
        String password = txtOpt.getText().toString();
        String privateKey = DataService.getInstance(this.getContext()).getPrivateKey(AppData.getInstance().userUUID,password);

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
               navController.navigate(R.id.navigation_new_pin);
            }
            else{
                Toast.makeText(this.getContext(),"Mật khẩu của khoá bí mật không đúng",Toast.LENGTH_SHORT).show();
                return;
            }

        } catch ( Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getContext(),"Mật khẩu của khoá bí mật không đúng",Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
