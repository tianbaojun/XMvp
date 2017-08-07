/*
 * 源文件名：PersonMoralEducationListActivity
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/3
 * 修改作者：captailgodwin
 * 修改日期：2016/11/3
 * 文件描述：个人德育列表
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */

package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeletePersonEduListBean;
import com.zjhz.teacher.NetworkRequests.request.PersonMoralEducationListRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonProjectParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.PersonMoralEducationListAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.Constants;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.PersonMoralEducationInputeBean;
import com.zjhz.teacher.bean.PersonMoralEducationListBean;
import com.zjhz.teacher.ui.delegate.PersonMoralEducationListDelegate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.BindView;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**
 * 个人德育列表
 */

public class PersonMoralEducationListActivity extends BaseActivity implements RefreshLayout.OnLoadListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, ListPopupWindow.DeleteAction {
    private final static String TAG = PersonMoralEducationListActivity.class.getSimpleName();
    @BindView(R.id.activity_more_education_drawer_layout)
    public DrawerLayout drawerLayout;
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
    @BindView(R.id.nv_drawer_layout)
    public LinearLayout nvDrawerLayout;  // 侧拉
    @BindView(R.id.classname_tv)
    public TextView classname_tv;
    @BindView(R.id.drawer_layout_subject_name)
    public TextView drawer_layout_subject_name;
    @BindView(R.id.subjectname_tv)
    public TextView subjectname_tv;
    public ArrayList<String> subStrs = new ArrayList<>();
    public Map<String, String> maps = new HashMap<>();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon0)
    ImageView right_icon0;
    @BindView(R.id.right_icon)
    ImageView right_icon;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout swipeLayout;
    @BindView(R.id.layout_person_moral)
    LinearLayout layout_person_moral;
    PersonMoralEducationListDelegate delegate;
    PersonMoralEducationListAdapter adapter;
    String page = "1";
    String pageSize = "1000";
    ListPopupWindow window;
    private DefaultLoadingLayout loadingLayout;
    private List<PersonMoralEducationListBean> lists = new ArrayList<>();
    private boolean isDelete = false;
    private boolean isVisible = true;
    private HintPopwindow hintPopwindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_moraledu);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);

        delegate = new PersonMoralEducationListDelegate(this);
        delegate.initialize();

        window = new ListPopupWindow(this, delegate, Constants.MODULE_PERSONMORAL, this);

        initView();
        initData();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private void initView() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, swipeLayout);
        titleTv.setText("个人德育");
        right_icon0.setVisibility(View.VISIBLE);
        right_icon.setVisibility(View.VISIBLE);
        refreshListview.setOnItemClickListener(this);
        initRefreshLayout();
    }

    private void initData() {
        adapter = new PersonMoralEducationListAdapter(lists, this);
        refreshListview.setAdapter(adapter);
        MainActivity.getClassz();
        dialog.show();
//        NetworkRequest.request(null, CommonUrl.gradeClsList, Config.gradeClsListedu);
        PersonProjectParams personProjectParams = new PersonProjectParams("1", "1", "1", "1000");
        NetworkRequest.request(personProjectParams, CommonUrl.PERSON_EDUCATION_SPECIAL_LIST, Config.PERSON_EDUCATION_SPECIAL_LIST_SCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();
        lists.clear();
        PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(pageSize, page);
        LogUtil.e("个人德育列表请求参数", GsonUtils.toJson(personMoralEducationListRequest));
        NetworkRequest.request(personMoralEducationListRequest, CommonUrl.PERSON_EDUCATION, Config.PERSON_EDUCATION);
    }


    @OnClick({R.id.title_back_img, R.id.right_icon0, R.id.right_icon, R.id.right_text, R.id.drawer_layout_clear})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:   //回退
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (isDelete) {
                    isDelete = false;
                    if (adapter != null) {
                        adapter.setAnimation(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.right_icon0:   //新增
                startActivity(PersonMoralEducationContentActivity.class);
                break;
            case R.id.right_icon:   //更多选项框显示
                window.showPopupWindow(right_icon);
                break;
            case R.id.right_text:   //多项项目删除
                ActionDelete();
                break;
            case R.id.drawer_layout_clear:
                LogUtil.e("---=====-==-");
                drawerLayoutDateStart.setText("");
                drawerLayoutDateEnd.setText("");
                drawerLayoutDateStart.setHint("请选择起始时间");
                drawerLayoutDateEnd.setHint("请选择截止时间");
                subjectname_tv.setText(" ");
                classname_tv.setText(" ");
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
                if (adapter != null) {
                    List<String> lists = adapter.lists;
                    if (lists.size() > 0) {
                        String moralManIds = "";
                        for (int i = 0; i < lists.size(); i++) {
                            moralManIds += lists.get(i) + ",";
                        }
                        moralManIds = moralManIds.substring(0, moralManIds.length() - 1);
                        DeletePersonEduListBean list = new DeletePersonEduListBean(moralManIds);
                        dialog.show();
                        NetworkRequest.request(list, CommonUrl.EDUPERSON_DELETE_ALL, Config.EDUPERSON_DELETE_ALL);
                    } else {
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
            hintPopwindow.showAtLocation(layout_person_moral, Gravity.CENTER, 0, 0);
        }


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
            case Config.PERSON_EDUCATION:
                dialog.dismiss();
                JSONObject list = (JSONObject) event.getData();
                if (list != null) {
                    JSONArray data = list.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                PersonMoralEducationListBean personMoralEducationListBean = new PersonMoralEducationListBean();
                                personMoralEducationListBean.time = o.optString("checkTime").substring(0, 11);
                                personMoralEducationListBean.clazz = o.optString("className");
                                personMoralEducationListBean.name = o.optString("targetorName");
                                personMoralEducationListBean.sub = o.optString("moralName");
                                personMoralEducationListBean.id = o.optString("moralManId");
                                personMoralEducationListBean.meterMode = o.optInt("meterMode");
                                personMoralEducationListBean.createUser = o.optString("createUser");
                                lists.add(personMoralEducationListBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if(adapter.getCount()>0) {
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                }else{
                    loadingLayout.onEmpty();
                }
                break;
           /* case Config.gradeClsListedu:
                JSONObject json = (JSONObject) event.getData();
                if (json != null) {
                    JSONArray data = json.optJSONArray("data");
                    if (data != null && data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data.get(i);
                                if (o != null) {
                                    StudentsCurrentPositionClassGrade mStudentsCurrentPositionClassGrade = new StudentsCurrentPositionClassGrade();
                                    mStudentsCurrentPositionClassGrade.gradeId = o.optString("gradeId");
                                    mStudentsCurrentPositionClassGrade.gradeName = o.optString("name");
                                    grades.add(o.optString("name"));
                                    JSONArray detail = o.optJSONArray("detail");
                                    ArrayList<String> strings = new ArrayList<>();
                                    if (detail != null && detail.length() > 0) {
                                        for (int j = 0; j < detail.length(); j++) {
                                            JSONObject o1 = (JSONObject) detail.get(j);
                                            if (o1 != null) {
                                                StudentsCurrentPositionClass mStudentsCurrentPositionClass = new StudentsCurrentPositionClass();
                                                mStudentsCurrentPositionClass.clazzId = o1.optString("classId");
                                                mStudentsCurrentPositionClass.clazzName = o1.optString("name");
                                                mStudentsCurrentPositionClassGrade.clazzs.add(mStudentsCurrentPositionClass);
                                                strings.add(o1.optString("name"));
                                            }
                                        }
                                    }
                                    clazzs.add(strings);
                                    clazzsGrades.add(mStudentsCurrentPositionClassGrade);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;*/
            case Config.PERSON_EDUCATION_SPECIAL_LIST_SCREEN:
                JSONObject data = (JSONObject) event.getData();
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) data1.get(i);
                                PersonMoralEducationInputeBean input = new PersonMoralEducationInputeBean();
                                input.name = o.optString("moralName");
                                input.id = o.optString("moralId");
                                subStrs.add(o.optString("moralName"));
                                maps.put(o.optString("moralName"), o.optString("moralId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case Config.PERSON_EDUCATION_SCREEN:
                dialog.dismiss();
                JSONObject screen = (JSONObject) event.getData();
                lists.clear();
                if (screen != null) {
                    JSONArray dataScreen = screen.optJSONArray("data");
                    if (dataScreen != null && dataScreen.length() > 0) {
                        for (int i = 0; i < dataScreen.length(); i++) {
                            try {
                                JSONObject o = (JSONObject) dataScreen.get(i);
                                PersonMoralEducationListBean personMoralEducationListBean = new PersonMoralEducationListBean();
                                personMoralEducationListBean.time = o.optString("checkTime").substring(0, 16);
                                personMoralEducationListBean.clazz = o.optString("className");
                                personMoralEducationListBean.name = o.optString("targetorName");
                                personMoralEducationListBean.sub = o.optString("moralName");
                                personMoralEducationListBean.id = o.optString("moralManId");
                                personMoralEducationListBean.createUser = o.optString("createUser");
                                lists.add(personMoralEducationListBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case Config.EDUPERSON_DELETE_ALL:
                dialog.dismiss();
                ToastUtils.toast("删除成功");
                for (int i = 0; i < adapter.positions.size(); i++) {
                    PersonMoralEducationListBean integer = adapter.positions.get(i);
                    lists.remove(integer);
                }
                adapter.clearList();
                adapter.setAnimation(false);
                adapter.notifyDataSetChanged();
                break;
//            case "perupdatecomplete":
//                lists.clear();
//                refreshListview.requestLayout();
//                adapter.notifyDataSetChanged();
//                swipeLayout.setRefreshing(false);
//                dialog.show();
//                PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(pageSize, page);
//                LogUtil.e("个人德育列表请求参数", GsonUtils.toJson(personMoralEducationListRequest));
//                NetworkRequest.request(personMoralEducationListRequest, CommonUrl.PERSON_EDUCATION, Config.PERSON_EDUCATION);
//                break;
            case "delete":
                isDelete = false;
                break;
            case "clear":
                LogUtil.e("清楚数据");

                break;
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
    public void onLoad() {
        swipeLayout.setLoading(false);
    }

    @Override
    public void onRefresh() {
        lists.clear();
        refreshListview.requestLayout();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
        dialog.show();
        PersonMoralEducationListRequest personMoralEducationListRequest = new PersonMoralEducationListRequest(pageSize, page);
        LogUtil.e("个人德育列表请求参数", GsonUtils.toJson(personMoralEducationListRequest));
        NetworkRequest.request(personMoralEducationListRequest, CommonUrl.PERSON_EDUCATION, Config.PERSON_EDUCATION);
    }

    private void initRefreshLayout() {
//        swipeLayout.post(new Thread(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(true);
//            }
//        }));
        swipeLayout.setColorSchemeResources(Constance.colors);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String id = lists.get(position).id;
        Intent intent = new Intent(this, PersonMoralEducationDetailActivity.class);
        intent.putExtra("person_moral_education_datail_id", id);
        startActivity(intent);
    }

    @Override
    public void deleteOnClick() {
        isVisible = true;
        isDelete = true;
        right_icon0.setVisibility(View.GONE);
        right_icon.setVisibility(View.GONE);
        right_text.setVisibility(View.VISIBLE);
        right_text.setText("完成");
        if (adapter != null) {
            adapter.setAnimation(true);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isDelete) {
                isDelete = false;
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (adapter != null) {
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
}
