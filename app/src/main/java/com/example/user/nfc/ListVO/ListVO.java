package com.example.user.nfc.ListVO;

import android.graphics.drawable.Drawable;


public class ListVO {

    private Drawable Menu_picture;

    private String Menu_name;

    private String Hash_tag;


    public Drawable getMenu_picture() {

        return Menu_picture;

    }


    public void setMenu_picture(Drawable img) {

        this.Menu_picture = img;

    }


    public String getMenu_name() {

        return Menu_name;

    }


    public void setMenu_name(String title) {

        this.Menu_name = title;

    }


    public String getHash_tag() {

        return Hash_tag;

    }


    public void setHash_tag(String context) {

        this.Hash_tag = context;

    }

}
