package com.example.sony.client.getdirectory;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.sony.client.Constants;

public class ApplicationLoader extends Application{
   
	 public static volatile Context applicationContext = null;
	
	
	@Override
    public void onCreate() {
        Log.i(Constants.TAG,"application onCreate");
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
