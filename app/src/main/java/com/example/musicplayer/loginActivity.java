package com.example.musicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class loginActivity extends Activity {

    private EditText mAccount;
    private EditText mPassword;
    private Button mLogin_btn;
    private static final String TAG = "loginActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        intEvent();
    }

    private void intEvent() {
        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String accountText = mAccount.getText().toString();
                String passwordText = mPassword.getText().toString();
                if (TextUtils.isEmpty(accountText)) {
                    Toast.makeText(getApplicationContext(),"你账号忘填了",Toast.LENGTH_SHORT).show();
                    return;
                }
                saveInfo(accountText,passwordText);
                //TODO
//                Intent intent = new Intent(loginActivity.this,MainActivity.class);
                Intent intent = new Intent(loginActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

    }



    private void saveInfo(String s1,String s2){
            File path = this.getFilesDir();
            Log.d(TAG,"Path is : "+path);
            File file = new File(path,"info.txt");
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(s1.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mAccount = this.findViewById(R.id.account);
        mPassword =  this.findViewById(R.id.pwd);
        mLogin_btn = this.findViewById(R.id.login_button);
    }
}
