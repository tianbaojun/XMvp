package com.zjhz.teacher.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.GuideAdapter;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 引导页
 * Created by xiangxue on 2016/6/7.
 */
public class GuideActivity extends BaseActivity {
    List<View> viewList;
    @BindView(R.id.guide_viewpager)
    ViewPager guideViewpager;
    @BindView(R.id.guide_btn)
    ImageView guideBtn;
    @BindView(R.id.cbs)
    CheckBox cbs;
    @BindView(R.id.cbs_layout)
    LinearLayout cbsLayout;
    @BindView(R.id.guide_dots)
    LinearLayout guideDots;
    private final static String TAG = GuideActivity.class.getSimpleName();

    private boolean isChecked= false;
    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initPager();
        guideViewpager.setAdapter(new GuideAdapter(GuideActivity.this,viewList));
        guideViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0 ;i < guideDots.getChildCount() ; i++ ){
                    if (i == position){
                        guideDots.getChildAt(position).setSelected(true);
                        guideBtn.setVisibility(View.VISIBLE);
                        cbsLayout.setVisibility(View.VISIBLE);
                    }else {
                        guideDots.getChildAt(i).setSelected(false);
                        guideBtn.setVisibility(View.GONE);
                        cbsLayout.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @OnCheckedChanged(R.id.cbs)
    public void checked(boolean isCheckeds){
        isChecked = isCheckeds;
    }

    @OnClick({R.id.guide_btn, R.id.cbs_tv})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.guide_btn:
                if (isChecked){
                    SharedPreferencesUtils.putSharePrefBoolean(ConstantKey.isFrist,false);
                    startActivityThenKill(LoginActivity.class);
                }else {
                    ToastUtils.showShort("请同意 《智鹏千校云用户隐私协议》");
                }
                break;
            case R.id.cbs_tv:
                startActivity(AgreementActivity.class);
                break;
        }
    }

    int[] images;
    //初始化ViewPager
    private void initPager(){
        viewList = new ArrayList<View>();
        images = new int[] { R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};
        for (int i = 0; i < images.length; i++) {
            viewList.add(initView(images[i]));
        }
        initDots(images.length);
    }

    private View initView(int res){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_guide, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.iguide_img);
        imageView.setImageBitmap(readBitMap(res));
        return view;
    }

    public Bitmap readBitMap(int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = this.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    //根据页面数初始化指示器
    private void initDots(int count){
        for (int j = 0; j < count; j++) {
            guideDots.addView(initDot());
        }
        guideDots.getChildAt(0).setSelected(true);
    }

    private View initDot(){
        return LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dot, null);
    }

}
