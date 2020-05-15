package com.example.user.nfc.ListVO;

import android.graphics.drawable.Drawable;


public class ListVO_order_history_purchase_list {

    private String Menu_name;
    private int count;
    private int price;



    public String getMenu_name() {

        return Menu_name;

    }


    public void setMenu_name(String Menu_name) {

        this.Menu_name = Menu_name;

    }

    public int getcount() {
        return count;
    }

    public void setcount(int count) {
        this.count = count;
    }

    public int getpirce(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

}
