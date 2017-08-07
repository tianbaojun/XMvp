/*
 * 源文件名：HomeWorkPreviewActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：图片公共类   行图片浏览
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.EditImageAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.adapter.ImageFragment;
import com.zjhz.teacher.utils.uk.co.senab.photoview.Bimp;
import com.zjhz.teacher.utils.uk.co.senab.photoview.HackyViewPager;
import com.zjhz.teacher.utils.uk.co.senab.photoview.PhotoView;
import com.zjhz.teacher.utils.uk.co.senab.photoview.PublicWay;
import com.zjhz.teacher.utils.uk.co.senab.photoview.ViewPagerFixed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

public class ImageViewActivity extends BaseActivity {
	private Intent intent;
	@BindView(R.id.rl)
	RelativeLayout rl;
	//返回按钮
	@BindView(R.id.title_back_img)
	TextView titleBackImg;
	//删除按钮
	@BindView(R.id.right_text_image)
	ImageView rightTextImageDel;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	//本地图片
	@BindView(R.id.gallery01)
	ViewPagerFixed viewPagerFixedpager;
	private MyPageAdapter myPageAdapter;
	//远程图片
	@BindView(R.id.pager)
	HackyViewPager hackyViewPager;
	private ImageViewPagerAdapter adapter;
	private EditImageAdapter adapterimg;   //图片选择adapter
	private final static String TAG = ImageViewActivity.class.getSimpleName();
	private Context mContext;
	private PhotoListBean bean;
	private int currentSize = 0;
	RelativeLayout photo_relativeLayout;

	private String imgListStr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		AppContext.getInstance().addActivity(TAG, this);
		ButterKnife.bind(this);
		PublicWay.activityList.add(this);
		mContext = this;

		titleBackImg.setVisibility(View.VISIBLE);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = intent.getIntExtra("position", 0);
		imgListStr = intent.getStringExtra("imgListStr");
		bean = (PhotoListBean) intent.getSerializableExtra("bean");

		if (imgListStr != null && !imgListStr.equals("")) {
			//查看远程图片
			rightTextImageDel.setVisibility(View.GONE);
			List<String> list = new ArrayList<>();
			String[] imglist = imgListStr.split(",");
			for (int i = 0; i < imglist.length; i++) {
				list.add(imglist[i]);
			}
			adapter = new ImageViewPagerAdapter(getSupportFragmentManager(), list);
			hackyViewPager.setAdapter(adapter);
			hackyViewPager.setCurrentItem(position);
			hackyViewPager.setOnPageChangeListener(pageChangeListener);//滑动到哪一张

		} else {
			//查看本地图片
			rightTextImageDel.setVisibility(View.VISIBLE);
			// 为发送按钮设置文字
			viewPagerFixedpager.setOnPageChangeListener(pageChangeListener);//滑动到哪一张
			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
				initListViews(Bimp.tempSelectBitmap.get(i).getImgPath());
//				initListViews(Bimp.tempSelectBitmap.get(i).getBitmap());
			}
			myPageAdapter = new MyPageAdapter(listViews);
			viewPagerFixedpager.setAdapter(myPageAdapter);
			viewPagerFixedpager.setPageMargin(10);
			viewPagerFixedpager.setCurrentItem(position);
		}

	}

	@Override
	protected boolean isBindEventBusHere() {
		return false;
	}

	//	@OnClick({R.id.gallery_back,R.id.send_button,R.id.gallery_del})
	@OnClick({R.id.right_text_image, R.id.title_back_img})
	public void clickEvent(View v) {
		if (ViewTools.avoidRepeatClick(v)) {
			return;
		}
		switch (v.getId()) {
			case R.id.title_back_img:
				finish();
				break;
			case R.id.right_text_image:
				View view1 = LayoutInflater.from(ImageViewActivity.this).inflate(R.layout.hintpop, null);
				TextView textView = (TextView) view1.findViewById(R.id.hint_tvs);
				textView.setText("确认放弃这张图片吗?");
				final PopupWindow popupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
				popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
				popupWindow.setOutsideTouchable(false);
				view1.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
					//确定删除
					@Override
					public void onClick(View view) {
						if (listViews.size() == 1) {
							Bimp.tempSelectBitmap.clear();
							Bimp.max = 0;
							Intent intent = new Intent("data.broadcast.action");
							sendBroadcast(intent);
							finish();
						} else {
							Bimp.tempSelectBitmap.remove(location);
							Bimp.max--;
							viewPagerFixedpager.removeAllViews();
							listViews.remove(location);
							myPageAdapter.setListViews(listViews);
							myPageAdapter.notifyDataSetChanged();
						}
						popupWindow.dismiss();
					}
				});
				view1.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
					//确定删除
					@Override
					public void onClick(View view) {
						popupWindow.dismiss();
					}
				});
				popupWindow.showAtLocation(rl, Gravity.CENTER, 0, 0);
//		}

		break;
	}
}
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

//	private void initListViews(Bitmap bm) {
private void initListViews(String uri) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		Bitmap bitmap=null;
		try {
			bitmap = Bimp.revitionImageSize(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		img.setImageBitmap(bitmap);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}


class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}


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
}

