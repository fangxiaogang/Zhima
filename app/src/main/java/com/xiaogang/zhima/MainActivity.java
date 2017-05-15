package com.xiaogang.zhima;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Zhima zhima;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zhima = (Zhima) findViewById(R.id.zhima);
        zhima.startRotateAnim();
    }
}
