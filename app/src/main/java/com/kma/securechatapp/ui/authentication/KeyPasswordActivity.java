package com.kma.securechatapp.ui.authentication;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.NavigationUI;

import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputEditText;
import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.UserKey;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.core.security.AES;
import com.kma.securechatapp.core.security.RSAUtil;
import com.kma.securechatapp.core.service.DataService;
import com.kma.securechatapp.helper.FileUtils;
import com.kma.securechatapp.utils.common.FileCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KeyPasswordActivity extends AppCompatActivity {
    @BindView(R.id.input_password)
    PinView inputPassword;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    UserKey userKey ;
    int type;
    ApiInterface api = ApiUtil.getChatApi();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    @OnClick(R.id.btn_save)
    void onSave(View view){
        String password = inputPassword.getText().toString().trim();
        if (password.isEmpty()) {
            return;
        }
        String privateKey = DataService.getInstance(this).getPrivateKey(AppData.getInstance().userUUID,password);
        PrivateKey objectPrivateKey = null;
        try {
            objectPrivateKey = RSAUtil.stringToPrivateKey(privateKey);
            PublicKey publicKey = RSAUtil.getPublicKey(objectPrivateKey);
            byte[] data = RSAUtil.RSAEncrypt("Helloworld",publicKey);
            String data2 = RSAUtil.RSADecrypt(data,objectPrivateKey );
            if (data2.endsWith("Helloworld"))
            {;
                String name = "pkey-" + AppData.getInstance().userUUID;
                File saveFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),name);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream(saveFile));
                outputStreamWriter.write(DataService.getInstance(this).getPrivateKey(AppData.getInstance().userUUID,null));
                outputStreamWriter.close();
                DownloadManager downloadmanager = (DownloadManager) MainActivity.instance.getSystemService(Context.DOWNLOAD_SERVICE);
                downloadmanager.addCompletedDownload(name, name, true, "application/octet-stream", saveFile.getPath(),saveFile.length(),true);
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this,"M???t kh???u c???a kho?? b?? m???t kh??ng ????ng",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
