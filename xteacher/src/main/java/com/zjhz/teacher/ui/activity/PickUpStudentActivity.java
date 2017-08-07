package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.fragment.PickUpFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.adapter.SimpleFragmentPagerAdapter;

public class PickUpStudentActivity extends BaseActivity {

    @BindView(R.id.pending_approval_tv)
    TextView pendApprovalTv;
    @BindView(R.id.approval_tv)
    TextView approvalTv;
    @BindView(R.id.approval_view_pager)
    ViewPager viewPager;

    private Fragment WSHFragment, YSHFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_student);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        WSHFragment = PickUpFragment.newInstance(PickUpFragment.WSH);
        YSHFragment = PickUpFragment.newInstance(PickUpFragment.YSH);
        fragmentList.add(WSHFragment);
        fragmentList.add(YSHFragment);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(), null, this, fragmentList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    pendApprovalTv.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
                    pendApprovalTv.setBackgroundResource(R.drawable.yuanjiao_message_white);
                    approvalTv.setTextColor(getResources().getColor(R.color.white));
                    approvalTv.setBackgroundResource(R.drawable.yuanjiao_contacts);
                }else {
                    pendApprovalTv.setTextColor(getResources().getColor(R.color.white));
                    pendApprovalTv.setBackgroundResource(R.drawable.yuanjiao_message);
                    approvalTv.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
                    approvalTv.setBackgroundResource(R.drawable.yuanjiao_contacts_wright);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.pending_approval_tv, R.id.approval_tv,R.id.title_back_img})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.pending_approval_tv:
                viewPager.setCurrentItem(0);
                break;
            case R.id.approval_tv:
                viewPager.setCurrentItem(1);
                break;
        }
    }



    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {

    }
}
