package com.kma.securechatapp.core.api.model.post;

import java.util.List;

public class Content {
    public List<String> image =null;
    public String text;
    public String type = "IMAGE";


    public Content(List<String> image, String text) {
       if (image ==null){
           this.text = text;
       }
       else {
           this.image = image;
           this.text = text;
       }
    }

    public Content(String text) {
        this.text = text;
    }

    public List<String> getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }
}
