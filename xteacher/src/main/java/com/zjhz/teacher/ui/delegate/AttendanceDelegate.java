package com.zjhz.teacher.ui.delegate;

import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.ui.activity.AttendanceActivity;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.zjhz.teacher.utils.BaseUtil.index_leave;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 考勤
 */
public class AttendanceDelegate implements View.OnClickListener{
    AttendanceActivity activity;
    public AttendanceDelegate(AttendanceActivity activity) {
        this.activity = activity;
    }
    public void initialize() {
        initView();
        setUpDrawer();
        initData();
    }

    private void initData() {
        activity.titleTv.setText("学生考勤");
        types.add("全部");
        types.add("入校");
        types.add("出校");
//        types.add("迟到");
//        types.add("早退");
    }

    private void initView() {
        activity.right_icon0.setVisibility(View.VISIBLE);
        activity.right_icon0.setImageResource(R.mipmap.select_icon);
        activity.name.setText("年级班级");
        activity.drawerLayoutSubjectName.setText("出入校");
        activity.relativeLayout.setVisibility(View.GONE);
        activity.linearLayout.setVisibility(View.VISIBLE);

        activity.title.setText("筛选");
        activity.left.setText("取消");
        activity.left.setOnClickListener(this);
        activity.right.setText("确定");
        activity.right.setOnClickListener(this);
        activity.title_back_img.setOnClickListener(this);
        activity.right_icon0.setOnClickListener(this);
        //设置修理状态不可见
        activity.findViewById(R.id.drawer_layout_state).setVisibility(View.GONE);

        activity.findViewById(R.id.drawer_layout_classes).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_subject).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_time).setOnClickListener(this);
        activity.findViewById(R.id.drawer_layout_clear).setOnClickListener(this);
    }

    public void openDrawer() {
        if (activity.drawerLayout == null)
            return;
        activity.drawerLayout.openDrawer(activity.nvDrawerLayout);
        ((TextView)(activity.findViewById(R.id.subjectname_tv_time))).setText(activity.date);
        if(index_leave>=0) {
            ((TextView) (activity.findViewById(R.id.subjectname_tv))).setText(types.get(index_leave));
        }
        if(index1>=0&&index2>=0) {
            ((TextView) (activity.findViewById(R.id.classname_tv))).setText(AppContext.getInstance().gradeBeans.get(index1).getDetail().get(index2).getName());
        }
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
    List<String> types = new ArrayList<>();
    public int index1 = -1,index2 = -1;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                activity.finish();
                break;
            case R.id.navigation_header_left:
                closeDrawer();
                break;
            case R.id.right_icon0:
                openDrawer();
                break;
            case R.id.navigation_header_right:
//                boolean check = false;
                String gradeId = "";
                String classId = "";
                if (SharePreCache.isEmpty(activity.classnameTv.getText().toString().trim())){
                    ToastUtils.showShort("选择年级班级");
                    break;

                }else{
                   gradeId= AppContext.getInstance().gradeBeans.get(index1).getGradeId();
                   classId= AppContext.getInstance().gradeBeans.get(index1).getDetail().get(index2).getClassId();
//                    check = true;
                }

//                types.add("全部");
//                types.add("入校");
//                types.add("出校");
//                types.add("迟到");
//                types.add("早退");
//                10入校
//                00出校
//                11迟到
//                02早退
                if (index_leave != -1){
                    int f = index_leave;
                    if (f == 0){
                        activity.status = 2;
                        activity.flag = 0;
                    }else if (f == 1){
                        activity.status  = 1;
                        activity.flag = 0;
                    }else if (f == 2){
                        activity.status  = 0;
                        activity.flag = 0;
                    }else if (f == 3){
                        activity.status  = 1;
                        activity.flag = 1;
                    }else if (f == 4){
                        activity.status = 0;
                        activity.flag = 2;
                    }
//                    check = true;
                }else {
                    ToastUtils.showShort("选择出入校");
                    break;
                }
                activity.date = activity.time.getText().toString();
                if (SharePreCache.isEmpty(activity.date)){
                    ToastUtils.showShort("选择日期");
                    break;
                }else{
//                    check = true;
                }
//                if(check) {

                    activity.getSchoolRfidData(gradeId, classId);
                    closeDrawer();
//                }else{
//                    ToastUtils.showShort("您还没有选择筛选条件...");
//                }
                break;
            case R.id.drawer_layout_classes:
                selectGradeCls();
                break;
            case R.id.drawer_layout_subject:
                BaseUtil.selectSubject(types,activity,activity.subjectnameTv);
                break;
            case R.id.drawer_layout_time:
                TimeUtil.selectDate(activity,activity.time);
                break;
            case R.id.drawer_layout_clear:
                index_leave = -1;
                activity.status = 0;
                activity.date = "";
                index2 = -1;
                index1 = -1;
                activity.classnameTv.setText("");
                activity.subjectnameTv.setText("");
                activity.time.setText("");
                break;
        }
    }
    private void selectGradeCls() {
        if(AppContext.getInstance().grades.size() > 0 && AppContext.getInstance().clazzs.size() > 0){
            final OptionsPickerView optionsPickerView = new OptionsPickerView(activity);
            optionsPickerView.setPicker((ArrayList)AppContext.getInstance().grades,AppContext.getInstance().clazzs,0,0,0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2,int options3,int options4) {
                    if (AppContext.getInstance().clazzs.get(options1).size() > 0 && options2 < AppContext.getInstance().clazzs.get(options1).size()){
                        activity.classnameTv.setText( AppContext.getInstance().clazzs.get(options1).get(options2));
                        index1 = options1;
                        index2 = options2;
                    }else {
                        activity.classnameTv.setText("");
                        index1 = 0;
                        index2 = 0;
                        ToastUtils.showShort(R.string.please_choose_effcitive_grades_and_classes);
                    }
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.showShort(R.string.data_not_found);
        }
    }
}
