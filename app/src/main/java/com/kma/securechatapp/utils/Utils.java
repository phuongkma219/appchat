package com.kma.securechatapp.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kma.securechatapp.Constant;
import com.kma.securechatapp.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.jvm.JvmStatic;
import pereira.agnaldo.previewimgcol.ImageCollectionView;

public final class Utils {

    @JvmStatic
    @BindingAdapter("setEditText")
    public static void setEditText(EditText edt, String string) {
        if (string == null) {
            edt.setText("");
        } else {
            edt.setText(string);
        }
    }

    @JvmStatic
    @BindingAdapter("setTextProfile")
    public static void setTextProfile(TextView tv, String string) {
        if (string == null) {
            tv.setVisibility(View.GONE);
            tv.setText("");
        } else {
            tv.setText(" Học Tại " + string);
        }
    }

    @JvmStatic
    @BindingAdapter("setGender")
    public static void setGender(TextView tv, String string) {
        if (string == null) {
            tv.setText("");
        } else {
            tv.setText(" Giới Tính: " + string);
        }
    }

    @JvmStatic
    @BindingAdapter("setText")
    public static void setText(TextView tv, String string) {
        tv.setText(string);
    }

    @JvmStatic
    @BindingAdapter("setImageColection")
    public static void setImageColection(ImageCollectionView icv, List<String> links) {
       if(links!=null){
           icv.clearImages();
           for (int i = 0; i < links.size(); i++) {
               Glide.with(icv.getContext())
                       .asBitmap()
                       .load(Constant.BASE_URL + links.get(i).replace("\\", "/"))
                       .into(new CustomTarget<Bitmap>() {
                           @Override
                           public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                               icv.addImage(resource);
                           }

                           @Override
                           public void onLoadCleared(@Nullable Drawable placeholder) {

                           }
                       });
           }
       }
       Glide.with(icv.getContext()).clear(icv);
    }

    @JvmStatic
    @BindingAdapter("setLike")
    public static void setLike(TextView tv, String isLike) {
        if (isLike.equals("true")) {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_true, 0, 0, 0);

        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_svgrepo_com, 0, 0, 0);
        }
    }

    @JvmStatic
    @BindingAdapter("setTime")
    public static void setTime(TextView tv, String time) {
        if (time.equals("vừa xong")) {
            tv.setText("vừa xong");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d'T'H:m:s", Locale.ENGLISH);
            try {
                Date dateS = simpleDateFormat.parse(time);
                String dateN = new SimpleDateFormat("y-M-d'T'H:m:s", Locale.getDefault()).format(new Date());
                Long diff = simpleDateFormat.parse(dateN).getTime() - dateS.getTime() - (7 * 60 * 60 * 1000);
                Long diffMinutes = diff / (60 * 1000) % 60;
                Long diffHours = diff / (60 * 60 * 1000);
                if (diffHours > 23) {
                    int date = (int) (diffHours / 24);
                    if (date > 5) {
                        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale("vi", "VN"));
                        SimpleDateFormat formatT = new SimpleDateFormat("HH:mm");
                        tv.setText(format.format(dateS) + " lúc " + formatT.format(dateS));

                    } else {
                        int hour = (int) (diffHours / 24);
                        tv.setText(hour + " ngày trước");
                    }
                } else if (diffHours < 23 && diffHours >= 1) {
                    tv.setText(diffHours + " giờ");

                } else if (diffHours < 1) {
                    if (diffMinutes > 1) {
                        tv.setText(diffMinutes + " phút");
                    } else {
                        tv.setText("vừa xong");

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    public static void loadImage(ImageView iv, String avt) {
        if (avt == null) {
            return;
        }
        Glide.with(iv.getContext())
                .load(Constant.BASE_URL + avt.replace("\\", "/"))
                .placeholder(R.drawable.aodai)
                .error(R.drawable.aodai)
                .into(iv);
    }

    @JvmStatic
    @BindingAdapter("setTotalLike")
    public static void setTotalLike(TextView tv, int total) {
        if (total == 0) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(Integer.toString(total));

        }
    }

    @JvmStatic
    @BindingAdapter("setTotalComment")
    public static void setTotalComment(TextView tv, int total) {
        if (total == 0) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(Integer.toString(total) + " bình luận");
        }
    }

    @JvmStatic
    @BindingAdapter("loadImageFromUri")
    public static void loadImageFromUri(ImageView iv, String avt) {
        Uri uri = Uri.parse(avt);
        if (avt == null) {
            return;
        }
        Glide.with(iv.getContext())
                .load(new File(uri.getPath()))
                .into(iv);
    }

}
