package com.why.week7demo.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.why.week7demo.R;
import com.why.week7demo.TeaDetailActivity;
import com.why.week7demo.adapters.TeaAdapter;
import com.why.week7demo.beans.Tea;
import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.nets.ByteAsyncTask;
import com.why.week7demo.uri.Uri;
import com.why.week7demo.utils.NetWorkUtils;
import com.why.week7demo.utils.ParseUtils;
import com.why.week7demo.utils.SdCardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private PullToRefreshListView mPullToRefreshListView;
    private BaseAdapter adapter;
    private List<Tea> data = new ArrayList<>();
    private int page = 0;
    private String mPath;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 198:
                    //刷新停止
                    if (mPullToRefreshListView.isRefreshing()) {
                        mPullToRefreshListView.onRefreshComplete();

                        /*String time = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
                                DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
                        */
                    }
                    break;
            }
        }
    };

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_head, container, false);

        int key = 0;

        Bundle arguments = getArguments();

        if (arguments != null) {
            key = arguments.getInt("key");
        }

        initPullToRefreshView(ret, key);

        initData(key);

        initTop(ret);
        return ret;
    }

    private void initTop(View ret) {
        ImageView top = (ImageView) ret.findViewById(R.id.top);
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView listView = mPullToRefreshListView.getRefreshableView();
                listView.setSelection(0);
            }
        });
    }


    private void initData(int key) {
        mPath = null;
        final String root = getContext().getExternalCacheDir().getAbsolutePath();
        String file = null;
        switch (key) {
            case 0:
                mPath = Uri.HEADLINE_URL + Uri.HEADLINE_TYPE + page;
                file = Uri.HEADLINE_TYPE + page;
                break;
            case 1:
                mPath = Uri.BASE_URL + Uri.CYCLOPEDIA_TYPE + page;
                file = Uri.CYCLOPEDIA_TYPE + page;
                break;
            case 2:
                mPath = Uri.BASE_URL + Uri.CONSULT_TYPE + page;
                file = Uri.CONSULT_TYPE + page;
                break;
            case 3:
                mPath = Uri.BASE_URL + Uri.OPERATE_TYPE + page;
                file = Uri.OPERATE_TYPE + page;
                break;
            case 4:
                mPath = Uri.BASE_URL + Uri.DATA_TYPE + page;
                file = Uri.DATA_TYPE + page;
                break;
        }

        if (NetWorkUtils.isConnected(getContext())){//有网络
            final String finalFile = file;
            new ByteAsyncTask(new ByteCallBack() {
                @Override
                public void callback(byte[] bytes) {

                    if (bytes!=null) {
                        //保存到磁盘
                        SdCardUtils.saveFile(bytes, root, finalFile);

                        List<Tea> teas = ParseUtils.parseTea(bytes);
                        data.addAll(teas);
                        adapter.notifyDataSetChanged();
                    }
                }
            }).execute(mPath);
        }else{//从磁盘获取

            String fileName = root+ File.separator+file;
            byte[] bytes = SdCardUtils.getByteFromFile(fileName);
            if (bytes!=null){
                List<Tea> teas = ParseUtils.parseTea(bytes);
                data.addAll(teas);
                adapter.notifyDataSetChanged();
            }
        }


    }

    private void initPullToRefreshView(View ret, final int key) {
        mPullToRefreshListView = (PullToRefreshListView)
                ret.findViewById(R.id.pullTORefreshListView);

        Log.d("flag", "----------------->initListView: key" + key);
        final ListView listView = mPullToRefreshListView.getRefreshableView();
        if (key == 0) {
            View headView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_head, listView, false);

            HeadImgFragment fragment = new HeadImgFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.headFrame, fragment, null)
                    .commit();

            listView.addHeaderView(headView);

        }

        adapter = new TeaAdapter(getContext(), data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        //设置下拉刷新的监听事件
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            //向下拉
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                data.clear();
                page = 0;
                initData(key);
                mHandler.sendEmptyMessage(198);
            }

            //向上拉刷新
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData(key);
                mHandler.sendEmptyMessage(198);
            }
        });

        //设置刷新模式
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Tea tea = null;
                if (key==0){
                    tea = data.get(position - 2);
                }else{
                    tea = data.get(position);
                }
                bundle.putParcelable("tea", tea);
                Intent intent = new Intent(getContext(), TeaDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setIcon(R.mipmap.icon_dialog)
                        .setTitle("亲，确定要删除吗？")
                        .setNegativeButton("取消", null);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //动画从右向左移除,补间动画
                        //获取屏幕的宽度
                        int width = getContext().getResources().getDisplayMetrics().widthPixels / 3;
                        TranslateAnimation translate = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, -1,//移除的宽度因为是相对于自己空间，所以只要弄个-1就可以了
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);

                        translate.setDuration(2000);
                        view.startAnimation(translate);

                        //动画的监听
                        translate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {//启动

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {//结束
                                //将数据删除，并且更新适配器
                                if (key==0){
                                    data.remove(position-2);
                                }else{
                                    data.remove(position);
                                }
                                Log.d("flag", "----------------->onAnimationEnd: " +data.toString());
                                adapter.notifyDataSetChanged();


                                int count = listView.getChildCount();

                                AnimationSet set = new AnimationSet(true);

                                //渐变
//                                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//                                alphaAnimation.setDuration(1000);

                                TranslateAnimation transAnimation = new TranslateAnimation(
                                        Animation.RELATIVE_TO_SELF, 0,
                                        Animation.RELATIVE_TO_SELF, 0,//移除的宽度因为是相对于自己空间，所以只要弄个-1就可以了
                                        Animation.RELATIVE_TO_SELF, 1,
                                        Animation.RELATIVE_TO_SELF, 0);
                                transAnimation.setDuration(1000);
                                set.addAnimation(transAnimation);

                                int currentTop = view.getTop();
                                for (int i = 0; i < count; i++) {

                                    View itemView = listView.getChildAt(i);

                                    //通过删除的item的top位置来设置删除位置的下面的item的动画效果
                                    //为什么不能用position？因为有复用问题
                                    if (itemView.getTop() >= currentTop) {
                                        itemView.startAnimation(set);
                                    }


                                }


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });

                builder.create().show();
                return true;
            }
        });


    }



}
