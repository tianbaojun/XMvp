package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AddSendMeetingParams;
import com.zjhz.teacher.NetworkRequests.request.PushListParams;
import com.zjhz.teacher.NetworkRequests.response.PushListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.TimePickerView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 添加会议通知,群发消息    会议通知合并群发消息接口2016/10/14.
 * Created by xiangxue on 2016/6/17.
 * Modify: fei.wang and 2016.7.16
 */
public class AddMeetingOrMessageNoticeActivity extends BaseActivity {
    private final static String TAG = AddMeetingOrMessageNoticeActivity.class.getSimpleName();
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.to_receive_ll_object)
    TextView to_receive_ll_object;
    @BindView(R.id.to_receive_ll_object1)
    TextView to_receive_ll_object1;
    @BindView(R.id.add_meeting_ed)
    EditText editText;
    @BindView(R.id.meeting_time)
    TextView meeting_time;
    @BindView(R.id.meeting_title)
    EditText meeting_title;
    @BindView(R.id.tv_co)
    TextView tv_co;
    @BindView(R.id.to_receive_ll_home)
    TextView to_receive_ll_home;
    @BindView(R.id.meeting_title_ll)
    LinearLayout meeting_title_ll;
    @BindView(R.id.cb_rl)
    LinearLayout cb_rl;
    @BindView(R.id.cb2)
    CheckBox cb21;
    @BindView(R.id.cb3)
    CheckBox cb31;
    private String teacheruserIds = "";
    private String teacherphoneNos = "";
    private String parentuserIds = "";
    private String parentphoneNos = "";
    private String content = "";
    private String meetingTime = "";
    private String meetingTitle = "";
    private boolean cb1 = true, cb2 = true, cb3 = false;
    private String schoolid = "";
    private int from;
    private String classesIds;//家长对象班级id

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        from = getIntent().getIntExtra("from", 0);
        schoolid = SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey);
        initTitle();
    }
    //获取判断是否有极光推送和短信推送
    private void getlistPush() {
        NetworkRequest.request(new PushListParams(schoolid, "1000"), CommonUrl.listPush, "listPush");
    }

    private void initTitle() {
        titleBackImg.setText("取消");
        titleBackImg.setCompoundDrawables(null, null, null, null);
        rightText.setText("发送");

        rightText.setVisibility(View.VISIBLE);
        if (from == 2) {//会议通知
            titleTv.setText("会议通知");
            getlistPush();
            meeting_time.setVisibility(View.VISIBLE);
            to_receive_ll_home.setVisibility(View.GONE);
            meeting_title_ll.setVisibility(View.VISIBLE);
            cb_rl.setVisibility(View.VISIBLE);
//            tv_co.setText("会议主题：");
        } else if (from == 1) {//群发消息
            titleTv.setText("群发消息");
            to_receive_ll_home.setVisibility(View.VISIBLE);
            meeting_time.setVisibility(View.GONE);
            meeting_title_ll.setVisibility(View.GONE);
            cb_rl.setVisibility(View.GONE);
//            tv_co.setText("内容：");
//            editText.setHint("请输入消息内容");
        }
    }

    @OnClick({R.id.to_receive_ll_teacher, R.id.to_receive_ll_home, R.id.meeting_time, R.id.right_text, R.id.title_back_img, R.id.to_receive_ll})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.to_receive_ll_teacher:
                ViewTools.hideSoftInput(this);
                Intent intent = new Intent(this, SendMessageTeacherActivity.class);  //选择教师
                intent.putExtra("type", String.valueOf(from));
//                intent.putExtra("userid",teacheruserIds);
                startActivityForResult(intent, 1);
                break;
            case R.id.to_receive_ll_home:
                ViewTools.hideSoftInput(this);
                Intent intents = new Intent(this, SendMessageParentActivity.class);    //选择家长
//                intents.putExtra("classesIds",classesIds);
                intents.putExtra("type", String.valueOf(from));
                startActivityForResult(intents, 2);
                break;
            case R.id.meeting_time:
                ViewTools.hideSoftInput(this);
                selectTime();
                break;
            case R.id.right_text:
                ViewTools.hideSoftInput(this);
                if (isRequst()) {
                    String msgTypes = "";
                    if (cb2) {//极光
                        msgTypes += "0" + ",";
                    }
                    if (cb3) {//短信
                        msgTypes += "1" + ",";
                    }
                    if (cb1) {//站内
                        msgTypes += "2" + ",";
                    }
                    String id = teacheruserIds + parentuserIds;
                    id = id.substring(0, id.length() - 1);
                    String phone = teacherphoneNos + parentphoneNos;
                    phone = phone.substring(0, phone.length() - 1);
                    dialog.setMessage("正在发送...");
                    dialog.show();
                    if (from == 2) {
                        if (!TextUtils.isEmpty(msgTypes))
                            msgTypes = msgTypes.substring(0, msgTypes.length() - 1);
                        NetworkRequest.request(new AddSendMeetingParams(msgTypes, content, id, phone, meetingTime, meetingTitle,"4"), CommonUrl.sendMessageByAlias, Config.ADDMESSAGE);
                    } else if (from == 1) {
                        Map<String,String> map = new HashMap<>();
                        map.put("msgTypes", "0,1,2");
                        map.put("content", content);
                        map.put("userIds", id);
                        map.put("phones", phone);
                        map.put("alert", "消息群发");
                        map.put("msgNature", "1");
                        NetworkRequest.request(map, CommonUrl.sendMessageByAlias, Config.ADDMESSAGE);
//                        NetworkRequest.request(new AddSendMessageParams("0,1,2", content, id, phone, "消息群发", "1"), CommonUrl.sendMessageByAlias, Config.ADDMESSAGE);
                    }
                }
                break;
            case R.id.title_back_img:
                ViewTools.hideSoftInput(this);
                finish();
                break;
            case R.id.to_receive_ll:
                if (from == 1) {
                    startActivity(PvwSendMseeageMeetActivity.class, "name", to_receive_ll_object.getText().toString().trim() + to_receive_ll_object1.getText().toString().trim());
                }else if (from == 2) {
                    startActivity(PvwSendMseeageActivity.class, "name", to_receive_ll_object.getText().toString().trim() + to_receive_ll_object1.getText().toString().trim());
                }

                break;
        }
    }

    private void selectTime() {
        TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.ALL,null,0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,String type) {
                String time = TimeUtil.getNowYMDHMSTime(date);
                meetingTime = time.substring(0,16);
                meeting_time.setText("会议时间：" + meetingTime);
            }
        });
    }
    /*@OnCheckedChanged(R.id.cb1)
    public void onChecked1(boolean checked) {
        cb1 = checked;
    }*/

    @OnCheckedChanged(R.id.cb2)
    public void onChecked2(boolean checked) {
        if(!checked){
            cb21.setChecked(true);
            cb2 = true;
            return;
        }else {
            cb2 = checked;
        }
    }

    @OnCheckedChanged(R.id.cb3)
    public void onChecked3(boolean checked) {
        cb3 = checked;
    }

    private boolean isRequst() {
        if (SharePreCache.isEmpty(teacheruserIds + parentuserIds)) {
            ToastUtils.showShort("请选择接收对象");
            return false;
        }
        if (from == 2) {
            meetingTitle = meeting_title.getText().toString().trim();
            if (SharePreCache.isEmpty(meetingTitle)) {
                ToastUtils.showShort("请输入会议标题");
                return false;
            }
            if (SharePreCache.isEmpty(meetingTime)) {
                ToastUtils.showShort("请选择会议时间");
                return false;
            }
            if (!cb1 && !cb2 && !cb3) {
                ToastUtils.showShort("请选择发送方式");
                return false;
            }
        }
        content = editText.getText().toString();
        if (SharePreCache.isEmpty(content)) {
            ToastUtils.showShort("请输入内容");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String names = data.getStringExtra("names");
            String obj1 = to_receive_ll_object.getText().toString().trim();
            String obj2 = to_receive_ll_object1.getText().toString().trim();
            to_receive_ll_object.setVisibility(View.VISIBLE);
            to_receive_ll_object1.setVisibility(View.VISIBLE);
            if (requestCode == 1) {
                teacheruserIds = data.getStringExtra("userId");
                teacherphoneNos = data.getStringExtra("phoneNo");
                if (SharePreCache.isEmpty(obj2)) {
                    to_receive_ll_object1.setVisibility(View.GONE);
                }
                to_receive_ll_object.setText(names);
            } else if (requestCode == 2) {
                parentuserIds = data.getStringExtra("userId");
                parentphoneNos = data.getStringExtra("phoneNo");
                classesIds = data.getStringExtra("classesIds");
                if (SharePreCache.isEmpty(obj1)) {
                    to_receive_ll_object.setVisibility(View.GONE);
                }
                to_receive_ll_object1.setText(names);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort("推送失败");
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                ToastUtils.showShort("推送失败");
                break;
            case Config.ADDMESSAGE:
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
                break;
            case "listPush":
                JSONObject j = (JSONObject) eventCenter.getData();
                List<PushListBean> beans = GsonUtils.toArray(PushListBean.class,j);
                if (beans != null && beans.size() > 0){
                    int code1 = 0,code2 = 0;
                    for (int i = 0 ; i < beans.size() ; i ++){
                        if (beans.get(i).appCode == 1){
                            code1 = 1;
                        }
                        if (beans.get(i).appCode == 2){
                            code2 = 2;
                        }
                    }
                    if (code1 == 1){
                        cb21.setVisibility(View.VISIBLE);
                    }
                    if (code2 == 2){
                        cb31.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }
}
