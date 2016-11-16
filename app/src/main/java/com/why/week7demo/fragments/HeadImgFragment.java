package com.why.week7demo.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.why.week7demo.MainActivity;
import com.why.week7demo.R;
import com.why.week7demo.adapters.HeadImgAdapter;
import com.why.week7demo.beans.HeadImg;
import com.why.week7demo.cache.MyLruCache;
import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.nets.ByteAsyncTask;
import com.why.week7demo.uri.Uri;
import com.why.week7demo.utils.NetWorkUtils;
import com.why.week7demo.utils.SdCardUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.why.week7demo.utils.SdCardUtils.saveFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class HeadImgFragment extends Fragment implements View.OnClickListener {

    private ViewPager mViewPager;
    private TextView mTitle;
    private int position = 0;
    private int lastPosition = 0;
    private View[] indictors;
    private MyLruCache myLruCache;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1://进行ViewPager的切换

                    if (this.hasMessages(1)) {
                        //移除多个Message,保证只有一个
                        this.removeMessages(1);
                    }
                    position++;

                    if (position == 3) {
                        position = 0;
                    }

                    mViewPager.setCurrentItem(position);

                    mHandler.sendEmptyMessageDelayed(1, 3000);
                    break;
                case 2://停止

                    if (this.hasMessages(1)) {
                        //移除了Mesaager，自动停止
                        this.removeMessages(1);
                    }

                    break;
                case 3:

                    //手滑动的时候，页码改变，页码重新赋值
                    position = msg.arg1;

                    this.sendEmptyMessageDelayed(1, 3000);

                    break;
            }
        }
    };

    public HeadImgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_head_img, container, false);

        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        //就是一个链表，相当于List
        //分配了内存了空间的八分之一
        myLruCache = new MyLruCache(maxSize);

        initData(ret);

        return ret;
    }

    private void initData(final View ret) {
        final String root = getContext().getExternalCacheDir().getAbsolutePath();
        if (NetWorkUtils.isConnected(getContext())) {//有网络
            new ByteAsyncTask(new ByteCallBack() {
                @Override
                public void callback(byte[] bytes) {

                    if (bytes != null) {
                        try {
                            //存储到SD卡中
                            boolean isCard = SdCardUtils.saveFile(bytes, root, "headimg");
                            Log.d("flag", "----------------->callback: isCard:" +isCard);

                            JSONObject jsonObject = new JSONObject(new String(bytes));

                            JSONArray array = jsonObject.optJSONArray("data");

                            List<HeadImg> data = JSON.parseArray(array.toString(), HeadImg.class);

                            initViewPager(ret, data);

                            indictors = new View[data.size()];
                            initIndictor(data, ret);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).execute(Uri.HEADERIMAGE_URL);
        } else {//从磁盘获取
            Log.d("flag", "----------------->initData: 无网络");
            String fileName = root + File.separator + "headimg";
            byte[] bytes = SdCardUtils.getByteFromFile(fileName);
            Log.d("flag", "----------------->initData: bytes:" +(bytes==null));
            if (bytes != null) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(bytes));

                    JSONArray array = jsonObject.optJSONArray("data");

                    List<HeadImg> data = JSON.parseArray(array.toString(), HeadImg.class);

                    initViewPager(ret, data);

                    indictors = new View[data.size()];
                    initIndictor(data, ret);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void initIndictor(List<HeadImg> data, View ret) {
        LinearLayout linear = (LinearLayout) ret.findViewById(R.id.indictor);
        for (int i = 0; i < data.size(); i++) {
            View view = new View(getContext());
            view.setBackgroundResource(R.drawable.indictor);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.rightMargin = 10;
            params.topMargin = 5;
            view.setLayoutParams(params);
            view.setEnabled(true);
            linear.addView(view);
        }
        for (int i = 0; i < linear.getChildCount(); i++) {
            indictors[i] = linear.getChildAt(i);
            indictors[i].setTag(i);
            indictors[i].setOnClickListener(this);
        }
        indictors[0].setEnabled(false);
    }

    private void initViewPager(View ret, final List<HeadImg> data) {

        mViewPager = (ViewPager) ret.findViewById(R.id.viewPager);
        mTitle = (TextView) ret.findViewById(R.id.title);
        mTitle.setText(data.get(0).getTitle());
        List<ImageView> imgData = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            final ImageView imageView = new ImageView(getContext());
            final String path = data.get(i).getImage();
            imageView.setTag(path);
            imageView.setImageResource(R.mipmap.ic_launcher);
            Bitmap cacheBitmap = getCache(path, i);
            Log.d("flag", "----------------->initViewPager: cacheBitmap:" +(cacheBitmap==null));
            if (cacheBitmap != null) {
                imageView.setImageBitmap(cacheBitmap);
                Log.d("flag", "----------------->initViewPager: cacheBitmap" );
            } else {

                final int finalI = i;
                new ByteAsyncTask(new ByteCallBack() {
                    @Override
                    public void callback(byte[] bytes) {
                        if (bytes != null && path.equals(imageView.getTag())) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            imageView.setImageBitmap(bitmap);

                            //存起来
                            //存到内存中,第一级缓存
                            myLruCache.put("img"+ finalI,bitmap);

                            //存入磁盘中，第二级缓存
                            String root = getContext().getExternalCacheDir().getAbsolutePath();
                            boolean is = saveFile(bytes, root, "img"+ finalI);
                            Log.d("flag", "----------------->callback: is:" +is);
                        }
                    }
                }).execute(path);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgData.add(imageView);

        }
        PagerAdapter adapter = new HeadImgAdapter(imgData);

        mViewPager.setAdapter(adapter);
        //调用Handler发送消息
        mHandler.sendEmptyMessageDelayed(1, 3000);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitle.setText(data.get(position).getTitle());
                mHandler.sendMessage(Message.obtain(mHandler, 3, position, 0));
                indictors[position].setEnabled(false);//选中状态

                //将之前选中的小圆点变成白色的
                indictors[lastPosition].setEnabled(true);
                //重新设置上一次选中的位置
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING://说明手正在进行拖拽

                        mHandler.sendEmptyMessage(2);

                        break;
                }
            }
        });

        //设置头布局的ViewPager监听事件
        //解决两个ViewPager嵌套滑动冲突问题
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((MainActivity) getContext()).getViewPager_main()
                        .requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private Bitmap getCache(String img, int i) {
        //从缓存中获取数据
        //从内存中获取数据---->myLruCache---->第一级
        img = "img"+i;
        Bitmap bitmap = myLruCache.get(img);
        Log.d("flag", "----------------->getCache: bitmap");
        if (bitmap != null) {
            Log.d("flag", "----------------->getCache: 从内存LruCache中获取数据");
            return bitmap;
        } else {//内存中没有该图片的数据
            //从磁盘获取这个数据----->第二级
            Log.d("flag", "----------------->getCache: 从磁盘取数据");
            String root = getContext().getExternalCacheDir().getAbsolutePath();
            String fileName = root + File.separator + img;
            byte[] bytes = SdCardUtils.getByteFromFile(fileName);
            if (bytes != null) {
                Log.d("flag", "----------------->getCache: 磁盘中有数据");
                Bitmap bitmapSd = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //将磁盘中数据保存到内存中
                myLruCache.put(img, bitmapSd);
                Log.d("flag", "----------------->getCache: 从磁盘获取数据");
                //从Sd卡获取了该图片
                return bitmapSd;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        //根据下标进行区分
        //参数view就要携带下标:view.setTag(index);
        int index = (int) v.getTag();

        //有了索引，切换ViewPager
        mViewPager.setCurrentItem(index);
    }
}
