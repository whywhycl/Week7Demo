package com.why.week7demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.softpo.viewpagertransformer.AccordionTransformer;
import com.why.week7demo.adapters.HeadFragmentPagerAdapter;
import com.why.week7demo.fragments.ListFragment;
import com.why.week7demo.utils.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] tabs = new String[]{"头条","百科","资讯","经营","数据"};
    private EditText keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initTabLayout();

        initViewPager();

    }

    private void initViewPager() {
        List<Fragment> data = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Fragment fragment = new ListFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("key", i);
            fragment.setArguments(bundle);

            data.add(fragment);

        }
        FragmentPagerAdapter adapter = new HeadFragmentPagerAdapter(getSupportFragmentManager(),
                data, tabs);

        //对viewPager设置动画效果
        /**
         * DefaultTransformer()：默认的效果
         * FilmPagerTransformer():效果跟网易电影票那个类似，滑动切换
         *AccordionTransformer():翻页效果
         * CubeInTransformer():正方体旋转效果
         * BackgroundToForegroundTransformer():从小到大从右下角出现
         *DepthPageTransformer()：效果一般，层叠出现
         * FlipHorizontalTransformer()：沿着y轴翻转
         *
         */
        mViewPager.setPageTransformer(false,new AccordionTransformer());
        mViewPager.setAdapter(adapter);

    }

    private void initTabLayout() {
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        keyword = (EditText) findViewById(R.id.keyword);
    }

    public void more(View view) {
        mDrawerLayout.openDrawer(GravityCompat.END);

    }

    public void click(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.collect:
                intent.setClass(this, CollectActivity.class);
                break;
            case R.id.history:
                intent.setClass(this, HistoryActivity.class);
                break;
            case R.id.banquan:
                intent.setClass(this, BanquanActivity.class);
                break;
            case R.id.adivce:
                intent.setClass(this, AdviceActivity.class);
                break;
        }
        startActivity(intent);
    }

    public ViewPager getViewPager_main(){
        return mViewPager;
    }

    public void back(View view) {
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    public void search(View view) {
        if (NetWorkUtils.isConnected(this)){
            Intent intent = new Intent();
            String key = keyword.getText().toString().trim();
            intent.putExtra("key", key);
            intent.setClass(this, SearchActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "当前无网络，请在有网的时候搜索", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            exitBy2Click();      //调用双击退出函数
        }

        return false;
    }

    private boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, Toast.LENGTH_LONG); // 如a果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
