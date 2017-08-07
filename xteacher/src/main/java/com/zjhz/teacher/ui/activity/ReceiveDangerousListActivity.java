package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ReceiveMessageDtlParams;
import com.zjhz.teacher.NetworkRequests.request.ReceviceMessageParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.ui.adapter.SystemMessageAndAttenceAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/7.
 * 接受的危险报警列表
 */
public class ReceiveDangerousListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    SystemMessageAndAttenceAdapter adapter ;
    private int page = 1, pageSize = 12,total = 0;
    private List<Message> datas = new ArrayList<>();
    private static final String TAG = ReceiveDangerousListActivity.class.getSimpleName();
    private int index = -1;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_list);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        dialog.setMessage("正在获取危险区域报警...");
        dialog.show();
        getData();
    }
    private void initView() {
        titleTv.setText("危险区域报警");
        titleBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        refreshLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }));
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new SystemMessageAndAttenceAdapter(this,datas);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (datas.get(position).getStatus() == 0){
                    index = position;
                    NetworkRequest.request(new ReceiveMessageDtlParams(datas.get(position).getMsgId(), SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),"PushLinkService.dtl","dangrousdetail");
                }
            }
        });
    }

    private void getData() {
        NetworkRequest.request(new ReceviceMessageParams(page,pageSize, SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey),"2"), CommonUrl.schoollist,"dangrousLinkList");
    }
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()){
            case Config.ERROR:
                stopRefreshListener();
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
            case Config.NOSUCCESS:
                stopRefreshListener();
                dialog.dismiss();
                break;
            case "dangrousLinkList":
                stopRefreshListener();
                JSONObject jsons = (JSONObject) eventCenter.getData();
                List<Message> bean = GsonUtils.toArray(Message.class,jsons);
                try {
                    total = jsons.getInt("total");
                    if (bean != null){
                        if (page == 1) {
                            datas.clear();
                            datas.addAll(bean);
                        } else {
                            datas.addAll(bean);
                        }
                        adapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.showShort("没有数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                break;
            case "dangrousdetail":
                EventBus.getDefault().post(new EventCenter("updateDataNum",null));
                datas.get(index).setStatus(1);
                adapter.notifyDataSetChanged();
                break;
        }
    }
    private void stopRefreshListener(){
        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoading(false);
        }
    }
    @Override
    public void onLoad() {
        if (datas.size() < total ){
            page++;
            getData();
        }else {
            refreshLayout.setLoading(false);
        }
    }
    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }
}
