package com.example.musicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.musicplayer.presenter.WebPresenter;

public class WebService extends Service {
    private static final String TAG = "WebService";
    private WebPresenter mPlayerPresenter;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"PlayServe has been created.");
        if(mPlayerPresenter == null){
                mPlayerPresenter = new WebPresenter();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"OnDestroy");
        mPlayerPresenter = null;
    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"OnBind.");
        return mPlayerPresenter;
    }
}
