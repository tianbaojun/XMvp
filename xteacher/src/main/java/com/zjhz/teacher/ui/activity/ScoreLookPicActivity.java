package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zjhz.teacher.NetworkRequests.response.GradeScoreBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.uk.co.senab.photoview.HackyViewPager;
import com.zjhz.teacher.utils.uk.co.senab.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/29.
 */
public class ScoreLookPicActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private GradeScoreBean bean;
    private static final String ISLOCKED_ARG = "isLocked";
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_look_pic);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        bean = (GradeScoreBean) intent.getSerializableExtra("images");
        viewpager.setAdapter(new SamplePagerAdapter());
        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) viewpager).setLocked(isLocked);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("待接口");
            }
        });
    }

    class SamplePagerAdapter extends PagerAdapter {
        String image = "http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg";
        private  final String[] sDrawables = {image, image, image,image,
                image,image,image};

        @Override
        public int getCount() {
            if (bean.getImages() != null && bean.getImages().size() > 0) {
                return bean.getImages().size();
            }else{
                return 0;
            }
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(ScoreLookPicActivity.this).load(bean.getImages().get(position).getDocPath()).placeholder(R.mipmap.new_default).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
