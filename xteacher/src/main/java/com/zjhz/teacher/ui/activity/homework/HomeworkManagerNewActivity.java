package com.zjhz.teacher.ui.activity.homework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.HomeworkSearchReqBean;
import com.zjhz.teacher.NetworkRequests.response.HomeworkListBeanRes;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.ListActivity;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.delegate.HomeworkSearchDelegate;
import com.zjhz.teacher.ui.delegate.IDrawerLayout;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeworkManagerNewActivity extends ListActivity<HomeworkListBeanRes> implements IDrawerLayout{

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.list_delegate)
    LinearLayout linearLayout;

    private HomeworkSearchDelegate delegate;

    private HomeworkSearchReqBean reqBean = new HomeworkSearchReqBean();

    @Override
    protected int getListItemLayoutId() {
        return R.layout.homework_manager_list_item;
    }

    @Override
    protected int getBRId() {
        return BR.homeworkListBeanRes;
    }

    @Override
    protected void initView() {
        linearLayout.addView(LayoutInflater.from(this).inflate(R.layout.homework_filter_layout, null));
        delegate = new HomeworkSearchDelegate(this,this);
        delegate.initialize();

        titleTv.setText("作业管理");
        addTv.setVisibility(View.VISIBLE);

        filterImg.setVisibility(View.VISIBLE);
        filterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.openDrawer();
            }
        });
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> stringList = new ArrayList<String>();
                stringList.add("书面作业");
                stringList.add("电子作业");
                BaseUtil.showListPopupWindow(addTv, stringList, new BaseUtil.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent();
                        if(position == 0)
                            intent.putExtra("nature","SYS_HOMEWORK_NATURE_1");
                        else
                            intent.putExtra("nature","SYS_HOMEWORK_NATURE_2");
                        intent.setClass(HomeworkManagerNewActivity.this, HomeworkAddActivity.class);
                        startActivityForResult(intent, 10000);
                    }
                });

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", dataList.get(position));
                Intent intent = new Intent();
                if(dataList.get(position).getStatus() == 1) {
                    bundle.putInt("type", 1);
                    intent.setClass(HomeworkManagerNewActivity.this, HomeworkAddActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 10000);
                }else {
                    intent.setClass(HomeworkManagerNewActivity.this, HomeworkManageDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, position);
                }
            }
        });
        adapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, int position) {
                CircleImageView header = (CircleImageView)rootView.findViewById(R.id.homework_header);
                GlideUtil.loadImageHead(dataList.get(position).getPhotoUrl(), header);
            }
        });
    }

    @Override
    protected void getSearcherList() {
        reqBean.page = page+"";
        reqBean.pageSize = "10";
        NetworkRequest.request(reqBean, CommonUrl.HOMEWORK_LIST, "homework_list");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event){
        super.onEventMainThread(event);
        switch (event.getEventCode()){
            case "homework_list":
                if(page == 1)
                    dataList.clear();
                dataList.addAll(GsonUtils.toArray(HomeworkListBeanRes.class, (JSONObject) event.getData()));
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getSearcherList();
    }

    @Override
    public void onLoad() {
        page ++;
        getSearcherList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10000) {//新增
            if (resultCode == RESULT_OK) {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        }else {
            if(resultCode == HomeworkManageDetailActivity.DFB){
                dataList.get(requestCode).setStatus(HomeworkManageDetailActivity.DFB);
                adapter.notifyDataSetChanged();
            }
            if(resultCode == HomeworkManageDetailActivity.FB){
                dataList.get(requestCode).setStatus(HomeworkManageDetailActivity.FB);
                adapter.notifyDataSetChanged();
            }else if(resultCode == HomeworkManageDetailActivity.WJ){
                dataList.get(requestCode).setStatus(HomeworkManageDetailActivity.WJ);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    @Override
    public ViewGroup getDeleteLayout() {
//        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.homework_filter_layout, null);
//        linearLayout.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }

    @Override
    public Serializable getDataBean() {
        return reqBean;
    }
}
