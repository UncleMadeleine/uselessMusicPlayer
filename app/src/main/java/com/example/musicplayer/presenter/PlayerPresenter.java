package com.example.musicplayer.presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.util.Log;

import com.example.musicplayer.interfaces.IPlayerControl;
import com.example.musicplayer.interfaces.IViewControl;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerPresenter extends Binder implements IPlayerControl {

    private IViewControl mViewControl;
    private static final String TAG = "PlayerPresent";
    private int mCurrentState = play_state_stop;
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private SeekTimeTask mSeekTimeTask;
    private String mPath = "/mnt/sdcard/Music/example.mp3";

    @Override
    public void play() {
        Log.d(TAG,"Play!");
        if (mCurrentState == play_state_stop) {
            initPlayer();
            try {
//                Log.d(TAG,"Start to play!");

                mMediaPlayer.setDataSource(mPath);
                Log.d(TAG,"InternetWork:Yes!");
                mMediaPlayer.prepare();
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
//        Log.d(TAG,"SeekTo:"+ seek);
        int tarSeek = (int) (seek * mMediaPlayer.getDuration() *1.0f / 100);
        mMediaPlayer.seekTo(tarSeek);
    }


    private class SeekTimeTask extends TimerTask{

        @Override
        public void run() {
            if (mMediaPlayer != null && mViewControl != null) {
//                Log.d(TAG,"The TimeTask is : "+mMediaPlayer.getCurrentPosition());
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



    public void setPath(String str){
        mPath = str;
    }



}
