package com.why.week7demo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.why.week7demo.beans.Tea;
import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.databases.CollectSQLiteOpenHelper;
import com.why.week7demo.databases.HistorySQLiteOpenHelper;
import com.why.week7demo.nets.ByteAsyncTask;
import com.why.week7demo.uri.Uri;
import com.why.week7demo.utils.NetWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeaDetailActivity extends AppCompatActivity {

    private TextView title,time,source;
    private WebView content;
    private HistorySQLiteOpenHelper historyHelper;
    private SQLiteDatabase db;
    private Tea mTea;
    private CollectSQLiteOpenHelper collectHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_detail);

        initView();

        Bundle extras = getIntent().getExtras();
        mTea = null;
        if (extras != null) {
            mTea = extras.getParcelable("tea");
            initData(mTea);
        }

        addHistory(mTea);

    }

    private void addHistory(Tea tea) {
        historyHelper = new HistorySQLiteOpenHelper(this);
        db = historyHelper.getReadableDatabase();
        String id = tea.getId();
        String title = tea.getTitle();
        String create_time = tea.getCreate_time();
        String nickname = tea.getNickname();
        String source = tea.getSource();
        String wap_thumb = tea.getWap_thumb();

        String titlesql = "select id from history where id = '"+id+"'";
        Cursor cursor = db.rawQuery(titlesql, null);//参数二：查询条件
        List<String> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id1 = cursor.getString(cursor.getColumnIndex("id"));
            ids.add(id1);
        }
        Log.d("flag", "----------------->addHistory: ids:" +ids.toString()+","+ids.size());
        String sql = "insert into history(id,title,source,wap_thumb,create_time,nickname) values(?,?,?,?,?,?)";
        if (ids.size()==0){
            db.execSQL(sql, new Object[]{id,title,source,wap_thumb,create_time,nickname});
        }else{

        }
        db.close();

    }

    private void initData(Tea tea) {
        title.setText(tea.getTitle());
        time.setText(tea.getCreate_time());
        source.setText(tea.getSource());

        if (NetWorkUtils.isConnected(this)){
            //设置，初始化操作
            //1.设置webView本应用加载数据
            content.setWebViewClient(new WebViewClient());
            //2.设置WebView事件js动画效果
            content.setWebChromeClient(new WebChromeClient());
            //3.允许js交互
            content.getSettings().setJavaScriptEnabled(true);
            //4.允许缓存数据
            content.getSettings().setAppCacheEnabled(true);
            //5.允许开启
            content.getSettings().setDomStorageEnabled(true);
            //6.
            content.getSettings().setDatabaseEnabled(true);

            new ByteAsyncTask(new ByteCallBack() {
                @Override
                public void callback(byte[] bytes) {
                    Log.d("flag", "----------------->callback: length" +bytes.length);
                    String data = null;
                    try {
                        JSONObject jsonObject = new JSONObject(new String(bytes));
                        JSONObject object = jsonObject.optJSONObject("data");
                        data = object.optString("wap_content");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    content.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
                }
            }).execute(Uri.CONTENT_URL+tea.getId());
        }else{
            Toast.makeText(this, "当前无网络，请在有网络的情况下在打开页面", Toast.LENGTH_LONG).show();
        }



    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        source = (TextView) findViewById(R.id.source);
        content = (WebView) findViewById(R.id.content);
    }

    public void click(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.share:
                break;
            case R.id.collect:
                addCollect(mTea);
                break;
        }
    }

    private void addCollect(Tea tea) {
        collectHelper = new CollectSQLiteOpenHelper(this);
        db = collectHelper.getReadableDatabase();
        String id = tea.getId();
        String title = tea.getTitle();
        String create_time = tea.getCreate_time();
        String nickname = tea.getNickname();
        String source = tea.getSource();
        String wap_thumb = tea.getWap_thumb();
        String sql = "insert into collect(id,title,source,wap_thumb,create_time,nickname) values(?,?,?,?,?,?)";

        String titlesql = "select id from collect where id = '"+id+"'";
        Cursor cursor = db.rawQuery(titlesql, null);//参数二：查询条件
        List<String> ids = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id1 = cursor.getString(cursor.getColumnIndex("id"));
            ids.add(id1);
        }
        if (ids.size()==0 && ids==null){
            db.execSQL(sql, new Object[]{id,title,source,wap_thumb,create_time,nickname});
            Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "已收藏", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
}
