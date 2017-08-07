package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteReceiveMessageParams;
import com.zjhz.teacher.NetworkRequests.request.ReceiveMessageDtlParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 会议通知详情
 * Created by Administrator on 2016/7/20.
 */
public class MessageDetailActivity extends BaseActivity {
    @BindView(R.id.msg_title_tv)
    TextView msgTitleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.msg_content_tv)
    TextView msgContentTv;
    @BindView(R.id.msg_time_tv)
    TextView msgTimeTv;
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    private int type;
    private String id = "",linkId = "";
    private boolean isupdate;
    private final static String TAG = MessageDetailActivity.class.getSimpleName();
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null){
            type = intent.getIntExtra("type",0);
            id = intent.getStringExtra("id");
            linkId = intent.getStringExtra("linkId");
            isupdate = intent.getBooleanExtra("isupdate",false);
        }
        titleTv.setText("详情");
        if (!SharePreCache.isEmpty(linkId)){
            right_text.setText("删除");
            right_text.setVisibility(View.VISIBLE);
        }
        if (SharePreCache.isEmpty(id)){
            ToastUtils.showShort("id丢失");
            return;
        }
        dialog.setMessage("正在获取详情...");
        dialog.show();
        getDetail();
    }

    private void getDetail() {
//        if(type == 1){ //会议通知
//            NetworkRequest.request(new ReceiveMeetingDtlParams(id, SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),"PushLinkService.dtl","detail");
//        }else if (type == 2){ //消息群发
            NetworkRequest.request(new ReceiveMessageDtlParams(id, SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)),"PushLinkService.dtl","detail");
//        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter e){
        dialog.dismiss();
        if (e.getEventCode().equals("detail")){
            JSONObject JS = (JSONObject) e.getData();
            try {
                Message message = GsonUtils.toObject(JS.getJSONObject("data").toString(),Message.class);
                initData(message);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if (isupdate){
                EventBus.getDefault().post(new EventCenter("updateSuccess"+type,null));//表示详情查看成功
                EventBus.getDefault().post(new EventCenter("updateDataNum",null));
            }
        }else if (e.getEventCode().equals(Config.ERROR)){
            ToastUtils.showShort("获取详情失败");
        }else if (e.getEventCode().equals(Config.NOSUCCESS)){
            ToastUtils.showShort("获取详情失败");
        }else if (e.getEventCode().equals("linkdeleteMsg")){
            ToastUtils.showShort("删除成功");
            if (type == 1){
                EventBus.getDefault().post(new EventCenter("deleteMeet",null));
            }else if (type == 2){
                EventBus.getDefault().post(new EventCenter("deleteMsg",null));
            }
            finish();
        }
    }

    private void initData(Message message ) {
        if (message != null){
            String times = DateUtil.getStandardDate(message.getCreateTime());
            msgTimeTv.setText(times);
            if (type == 2){
                //表示群发详情
                msgTitleTv.setText(message.getContent());
                msgContentTv.setVisibility(View.GONE);
            }else {
                msgTitleTv.setText(message.getTitle());
                msgContentTv.setText(message.getContent());
            }
        }
    }

    @OnClick({R.id.title_back_img,R.id.right_text})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
//                if (type == 2){
                    NetworkRequest.request(new DeleteReceiveMessageParams(linkId),"PushLinkService.remove","linkdeleteMsg");
//                }else if (type == 1){
//                    NetworkRequest.request(new DeleteReceiveMessageParams(linkId),"OaMeetingService.removeMeetLink","linkdeleteMsg");
//                }
                break;
        }
    }
}
