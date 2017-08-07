/*
 * 源文件名：HomeWorkManagerActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述：作业管理统一弹出框，兼容‘筛选’、‘删除’等等其它操作功能
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */

package com.zjhz.teacher.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.request.DeleteHomeworkParams;
import com.zjhz.teacher.NetworkRequests.request.PageNumSizeStatus;
import com.zjhz.teacher.NetworkRequests.request.RetrieveParams;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.SubjectBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.WorkManagerAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.Constants;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.delegate.HomeWorkManagerDrawerLayoutPresenter;
import com.zjhz.teacher.ui.dialog.ListPopupWindow;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;import butterknife.BindView;


public class HomeWorkManagerActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener,
        ListPopupWindow.DeleteAction {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.right_icon0)                           //添加、编辑作业按钮
            ImageView right_icon0;
    @BindView(R.id.right_icon)                            //更多按钮
            ImageView right_icon;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.activity_work_manager_drawer_layout)
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
    public LinearLayout nvDrawerLayout;              // 侧拉
    @BindView(R.id.classname_tv)
    public TextView classname_tv;
    @BindView(R.id.subjectname_tv)
    public TextView subjectname_tv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.layout_work)
    LinearLayout layout_work;
    @BindView(R.id.drawer_layout_state)
    public LinearLayout drawer_layout_state;

    private DefaultLoadingLayout loadingLayout;
    private WorkManagerAdapter adapter;
    private HomeWorkManagerDrawerLayoutPresenter mWorkManagerDrawerLayoutPresenter; // 作业管理查询侧拉框
    public List<HomeWorkBean> workManagers = new ArrayList<>();
    public List<SubjectBeans> subObj = new ArrayList<>();//科目集合
    public List<String> subStr = new ArrayList<>();
    public List<ClassesBeans> classBeans = new ArrayList<>();//班级集合
    public List<String> classnames = new ArrayList<>();//班级集合

    public int pageNum = 1;
    private int pageSize = 12;
    private int total = 0;
    private boolean isVisible = true;
    private int status = 2;
    public String subjectId = "";
    public String classId = "";
    private String startTime = "";
    private String endTime = "";
    public int type = 0;
    private String homeworkIds = "";
    private BroadcastReceivers broadcastReceivers;
    private final static String TAG = HomeWorkManagerActivity.class.getSimpleName();
    boolean isDelete = false;
    ListPopupWindow window;
    private HintPopwindow hintPopwindow;

    class BroadcastReceivers extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            pageNum = 1;
            getData();
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);

        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        // 作业管理查询侧拉框
        mWorkManagerDrawerLayoutPresenter = new HomeWorkManagerDrawerLayoutPresenter(this);
        mWorkManagerDrawerLayoutPresenter.initialize();

        window = new ListPopupWindow(this, mWorkManagerDrawerLayoutPresenter, Constants.MODULE_HOMEWORK, this);

        initData();
        initListener();
        getSubjectData();
        getClassData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();
        pageNum = 1;
        NetworkRequest.request(new PageNumSizeStatus(pageNum, pageSize, status, SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey)), CommonUrl.homeworkList, "workList");
    }

    private void initData() {

        titleTv.setText(getResources().getText(R.string.work_manager));
        right_icon0.setVisibility(View.VISIBLE);//添加、编辑作业按钮
        right_icon.setVisibility(View.VISIBLE);//更多按钮

        broadcastReceivers = new BroadcastReceivers();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("HomeWorkManagerActivity");
        registerReceiver(broadcastReceivers, intentFilter);

        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        adapter = new WorkManagerAdapter(this, workManagers, true);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isVisible = true;
                Intent intent = new Intent(HomeWorkManagerActivity.this, HomeWorkPreviewActivity.class);
                intent.putExtra("homeworkId", workManagers.get(position).getHomeworkId());
                intent.putExtra("praisenu", workManagers.get(position).getPraiseNum());
                LogUtil.e("homeId-----", workManagers.get(position).getHomeworkId());
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
    }

    @OnClick({R.id.title_back_img, R.id.right_icon0, R.id.right_icon, R.id.right_text, R.id.drawer_layout_clear})
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
                    if (adapter != null) {
                        adapter.setAnimation(false);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    finish();
                }
                break;
            //发布
            case R.id.right_icon0:
                isVisible = true;
                startActivityForResult(new Intent(this, HomeWorkEditFisrtActivity.class), 1);
                break;
            //更多
            case R.id.right_icon:
                window.showPopupWindow(right_icon);
                break;
            //执行多条删除操作
            case R.id.right_text:
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
            hintPopwindow.setTitleMessage("确认删除此作业内容吗?");
        }
        hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
            @Override
            public void sureClick() {
                hintPopwindow.dismiss();
                dialog.setMessage("正在删除...");
                dialog.show();

                isDelete = false;
                right_icon0.setVisibility(View.VISIBLE);
                right_icon.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                if (adapter != null) {
                    int size = adapter.getLists().size();
                    for (int i = 0; i < size; i++) {
                        String id = adapter.getLists().get(i);
                        homeworkIds += (id + ",");
                    }
                    if (!SharePreCache.isEmpty(homeworkIds)) {
                        homeworkIds = homeworkIds.substring(0, homeworkIds.length() - 1);
                        deleteData();
                    }
                    adapter.setLists();
                    adapter.setAnimation(false);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void cancelClick() {
                hintPopwindow.dismiss();
            }
        });

        if (!hintPopwindow.isShowing()) {
            hintPopwindow.showAtLocation(layout_work, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mWorkManagerDrawerLayoutPresenter.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        if (type == 0) {
            getData();
        } else if (type == 1) {
            retrieveData();
        }
    }

    @Override
    public void onLoad() {
        if (workManagers.size() < total) {
            pageNum++;
            if (type == 0) {
                getData();
            } else if (type == 1) {
                retrieveData();
            }
        } else {
            refreshLayout.setLoading(false);
        }
    }

    private void deleteData() {
        NetworkRequest.request(new DeleteHomeworkParams(homeworkIds), CommonUrl.homeworkRemove, "deleteworks");
    }

    private void getSubjectData() {
        NetworkRequest.request(null, CommonUrl.homeworkSubjectList, "subList");
    }

    private void getClassData() {
        NetworkRequest.request(new CenterParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey), 1, 100), CommonUrl.homeworkClassesList, "classBeansList");
    }

    private void getData() {
        dialog.show();
        NetworkRequest.request(new PageNumSizeStatus(pageNum, pageSize, status, SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey)), CommonUrl.homeworkList, "workList");
    }

    //筛选数据
    public void retrieveData() {
        if (isRequest()) {
            mWorkManagerDrawerLayoutPresenter.closeDrawer();
            NetworkRequest.request(new RetrieveParams(classId, subjectId, startTime, endTime, pageNum, pageSize, SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey)), CommonUrl.homeworkList, "workList");
        }
    }

    public boolean isRequest() {
        if (SharePreCache.isEmpty(classId)) {
             ToastUtils.showShort("选择班级");
            return false;
        }
        if (SharePreCache.isEmpty(subjectId)) {
             ToastUtils.showShort("选择科目");
            return false;
        }
        startTime = drawerLayoutDateStart.getText().toString().trim();
        if (SharePreCache.isEmpty(startTime)) {
             ToastUtils.showShort("选择起始时间");
            return false;
        }
        endTime = drawerLayoutDateEnd.getText().toString().trim();
        if (SharePreCache.isEmpty(endTime)) {
             ToastUtils.showShort("选择截止时间");
            return false;
        }
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case "noSuccess":
                hintRefresh();
                break;
            case "workList":
                dialog.dismiss();
                hintRefresh();
                JSONObject object = (JSONObject) event.getData();
                total = object.optInt("total");
                setAdapterData(object);
                break;
            case "subList":
                JSONObject obj = (JSONObject) event.getData();
                subObj = GsonUtils.toArray(SubjectBeans.class, obj);
                subStr.clear();
                if (subObj.size() > 0) {
                    for (int i = 0; i < subObj.size(); i++) {
                        subStr.add(subObj.get(i).getName());
                    }
                }
                break;
            case "classBeansList":
                JSONObject os = (JSONObject) event.getData();
                classBeans = GsonUtils.toArray(ClassesBeans.class, os);
                classnames.clear();
                if (classBeans.size() > 0) {
                    for (int i = 0; i < classBeans.size(); i++) {
                        classnames.add(classBeans.get(i).getName());
                    }
                }
                break;
            case "deleteworks":
                homeworkIds = "";
                 ToastUtils.showShort("删除成功");
                pageNum = 1;
                getData();
                break;
        }
    }

    private void setAdapterData(JSONObject object) {
        LogUtil.e("作业列表数据 = ", object.toString());
        List<HomeWorkBean> homeWorkBeens = GsonUtils.toArray(HomeWorkBean.class, object);
        if (homeWorkBeens != null) {
            if (pageNum == 1) {
                workManagers.clear();
            }
            workManagers.addAll(homeWorkBeens);
            if(workManagers.size()>0) {
                loadingLayout.onDone();
                adapter.notifyDataSetChanged();
            }else{
                if (type == 1 && pageNum == 1) {//表示筛选没有数据，重新获取发布信息
                    loadingLayout.onEmpty();
                    type = 0;
                    getData();
                }else{
                    loadingLayout.onEmpty();
                    adapter.notifyDataSetChanged();
                }
            }
        }else{
            loadingLayout.onEmpty();
        }
    }

    private void hintRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceivers);
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
