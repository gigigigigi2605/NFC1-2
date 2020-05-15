package com.example.user.nfc;

public class Menu {
    int _index, price, visibility, likeCount;
    String picture, name, tag;

    public Menu(int i, int p, int v, int l, String... ss) {
        _index = i;
        price = p;
        visibility = v;
        likeCount = l;
        picture = ss[0];
        name = ss[1];
        tag = ss[2];
    }
}
