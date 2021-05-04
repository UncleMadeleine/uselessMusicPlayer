package com.example.musicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.musicplayer.interfaces.IPlayerControl;
import com.example.musicplayer.interfaces.IViewControl;
import com.example.musicplayer.presenter.PlayerPresenter;

public class PlayerService extends Service {

    private static final String TAG = "PlayService";
    private PlayerPresenter mPlayerPresenter;
    private String mPath = "/mnt/sdcard/Music/example.mp3";

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"PlayServe has been created.");
        if(mPlayerPresenter == null){
            mPlayerPresenter = new PlayerPresenter();
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"OnDestroy");
        mPlayerPresenter = null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        mPath = "/mnt/sdcard/Music/example.mp3";
        Log.d(TAG,"Unbind!!!!The path is : "+mPath);
        super.onUnbind(intent);
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mPath = intent.getStringExtra("PATH");
        Log.d(TAG,"OnBind.The Path is : "+mPath);
        mPlayerPresenter.setPath(mPath);
        return mPlayerPresenter;
    }

    @Override
    public void onRebind(Intent intent) {
//        super.onRebind(intent);
        Log.d(TAG,"OnRebind.The last Path is : "+mPath);
        mPath = intent.getStringExtra("PATH");
        Log.d(TAG,"OnRebind.The Path is : "+mPath);
        mPlayerPresenter.setPath(mPath);
        super.onRebind(intent);
    }
}
