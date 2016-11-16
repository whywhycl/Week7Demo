package com.why.week7demo.nets;

import android.os.AsyncTask;

import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.utils.HttpUtils;

/**
 * Created by my on 2016/11/14.
 */

public class ByteAsyncTask extends AsyncTask<String, Void, byte[]> {

    private ByteCallBack mByteCallBack;

    public ByteAsyncTask(ByteCallBack byteCallBack) {
        mByteCallBack = byteCallBack;
    }

    @Override
    protected byte[] doInBackground(String... params) {

        byte[] bytes = HttpUtils.loadByte(params[0]);

        return bytes;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        super.onPostExecute(bytes);
        mByteCallBack.callback(bytes);
    }
}
