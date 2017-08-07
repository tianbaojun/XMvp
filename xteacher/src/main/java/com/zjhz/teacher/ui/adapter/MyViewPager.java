package com.zjhz.teacher.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class MyViewPager extends PagerAdapter{

    private List<View> mList;
    public void setViews(List<View> views) {
        this.mList = views;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return (arg0)==arg1;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(mList.get(position));
        return mList.get(position);
    }
    @Override
    public void destroyItem(View view, int position, Object obj) {
        ((ViewPager) view).removeView(mList.get(position));
    }
}
