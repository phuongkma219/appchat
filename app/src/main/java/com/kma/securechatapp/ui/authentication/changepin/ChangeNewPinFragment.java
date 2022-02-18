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

import com.chaos.view.PinView;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.service.CacheService;
import com.kma.securechatapp.core.service.DataService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeNewPinFragment extends Fragment {
    @BindView(R.id.btn_done)
    Button btnDone;
    @BindView(R.id.input_opt)
    PinView txtOpt;
    NavController navController;
    boolean edit = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_newpin_input, container, false);
        ButterKnife.bind(this, root);
        navController = NavHostFragment.findNavController(this);
        return root;

    }

    @OnClick(R.id.btn_done)
    void onDone(View view){
        if (edit) {
            return;
        }
        edit = true;
        try {
            String password =  txtOpt.getText().toString();
            if ( password == null ||  password.length() < 6) {
                Toast.makeText(this.getContext(),"Bạn cần nhập mã PIN có 6 ký tự", Toast.LENGTH_LONG).show();
                return ;
            }
            AppData.getInstance().password = password;
            CacheService.getInstance().changeRealmPassword(this.getContext(),AppData.getInstance().password);
            String privateKey = AppData.getInstance().userKey.privateKey;
            DataService.getInstance(this.getContext()).storePrivateKey(AppData.getInstance().userUUID, privateKey,  AppData.getInstance().password);
            DataService.getInstance(this.getContext()).save();
            Toast.makeText(this.getContext(),"Đỗi mã PIN thành công", Toast.LENGTH_LONG).show();
            edit = false;
            this.getActivity().finish();
        }catch (Exception e) {
            edit = false;
            e.printStackTrace();
            Toast.makeText(this.getContext(),"Lỗi cơ sở dữ liệu realm, Đổi mã PIN thất bại", Toast.LENGTH_LONG).show();
        }
    }
}
