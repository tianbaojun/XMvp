package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PageNumSize;
import com.zjhz.teacher.NetworkRequests.response.OutgoingBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.OutgoingAnnouncementAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import pro.kit.ViewTools;

/**
 * 外出公告
 * Created by Administrator on 2016/6/27.
 */
public class OutgoingAnnouncementActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
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

    private DefaultLoadingLayout loadingLayout;
    private int pageNum = 1;
    private int pageSize = 12;
    private int total = 0;
    private OutgoingAnnouncementAdapter adapter;
    private final static String TAG = OutgoingAnnouncementActivity.class.getSimpleName();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        ButterKnife.bind(this);
        refresh_ll.setBackgroundResource(R.color.lightest_gray);
        AppContext.getInstance().addActivity(TAG, this);
        initTitle();
        initView();
        initListener();
        getData();
    }

    private void initView() {
        loadingLayout = DefaultLoadingLayout.createDefaultLayout(this, refreshLayout);
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshListview.setSelector(R.color.transparent);
        adapter = new OutgoingAnnouncementAdapter(this);
        refreshListview.setAdapter(adapter);
    }

    private void initTitle() {
        titleTv.setText("外出公告");
        rightIcon.setVisibility(View.VISIBLE);
        rightIcon.setImageResource(R.mipmap.add_icon);
    }

    @OnClick({R.id.title_back_img, R.id.right_icon})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_icon:
                Intent intent = new Intent(this, AddOutgoingActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
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
        NetworkRequest.request(new PageNumSize(pageNum, pageSize), CommonUrl.outgoingList, Config.OUTGOINGLIST);
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                hintRefresh();
                break;
            case Config.NOSUCCESS:
                hintRefresh();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            case Config.OUTGOINGLIST:
                hintRefresh();
                JSONObject json = (JSONObject) event.getData();
                total = json.optInt("total");
                List<OutgoingBean> datas = GsonUtils.toArray(OutgoingBean.class, json);
                if (datas != null) {
                    if (pageNum == 1) {
                        adapter.setDatas(datas);
                    } else {
                        adapter.addAllDatas(datas);
                    }
                    if (adapter.getCount() > 0) {
                        loadingLayout.onDone();
                        adapter.notifyDataSetChanged();
                    }else{
                        loadingLayout.onEmpty();
                    }
                }
                break;
        }
    }

    private void hintRefresh() {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }

    private void initListener() {
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onLoad() {
        if (adapter.getDatas().size() < total) {
            pageNum++;
            getData();
        } else {
            refreshLayout.setLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        pageNum = 1;
        getData();
    }
}
