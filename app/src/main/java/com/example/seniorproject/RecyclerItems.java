package com.example.seniorproject;

import android.graphics.Bitmap;

public class RecyclerItems {
    String rObjectId;
    Bitmap rImage;
    String rName;
    String rPrice;

    public RecyclerItems(String rObjectId, Bitmap rImage, String rName, String rPrice) {
        this.rObjectId = rObjectId;
        this.rImage = rImage;
        this.rName = rName;
        this.rPrice = rPrice;
    }
}
