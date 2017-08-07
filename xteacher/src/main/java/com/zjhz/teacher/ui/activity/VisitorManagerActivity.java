package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.VisitorListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.VisitorBean;
import com.zjhz.teacher.ui.delegate.IDrawerLayout;
import com.zjhz.teacher.ui.delegate.VisitDelegate;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.popuwindow.TitleBarMorePopupWindow;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zjhz.teacher.R.id.right_icon0;

public class VisitorManagerActivity extends BaseActivity implements IDrawerLayout, RefreshLayout.OnLoadListener,
        SwipeRefreshLayout.OnRefreshListener {

    private final static int AddVisitor = 10000;
    @BindView(R.id.visitor_list)
    public ListView visitorListView;
    @BindView(R.id.right_icon0)
    public ImageView addTextView;
    @BindView(R.id.right_icon1)
    public ImageView filterImageView;
    @BindView(R.id.right_icon)
    public ImageView moreImageView;
    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.navigation_header_left)
    public TextView navigationHeaderLeft;
    @BindView(R.id.navigation_header_right)
    public TextView navigationHeaderRight;
    @BindView(R.id.visit_name_delegate)
    public TextView visitNameTv;
    @BindView(R.id.visit_state_delegate_ll)
    public LinearLayout visitStateLL;
    @BindView(R.id.visit_state_delegate_tv)
    public TextView visitStateTv;
    @BindView(R.id.visit_start_time)
    public TextView visitStartTimeTv;
    @BindView(R.id.visit_end_time)
    public TextView visitEndTimeTv;
    @BindView(R.id.drawer_layout_clear)
    public TextView drawerClearTv;
    public int useRole = -1;//userType:1、教师，2、保安，99、未知
    @BindView(R.id.activity_visitor_manager)
    LinearLayout activity_visitor_manager;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.visitor_manager_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.visit_list_delegate_layout)
    LinearLayout drawerLayoutLL;
    @BindView(R.id.visit_refresh)
    RefreshLayout refreshLayout;
    @BindView(R.id.title_back_img)
    TextView back_icon;
    private List<VisitorBean> visitorList = new ArrayList<>();
    private VisitDelegate delegate;
    private VisitorListBean listBean;
    private CommonAdapter adapter;
    private int total = 0;
    private TitleBarMorePopupWindow window;
    private boolean isDelete = false;

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ViewGroup getDeleteLayout() {
        return drawerLayoutLL;
    }

    @Override
    public Serializable getDataBean() {
        return listBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_manager);
        ButterKnife.bind(this);
        initView();
        delegate = new VisitDelegate(this,this);
        delegate.initialize();

        window = new TitleBarMorePopupWindow(this, new TitleBarMorePopupWindow.MoreCallBack() {
            @Override
            public void deleteAction() {
                isDelete = true;
                addTextView.setVisibility(View.GONE);
                moreImageView.setVisibility(View.GONE);
                right_text.setVisibility(View.VISIBLE);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void filterAction() {
                delegate.openDrawer();
            }
        });
    }

    private void initView() {
        titleTv.setText("访客管理");
        right_text.setText("完成");
        addTextView.setVisibility(View.VISIBLE);
        moreImageView.setVisibility(View.VISIBLE);
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

        visitorListView.setDividerHeight(0);

        adapter = new CommonAdapter<VisitorBean>(this, R.layout.visitor_list_item, visitorList) {
            @Override
            protected void convert(ViewHolder viewHolder, VisitorBean item, final int position) {
                if (item == null)
                    return;
                if (isDelete) {
                    CheckBox checkBox = viewHolder.getView(R.id.visitor_list_item_check);

                    checkBox.setVisibility(View.VISIBLE);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            visitorList.get(position).isChecked = isChecked;
                        }
                    });

                    checkBox.setChecked(item.isChecked);
                } else {
                    viewHolder.getView(R.id.visitor_list_item_check).setVisibility(View.GONE);
                }
                if (item.appointmentTime != null) {
                    viewHolder.setText(R.id.visit_date, item.appointmentTime.split(" ")[0].substring(2));
                    int length = item.appointmentTime.split(" ")[1].length();
                    if(length == 8)
                        viewHolder.setText(R.id.visit_time, item.appointmentTime.split(" ")[1].substring(0,length-3));
                    else
                        viewHolder.setText(R.id.visit_time, item.appointmentTime.split(" ")[1].substring(0,length));
                }
                if(useRole == 2){
                    viewHolder.setVisible(R.id.visit_teacher, true);
                    viewHolder.setText(R.id.visit_teacher, item.teacherName);
                }
                viewHolder.setText(R.id.visit_name, item.visitorName);
                viewHolder.setText(R.id.visit_state, item.getStateName());
            }
        };

        visitorListView.setAdapter(adapter);

        visitorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            route(useRole, visitorList.get(position).recordId);
                if (position < visitorList.size())
                    route(useRole, visitorList.get(position), position);
            }
        });
        initBean();
        NetworkRequest.request(listBean, CommonUrl.VISITOR_LIST, "visitor_list");
        NetworkRequest.request(null, CommonUrl.USER_ROLE, "user_role");
        dialog.show();
    }

    public void initBean() {
        listBean = new VisitorListBean();
        listBean.setPage(1);
        listBean.setPageSize(CommonUrl.PAGE_SIZE);
//        listBean.setTeacherId(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey));
//        listBean.setTeacherName(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserNameKey));
    }

    @Override
    protected void onStop() {
        super.onStop();
        initBean();
        delegate.resetData();
    }

    @OnClick({right_icon0, R.id.right_icon, R.id.right_icon1, R.id.title_back_img, R.id.right_text})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                back();
                break;
            case R.id.right_text:
                int count = 0;
                for (VisitorBean visitorBean : visitorList)
                    if (visitorBean.isChecked)
                        count++;
                if (count > 0)
                    ActionDelete();
                else {
                    right_text.setVisibility(View.GONE);
                    isDelete = false;
                    addTextView.setVisibility(View.VISIBLE);
                    moreImageView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }
                break;
            case right_icon0:
//                startActivity(AddVisitorActivity.class);
                Intent intent = new Intent(this, AddVisitorActivity.class);
                startActivityForResult(intent, AddVisitor);
                break;

            case R.id.right_icon:
                window.showPopupWindow(moreImageView);
                break;
            case R.id.right_icon1:
                delegate.openDrawer();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (back())
            super.onBackPressed();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private boolean back() {
        if (drawerLayout.isDrawerOpen(drawerLayoutLL)) {
            delegate.closeDrawer();
            return false;
        } else {
            if (isDelete) {
                right_text.setVisibility(View.GONE);
                isDelete = false;
                addTextView.setVisibility(View.VISIBLE);
                moreImageView.setVisibility(View.VISIBLE);
                for (VisitorBean visitorBean : visitorList)
                    visitorBean.isChecked = false;
                adapter.notifyDataSetChanged();
                return false;
            } else {
                finish();
                return true;
            }
        }
    }

    //执行删除操作
    private void ActionDelete() {
        final HintPopwindow hintPopwindow;
        hintPopwindow = new HintPopwindow(this);
        hintPopwindow.setTitleMessage("确认删除吗?");

        hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
            @Override
            public void sureClick() {
                hintPopwindow.dismiss();
                dialog.show();

                String recordIds = "";
                for (VisitorBean visitorBean : visitorList) {
                    if (visitorBean.isChecked) {
                        if (TextUtils.isEmpty(recordIds))
                            recordIds = visitorBean.recordId;
                        else
                            recordIds = recordIds + "," + visitorBean.recordId;
                    }
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("recordIds", recordIds);
                NetworkRequest.request(map, CommonUrl.VISITOR_REMOVE, "visitor_remove");
            }

            @Override
            public void cancelClick() {
                hintPopwindow.dismiss();
            }
        });

        if (!hintPopwindow.isShowing()) {
            hintPopwindow.showAtLocation(activity_visitor_manager, Gravity.CENTER, 0, 0);
        }
    }

    private void route(int useRole, VisitorBean visitorBean, int position) {

        Bundle bundle = new Bundle();
//        bundle.putString("recordId", recordId);
        bundle.putSerializable("bean", visitorBean);
        if (useRole == 1) {
            if (visitorBean.visitStatus == 0) {
                Intent intent = new Intent(this, AddVisitorActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, position);
            } else {
                Intent intent = new Intent(this, VisitorInfoActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, position);
            }
        } else if (useRole == 2) {
            Intent intent = new Intent(this, VisitorManagerGuardActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, position);
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求失败");
                break;
            case "visitor_list":
                JSONObject data = (JSONObject) event.getData();
                try {
                    total = data.getInt("total");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<VisitorBean> list = GsonUtils.jsonToArray(VisitorBean.class, data);
                if (listBean.getPage() == 1) {
                    visitorList.clear();
                }
                visitorList.addAll(list);
                refreshLayout.setLoading(false);
//                if(total == visitorList.size())
//                    refreshLayout.setLoading(false);
//                else
//                    refreshLayout.setLoading(true);
                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                break;
            case "user_role":
                JSONObject data1 = (JSONObject) event.getData();
                try {
                    useRole = data1.getJSONObject("data").getInt("userType");
                    if (useRole == 2) {
                        addTextView.setVisibility(View.GONE);
                        moreImageView.setVisibility(View.GONE);
                        filterImageView.setVisibility(View.VISIBLE);
                        delegate.deleteDraft();
                        findViewById(R.id.visit_teacher).setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                    delegate.setUseRole(useRole);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "visitor_remove":
                right_text.setVisibility(View.GONE);
                isDelete = false;
                addTextView.setVisibility(View.VISIBLE);
                moreImageView.setVisibility(View.VISIBLE);
                for (int i = 0; i < visitorList.size(); i++) {
                    if (visitorList.get(i).isChecked) {
                        visitorList.remove(i);
                        i--;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoad() {
        if (total > visitorList.size()) {
            listBean.setPage(listBean.getPage() + 1);
            NetworkRequest.request(listBean, CommonUrl.VISITOR_LIST, "visitor_list");
//            refreshLayout.setLoading(true);
        } else {
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        listBean.setPage(1);
        NetworkRequest.request(listBean, CommonUrl.VISITOR_LIST, "visitor_list");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        VisitorBean visitorBean = (VisitorBean) data.getExtras().getSerializable("bean");
        if (visitorBean == null)
            return;

        if (requestCode == AddVisitor) {
            visitorList.add(0, visitorBean);
        } else {
            visitorList.set(requestCode, visitorBean);
        }
        adapter.notifyDataSetChanged();
    }
}
