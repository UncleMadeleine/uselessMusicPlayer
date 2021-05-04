package com.example.musicplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicplayer.interfaces.IPlayerControl;
import com.example.musicplayer.interfaces.IViewControl;
import com.example.musicplayer.services.WebService;

import java.io.File;

import static com.example.musicplayer.interfaces.IPlayerControl.play_state_pause;
import static com.example.musicplayer.interfaces.IPlayerControl.play_state_play;
import static com.example.musicplayer.interfaces.IPlayerControl.play_state_stop;

public class WebActivity extends Activity {

    private static String TAG = "WebActivity";
    private PlayerConnection mPlayerConnection;
    private SeekBar mSeekBar;
    private Button mStopButton;
    private Button mPlayButton;
    private IPlayerControl service;
    private IPlayerControl mPlayerControl;
    private boolean isUserTouchProgressBar = false;
    private File sdcardfile = null;
    private Button mInternetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
//        getSDCardFile();
//        check();
        initView();
        initEvent();
        initService();
        initBindServe();
    }

    private void initEvent() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int printProgress = seekBar.getProgress();
                Log.d(TAG, "This is the progress: " + printProgress);
                if (mPlayerConnection != null) {
                    mPlayerControl.seekTo(seekBar.getProgress());
                }
                isUserTouchProgressBar = false;
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerControl.play();
            }
        });



        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerControl.stopPlayer();
            }
        });




        mInternetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebActivity.this,MainActivity.class);
                if (mPlayerConnection != null) {
                    mPlayerControl.unRegisterViewController();
                    unbindService(mPlayerConnection);
                }
                startActivity(intent);
            }
        });
    }





    private void initView() {
        mSeekBar = (SeekBar) this.findViewById(R.id.seek_bar);
        mPlayButton = (Button) this.findViewById(R.id.play_btn);
        mStopButton = (Button) this.findViewById(R.id.stop_btn);
        mInternetButton = (Button) this.findViewById(R.id.Local_btn);
    }


    //开启服务
    private void initService() {
        startService(new Intent(this, WebService.class));
    }


    //绑定服务
    private void initBindServe() {
        Log.d(TAG, "InitBindServes");
        Intent intent = new Intent(this, WebService.class);
        if (mPlayerConnection == null) {
            mPlayerConnection = new PlayerConnection();
        }
        bindService(intent, mPlayerConnection, BIND_AUTO_CREATE);

    }


    public class PlayerConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "ServiceConnected");
            mPlayerControl = (IPlayerControl) service;
            mPlayerControl.setViewControl(mViewControl);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "ServiceDisconnected");
            mPlayerControl = null;
        }
    }


//    private void check() {//动态获取权限
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                Log.d(TAG, "---------------------写权限不够-----------------");
//            }
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
//                Log.d(TAG, "---------------------读权限不够-----------------");
//            }
//            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.RECORD_AUDIO},1);
//            }
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "The program has been finished.");
        if (mPlayerConnection != null) {
            mPlayerControl.unRegisterViewController();
            unbindService(mPlayerConnection);
        }
    }


    private IViewControl mViewControl = new IViewControl() {
        @Override
        public void onStateChange(int state) {
            if(state == play_state_play){
                mPlayButton.setText("暂停");
            }
            else if(state == play_state_pause){
                mPlayButton.setText("播放");
            }
            else if(state == play_state_stop){
                mPlayButton.setText("播放");
            }
        }

        @Override
        public void onSeekChange(final int seek) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isUserTouchProgressBar) {
                        mSeekBar.setProgress(seek);
                    }
                }
            });
        }


    };

//    //康康SD卡在不在
//    private void getSDCardFile() {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//内存卡存在
//            sdcardfile=Environment.getExternalStorageDirectory();//获取目录文件
//            Log.d(TAG,"SDcard has been found.");
//        }else {
//            Toast.makeText(this,"未找到内存卡",Toast.LENGTH_SHORT).show();
//        }
//    }

}
