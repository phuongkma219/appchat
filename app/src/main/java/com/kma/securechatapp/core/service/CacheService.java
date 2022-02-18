package com.kma.securechatapp.core.service;

import android.content.Context;
import android.widget.Toast;

import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.core.AppData;
import com.kma.securechatapp.core.api.ApiInterface;
import com.kma.securechatapp.core.api.ApiUtil;
import com.kma.securechatapp.core.api.model.ApiResponse;
import com.kma.securechatapp.core.api.model.Conversation;
import com.kma.securechatapp.core.api.model.Message;
import com.kma.securechatapp.core.api.model.MessagePlaneText;
import com.kma.securechatapp.core.api.model.PageResponse;
import com.kma.securechatapp.core.api.model.UserInfo;
import com.kma.securechatapp.core.event.EventBus;
import com.kma.securechatapp.core.realm_model.RConversation;
import com.kma.securechatapp.core.realm_model.RMessage;
import com.kma.securechatapp.core.realm_model.RUserInfo;
import com.kma.securechatapp.core.security.RSAUtil;
import com.kma.securechatapp.core.security.SecureChatSystem;
import com.kma.securechatapp.helper.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CacheService {

    private static CacheService instance;
    public Realm rdb;
    String db;
    ApiInterface api = ApiUtil.getChatApi();
    public static  CacheService getInstance(){
        if (instance == null){
            instance = new CacheService();
        }
        return instance;
    }
    public void getUserPublicKey(String uuid){

    }

    public void removeDB(Context context) {
        try {
            if (rdb != null ){
                rdb.close();
            }
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .name(db)
                    .build();
            new File(config.getPath()).delete();
            this.init(context,this.db,AppData.getInstance().password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void init(Context context, String db ,String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
      try {
          this.db = db;
          Realm.init(context);
          RealmConfiguration config = new RealmConfiguration.Builder()
                  .deleteRealmIfMigrationNeeded()
                  .name(db)
                  .encryptionKey(RSAUtil.getSHA512(password))
                  .allowWritesOnUiThread(true)
                  .build();

          rdb = Realm.getInstance(config);
      }catch ( Exception e){
            e.printStackTrace();
      }
    }
    public void changeRealmPassword(Context context,String newPassword) throws IOException, NoSuchAlgorithmException {

        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(db)
                .encryptionKey(RSAUtil.getSHA512(newPassword))
                .build();
                File file = new File(context.getFilesDir(), db + "-temp");
                File fileDest = new File(config.getPath());
            if (rdb != null) {

                if (file.exists()) {
                    file.delete();
                }
                rdb.writeEncryptedCopyTo(file, RSAUtil.getSHA512(newPassword));
                rdb.close();
                if (fileDest.exists()) {
                    fileDest.delete();
                }
            } else {
                if (fileDest.exists()) {
                    fileDest.delete();
                }
            }
            FileUtils.copy(file, fileDest);
            this.init(context, this.db, newPassword);

    }

    public String accountToDbName(String account){
        String key = account;
        try {
            key = RSAUtil.bytesToHex( RSAUtil.getSHA(account) );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "x-encr-db-"+key+"-realm.gn";
    }

    public void removeConversation(String uuid){
        try{
            rdb.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        RealmResults<RMessage> rows = realm.where(RMessage.class).equalTo("threadUuid", uuid).findAll();
                        rows.deleteAllFromRealm();
                        RealmResults<RConversation> conversations = realm.where(RConversation.class).equalTo("UUID", uuid).findAll();
                        conversations.deleteAllFromRealm();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeMessage(String message){
        try{
            rdb.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        RealmResults<RMessage> rows = realm.where(RMessage.class).equalTo("uuid", message).findAll();
                        rows.first().deleteFromRealm();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addConveration(Conversation con){
        RConversation realmCon = new RConversation();
        realmCon.fromModel(con);
        rdb.beginTransaction();
        rdb.insertOrUpdate(realmCon);
        rdb.commitTransaction();
    }

    public void fetchConversation(int page){
        api.pageConversation(page).enqueue(new Callback<ApiResponse<PageResponse<Conversation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<Conversation>>> call, Response<ApiResponse<PageResponse<Conversation>>> response) {
               // response.body().data.content;
                if (response.body() == null || response.body().data == null || response.body().data.content == null) {
                    return;
                }

                if (rdb == null ){
                    Toast.makeText(MainActivity.instance, "Cơ sở dữ liệu realm lỗi trên thiết bị này, ứng dụng không để hoạt động",Toast.LENGTH_LONG).show();
                    return;
                }
                rdb.beginTransaction();
                for (Conversation conversation : response.body().data.content) {
                    RConversation realmCon = new RConversation();
                    realmCon.fromModel(conversation);
                    rdb.insertOrUpdate(realmCon);
                }
                rdb.commitTransaction();
            }
            @Override
            public void onFailure(Call<ApiResponse<PageResponse<Conversation>>> call, Throwable t) {
                EventBus.getInstance().noticShow("Lỗi kết nối","Có lỗi xảy ra");
            }
        });
    }

    public void fetchMessages(long time, String conversationUuid, byte [] key){

        api.pageMessage(conversationUuid,time).enqueue(new Callback<ApiResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Message>>> call, Response<ApiResponse<List<Message>>> response) {
                if (response.body() == null){
                    return;
                }
                if (response.body().data == null){
                    return;
                }

                List<MessagePlaneText> messages = SecureChatSystem.getInstance().decoder(response.body().data, key);

                rdb.beginTransaction();
                for (MessagePlaneText msg : messages) {
                    RMessage rmsg = new RMessage();
                    rmsg.fromModel(msg);
                    rdb.insertOrUpdate(rmsg);
                }
                rdb.commitTransaction();

            }

            @Override
            public void onFailure(Call<ApiResponse<List<Message>>> call, Throwable t) {

            }
        });

    }

    public RealmResults<RConversation>  queryConversation(){
        try {
            return rdb.where(RConversation.class).sort("lastMessageAt", Sort.DESCENDING).findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void saveUser(UserInfo userInfo,String userName){
        if (rdb == null) {
            return ;
        }
        rdb.beginTransaction();
        RUserInfo info = new RUserInfo();
        info.fromModel(userInfo);
        info.userName = userName;
        rdb.insertOrUpdate(info);
        rdb.commitTransaction();
    }

    public UserInfo getUser(String uuid){
        if ( rdb == null){
            return null;
        }
        RUserInfo user =  rdb.where(RUserInfo.class).equalTo("uuid",uuid).findFirst();
        if ( user == null ){
            return null;
        }
       return user.toModel();
    }

    public Conversation getConversationInfo(String uuid) {
        try {
            return rdb.where(RConversation.class).equalTo("UUID", uuid).findFirst().toModel();

        } catch (Exception e){
            return null;
        }
    }

    public void updateConversation(Conversation con){
        rdb.beginTransaction();
        RConversation rCon = new RConversation();
        rCon.fromModel(con);
        rdb.insertOrUpdate(rCon);
        rdb.commitTransaction();
    }

    public void addNewMessage(MessagePlaneText message){
        RMessage rmesg = new RMessage();
        rmesg.fromModel(message);
        rdb.beginTransaction();
        rdb.insertOrUpdate(rmesg);
        rdb.commitTransaction();
    }
    public RealmResults<RMessage> queryMessage(String conversation, Long time ){
        if (time  > 0 )
            return rdb.where(RMessage.class).equalTo("threadUuid",conversation).lessThan("time",time).sort("time",Sort.DESCENDING).findAll();
        else
            return rdb.where(RMessage.class).equalTo("threadUuid",conversation).sort("time",Sort.DESCENDING).findAll();

    }


}

