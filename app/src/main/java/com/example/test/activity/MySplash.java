package com.example.test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.MainActivity;
import com.example.test.R;

public class MySplash extends AppCompatActivity {
    private ImageView imageView;
    private SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_splash);
        imageView=findViewById(R.id.logo_id);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        imageView.animate().rotation(180).setDuration(3000).start();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if(pref.contains("username") && pref.contains("password")){
                        Intent intent=new Intent(MySplash.this, MyMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                   else {
                        Intent intent=new Intent(MySplash.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        getSupportActionBar().hide();

    }
}
