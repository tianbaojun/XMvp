/*
 * 源文件名：ClassAndGradeEducationListActivity
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/3
 * 修改作者：captailgodwin
 * 修改日期：2016/11/3
 * 文件描述：班级德育列表
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClassAndGradeEducationListParams;
import com.zjhz.teacher.NetworkRequests.request.PersonEduSpecialParam;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.ClassAndGradeEducationAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.Constants;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClassAndGradeEducation;
import com.zjhz.teacher.bean.DeleteBean;
import com.zjhz.teacher.bean.DeleteListBean;
import com.zjhz.teacher.bean.SubjectBeansBean;
import com.zjhz.teacher.ui.delegate.ClassAndGradeEducationListDelegate;
import com.zjhz.teacher.ui.dialog.ListPopupWindow;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**班级德育列表*/
public class ClassAndGradeEducationListActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, ListPopupWindow.DeleteAction {

    private final static String TAG = ClassAndGradeEducationListActivity.class.getSimpleName();
    @BindView(R.id.activity_class_and_grade_education_list_drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.navigation_header_left)
    public TextView navigationHeaderLeft;
    @BindView(R.id.navigation_header_title)
    public TextView navigationHeaderTitle;
    @BindView(R.id.navigation_header_right)
    public TextView navigationHeaderRight;
    @BindView(R.id.drawer_layout_classes_name)
    public TextView drawerLayoutClassesName;
    @BindView(R.id.classname_tv)
    public TextView classnameTv;
    @BindView(R.id.drawer_layout_subject_name)
    public TextView drawerLayoutSubjectName;
    @BindView(R.id.subjectname_tv)
    public TextView subjectnameTv;
    @BindView(R.id.drawer_layout_date_start)
    public TextView drawerLayoutDateStart;
    @BindView(R.id.drawer_layout_date_end)
    public TextView drawerLayoutDateEnd;
    @BindView(R.id.nv_drawer_layout)
    public LinearLayout nvDrawerLayout;
    public List<SubjectBeansBean> subObj = new ArrayList<>();//德育方案集合
    public List<String> subStr = new ArrayList<>(); //德育方案名称集合
   /* public ArrayList<String> grades = new ArrayList<>();
    public ArrayList<ArrayList<String>> clazzs = new ArrayList<>();
    public ArrayList<StudentsCurrentPositionClassGrade> clazzsGrades = new ArrayList<>();*/
    public String subjectId = "";
    public String gradeId = "";
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.right_icon0)
    ImageView right_icon0;
    @BindView(R.id.right_icon)
    ImageView right_icon;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_class_and_grade_education_list_recyclerview)
    ListView listView;
    @BindView(R.id.activity_class_and_grade_education_list_swipe_refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.layout_class_moral)
    LinearLayout layout_class_moral;
    ListPopupWindow window;
    ClassAndGradeEducationListDelegate delegate;
    private HintPopwindow hintPopwindow;
    private boolean isDelete =false;
    private boolean isVisible = true;
    private ClassAndGradeEducationAdapter adapter;
    private List<ClassAndGradeEducation> datas = new ArrayList<>();

    private DefaultLoadingLayout loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_and_grade_education_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        loadingLayout.setEmptyViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
        titleTv.setText("班级德育");
        right_icon0.setVisibility(View.VISIBLE);
        right_icon.setVisibility(View.VISIBLE);
        AppContext.getInstance().addActivity(TAG,this);

        delegate = new ClassAndGradeEducationListDelegate(this);
        delegate.initialize();
        window = new ListPopupWindow(this,delegate, Constants.MODULE_CALSSMORAL, this);

        adapter = new ClassAndGradeEducationAdapter(this,datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        refreshLayout.setColorSchemeResources(Constance.colors);

        initListener();
        getList();
//        getGradeClass();
        MainActivity.getClassz();
        getCheckProject();
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getList();
        loadingLayout.onLoading();
    }

    @Override
    protected boolean isBindEventBusHere() {return true;}

    @OnClick({R.id.title_back_img, R.id.right_icon0, R.id.right_icon,R.id.right_text})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (isDelete) {
                    isDelete = false;
                    if (adapter != null){
                        adapter.setAnimation(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    finish();
                }
                break;
            //添加
            case R.id.right_icon0:
                startActivity(ClassAndGradeEducationListCheckActivity.class);
                break;
            //更多
            case R.id.right_icon:
                window.showPopupWindow(right_icon);
                break;
            //多项项目删除
            case R.id.right_text:
                ActionDelete();
                break;
        }
    }

    //执行删除操作
    private void ActionDelete() {
        if (hintPopwindow == null) {
            hintPopwindow = new HintPopwindow(this);
            hintPopwindow.setTitleMessage("确认删除此德育项目吗?");
        }
        hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
            @Override
            public void sureClick() {
                hintPopwindow.dismiss();
                dialog.setMessage("正在删除...");
//                dialog.show();

                isDelete = false;
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (adapter != null){
                    List<DeleteBean> lists = adapter.lists;
//                    Log.d("main", String.valueOf(lists.size()));
                    if (lists.size() > 0){
                        DeleteListBean list = new DeleteListBean(lists);
                        dialog.show();
                        NetworkRequest.request(list, CommonUrl.EDUCLASS_SCORE_MODIFY_DELETE_ALL,Config.EDUCLASS_SCORE_MODIFY_DELETE_ALL);
                    }else {
                        ToastUtils.showShort("没有选择项目");
                        adapter.setAnimation(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void cancelClick() {
                hintPopwindow.dismiss();
            }
        });

        if (!hintPopwindow.isShowing()) {
            hintPopwindow.showAtLocation(layout_class_moral, Gravity.CENTER, 0, 0);
        }
    }


    @Override
    public void deleteOnClick() {
        isVisible = true;
        isDelete = true;
        right_icon0.setVisibility(View.GONE);
        right_icon.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText("完成");
        if (adapter != null){
            adapter.setAnimation(true);
            adapter.notifyDataSetChanged();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isDelete) {
                isDelete = false;
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (adapter != null){
                    adapter.setAnimation(false);
                    adapter.notifyDataSetChanged();
                }
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getList(){
        dialog.show();
//        ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams("100","1", TimeUtil.refFormatNowDate());
        ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams("1000","1");
        LogUtil.e("主列表请求参数 = ", GsonUtils.toJson(mClassAndGradeEducationListParams));
        NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.PERSON_EDUCATION_CLAZZ_PROJECT_LIST,Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST);
    }

    private void getCheckProject(){
        dialog.show();
        NetworkRequest.request(new PersonEduSpecialParam(1,"1","100"), CommonUrl.PERSON_EDUCATION_CLAZZ_PROJECT,Config.PERSON_EDUCATION_CLAZZ_PROJECT);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.toast("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.PERSON_EDUCATION_CLAZZ_PROJECT:
                dialog.dismiss();
                JSONObject project = (JSONObject) event.getData();
                LogUtil.e("班级德育方案列表 = ",project.toString());
                if (project != null) {
                    AppContext.getInstance().eProject.clear();
                    AppContext.getInstance().eduProjects.clear();
                    JSONArray data = project.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                SubjectBeansBean mSubjectBeans = new SubjectBeansBean();
                                mSubjectBeans.schemeName = o.optString("schemeName");
                                mSubjectBeans.schemeId = o.optString("schemeId");
                                mSubjectBeans.meterMode = o.optString("meterMode");
                                subStr.add(o.optString("schemeName"));
                                subObj.add(mSubjectBeans);
                                AppContext.getInstance().eProject.add(o.optString("schemeName"));
                                AppContext.getInstance().eduProjects.add(mSubjectBeans);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST://班级德育主列表
                dialog.dismiss();
                JSONObject list = (JSONObject) event.getData();
                LogUtil.e("班级德育主列表 = ",list.toString());
                List<ClassAndGradeEducation> classAndGradeEducationList = GsonUtils.jsonToArray(ClassAndGradeEducation.class,list);
                datas.clear();
                if (classAndGradeEducationList != null && classAndGradeEducationList.size() > 0){
                    datas.addAll(classAndGradeEducationList);
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                }else {
                    loadingLayout.onEmpty();
                }
                break;
            case Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST_SCREEN:
                dialog.dismiss();
                datas.clear();
                listView.requestLayout();
                JSONObject screen = (JSONObject) event.getData();
                LogUtil.e("班级德育主列表筛选 = ",screen.toString());
                List<ClassAndGradeEducation> classAndGradeEducationList1 = GsonUtils.jsonToArray(ClassAndGradeEducation.class,screen);
                if (classAndGradeEducationList1 != null && classAndGradeEducationList1.size() > 0){
                    datas.addAll(classAndGradeEducationList1);
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                }else {
                    loadingLayout.onEmpty();
                }
                break;
            case Config.EDUCLASS_SCORE_MODIFY_DELETE_ALL:
                dialog.dismiss();
                ToastUtils.toast("删除成功");
                LogUtil.e("adapter.positions.size() = ",adapter.positions.size() + "");
                for (int i = 0; i < adapter.positions.size(); i++) {
                    LogUtil.e("fcsacf大啊大大 =",adapter.positions.get(i) + "");
                    ClassAndGradeEducation integer = adapter.positions.get(i);
                    datas.remove(integer);
                }
                adapter.clearList();
                LogUtil.e("集合大小 = ",adapter.positions.size() + "hhh" + adapter.schems.size() + "jjj" + adapter.grades.size() + "ggg" + adapter.times.size());
                adapter.notifyDataSetChanged();
                adapter.setAnimation(false);
                break;
            case "updatecomplete":
                datas.clear();
                dialog.show();
                ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams("1000","1");
                NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.PERSON_EDUCATION_CLAZZ_PROJECT_LIST,Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,ClassAndGradeEducationDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ClassAndGradeEducation",datas.get(i));
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    @Override
    public void onLoad() {
        refreshLayout.setLoading(false);
    }

    @Override
    public void onRefresh() {
        datas.clear();
        dialog.show();

        ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams("1000","1");
//        ClassAndGradeEducationListParams mClassAndGradeEducationListParams = new ClassAndGradeEducationListParams("100","1", TimeUtil.refFormatNowDate());
        NetworkRequest.request(mClassAndGradeEducationListParams, CommonUrl.PERSON_EDUCATION_CLAZZ_PROJECT_LIST,Config.PERSON_EDUCATION_CLAZZ_PROJECT_LIST);
        refreshLayout.setRefreshing(false);
    }
}
