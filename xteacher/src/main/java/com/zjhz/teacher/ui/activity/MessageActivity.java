package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.UnreadMsgParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 学校消息
 * Created by Administrator on 2016/7/12.
 */
public class MessageActivity extends BaseActivity {
    private final static String TAG = RepairsProposerListActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.system_noread_tv)
    TextView system_noread_tv;
    @BindView(R.id.school_noread_tv)
    TextView school_noread_tv;
    @BindView(R.id.type_news1)
    TextView typeNews1;
    @BindView(R.id.type_news2)
    TextView typeNews2;
    @BindView(R.id.dangerous_tv1)
    TextView dangerous_tv1;
    @BindView(R.id.icon_type1)
    ImageView iconType1;
    @BindView(R.id.icon_type2)
    ImageView iconType2;
    @BindView(R.id.dangerous_ll)
    LinearLayout dangerous_ll;
    private int unReadMsgNum = 0 ,unReadMeetNum = 0 ,unReadDangerousNum = 0;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        Intent intent = getIntent();
        if (intent != null){
            unReadMsgNum = intent.getIntExtra("unReadMsgNum",0);
            unReadMeetNum = intent.getIntExtra("unReadMeetNum",0);
            unReadDangerousNum = intent.getIntExtra("unReadDangerousNum",0);
        }
        init();
    }
    private void init() {
        titleTv.setText("学校消息");
        typeNews1.setText("群发消息");
        typeNews2.setText("会议通知");
        dangerous_ll.setVisibility(View.VISIBLE);
        iconType1.setImageResource(R.mipmap.message_icon);
        iconType2.setImageResource(R.mipmap.meeting_icon);
        system_noread_tv.setText("您有"+ unReadMsgNum+"条未读消息");
        school_noread_tv.setText("您有"+ unReadMeetNum+"条未读消息");
        dangerous_tv1.setText("您有"+ unReadDangerousNum+"条未读消息");
    }
    @OnClick({R.id.title_back_img,R.id.system_ll,R.id.school_ll,R.id.dangerous_ll})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.system_ll:
                startActivity(ReceiveMessageListActivity.class);
                break;
            case R.id.school_ll:
                startActivity(ReceiveMeetingNoticeListActivity.class);
                break;
            case R.id.dangerous_ll:
                startActivity(ReceiveDangerousListActivity.class);
                break;
        }
    }
    private void getUnReadMsgNums() {
        NetworkRequest.request(new UnreadMsgParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey), "1,2,3,4", "0"), CommonUrl.unReadNum, "messunReadNum");
    }
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (eventCenter.getEventCode().equals("messunReadNum")){
            JSONObject jsonObject = (JSONObject) eventCenter.getData();
            try {
                unReadMsgNum =jsonObject.getJSONObject("data").getInt("massNum");
                unReadMeetNum =jsonObject.getJSONObject("data").getInt("meetNum");
                unReadDangerousNum = jsonObject.getJSONObject("data").getInt("dangerousNum");
                system_noread_tv.setText("您有"+ unReadMsgNum+"条未读消息");
                school_noread_tv.setText("您有"+ unReadMeetNum+"条未读消息");
                dangerous_tv1.setText("您有"+ unReadDangerousNum+"条未读消息");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (eventCenter.getEventCode().equals("updateDataNum")){
            getUnReadMsgNums();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadCast);
    }
}
