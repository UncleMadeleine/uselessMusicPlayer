package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.presenter.Adapter;
import com.example.musicplayer.presenter.MusicUtils;
import com.example.musicplayer.presenter.SongPresenter;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {


    private static final String TAG = "MenuActivity";
    private ListView mListView;
    private List<SongPresenter> list;
    private Adapter adapter;
//    private Button mSong_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initView();
        intEvent();
    }

    private void intEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"跳转！");
                String str=mListView.getItemIdAtPosition(position)+" ";
                Log.d(TAG,"The Item has been clicked : "+str);
//                str=mListView.get
                SongPresenter song = (SongPresenter) mListView.getItemAtPosition(position);
                str=song.path;
                Log.d(TAG,"The Path of song is : "+str);
                Intent intent = new Intent(MenuActivity.this,MainActivity.class);
                intent.putExtra("PATH",str);
                startActivity(intent);
            }
        });


    }

    /**
     * 初始化view
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.main_list_view);
        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this);
        adapter = new Adapter(this,list);
        mListView.setAdapter(adapter);

    }
}  