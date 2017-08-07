/*
 * 源文件名：HomeWorkManagerDrawerLayoutPresenter
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：业管理侧拉框，兼容‘筛选’、‘删除’等等其它操作功能
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */

package com.zjhz.teacher.ui.delegate;

import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.HomeWorkManagerActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;

public class HomeWorkManagerDrawerLayoutPresenter implements View.OnClickListener {

    private HomeWorkManagerActivity mWorkManagerActivity;
    private int index_subject = 0,index_classes = 0;

    public HomeWorkManagerDrawerLayoutPresenter(HomeWorkManagerActivity mWorkManagerActivity) {
        this.mWorkManagerActivity = mWorkManagerActivity;
    }

    public void initialize() {
        initView();
        setUpDrawer();
    }

    private void initView() {
        mWorkManagerActivity.navigationHeaderTitle.setText("筛选");
        mWorkManagerActivity.navigationHeaderLeft.setText("取消");
        mWorkManagerActivity.navigationHeaderLeft.setOnClickListener(this);
        mWorkManagerActivity.navigationHeaderRight.setText("确定");
        mWorkManagerActivity.navigationHeaderRight.setOnClickListener(this);
        mWorkManagerActivity.drawerLayoutDateStart.setOnClickListener(this);
        mWorkManagerActivity.drawerLayoutDateEnd.setOnClickListener(this);
        mWorkManagerActivity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
        mWorkManagerActivity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
        mWorkManagerActivity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
        mWorkManagerActivity.drawer_layout_state.setVisibility(View.GONE);//修理状态隐藏
    }

    public void openDrawer() {
        if (mWorkManagerActivity.drawerLayout == null)
            return;
        mWorkManagerActivity.drawerLayout.openDrawer(mWorkManagerActivity.nvDrawerLayout);
    }

    public void closeDrawer() {
        if (mWorkManagerActivity.drawerLayout == null)
            return;
        mWorkManagerActivity.drawerLayout.closeDrawer(mWorkManagerActivity.nvDrawerLayout);
    }

    private void setUpDrawer() {
        if (mWorkManagerActivity.drawerLayout == null) {
            return;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_header_left:
                closeDrawer();
                break;
            case R.id.navigation_header_right:
                mWorkManagerActivity.pageNum = 1;
                mWorkManagerActivity.type = 1;
                mWorkManagerActivity.workManagers.clear();
                mWorkManagerActivity.retrieveData();
                break;
            case R.id.drawer_layout_classes:
                selectClasses();
                break;
            case R.id.drawer_layout_subject:
                selectSubject();
                break;
            case R.id.drawer_layout_date_start:
                BaseUtil.selectDate(mWorkManagerActivity,mWorkManagerActivity.drawerLayoutDateStart);
                break;
            case R.id.drawer_layout_date_end:
                BaseUtil.selectDate(mWorkManagerActivity,mWorkManagerActivity.drawerLayoutDateEnd);
                break;
            case R.id.drawer_layout_clear:
                setTextViewNull();
                break;
        }
//        closeDrawer();
    }

    public void setTextViewNull(){
        mWorkManagerActivity.subjectId = " ";
        mWorkManagerActivity.classId = " ";
        index_subject = 0;
        index_classes = 0;
        mWorkManagerActivity.drawerLayoutDateStart.setText("");
        mWorkManagerActivity.drawerLayoutDateEnd.setText("");
        mWorkManagerActivity.drawerLayoutDateStart.setHint("请选择起始时间");
        mWorkManagerActivity.drawerLayoutDateEnd.setHint("请选择截止时间");

        mWorkManagerActivity.subjectname_tv.setText(" ");
        mWorkManagerActivity.classname_tv.setText(" ");
    }

    /**
     * 选择班级
     */
    private void selectClasses() {
        if (mWorkManagerActivity.classnames.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(mWorkManagerActivity);
            optionsPickerView.setPicker((ArrayList) mWorkManagerActivity.classnames, index_classes, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    mWorkManagerActivity.classname_tv.setText(mWorkManagerActivity.classBeans.get(options1).getName());
                    mWorkManagerActivity.classId = mWorkManagerActivity.classBeans.get(options1).getClassId();
                    index_classes = options1;
                }
            });
            optionsPickerView.show();
        }else {
           ToastUtils.showShort("没有数据");
        }
    }
    /**
     * 选择科目
     */
    private void selectSubject() {
        if (mWorkManagerActivity.subStr.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(mWorkManagerActivity);
            optionsPickerView.setPicker((ArrayList) mWorkManagerActivity.subStr, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    mWorkManagerActivity.subjectname_tv.setText(mWorkManagerActivity.subObj.get(options1).getName());
                    mWorkManagerActivity.subjectId = mWorkManagerActivity.subObj.get(options1).getSubjectId();
                    index_subject = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }

//    /**
//     * 选择日期
//     */
//    private void selectDate(final TextView view) {
//        TimePickerView pvTime = new TimePickerView(mWorkManagerActivity, TimePickerView.Type.YEAR_MONTH_DAY);
//        // 控制时间范围在2016年-20之间,去掉将显示全部
//        Calendar calendar = Calendar.getInstance();
//        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
//        pvTime.setTime(new Date());
//        pvTime.setCyclic(false);// 是否滚动
//        pvTime.setCancelable(true);
//        // 弹出时间选择器
//        pvTime.show();
//        // 时间选择后回调
//        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                view.setText(TimeUtil.getYMD(date));
//            }
//        });
//    }
}
