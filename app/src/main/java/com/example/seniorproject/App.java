package com.example.seniorproject;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("M239zkUVm29G0aC8eb2Y9l5B9Uj8r2SDXLuSzvbE")
                // if defined
                .clientKey("xzXFnxPdGt6YypZH7dQKatMWGLZEhnziDM3Sa5Ku")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
