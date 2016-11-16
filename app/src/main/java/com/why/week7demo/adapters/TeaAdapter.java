package com.why.week7demo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.why.week7demo.R;
import com.why.week7demo.beans.Tea;
import com.why.week7demo.cache.MyLruCache;
import com.why.week7demo.callbacks.ByteCallBack;
import com.why.week7demo.nets.ByteAsyncTask;
import com.why.week7demo.utils.SdCardUtils;

import java.io.File;
import java.util.List;

/**
 * Created by my on 2016/11/12.
 */
public class TeaAdapter extends BaseAdapter {

    private Context context;
    private List<Tea> data;
    private MyLruCache myLruCache;

    public TeaAdapter(Context context, List<Tea> data) {
        this.context = context;
        this.data = data;
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
        //就是一个链表，相当于List
        //分配了内存了空间的八分之一
        myLruCache = new MyLruCache(maxSize);
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View ret = null;
        ViewHolder holder = null;
        if (convertView != null) {
            ret = convertView;
            holder = (ViewHolder) ret.getTag();
        }else{
            ret = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView) ret.findViewById(R.id.icon);
            holder.title = (TextView) ret.findViewById(R.id.title);
            holder.time = (TextView) ret.findViewById(R.id.time);
            holder.source = (TextView) ret.findViewById(R.id.source);
            holder.author = (TextView) ret.findViewById(R.id.author);
            ret.setTag(holder);
        }

        Tea tea = data.get(position);

        holder.title.setText(tea.getTitle());
        holder.source.setText(tea.getSource());
        holder.author.setText(tea.getNickname());
        holder.time.setText(tea.getCreate_time());

        final String path = tea.getWap_thumb();

        if (path != null && !"".equals(path)) {

            final ViewHolder finalHolder = holder;
            finalHolder.icon.setTag(path);
            finalHolder.icon.setVisibility(View.VISIBLE);
            //从缓存中获取数据：第一级和第二级
            Bitmap cacheBitmap = getCache(path, position);
            if (cacheBitmap!=null){
                holder.icon.setImageBitmap(cacheBitmap);
            }else{
                new ByteAsyncTask(new ByteCallBack() {
                    @Override
                    public void callback(byte[] bytes) {
                        String iconPath = (String) finalHolder.icon.getTag();
                        if (bytes!=null && path.equals(iconPath)){
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            finalHolder.icon.setImageBitmap(bitmap);

                            //存起来
                            //存到内存中,第一级缓存
                            myLruCache.put("listImg"+position,bitmap);

                            //存入磁盘中，第二级缓存
                            String root = context.getExternalCacheDir().getAbsolutePath();
                            SdCardUtils.saveFile(bytes,root,"listImg"+position);
                        }
                    }
                }).execute(path);
            }

        }else{
            holder.icon.setVisibility(View.GONE);

        }

        return ret;
    }

    private Bitmap getCache(String img, int position) {
        //从缓存中获取数据
        //从内存中获取数据---->myLruCache---->第一级
        img = "listImg"+position;
        Bitmap bitmap = myLruCache.get(img);
        if (bitmap != null) {
            Log.d("flag", "----------------->getCache: 从内存LruCache中获取数据");
            return bitmap;
        }else {//内存中没有该图片的数据
            //从磁盘获取这个数据----->第二级
            String root = context.getExternalCacheDir().getAbsolutePath();
            String fileName = root+ File.separator+img;
            byte[] bytes = SdCardUtils.getByteFromFile(fileName);
            if (bytes != null) {
                Bitmap bitmapSd = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                //将磁盘中数据保存到内存中
                myLruCache.put(img,bitmapSd);
                Log.d("flag", "----------------->getCache: 从磁盘获取数据");
                //从Sd卡获取了该图片
                return bitmapSd;
            }
        }
        return null;
    }

    private static class ViewHolder{
        private ImageView icon;
        private TextView title,source,author,time;
    }

}
