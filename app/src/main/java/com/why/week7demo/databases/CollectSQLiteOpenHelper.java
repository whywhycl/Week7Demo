package com.why.week7demo.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by my on 2016/11/14.
 */

public class CollectSQLiteOpenHelper extends SQLiteOpenHelper {

    public CollectSQLiteOpenHelper(Context context) {
        super(context, "collect.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table collect(_id integer primary key autoincrement, id varchar,title varchar," +
                "source varchar,wap_thumb varchar,create_time varchar,nickname varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.close();

    }
}
