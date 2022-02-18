package com.kma.securechatapp.adapter.viewholder;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kma.securechatapp.BuildConfig;
import com.kma.securechatapp.MainActivity;
import com.kma.securechatapp.R;
import com.kma.securechatapp.core.api.model.MessagePlaneText;
import com.kma.securechatapp.helper.FileUtils;
import com.kma.securechatapp.ui.control.ImagePreview;
import com.kma.securechatapp.utils.common.EncryptFileLoader;
import com.kma.securechatapp.utils.common.ImageLoader;
import com.kma.securechatapp.utils.common.StringHelper;
import com.kma.securechatapp.utils.misc.AudioUi;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessageSenderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_message_body)
    View txtBody;
    @BindView(R.id.text_message_time)
    TextView txtTime;
    @BindView(R.id.msg_time_status)
    TextView txtLongTime;
    public MessageSenderViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind (MessagePlaneText msg,boolean hideIcon){
        if (msg.type == 1) {
            //ImageLoader.getInstance().DisplayImage(BuildConfig.HOST +msg.mesage+"?type=small",(ImageView)txtBody);
            if (msg.password != null) {
                EncryptFileLoader.getInstance().loadEncryptImage(BuildConfig.HOST + msg.mesage, msg.password, (ImageView) txtBody, null);
            } else {
                ImageLoader.getInstance().DisplayImage(BuildConfig.HOST + msg.mesage, (ImageView) txtBody);
            }

            txtBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // MainActivity.instance.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.HOST +msg.mesage)));
                    Context context = MessageSenderViewHolder.this.itemView.getContext();
                    Intent intent = new Intent( context, ImagePreview.class);
                    try {
                        ImagePreview.bitmap = ((BitmapDrawable) ((ImageView) txtBody).getDrawable()).getBitmap();
                    } catch (Exception e) {

                    }
                    System.out.println(BuildConfig.HOST + msg.mesage);
                    intent.putExtra("url",BuildConfig.HOST + msg.mesage);
                    intent.putExtra("key",msg.password);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)context, (View)txtBody, "imageView");

                    context.startActivity(intent/*,options.toBundle()*/);
                }
            });

        }else if (msg.type == 0){
            ((TextView)txtBody).setText(msg.mesage);
        }else if (msg.type == 2)
        {
            EncryptFileLoader.getInstance()
                    .loadEncryptFile(BuildConfig.HOST + msg.mesage, msg.password, new EncryptFileLoader.EventLoadedEncryptFile() {
                        @Override
                        public void onLoaded(int error, File file) {
                            if ( error == 1 ){
                                return;
                            }
                            ((AudioUi)txtBody).setPath(file);
                        }
                    });


            /*
            ((AudioUi)txtBody).setUrl(BuildConfig.HOST +msg.mesage);
            ((AudioUi)txtBody).addHeader("Authorization","Bearer "+ AppData.getInstance().getToken());

            */
        }
        else if (msg.type == 3)
        {
            if (msg.mesage != null ){
                String[] split = msg.mesage.split("::");
                if (split.length < 2)
                    return;
                int index = Integer.decode(split[1]);
                ImageLoader.getInstance().DisplayImage(ImageLoader.getStickerUrl(split[0],index),(ImageView)txtBody);
            }
        }
        else if (msg.type == 10) {

            String[] values = msg.mesage.split("@@");
            if (values.length > 1) {
                String name = values[1].replaceAll(".crypt","");
                String url =  values[0];
                ((TextView) txtBody).setText(name);
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.instance.getApplicationContext(),"Đang tải xuống tệp tin..", Toast.LENGTH_LONG).show();

                        EncryptFileLoader.getInstance()
                                .loadEncryptFile(BuildConfig.HOST + url, msg.password, new EncryptFileLoader.EventLoadedEncryptFile() {
                                    @Override
                                    public void onLoaded(int error, File file) {
                                        if ( error == 1 ){
                                            Toast.makeText(MainActivity.instance.getApplicationContext(),"Lỗi tải file", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        if (!file.exists()){
                                            Toast.makeText(MainActivity.instance.getApplicationContext(),"Lỗi lưu file", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        DownloadManager downloadmanager = (DownloadManager) MainActivity.instance.getSystemService(Context.DOWNLOAD_SERVICE);
                                        Uri uri =  Uri.fromFile(file);

                                         File destFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),name);

                                        try {
                                            FileUtils.copy(file, destFile);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Toast.makeText(MainActivity.instance.getApplicationContext(),"Lỗi lưu file", Toast.LENGTH_LONG).show();

                                        }

                                        downloadmanager.addCompletedDownload(name, name, true, "application/octet-stream", destFile.getPath(),destFile.length(),true);

                                    }
                                });
                    }
                });
            }
        }


        txtTime.setText(StringHelper.getTimeText(msg.time));
        txtTime.setVisibility(View.GONE);
        txtLongTime.setText(StringHelper.getLongTextChat(msg.time));
        txtLongTime.setVisibility(View.GONE);



        if (msg.type != 1 && msg.type != 2 && msg.type != 10){
            this.txtBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txtTime.getVisibility() == View.GONE)
                        txtTime.setVisibility(View.VISIBLE);
                    else
                        txtTime.setVisibility(View.GONE);
                }
            });
        }
    }
    public void showLongTime(){
        txtLongTime.setVisibility(View.VISIBLE);
    }
}
