package com.why.week7demo.nets;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.why.week7demo.beans.Tea;
import com.why.week7demo.callbacks.HeadCallback;
import com.why.week7demo.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/11/12.
 */

public class HeadAsyncTask extends AsyncTask<String, Void, List<Tea>> {

    private HeadCallback mHeadCallback;

    public HeadAsyncTask(HeadCallback headCallback) {
        mHeadCallback = headCallback;
    }

    @Override
    protected List<Tea> doInBackground(String... params) {

        Log.d("flag", "----------------->doInBackground: head" +params[0]);
        byte[] bytes = HttpUtils.loadByte(params[0]);

        List<Tea> data = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(new String(bytes));

            JSONArray array = jsonObject.optJSONArray("data");

            List<Tea> teas = JSON.parseArray(array.toString(), Tea.class);

            data.addAll(teas);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("flag", "----------------->doInBackground: " +data.toString());

        return data;
    }

    @Override
    protected void onPostExecute(List<Tea> teas) {
        super.onPostExecute(teas);
        mHeadCallback.callback(teas);
    }
}
