package com.why.week7demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.why.week7demo.adapters.TeaAdapter;
import com.why.week7demo.beans.Tea;
import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.nets.ByteAsyncTask;
import com.why.week7demo.uri.Uri;
import com.why.week7demo.utils.ParseUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextView keyword;
    private ListView mListView;
    private BaseAdapter adapter;
    private List<Tea> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String keyword = getIntent().getStringExtra("key");

        initView();

        initListView();

        initDate(keyword);

        this.keyword.setText(keyword);

    }

    private void initDate(String keyword) {
        String path = Uri.SEARCH_URL+keyword;
        new ByteAsyncTask(new ByteCallBack() {
            @Override
            public void callback(byte[] bytes) {
                List<Tea> teas = ParseUtils.parseTea(bytes);
                data.addAll(teas);
                adapter.notifyDataSetChanged();
            }
        }).execute(path);
    }

    private void initListView() {
        adapter = new TeaAdapter(this, data);
        mListView.setAdapter(adapter);
    }

    private void initView() {
        keyword = (TextView) findViewById(R.id.keyword);
        mListView = (ListView) findViewById(R.id.listView);

    }

    public void back(View view) {
        this.finish();
    }
}
