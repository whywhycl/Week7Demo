package com.why.week7demo.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by my on 2016/11/12.
 */
public class HeadFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> data;
    private String[] tabs;

    public HeadFragmentPagerAdapter(FragmentManager supportFragmentManager,
                                    List<Fragment> data, String[] tabs) {
        super(supportFragmentManager);
        this.data = data;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
