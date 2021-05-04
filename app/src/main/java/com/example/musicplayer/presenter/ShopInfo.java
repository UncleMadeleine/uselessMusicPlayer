package com.example.musicplayer.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopInfo {
    private static final String TAG = "ShopInfo";
    // "code": 1,
//         "data": {
//        "name": "隔壁泰山",
//                "url": "http:\/\/music.163.com\/song\/media\/outer\/url?id=862101001.mp3",
//                "picurl": "http:\/\/p1.music.126.net\/pbT0ag5PXJwYzFJ7YklMCA==\/109951163386629013.jpg"
    private String code;
    private String data;

    public ShopInfo(String code, String na) {
        this.code = code;
        this.data = na;
    }

    public ShopInfo() {
    }




    public String getData(){
        return this.data;
    }
    public String getUrl(){
        try {
            //String转JSONObject
            JSONObject result = new JSONObject(this.data);
            //取数据
            String str =  (String)result.get("url");
            Log.d(TAG,"The url is : "+str);
            return str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
