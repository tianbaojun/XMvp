package com.zjhz.teacher.ui.delegate;

import android.text.TextUtils;
import android.view.View;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClassAndGradeEducationListParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.ui.activity.ClassAndGradeEducationListActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-09-05
 * Time: 15:57
 * Description: 班级德育
 */
public class ClassAndGradeEducationListDelegate implements View.OnClickListener {
    private ClassAndGradeEducationListActivity activity;

    public ClassAndGradeEducationListDelegate(ClassAndGradeEducationListActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        initView();
        setUpDrawer();
    }

    private void initView() {
        activity.navigationHeaderTitle.setText("筛选");
        activity.navigationHeaderLeft.setText("取消");
        activity.navigationHeaderLeft.setOnClickListener(this);
        activity.navigationHeaderRight.setText("确定");
        activity.navigationHeaderRight.setOnClickListener(this);
        activity.drawerLayoutDateStart.setOnClickListener(this);
        activity.drawerLayoutDateEnd.setOnClickListener(this);
        //设置德育筛选不出现修理状态
        activity.findViewById(R.id.drawer_layout_state).setVisibility(View.GONE);
        activity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);

        activity.drawerLayoutClassesName.setText("德育方案");
        activity.drawerLayoutSubjectName.setText("年级");
    }

    public void openDrawer() {
        if (activity.drawerLayout == null)
            return;
        activity.drawerLayout.openDrawer(activity.nvDrawerLayout);
    }

    public void closeDrawer() {
        if (activity.drawerLayout == null)
            return;
        activity.drawerLayout.closeDrawer(activity.nvDrawerLayout);
    }

    private void setUpDrawer() {
        if (activity.drawerLayout == null) {
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
//                activity.pageNum = 1;
//                activity.type = 1;
//                activity.retrieveData();
                String subjectnameTv = activity.subjectnameTv.getText().toString().trim();
                String classnameTv = activity.classnameTv.getText().toString().trim();
                String drawerLayoutDateStart = activity.drawerLayoutDateStart.getText().toString().trim();
                String drawerLayoutDateEnd = activity.drawerLayoutDateEnd.getText().toString().trim();
                if (!TextUtils.isEmpty(classnameTv) || !TextUtils.isEmpty(subjectnameTv) ||( !TextUtils.isEmpty(drawerLayoutDateStart) && !TextUtils.isEmpty(drawerLayoutDateEnd))) {
                    closeDrawer();
                    activity.dialog.show();
                    ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams(classnameTv,drawerLayoutDateStart,drawerLayoutDateEnd,activity.gradeId,"1","100");
                    LogUtil.e("班级德育主列表筛选 = ", GsonUtils.toJson(mClassAndGradeEducationListParams));
                    NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.PERSON_EDUCATION_CLAZZ_PROJECT_LIST, Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST_SCREEN);
                }else{
                    ToastUtils.toast("筛选条件不完整...");
                }
                break;
            case R.id.drawer_layout_classes:
                selectSubject();
                break;
            case R.id.drawer_layout_subject:
                selectClasses();
                break;
            case R.id.drawer_layout_date_start:
                BaseUtil.selectDate(activity,activity.drawerLayoutDateStart);
                break;
            case R.id.drawer_layout_date_end:
                BaseUtil.selectDate(activity,activity.drawerLayoutDateEnd);
                break;
            case R.id.drawer_layout_clear:
                setTextViewNull();
                break;
        }
    }

    public void setTextViewNull(){
        activity.subjectId = "";
        activity.gradeId = "";
        index_subject = 0;
        index_classes = 0;
        activity.drawerLayoutDateStart.setText("");
        activity.drawerLayoutDateEnd.setText("");
        activity.drawerLayoutDateStart.setHint("请选择起始时间");
        activity.drawerLayoutDateEnd.setHint("请选择截止时间");
        activity.subjectnameTv.setText(" ");
        activity.classnameTv.setText(" ");
    }

    private int index_classes = 0;
    /**选择班级*/
    private void selectClasses() {
        if (AppContext.getInstance().grades.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker((ArrayList) AppContext.getInstance().grades, index_classes, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    activity.subjectnameTv.setText(AppContext.getInstance().gradeBeans.get(options1).getName());
                    activity.gradeId = AppContext.getInstance().gradeBeans.get(options1).getGradeId();
                    index_classes = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }

    private int index_subject = 0;
    /**选择科目*/
    private void selectSubject() {
        if (activity.subStr.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker((ArrayList) activity.subStr, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    activity.classnameTv.setText(activity.subObj.get(options1).schemeName);
                    activity.subjectId = activity.subObj.get(options1).schemeId;
                    index_subject = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }
}
