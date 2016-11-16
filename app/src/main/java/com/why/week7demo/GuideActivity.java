package com.why.week7demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.why.week7demo.adapters.GuidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Button to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getSupportActionBar().hide();
        initVeiw();
        initViewPager();
    }

    private void initViewPager() {
        List<ImageView> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            int id = getResources().getIdentifier("slide"+(i+1), "mipmap", getPackageName());

            imageView.setImageResource(id);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            data.add(imageView);
        }

        PagerAdapter adapter = new GuidePagerAdapter(data);

        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==2){
                    to.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initVeiw() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        to = (Button) findViewById(R.id.to);
    }

    public void toMain(View view) {
        //存储记录，用户已经第一次使用
        SharedPreferences sp = getSharedPreferences("appConfig", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean("isFirst", false);//用户不是第一次使用

        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
