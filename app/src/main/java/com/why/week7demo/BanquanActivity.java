package com.why.week7demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BanquanActivity extends AppCompatActivity {

    private TextView content,more;
    private ImageView moreIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banquan);
        initView();
    }

    private void initView() {
        content = (TextView) findViewById(R.id.content);
        more = (TextView) findViewById(R.id.more);
        moreIcon = (ImageView) findViewById(R.id.moreicon);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void more(View view) {
        int maxLines = content.getMaxLines();

        if (maxLines==2){
            content.setMaxLines(5);
            more.setText("收起");
            moreIcon.setImageResource(R.mipmap.backbtn);
        }else{
            content.setMaxLines(2);
            more.setText("更多");
            moreIcon.setImageResource(R.mipmap.morebtn);
        }
    }
}
