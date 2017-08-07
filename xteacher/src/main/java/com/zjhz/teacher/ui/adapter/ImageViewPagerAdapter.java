package com.zjhz.teacher.ui.adapter;

/**
 * Created by Administrator on 2016/10/27.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**远程图片浏览适配器
 * Created by csonezp on 15-12-28.
 */
public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String IMAGE_URL = "image";

    List<String> mDatas;

    public ImageViewPagerAdapter(FragmentManager fm, List data) {
        super(fm);
        mDatas = data;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mDatas.get(position);
        Fragment fragment = ImageFragment.newInstance(url);
        return fragment;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}