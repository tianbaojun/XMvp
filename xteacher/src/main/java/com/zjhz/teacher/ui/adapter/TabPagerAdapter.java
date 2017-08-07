package com.zjhz.teacher.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-15
 * Time: 15:57
 * Description: 请假申请列表 适配器
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
    private Fragment[] fragments;
    private ViewPager mViewPager;

    public TabPagerAdapter(FragmentManager fm, Fragment[] fragments, ViewPager mViewPager) {
        super(fm);
        this.fragments = fragments;
        this.mViewPager = mViewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
