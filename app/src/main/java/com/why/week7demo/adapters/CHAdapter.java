package com.why.week7demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.why.week7demo.R;
import com.why.week7demo.beans.Tea;

import java.util.List;

/**
 * Created by my on 2016/11/15.
 */
public class CHAdapter extends BaseAdapter {
    private Context context;
    private List<Tea> data;
    public CHAdapter(Context context, List<Tea> data) {
        this.context = context;
        this.data = data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        ViewHolder holder = null;
        if (convertView != null) {
            ret = convertView;
            holder = (ViewHolder) ret.getTag();
        }else{
            ret = LayoutInflater.from(context).inflate(R.layout.chlist_item, parent, false);
            holder = new ViewHolder();
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

        return ret;
    }

    private static class ViewHolder{
        private TextView title,source,author,time;
    }
}
