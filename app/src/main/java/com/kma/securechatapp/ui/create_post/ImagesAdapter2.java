package com.kma.securechatapp.ui.create_post;


import com.kma.securechatapp.R;
import com.kma.securechatapp.base.BaseAdapter;
import com.kma.securechatapp.base.BaseListener;
import com.kma.securechatapp.core.api.model.upload_imgae.ItemImage;

public class ImagesAdapter2 extends BaseAdapter<ItemImage> {
    public ImagesAdapter2() {
        super (R.layout.item_image2);
    }
    public interface IMyImage  extends BaseListener
    {
        void onItemRemove(ItemImage image,int position);
    }

}
