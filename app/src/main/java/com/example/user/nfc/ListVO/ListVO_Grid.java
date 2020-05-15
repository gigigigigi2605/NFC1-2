package com.example.user.nfc.ListVO;

import android.graphics.drawable.Drawable;


public class ListVO_Grid {

    private Drawable Menu_picture_left;
    private Drawable Menu_picture_right;

    private String Menu_name_left;
    private String Menu_name_right;


    public Drawable getMenu_picture_left() {

        return Menu_picture_left;

    }


    public void setMenu_picture_left(Drawable img_1) {

        this.Menu_picture_left = img_1;

    }

    public  Drawable getMenu_picture_right() {
        return Menu_picture_right;
    }

    public void setMenu_picture_right(Drawable img_2) {
        this.Menu_picture_right = img_2;
    }


    public String getMenu_name_left() {

        return Menu_name_left;

    }


    public void setMenu_name_left(String title_1) {

        this.Menu_name_left = title_1;

    }

    public String getMenu_name_right() {
        return Menu_name_right;
    }

    public void setMenu_name_right(String title_2) {
        this.Menu_name_right = title_2;
    }

}
