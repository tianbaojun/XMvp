package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ReceviceMessageParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.MessageAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 接收的群发消息列表
 * Created by Administrator on 2016/8/1.
 */
public class ReceiveMessageListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.refresh_listview)
    ListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_ll)
    LinearLayout refresh_ll;
    MessageAdapter adapter ;
    private int page = 1, pageSize = 12,total = 0;
    private List<Message> datas = new ArrayList<>();
    private static final String TAG = ReceiveMessageListActivity.class.getSimpleName();
    private int index = -1 ,isUnread = -1;
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
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        dialog.setMessage("正在消息群发列表...");
        dialog.show();
        getData();
    }
    private void initView() {
        titleTv.setText("群发消息");
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
        adapter = new MessageAdapter(this,datas);
        adapter.setType(2);
        refreshListview.setAdapter(adapter);
        refreshListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("status==",datas.get(position).getStatus()+"");
                isDelete = false;
                index = position;
                Intent intent = new Intent(ReceiveMessageListActivity.this,MessageDetailActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("linkId",datas.get(position).getLinkId());
                intent.putExtra("id",datas.get(position).getMsgId());
                if (datas.get(position).getStatus() == 0){
                    intent.putExtra("isupdate",true);
                }
                startActivity(intent);
            }
        });
//        refreshListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                if (hintPopwindow == null){
//                    hintPopwindow = new HintPopwindow(ReceiveMessageListActivity.this);
//                    hintPopwindow.setTitleMessage("确认删除？");
//                }
//                hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
//                    @Override
//                    public void sureClick() {
//                        isDelete = true;
//                        isUnread = datas.get(position).getStatus();
//                        index = position;
//                        dialog.setMessage("正在删除...");
//                        dialog.show();
//                        NetworkRequest.request(new DeleteReceiveMessageParams(datas.get(position).getLinkId()),"PushLinkService.remove","deleteMsg");
//                        hintPopwindow.dismiss();
//                    }
//                    @Override
//                    public void cancelClick() {
//                        hintPopwindow.dismiss();
//                    }
//                });
//                hintPopwindow.showAtLocation(refresh_ll, Gravity.CENTER,0,0);
//                return true;
//            }
//        });
    }

    private void getData() {
        NetworkRequest.request(new ReceviceMessageParams(page,pageSize, SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)), CommonUrl.schoollist,"schoollist");
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()){
            case Config.ERROR:
                stopRefreshListener();
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefreshListener();
                if (isDelete){
                    ToastUtils.showShort("删除失败");
                }
                break;
            case "schoollist":
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
            case "updateSuccess2":
                datas.get(index).setStatus(1);
                adapter.notifyDataSetChanged();
                break;
            case "deleteMsg":
                dialog.dismiss();
//                if (isUnread == 0){
//                    EventBus.getDefault().post(new EventCenter("updateDataNum",null));
//                }
                if (datas.size() < pageSize){
                    page = 1;
                    getData();
                }else {
                    datas.remove(datas.get(index));
                    adapter.notifyDataSetChanged();
                }
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
        isDelete = false;
        if (datas.size() < total ){
            page++;
            getData();
        }else {
            refreshLayout.setLoading(false);
        }
    }
    @Override
    public void onRefresh() {
        isDelete = false;
        page = 1;
        getData();
    }
}
