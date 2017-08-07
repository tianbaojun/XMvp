package com.zjhz.teacher.ui.delegate;

import android.text.TextUtils;
import android.view.View;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationListRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.ui.activity.PersonMoralEducationListActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-02
 * Time: 10:57
 * Description: 个人德育 筛选
 */
public class PersonMoralEducationListDelegate  implements View.OnClickListener {
    private PersonMoralEducationListActivity activity;

    public PersonMoralEducationListDelegate(PersonMoralEducationListActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        initView();
        setUpDrawer();
//        setTextViewNull();
    }

    private void initView() {
        activity.navigationHeaderTitle.setText("筛选");
        activity.navigationHeaderLeft.setText("取消");
        activity.navigationHeaderLeft.setOnClickListener(this);
        activity.navigationHeaderRight.setText("确定");
        activity.navigationHeaderRight.setOnClickListener(this);
        activity.drawerLayoutDateStart.setOnClickListener(this);
        activity.drawerLayoutDateEnd.setOnClickListener(this);
        activity.drawer_layout_subject_name.setText("德育项目");

        activity.findViewById(R.id.drawer_layout_state).setVisibility(View.GONE);
        activity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
//        activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
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
                String clazzName = activity.classname_tv.getText().toString().trim(); // 班级
                String project = activity.subjectname_tv.getText().toString().trim(); // 项目
                String subId = activity.maps.get(activity.subjectname_tv.getText().toString().trim());  // 项目id
                String startTime = activity.drawerLayoutDateStart.getText().toString().trim();
                String endTime = activity.drawerLayoutDateEnd.getText().toString().trim();
                if (!TextUtils.isEmpty(clazzName) || !TextUtils.isEmpty(project) ||( !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime))) {
                    closeDrawer();
                    activity.dialog.show();
                    PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(endTime,startTime,String.valueOf("1"),"50",clazzId,subId);
                    LogUtil.e("个人德育列表筛选请求参数", GsonUtils.toJson(personMoralEducationListRequest));
                    NetworkRequest.request(personMoralEducationListRequest, CommonUrl.PERSON_EDUCATION, Config.PERSON_EDUCATION_SCREEN);
                }else{
                    ToastUtils.toast("筛选条件不全...");
                }
                break;
            case R.id.drawer_layout_classes:
                selectGradeCls();
                break;
            case R.id.drawer_layout_subject:
                selectSubject();
                break;
            case R.id.drawer_layout_date_start:
                BaseUtil.selectDate(activity,activity.drawerLayoutDateStart);
                break;
            case R.id.drawer_layout_date_end:
                BaseUtil.selectDate(activity,activity.drawerLayoutDateEnd);
                break;
            case R.id.drawer_layout_clear:
//                activity.drawerLayoutDateStart.setText("");
//                activity.drawerLayoutDateEnd.setText("");
//                activity.subjectname_tv.setText("");
//                activity.classname_tv.setText("");
//                EventBus.getDefault().post(new EventCenter<String>("clear"));
//                LogUtil.e("点击清除按钮");
                break;
        }
    }

    public void setTextViewNull(){
        activity.drawerLayoutDateStart.setText("");
        activity.drawerLayoutDateEnd.setText("");
        activity.subjectname_tv.setText("");
        activity.classname_tv.setText("");
    }

    int index1 = 0,index2 = 0;
    String clazzId;
    /**选择班级*/
    private void selectGradeCls() {
        if(AppContext.getInstance().grades.size() > 0 && AppContext.getInstance().clazzs.size() > 0){
            final OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker(AppContext.getInstance().grades,AppContext.getInstance().clazzs,index1,index2,0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2,int options3,int options4) {
                    if (AppContext.getInstance().clazzs.get(options1).size() > 0 && options2 < AppContext.getInstance().clazzs.get(options1).size()){
                        activity.classname_tv.setText( AppContext.getInstance().clazzs.get(options1).get(options2));
                        clazzId = AppContext.getInstance().gradeBeans.get(options1).getDetail().get(options2).getClassId();
                        index1 = options1;
                        index2 = options2;
                    }else {
                        activity.classname_tv.setText("");
                        index1 = 0;
                        index2 = 0;
                        ToastUtils.showShort("班级为空，不能选择");
                    }
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }

    int index_subject = 0;
    /**选择科目*/
    private void selectSubject() {
        if (activity.subStrs.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker((ArrayList) activity.subStrs, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    activity.subjectname_tv.setText(activity.subStrs.get(options1));
//                    activity.subjectId = activity.subObj.get(options1).getSubjectId();
                    index_subject = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }
}
