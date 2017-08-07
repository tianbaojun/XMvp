package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.MyRepairsParams;
import com.zjhz.teacher.NetworkRequests.request.WaitRepairsParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.ui.delegate.LeaveApplyForListDelegate;
import com.zjhz.teacher.ui.dialog.LeavePopwindow;
import com.zjhz.teacher.ui.fragment.LeaveApproveFragment;
import com.zjhz.teacher.ui.fragment.LeaveFragment;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;


import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;import butterknife.BindView;

/*
 * 源文件名：LeaveApplyForListActivity
 * 文件版本：1.0.0
 * 创建作者：fei.wang
 * 创建日期：2016-06-28
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：请假管理申请页面
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
public class LeaveApplyForListActivity extends BaseActivity implements LeaveApplyForListDelegate.OnTextViewClick {
    public final static int REFRESH_ONE = 1;
    public final static int REFRESH_TWO = 2;
    private final static String TAG = LeaveApplyForListActivity.class.getSimpleName();
    @BindView(R.id.navigation_header_left)
    public TextView navigationHeaderLeft;
    @BindView(R.id.navigation_header_title)
    public TextView navigationHeaderTitle;
    @BindView(R.id.navigation_header_right)
    public TextView navigationHeaderRight;
    @BindView(R.id.drawer_layout_date_start)
    public TextView drawerLayoutDateStart;
    @BindView(R.id.drawer_layout_date_end)
    public TextView drawerLayoutDateEnd;
    @BindView(R.id.drawer_layout_classes_name)
    public TextView name;
    @BindView(R.id.classname_tv)
    public TextView state;
    @BindView(R.id.drawer_layout_subject_name)
    public TextView subject;
    @BindView(R.id.subjectname_tv)
    public TextView type;
    @BindView(R.id.nv_drawer_layout)
    public LinearLayout nvDrawerLayout;  // 侧拉
    @BindView(R.id.drawer_layout_classes)
    public LinearLayout drawer_layout_classes;
    @BindView(R.id.drawer_layout_subject)
    public LinearLayout drawer_layout_subject;
    @BindView(R.id.drawer_layout_state)
    public LinearLayout drawer_layout_state;
    @BindView(R.id.activity_leave_apply_drawer_layout)
    public DrawerLayout drawerLayout;
    public LeaveFragment leaveFragment1;
    public LeaveApproveFragment leaveFragment;
    public String one = "1";
    LeaveApplyForListDelegate delegate;
    @BindView(R.id.leave_list_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.leave_title_apply)
    TextView apply;
    @BindView(R.id.leave_title_approve)
    TextView approve;
    //添加按钮
    @BindView(R.id.right_addicon)
    ImageView right_addicon;
    //筛选按钮
    @BindView(R.id.right_selecticon)
    ImageView right_selecticon;
    LeavePopwindow window;
    private Fragment[] fragments;
    private TabPagerAdapter mPagerAdapter;
    private  boolean EntryFlag;//true为有审批权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply_for_list);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        delegate = new LeaveApplyForListDelegate(this,this);
        delegate.initialize();
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        leaveFragment1 = new LeaveFragment();
        leaveFragment = new LeaveApproveFragment();
        fragments = new Fragment[]{leaveFragment1, leaveFragment};
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();
        window = new LeavePopwindow(this,delegate);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @OnClick({R.id.leave_title_left, R.id.right_selecticon,R.id.right_addicon,R.id.leave_title_apply,R.id.leave_title_approve})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.leave_title_left:
                finish();
                break;
            case R.id.right_selecticon://筛选
//                window.showPopupWindow(leave_title_right);
                delegate.openDrawer();
                break;
            case R.id.right_addicon://添加
                Intent intent =new Intent(this, LeaveApplyForContentActivity.class);
                intent.putExtra("type","2");
                startActivityForResult(intent, REFRESH_ONE);
                 //window.showPopupWindow(leave_title_right);
                break;
            case R.id.leave_title_apply:
                changeBackground("1");
                break;
            case R.id.leave_title_approve:
                changeBackground("-1");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REFRESH_ONE:
                leaveFragment1.onRefresh();
                break;
            case REFRESH_TWO:
                if(resultCode == RESULT_OK){
                    leaveFragment.onRefresh();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            delegate.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    private void changeBackground(String type){
        if ("1".equals(type)) {
            one = "1";
            apply.setBackgroundResource(R.drawable.yuanjiao_message_white);
            approve.setBackgroundResource(R.drawable.yuanjiao_contacts);
            apply.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
            approve.setTextColor(getResources().getColor(R.color.white));
            right_addicon.setVisibility(View.VISIBLE);
            mViewPager.setCurrentItem(0);
        }else{
            one = "-1";
            apply.setBackgroundResource(R.drawable.yuanjiao_message);
            approve.setBackgroundResource(R.drawable.yuanjiao_contacts_wright);
            apply.setTextColor(getResources().getColor(R.color.white));
            approve.setTextColor(getResources().getColor(R.color.main_bottom_text_color));
            right_addicon.setVisibility(View.GONE);
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onTextViewClick() {
        String start = drawerLayoutDateStart.getText().toString().trim();
        String end = drawerLayoutDateEnd.getText().toString().trim();
        if ("1".equals(one)) {   // 申请
            leaveFragment1.lists.clear();
            leaveFragment1.adapter.notifyDataSetChanged();
//            leaveFragment1.dia.show();
            MyRepairsParams myRepairsParams = new MyRepairsParams(1,15,start+ " " + "00:00:00",end+ " " +"23:59:59");
            LogUtil.e("筛选申请列表请求参数", GsonUtils.toJson(myRepairsParams));
            NetworkRequest.request(myRepairsParams, CommonUrl.LEAVELIST, Config.LEAVELIST); // 得到申请请假列表
        }else if("-1".equals(one)){
            leaveFragment.lists.clear();
            leaveFragment.adapter.notifyDataSetChanged();
//            leaveFragment.dia.show();
            WaitRepairsParams waitRepairsParams = new WaitRepairsParams(start+ " " + "00:00:00",end+ " " +"23:59:59");
            LogUtil.e("筛选审批列表请求参数", GsonUtils.toJson(waitRepairsParams));
            NetworkRequest.request(waitRepairsParams, CommonUrl.LEAVECHECKLIST, Config.LEAVECHECKLIST); // 得到审批请假列表
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
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
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            if (0 == i){
                changeBackground("1");
            }else{
                changeBackground("-1");
            }
        }
    }
}
