package com.why.week7demo.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by my on 2016/11/16.
 */

public class MyLruCache extends LruCache<String,Bitmap> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    //内存缓存开辟空间
    //一般情况内存是运行内存最大八分之一
    //强引用，GC不是回收强引用
    public MyLruCache(int maxSize) {
        super(maxSize);
    }

    //LruCache链表，
    //当向LruCache存数据（Bitmap），需要告诉LruCache存入的图片多大
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes()*value.getHeight();
    }
}

