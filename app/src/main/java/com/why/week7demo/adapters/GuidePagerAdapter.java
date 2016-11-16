package com.why.week7demo.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by my on 2016/11/12.
 */
public class GuidePagerAdapter extends PagerAdapter {

    private List<ImageView> data;

    public GuidePagerAdapter(List<ImageView> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView ret = data.get(position);

        container.addView(ret);

        return ret;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);

        View ret = (View) object;

        container.removeView(ret);
    }
}
