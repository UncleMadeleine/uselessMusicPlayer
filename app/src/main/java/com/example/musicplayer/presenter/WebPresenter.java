package com.example.musicplayer.presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import com.example.musicplayer.interfaces.IPlayerControl;
import com.example.musicplayer.interfaces.IViewControl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class WebPresenter extends Binder implements IPlayerControl {
    private IViewControl mViewControl;
    private static final String TAG = "WebPresent";
    private int mCurrentState = play_state_stop;
    private MediaPlayer mMediaPlayer;
    private ShopInfo mShopInfo;
    private Timer mTimer;
    private SeekTimeTask mSeekTimeTask;
    private static final String url_muc = "https://api.uomg.com/api/comments.163?format=json";
//    private static List<String> w_url ;

    @Override
    public void play() {
        Log.d(TAG,"Play!");
        if (mCurrentState == play_state_stop) {
            initPlayer();
            try {
                //TODO
//                mMediaPlayer.stop();
//                mMediaPlayer.reset();
                Log.d(TAG,"InternetWork:?");
//                loadjson();
                mMediaPlayer.setDataSource("http://music.163.com/song/media/outer/url?id=1816721695.mp3");
//                mMediaPlayer.setDataSource(mShopInfo.getUrl());
                Log.d(TAG,"InternetWork:Yes!");
//                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepare();
//                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
                startTimer();
                mCurrentState = play_state_play;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(mCurrentState == play_state_play){
            Log.d(TAG,"Pause!");
            if(mMediaPlayer != null){
                mMediaPlayer.pause();
                mCurrentState = play_state_pause;
            }
            stopTimer();
        }
        else if(mCurrentState == play_state_pause){
            if(mMediaPlayer != null){
                mMediaPlayer.start();
                mCurrentState = play_state_play;
            }
            stopTimer();
        }
        if(mViewControl != null){
            mViewControl.onStateChange(mCurrentState);
        }
    }

    private void initPlayer() {
        if(mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }


    @Override
    public void stopPlayer() {
        Log.d(TAG,"StopPlay!");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mCurrentState = play_state_stop;
            mMediaPlayer.release();
            mMediaPlayer = null;
            if(mViewControl != null){
                mViewControl.onStateChange(mCurrentState);
            }
        }
    }

    @Override
    public void seekTo(int seek) {
        Log.d(TAG,"SeekTo:"+ seek);
        int tarSeek = (int) (seek * mMediaPlayer.getDuration() *1.0f / 100);
        mMediaPlayer.seekTo(tarSeek);
    }


    private class SeekTimeTask extends TimerTask {

        @Override
        public void run() {
            if (mMediaPlayer != null && mViewControl != null) {
                Log.d(TAG,"The TimeTask is : "+mMediaPlayer.getCurrentPosition());
                int cur = mMediaPlayer.getCurrentPosition()*100/mMediaPlayer.getDuration();
                mViewControl.onSeekChange(cur);
            }
        }
    }

    private void startTimer(){
        if( mTimer == null){
            mTimer = new Timer();
        }
        if (mSeekTimeTask == null) {
            mSeekTimeTask = new SeekTimeTask();
        }
        mTimer.schedule(mSeekTimeTask,0,500);
    }

    private  void stopTimer(){
        if (mSeekTimeTask != null) {
            mSeekTimeTask.cancel();
            mSeekTimeTask=null;
        }
        if( mTimer != null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    @Override
    public void setViewControl(IViewControl view) {
        this.mViewControl = view;
    }

    @Override
    public void unRegisterViewController() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mCurrentState = play_state_stop;
            mMediaPlayer.release();
            mMediaPlayer = null;
            if(mViewControl != null){
                mViewControl.onStateChange(mCurrentState);
            }
        }
        this.mViewControl = null;
    }

    public void loadjson(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(url_muc);
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    if (connection.getResponseCode()!=200) {
                        Log.d(TAG,"NetWork is broken.");
                        return;
                    }
                    Log.d(TAG,"Connected!");
//                    Log.d(TAG,"The content is : "+connection.getContent().toString());

                    Gson gson = new Gson();

//                    mShopInfo = gson.fromJson((JsonObject)connection.getContent(), ShopInfo.class);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }




}
