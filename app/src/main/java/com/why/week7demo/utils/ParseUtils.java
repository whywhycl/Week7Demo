package com.why.week7demo.utils;

import com.alibaba.fastjson.JSON;
import com.why.week7demo.beans.Tea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by my on 2016/11/15.
 */
public class ParseUtils {
    public static List<Tea> parseTea(byte[] bytes) {

        try {
            JSONObject jsonObject = new JSONObject(new String(bytes));

            JSONArray array = jsonObject.optJSONArray("data");

            List<Tea> teas = JSON.parseArray(array.toString(), Tea.class);
            return teas;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
