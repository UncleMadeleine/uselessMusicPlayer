package com.example.musicplayer.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.presenter.SongPresenter;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.AudioColumns.IS_MUSIC;

public class MusicUtils {
    public static final String TAG = "MusicUtils";
    public static List<SongPresenter> getMusicData(Context context) {
        List<SongPresenter> list = new ArrayList<SongPresenter>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, IS_MUSIC);

        Log.d(TAG,"扫描完成");
        if (cursor.moveToFirst()) {
            Log.d(TAG,"音乐文件不为空");
            int cnt=0;
            while (cursor.moveToNext()) {
                Log.d(TAG,"The Songs has been a number of "+cnt+"!");
                ++cnt;
                SongPresenter song = new SongPresenter();
                song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (song.size > 1000 * 800) {
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
                        song.singer = str[0];
                        song.song = str[1];
                    }
                    list.add(song);
                    Log.d(TAG,"Song "+cnt+" has been added to the list.");
                }
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }
}


