package com.zjhz.teacher.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AddSendMeetingParams;
import com.zjhz.teacher.NetworkRequests.response.MeetingBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.MeetingNoticeAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.SwipeListView;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * 会议通知列表
 * Created by XIANGXUE on 2016/6/17.
 */
public class SendMeetingNoticeListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_icon0)
    ImageView add;
    @BindView(R.id.right_icon)
    ImageView delete;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.refresh_listview)
    SwipeListView refreshListview;
    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_ll)
    LinearLayout refresh_ll;
    private MeetingNoticeAdapter adapter;
    private int pageNum = 1,pageSize = 12,total = 0;
    private final static String TAG = SendMeetingNoticeListActivity.class.getSimpleName();
    //单个删除的index
    private int index = -1;
    private DefaultLoadingLayout loadingLayout;
    private boolean isDelete;

    private List<MeetingBean> beans = new ArrayList<>();

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_refresh_delete_list);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        initTitle();
        initView();
        dialog.setMessage("正在获取列表...");
        dialog.show();
        getData();
    }
    private void initTitle() {
        titleTv.setText("会议通知");
        add.setVisibility(View.VISIBLE);
        add.setImageResource(R.mipmap.add_icon);
        delete.setVisibility(View.VISIBLE);
        delete.setImageResource(R.mipmap.work_delete);
        right_text.setText(R.string.delete);
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
        adapter = new MeetingNoticeAdapter(this,beans,refreshListview.getRightViewWidth());
        adapter.setOnItemRightClickListener(new MeetingNoticeAdapter.IOnItemRightClickListener() {
            @Override
            public void onRightClick(View v, int position) {
                Map<String,String> map = new HashMap<>();
                map.put("msgId", beans.get(position).getMsgId());
                NetworkRequest.request(map,CommonUrl.SETREMOVE,"deleteMsg");
                dialog.setMessage("删除...");
                dialog.show();
                index = position;
                refreshListview.hiddenRight();
            }
        });
        refreshListview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            pageNum = 1;
            getData();
        }
    }
    private void getData() {
        NetworkRequest.request(new AddSendMeetingParams(pageNum,pageSize,"4"), CommonUrl.MessageList,Config.MEETINGLIST);
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {

        switch (event.getEventCode()){
            case Config.ERROR:
                dialog.dismiss();
                stopRefresh();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                stopRefresh();
                if (isDelete){
                    delete.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    right_text.setVisibility(View.GONE);
                    adapter.setShowCheckBox(false);
                    ToastUtils.showShort("删除失败");
                    isDelete = false;
                }
                break;
            case Config.MEETINGLIST:
                dialog.dismiss();
                stopRefresh();
                JSONObject o = (JSONObject) event.getData();
                List<MeetingBean> mettings = GsonUtils.toArray(MeetingBean.class,o);
                try {
                    total = o.getInt("total");
                    if (mettings != null){
                        if (pageNum == 1){
                            beans.clear();
                        }
                        beans.addAll(mettings);
                        adapter.notifyDataSetChanged();
                        if(beans.size()>0) {
                            loadingLayout.onDone();
                            if(!isDelete) {
                                delete.setVisibility(View.VISIBLE);
                            }
                        }else{
                            loadingLayout.onEmpty();
                            delete.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "deleteMsg":
                dialog.dismiss();
                delete.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                right_text.setVisibility(View.GONE);
                getData();
                adapter.setShowCheckBox(false);
                ToastUtils.showShort("删除成功");
                break;
        }
    }

    @OnClick({R.id.right_text,R.id.title_back_img,R.id.right_icon, R.id.right_icon0})
    public void clickEvent(View view){
        switch(view.getId()){
            case R.id.right_text:  //多选删除
                MultiDelete();
                break;
            case R.id.title_back_img:   //回退按钮
                if(adapter.isShowCheckBox()){
                    adapter.setShowCheckBox(false);
                    delete.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                    right_text.setVisibility(View.GONE);
                    isDelete = false;
                }else {
                    finish();
                }
                break;
            case R.id.right_icon0:  //新增
                Intent in = new Intent(SendMeetingNoticeListActivity.this,AddMeetingOrMessageNoticeActivity.class);
                in.putExtra("from",2);
                startActivityForResult(in,2);
                break;
            case R.id.right_icon:   //删除按钮
                isDelete = true;
                delete.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                right_text.setVisibility(View.VISIBLE);
                if (adapter != null) {
                    adapter.setShowCheckBox(true);
                    adapter.notifyDataSetChanged();
                }
                break;
        }

    }

    /**
     * 多选删除的逻辑
     */
    private void MultiDelete() {
        if(adapter.ids.size()>0){
            new AlertDialog.Builder(SendMeetingNoticeListActivity.this).setTitle("确定删除这"+adapter.ids.size()+"条消息？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.setShowCheckBox(false);
                            right_text.setVisibility(View.GONE);
                            delete.setVisibility(View.VISIBLE);
                            add.setVisibility(View.VISIBLE);
                            isDelete = false;
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showWaitDialog("删除中...");
                            Map<String,String> map = new HashMap<>();
                            StringBuilder sb = new StringBuilder();
                            for(String str:adapter.ids){
                                sb.append(str+",");
                            }
                            map.put("msgIds",sb.substring(0,sb.length()-1));
                            NetworkRequest.request(map, CommonUrl.SETREMOVEALL,"deleteMsg");
                            isDelete = false;
                        }
                    }).show();
        }else{
            ToastUtils.showShort("没有选择...");
        }
    }

    private void stopRefresh() {
        if (refreshLayout != null){
            refreshLayout.setLoading(false);
            refreshLayout.setRefreshing(false);
        }
    }
    @Override
    public void onRefresh() {
        pageNum=1;
        getData();
    }
    @Override
    public void onLoad() {
        if (adapter!=null && adapter.getBeans().size() < total){
            pageNum++;
            getData();
        }else {
            refreshLayout.setLoading(false);
        }
    }
}
