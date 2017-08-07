package com.zjhz.teacher.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class GuideAdapter extends PagerAdapter {

	private List<View> data;
	private Context mContext;
	
	public GuideAdapter(Context mContext, List<View> data)
	{
		super();
		this.data = data;
		this.mContext=mContext;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		container.addView(data.get(position));
		return data.get(position);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(data.get(position));
	}
	
}

