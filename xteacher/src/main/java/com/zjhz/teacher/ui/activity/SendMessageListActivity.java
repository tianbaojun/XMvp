package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SendMessageListParams;
import com.zjhz.teacher.NetworkRequests.response.MassgeBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.SendMessageListAdapter;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * 群发信息
 * Created by Administrator on 2016/7/12.
 */
public class SendMessageListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon)
    ImageView rightIcon;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_ll)
    LinearLayout refresh_ll;
    SendMessageListAdapter adapter;
    private DefaultLoadingLayout loadingLayout;
    private int pageNum = 1, pageSize = 12, total = 0;
    private final static String TAG = SendMessageListActivity.class.getSimpleName();
    private int index = -1;
    private HintPopwindow hintPopwindow;
    private boolean isDelete;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initTitle();
        initView();
        dialog.setMessage("正在获取消息列表...");
        dialog.show();
        getData();
    }

    private void initTitle() {
        titleTv.setText("群发信息");
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(R.mipmap.add_icon);
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SendMessageListActivity.this, AddMeetingOrMessageNoticeActivity.class);
                in.putExtra("from", 1);
                startActivityForResult(in, 1);
            }
        });
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, refreshLayout);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new SendMessageListAdapter(this);
        refreshListview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                pageNum = 1;
                getData();
            }
        }
    }

    private void getData() {
        NetworkRequest.request(new SendMessageListParams(pageNum, pageSize, ""), CommonUrl.MessageList, Config.MESSAGELIST);
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                stopRefresh();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefresh();
                if (isDelete) {
                    ToastUtils.showShort("删除失败");
                }
                break;
            case Config.MESSAGELIST:
                dialog.dismiss();
                stopRefresh();
                JSONObject jsonObject = (JSONObject) eventCenter.getData();
                List<MassgeBean> beans = GsonUtils.toArray(MassgeBean.class, jsonObject);
                total = jsonObject.optInt("total");
                if (beans != null && beans.size() > 0) {
                    if (pageNum == 1) {
                        adapter.setBeans(beans);
                    } else {
                        adapter.addBeans(beans);
                    }
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                } else {
                    loadingLayout.onEmpty();
                }
                break;
            case "deleteSendMsg":
                dialog.dismiss();
                List<MassgeBean> massgeBeanList = adapter.getBeans();
                if (massgeBeanList.size() < pageSize) {
                    pageNum = 1;
                    getData();
                } else {
                    massgeBeanList.remove(massgeBeanList.get(index));
                    adapter.notifyDataSetChanged();
                }
                ToastUtils.showShort("删除成功");
                break;
        }
    }

    private void stopRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        isDelete = false;
        pageNum = 1;
        getData();
    }

    @Override
    public void onLoad() {
        isDelete = false;
        if (adapter.getBeans() != null && adapter.getBeans().size() < total) {
            pageNum++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }
}
