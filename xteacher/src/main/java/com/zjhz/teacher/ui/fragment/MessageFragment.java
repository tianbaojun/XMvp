package com.zjhz.teacher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.UnreadMsgParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.MessageActivity;
import com.zjhz.teacher.ui.activity.SystemMessageActivity;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 消息
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.system_noread_tv)
    TextView system_noread_tv;
    @BindView(R.id.school_noread_tv)
    TextView school_noread_tv;
    @BindView(R.id.system_ll)
    LinearLayout systemLl;
    @BindView(R.id.school_ll)
    LinearLayout schoolLl;
    private int unReadMsgNum = 0,unReadMeetNum = 0,unReadDangerousNum = 0;
    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_message, null, false);
    }
    @Override
    protected void initViewsAndEvents() {
        title_back_img.setVisibility(View.GONE);
        titleTv.setText(getResources().getText(R.string.message));
        systemLl.setOnClickListener(this);
        schoolLl.setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
       getData();
    }
    private void getData() {
        NetworkRequest.request(new UnreadMsgParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey)), CommonUrl.unReadNum,"unReadNums");
    }
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {

        if (eventCenter.getEventCode().equals("unReadNums")){
            JSONObject jsonObject = (JSONObject) eventCenter.getData();
            try {
                unReadMsgNum =jsonObject.getJSONObject("data").getInt("massNum");
                unReadMeetNum =jsonObject.getJSONObject("data").getInt("meetNum");
                unReadDangerousNum = jsonObject.getJSONObject("data").getInt("dangerousNum");
                int total = unReadMsgNum + unReadMeetNum + unReadDangerousNum;
                school_noread_tv.setText("您有"+ total+"条未读消息");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (eventCenter.getEventCode().equals("updateDataNum")){
            LogUtil.e("MessageFragment接收到消息 = ","MessageFragment");
            getData();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        getActivity().unregisterReceiver(broadCast);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.system_ll:
                startActivity(SystemMessageActivity.class);
                break;
            case R.id.school_ll:
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                intent.putExtra("unReadMsgNum",unReadMsgNum);
                intent.putExtra("unReadMeetNum",unReadMeetNum);
                intent.putExtra("unReadDangerousNum",unReadDangerousNum);
                startActivity(intent);
                break;
        }
    }
}
