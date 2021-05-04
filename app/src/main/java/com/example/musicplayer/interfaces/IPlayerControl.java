package com.example.musicplayer.interfaces;

public interface IPlayerControl {

    int play_state_play =1;
    int play_state_pause =2;
    int play_state_stop =3;


    //TODO
    void play();


    //TODO
    void stopPlayer();

    //TODO
    void seekTo(int seek);

    //TODO
    void setViewControl(IViewControl view);

    //TODO
    void unRegisterViewController();

}
