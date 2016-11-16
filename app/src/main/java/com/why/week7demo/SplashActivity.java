package com.why.week7demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private TextView showTime;
    private int time = 3;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 100:

                    time--;

                    showTime.setText(time+"秒");

                    if (time==0){
                        SharedPreferences sp = getSharedPreferences("appConfig", MODE_PRIVATE);

                        boolean isFirst = sp.getBoolean("isFirst", true);

                        if (isFirst){//用户第一次登陆
                            mHandler.sendEmptyMessage(0);
                        }else{//用户使用过，导航界面，不需要再次进入
                            mHandler.sendEmptyMessage(1);
                        }
                    }else{

                        mHandler.sendEmptyMessageDelayed(100, 1000);
                    }


                    break;
                case 0://用户第一次使用，需要跳转到导航界面
                    SplashActivity.this
                            .startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    SplashActivity.this.finish();
                    break;
                case 1://用户直接跳转到主界面
                    SplashActivity.this.
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    SplashActivity.this.finish();
                    break;


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        initView();
        mHandler.sendEmptyMessageDelayed(100, 1000);
    }

    private void initView() {
        showTime = (TextView) findViewById(R.id.showTime);
    }
}
