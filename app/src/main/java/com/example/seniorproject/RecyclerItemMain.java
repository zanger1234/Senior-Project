package com.example.seniorproject;

import android.graphics.Bitmap;

public class RecyclerItemMain {
    String rObjectId;
    Bitmap rImage;
    String rName;
    String rPrice;
    String rDescription;

    public RecyclerItemMain(String rObjectId, Bitmap rImage, String rName, String rPrice, String rDescription) {
        this.rObjectId = rObjectId;
        this.rImage = rImage;
        this.rName = rName;
        this.rPrice = rPrice;
        this.rDescription = rDescription;
    }
}
