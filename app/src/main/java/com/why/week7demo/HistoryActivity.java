package com.why.week7demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.why.week7demo.adapters.CHAdapter;
import com.why.week7demo.beans.Tea;
import com.why.week7demo.databases.HistorySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private HistorySQLiteOpenHelper mHelper;
    private SQLiteDatabase db;
    private ListView mListView;
    private List<Tea> data = new ArrayList<>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mHelper = new HistorySQLiteOpenHelper(this);


        initView();

        initData();

        initListView();

    }

    private void initListView() {
        adapter = new CHAdapter(this, data);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tea tea = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("tea", tea);
                Intent intent = new Intent(HistoryActivity.this, TeaDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                builder.setIcon(R.mipmap.ic_logo)
                        .setTitle(data.get(position).getTitle())
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        builder.setTitle("提示：")
                                .setMessage("确定要删除吗")
                                .setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db = mHelper.getReadableDatabase();
                                db.execSQL("delete from history where id = '"+
                                        data.get(position).getId()+"'");
                                db.close();
                                initData();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        builder.create().show();
                    }
                });

                builder.create().show();

                return true;
            }
        });

    }

    private void initData() {
        db = mHelper.getReadableDatabase();
        String sql = "select * from history";
        Cursor cursor = db.rawQuery(sql, null);//参数二：查询条件
        data.clear();
        while (cursor.moveToNext()) {
            Tea tea = new Tea();
            //获取数据
            tea.setId(cursor.getString(cursor.getColumnIndex("id")));
            tea.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            tea.setSource(cursor.getString(cursor.getColumnIndex("source")));
            tea.setWap_thumb(cursor.getString(cursor.getColumnIndex("wap_thumb")));
            tea.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
            tea.setNickname(cursor.getString(cursor.getColumnIndex("nickname")));

            data.add(tea);
        }
        cursor.close();
        db.close();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
    }

    public void back(View view) {
        this.finish();
    }
}
