package com.why.week7demo.nets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.why.week7demo.callbacks.BitmapCallback;
import com.why.week7demo.utils.HttpUtils;

/**
 * Created by my on 2016/11/12.
 */

public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private BitmapCallback mCallback;

    public ImageAsyncTask(BitmapCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        byte[] bytes = HttpUtils.loadByte(params[0]);

        Bitmap mBitmap = null;
        if (bytes != null) {
            mBitmap =  BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return mBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        mCallback.callback(bitmap);
    }
}
