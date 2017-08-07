package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarAddRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarAllRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarRequest;
import com.zjhz.teacher.NetworkRequests.response.PersonalCalendarAddResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.PersonalCalendarAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.DeleteEvent;
import com.zjhz.teacher.bean.Event;
import com.zjhz.teacher.bean.PersonalCalendar;
import com.zjhz.teacher.ui.delegate.PersonalCalendarDelegate;
import com.zjhz.teacher.ui.view.PullToRefreshView;
import com.zjhz.teacher.ui.view.calendar.MyMonthView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 个人行事历
 */
public class PersonalCalendarActivity extends BaseActivity implements PersonalCalendarDelegate.OnDateClicklistener {
    private final static String TAG = PersonalCalendarActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.activity_personal_calendar_year_month)
    public TextView yearMonth;
    @BindView(R.id.activity_personal_calendar_monthDateView)
    public MyMonthView myMonthView;
    @BindView(R.id.activity_personal_calendar_list)
    public ListView listView;
    @BindView(R.id.activity_personal_calendar_list_text)
    public TextView listViewText;
    @BindView(R.id.activity_personal_calendar_pull_refresh_view)
    public PullToRefreshView mPullToRefreshView;
    @BindView(R.id.activity_personal_calendar_one)
    public TextView one;
    @BindView(R.id.activity_personal_calendar_two)
    public TextView two;
    @BindView(R.id.activity_personal_calendar_three)
    public TextView three;
    @BindView(R.id.activity_personal_calendar_four)
    public TextView four;
    @BindView(R.id.activity_personal_calendar_five)
    public TextView five;
    @BindView(R.id.activity_personal_calendar_six)
    public TextView six;
    @BindView(R.id.activity_personal_calendar_seven)
    public TextView seven;
    @BindView(R.id.activity_personal_calendar_month_day)
    public TextView monthDay;  // 最下面的月日
    @BindView(R.id.activity_personal_calendar_ll_layout)
    public LinearLayout linearLayout;
    @BindView(R.id.activity_personal_calendar_rl)
    public RelativeLayout personalRe;
    @BindView(R.id.activity_personal_calendar_rl_one)
    public RelativeLayout personalReOne;
    @BindView(R.id.activity_personal_calendar_et_comment)
    public TextView editText;
    public PersonalCalendarAdapter adapter;
    public List<PersonalCalendar> lists = new ArrayList<>();
    public boolean release = false;
    public ArrayList<String> dutys = new ArrayList<>();  // 值日领导
    public ArrayList<String> leaders = new ArrayList<>();  // 值日老师
    @BindView(R.id.activity_personal_calendar_edit)
    EditText editTextOne;
    @BindView(R.id.activity_personal_calendar_edit_view)
    View viewF;
    private PersonalCalendarDelegate delegate;
    private String day;
    private String time;
    private List<String> dates = new ArrayList<>();  // 事件日期
    private int height = 0;
    private int center = 0;
    private int none = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_calendar);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        time = TimeUtil.refFormatNowDate();
        dialog.show();
        PersonalCalendarAllRequest mPersonalCalendarRequest = new PersonalCalendarAllRequest(myMonthView.getmSelDay());
        LogUtil.e("所有日期参数",GsonUtils.toJson(mPersonalCalendarRequest));
        NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONCALENDEREVENT, Config.PERSONCALENDEREVENT);
        delegate = new PersonalCalendarDelegate(this);
        delegate.initialize(this);
        day = myMonthView.getmSelYear()+ "-" + myMonthView.getmSelMonth() + "-" + myMonthView.getmSelDayOne();
        myMonthView.setDaysHasThingList(AppContext.getInstance().persons);  // =======================================================================================================================================================================
        initData();
    }

    @Override
    protected boolean isBindEventBusHere() {return true;}

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseUtil.isEmpty(lists)) {
            delegate.setVisible();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }

    @OnClick({R.id.title_back_img,R.id.activity_personal_calendar_send_cancle, R.id.activity_personal_calendar_send_one,R.id.activity_personal_calendar_et_comment})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(TimeUtil.refFormatNowDate());
                NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENTHOME);
                finish();
                break;
            case R.id.activity_personal_calendar_et_comment:
                BaseUtil.upKeyboard(editTextOne);
                personalRe.setVisibility(View.GONE);
                personalReOne.setVisibility(View.VISIBLE);
                editTextOne.requestFocus();
                viewF.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_personal_calendar_send_cancle:
                BaseUtil.downKeyboard(editTextOne);
                personalRe.setVisibility(View.VISIBLE);
                personalReOne.setVisibility(View.GONE);
                viewF.setVisibility(View.GONE);
                break;
            case R.id.activity_personal_calendar_send_one:
                String comment = editTextOne.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.toast("请输入行事历标题");
                    return;
                }

                if (comment.length() > 32) {
                    ToastUtils.toast("标题输入的字数长度不能大于32");
                    return;
                }
                viewF.setVisibility(View.GONE);
                personalRe.setVisibility(View.VISIBLE);
                personalReOne.setVisibility(View.GONE);
                BaseUtil.downKeyboard(editTextOne);
                SharedPreferencesUtils.putSharePrefString("personal_content",editTextOne.getText().toString().trim());
                /*PersonalCalendar mPersonalCalendar = new PersonalCalendar();
                mPersonalCalendar.image = R.mipmap.person_none_grade_one;
                mPersonalCalendar.text = editTextOne.getText().toString().trim();
                mPersonalCalendar.time = "今天";
                mPersonalCalendar.type = 5;
                mPersonalCalendar.priority = R.mipmap.person_none_grade;*/
                PersonalCalendarAddRequest mPersonalCalendarAddRequest = new PersonalCalendarAddRequest("0","0",time,editTextOne.getText().toString().trim());
                NetworkRequest.request(mPersonalCalendarAddRequest, CommonUrl.PERSONEVENTADD, Config.PERSONEVENTADD);
//                lists.add(mPersonalCalendar);
                delegate.setVisible();
                LogUtil.e("个人事历","集合大小 = " + lists.size());
//                listView.requestLayout();
//                adapter.notifyDataSetChanged();
                editTextOne.setText("");
                editText.setText("");
                LogUtil.e("个人事历", "bespeakTime = " + time);
                LogUtil.e("个人事历day", "bespeakTime = " + day);
                AppContext.getInstance().persons.add(day);
                myMonthView.setDaysHasThingList(AppContext.getInstance().persons);
                break;
        }
    }

    private void initData() {
//        PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(myMonthView.getmSelDay());
//        NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
        adapter = new PersonalCalendarAdapter(this, lists);
        delegate.setVisible();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LogUtil.e("item点击事件角标","position = " + position);
                if (5 != lists.get(position).type) {
                    setPosition(position);
                }

                if (lists.get(position).image == R.mipmap.person_top) {
                    Bundle bundle = new Bundle();
                    bundle.clear();
                    bundle.putStringArrayList("pvwPensonActivity_leader",dutys);
                    bundle.putStringArrayList("pvwPensonActivity_teacher",leaders);
                    Intent  intent = new Intent(PersonalCalendarActivity.this,PvwPensonActivity.class);
                    intent.putExtra("pvwPensonActivity_bundle",bundle);
                    startActivity(intent);
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("person_detail_time",monthDay.getText().toString());
                    bundle.putString("person_detail_context",lists.get(position).text);
                    bundle.putString("person_detail_id",lists.get(position).id);
                    bundle.putString("person_detail_position",position + "");
                    bundle.putString("person_detail_day",day);
                    bundle.putString("person_detail_summary",lists.get(position).summary);
                    bundle.putInt("person_detail_image",lists.get(position).priority);
                    Intent  intent = new Intent(PersonalCalendarActivity.this,PersonalCalendarDetailActivity.class);
                    intent.putExtra("personal_bundle",bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case "error":
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case "noSuccess":
                dialog.dismiss();
                break;
            case Config.PERSONCALENDEREVENTFLY:   // 左右滑动月份的时候调用的
                dialog.dismiss();
                JSONObject personAllDate = (JSONObject) event.getData();
                LogUtil.e("TAG","personAll = " + personAllDate.toString());
                if (personAllDate != null) {
                    JSONObject data = personAllDate.optJSONObject("data");
                    if (data != null) {
                        JSONArray calendar = data.optJSONArray("calendar");
                        if (calendar != null && calendar.length() > 0) {
                            AppContext.getInstance().persons.clear();
                            for (int i = 0; i < calendar.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) calendar.get(i);
                                    AppContext.getInstance().persons.add(o.optString("scheduleTime").substring(0,11).trim());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                myMonthView.setDaysHasThingList(AppContext.getInstance().persons);
                break;
            case Config.PERSONCALENDEREVENT:  // 获取所有日期的参数
                JSONObject personAll = (JSONObject) event.getData();
                LogUtil.e("TAG","personAll = " + personAll.toString());
                if (personAll != null) {
                    JSONObject data = personAll.optJSONObject("data");
                    if (data != null) {
                        JSONArray calendar = data.optJSONArray("calendar");
                        if (calendar != null && calendar.length() > 0) {
                            AppContext.getInstance().persons.clear();
                            for (int i = 0; i < calendar.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) calendar.get(i);
                                    AppContext.getInstance().persons.add(o.optString("scheduleTime").substring(0,11).trim());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    MyMonthView myMonthView = (MyMonthView) findViewById(R.id.activity_personal_calendar_monthDateView);
                    myMonthView.setDaysHasThingList(AppContext.getInstance().persons);
                    dialog.show();
                    PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(day);
                    LogUtil.e("当天日期的参数",GsonUtils.toJson(mPersonalCalendarRequest));
                    NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
                }
                break;
            case "height":
                ++height;
                Event positionHeight = (Event) event.getData();
                if (BaseUtil.isStringEmpty(positionHeight.getPosition())) {
                    int index = Integer.parseInt(positionHeight.getPosition());
                    PersonalCalendar personalCalendar = lists.get(index);
                    personalCalendar.image = R.mipmap.person_hight_grade_one;
                    personalCalendar.type = 4;
                    if (!TextUtils.isEmpty(positionHeight.getTagContent())) {
                        personalCalendar.tag = true;
                    }
                    LogUtil.e("height-----","lists.size = " + lists.size());
//                    setPosition(index);
                    lists.remove(index);
                    lists.add(0,personalCalendar);
                    LogUtil.e("height+++++","lists.size = " + lists.size());
                    listView.requestLayout();
                    adapter.notifyDataSetChanged();
                }
                break;
            case "center":
                ++center;
                Event positionCenter = (Event) event.getData();
                if (BaseUtil.isStringEmpty(positionCenter.getPosition())) {
                    int index = Integer.parseInt(positionCenter.getPosition());
                    PersonalCalendar personalCalendar = lists.get(index);
                    personalCalendar.image = R.mipmap.person_center_grade_one;
                    personalCalendar.type = 3;
                    if (!TextUtils.isEmpty(positionCenter.getTagContent())) {
                        personalCalendar.tag = true;
                    }
                    LogUtil.e("center-----","lists.size = " + lists.size());
//                    setPosition(index);
                    lists.remove(index);
                    LogUtil.e("center","height = " + height);
                    lists.add(height,personalCalendar);
                    LogUtil.e("center+++++","lists.size = " + lists.size());
                    listView.requestLayout();
                    adapter.notifyDataSetChanged();
                }
                break;
            case "low":
                Event positionLow = (Event) event.getData();
                if (BaseUtil.isStringEmpty(positionLow.getPosition())) {
                    int index = Integer.parseInt(positionLow.getPosition());
                    PersonalCalendar personalCalendar = lists.get(index);
                    personalCalendar.image = R.mipmap.person_low_grade_one;
                    personalCalendar.type = 2;
                    if (!TextUtils.isEmpty(positionLow.getTagContent())) {
                        personalCalendar.tag = true;
                    }
                    LogUtil.e("low-----","lists.size = " + lists.size());
//                    setPosition(index);
                    lists.remove(index);
                    LogUtil.e("low","center + height = " + (center + height));
                    lists.add((center + height),personalCalendar);
                    LogUtil.e("low+++++","lists.size = " + lists.size());
                    listView.requestLayout();
                    adapter.notifyDataSetChanged();
                }
                break;
            case "none":
                ++none;
                Event positionNone = (Event) event.getData();
                if (BaseUtil.isStringEmpty(positionNone.getPosition())) {
                    int index = Integer.parseInt(positionNone.getPosition());
                    PersonalCalendar personalCalendar = lists.get(index);
                    personalCalendar.image = R.mipmap.person_none_grade_one;
                    personalCalendar.type = 1;
                    if (!TextUtils.isEmpty(positionNone.getTagContent())) {
                        personalCalendar.tag = true;
                    }
                    LogUtil.e("none-----","lists.size = " + lists.size());
//                    setPosition(index);
                    lists.remove(index);
                    lists.add(personalCalendar);
                    LogUtil.e("none+++++","lists.size = " + lists.size());
                    listView.requestLayout();
                    adapter.notifyDataSetChanged();
                }
                break;
            case "delete":
                DeleteEvent deleteEvent = (DeleteEvent) event.getData();// 删除position对应的lists集合中的数据
                LogUtil.e("删除position  -- f",deleteEvent.getPosition());
                lists.remove(Integer.parseInt(deleteEvent.getPosition()));
//                dates.remove("2016-07-16");
                if (BaseUtil.isEmpty(lists)) {
                    for (int i = 0; i < dates.size(); i++) {
                        if (dates.get(i).equals(deleteEvent.getDay())) {
                            dates.remove(i);
                            i--;
                        }
                    }
                }
                myMonthView.setDaysHasThingList(dates);
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                break;
            case Config.PERSONEVENT:  //TODO 列表
                dialog.dismiss();
                lists.clear();
                dutys.clear();
                leaders.clear();
                String flyh = myMonthView.getmSelDay();
                JSONObject person = (JSONObject) event.getData();
                if (person != null) {
                    JSONObject data = person.optJSONObject("data");
                    if (data != null) {
                        JSONArray calendar = data.optJSONArray("calendar");
                        if (calendar != null && calendar.length() > 0) {
                            for (int i = 0; i < calendar.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) calendar.get(i);
                                    PersonalCalendar mPersonalCalendar = new PersonalCalendar();
                                    switch (o.optInt("priority")){
                                        case 3:
                                            mPersonalCalendar.priority = R.mipmap.person_hight_grade;
                                            mPersonalCalendar.image = R.mipmap.person_hight_grade_one;
                                            break;
                                        case 2:
                                            mPersonalCalendar.priority = R.mipmap.person_center_grade;
                                            mPersonalCalendar.image = R.mipmap.person_center_grade_one;
                                            break;
                                        case 1:
                                            mPersonalCalendar.priority = R.mipmap.person_low_grade;
                                            mPersonalCalendar.image = R.mipmap.person_low_grade_one;
                                            break;
                                        case 0:
                                            mPersonalCalendar.priority = R.mipmap.person_none_grade;
                                            mPersonalCalendar.image = R.mipmap.person_none_grade_one;
                                            break;
                                    }
                                    mPersonalCalendar.text = o.optString("title");
                                    mPersonalCalendar.time = o.optString("scheduleTime").substring(0,11);
                                    mPersonalCalendar.id = o.optString("calendarId");
                                    mPersonalCalendar.summary = o.optString("summary");
                                    lists.add(mPersonalCalendar);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        JSONArray dutyDay = data.optJSONArray("dutyDay");
                        if (dutyDay != null && dutyDay.length() > 0) {
                            for (int i = 0; i < dutyDay.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) dutyDay.get(i);
                                    if (o != null) {
                                        dutys.add(o.optString("teacherName"));  // 领导
                                        JSONArray teachers = o.optJSONArray("teachers");
                                        if (teachers != null && teachers.length() > 0 && i == 0) {
                                            for (int j = 0; j < teachers.length(); j++) {
                                                JSONObject o1 = (JSONObject) teachers.get(j);
//                                                JSONObject o1 = (JSONObject) teachers.get(0);
//                                                leaders.add(o1.optString("teacherName"));
                                                leaders.add(o1.optString("teacherName"));
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            PersonalCalendar mPersonalCalendar = new PersonalCalendar();
                            String s = listToString(dutys);
//                            String s1 = listToString(leaders);

                            String s1 = null;
                            if (leaders != null && leaders.size() > 0) {
                                s1 = leaders.get(0);
                            }
                            LogUtil.e(". 值日老师:",s1);
                            mPersonalCalendar.text = "值日领导:" + s + ". 值日老师:" + s1;
                            mPersonalCalendar.image = R.mipmap.person_top;
                            mPersonalCalendar.time = day;
                            lists.add(0,mPersonalCalendar);
                        }
                    }
                }
                delegate.setVisible();
                LogUtil.e("个人事历","集合大小 = " + lists.size());
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                break;
            case Config.PERSONEVENTADD:
                dialog.dismiss();
                JSONObject personAdd = (JSONObject) event.getData();
                PersonalCalendarAddResponse mPersonalCalendarAddResponse = GsonUtils.toObject(personAdd.toString(), PersonalCalendarAddResponse.class);
                ToastUtils.toast(mPersonalCalendarAddResponse.msg + "");
                PersonalCalendarRequest re = new PersonalCalendarRequest(time);
                NetworkRequest.request(re, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
                SharedPreferencesUtils.putSharePrefString("person_calendarId",mPersonalCalendarAddResponse.data.calendarId);
                break;
            case Config.PERSONEVENTMODIFY:
//                dialog.dismiss();
//                ToastUtils.toast("修改成功");
                String positio = SharedPreferencesUtils.getSharePrefString("PersonalCalendarModify");
                Integer.parseInt(positio);
                lists.get(Integer.parseInt(positio)).text = SharedPreferencesUtils.getSharePrefString("PersonalCalendarModify_title");
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                LogUtil.e("PersonalCalendarActivity","修改成功");
                lists.clear();
                listView.requestLayout();
                adapter.notifyDataSetChanged();
                PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(day);
                LogUtil.e("当天日期的参数",GsonUtils.toJson(mPersonalCalendarRequest));
                NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
                break;
            case Config.PERSONEVENTDELETE:
                dialog.dismiss();
                ToastUtils.toast("删除成功");
                break;
        }
    }

    @Override
    public void OnDateClicklistener(String date,String time) {  // TODO 日期点击
        if (!TextUtils.isEmpty(time) && !time.endsWith("00")) {
            this.time = time;
            day = date;
            dialog.show();
            dutys.clear();
            leaders.clear();
            lists.clear();
            listView.requestLayout();
            adapter.notifyDataSetChanged();
            LogUtil.e("上传时间", "bespeakTime = " + time);
            LogUtil.e("======================中秋节==========================");
            PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(time);
            NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENT);
        }
    }

    private void setPosition(int position){
        PersonalCalendar personalCalendar = lists.get(position);
        LogUtil.e("个人行事","type = " + personalCalendar.type);
        LogUtil.e("个人行事","position = " + position);
        switch (personalCalendar.type){
            case 4:
                --height;
                LogUtil.e("--height","height = " + height);
                break;
            case 3:
                --center;
                LogUtil.e("--center","center = " + center);
                break;
            case 2:
                LogUtil.e("--low","low = ");
                break;
            case 1:
                --none;
                LogUtil.e("--none","none = " + none);
                break;
        }
    }

    public String listToString(ArrayList<String> stringList){
        if (stringList == null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(","); // 分隔符
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(TimeUtil.refFormatNowDate());
            NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENTHOME);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
