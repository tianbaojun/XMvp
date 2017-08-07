package com.zjhz.teacher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.HomeBannerParams;
import com.zjhz.teacher.NetworkRequests.request.HomeListParams;
import com.zjhz.teacher.NetworkRequests.request.NewsAndAnnouncementListParams;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarAllRequest;
import com.zjhz.teacher.NetworkRequests.request.PersonalCalendarRequest;
import com.zjhz.teacher.NetworkRequests.response.HomeListResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.HomeGridViewAdapter;
import com.zjhz.teacher.adapter.HomeListViewSchoolAdapter;
import com.zjhz.teacher.adapter.PersonalCalendarHomeAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.HomeGridView;
import com.zjhz.teacher.bean.HomeListData;
import com.zjhz.teacher.bean.HomeNewDetail;
import com.zjhz.teacher.bean.PersonalCalendar;
import com.zjhz.teacher.ui.activity.AnnounceDetailActivity;
import com.zjhz.teacher.ui.activity.AnnouncementActivity;
import com.zjhz.teacher.ui.activity.AttendanceActivity;
import com.zjhz.teacher.ui.activity.ClassAndGradeEducationListActivity;
import com.zjhz.teacher.ui.activity.ExtramuralLocationActivity;
import com.zjhz.teacher.ui.activity.FoodActivity;
import com.zjhz.teacher.ui.activity.HomeMoreActivity;
import com.zjhz.teacher.ui.activity.HomeWorkManagerActivity;
import com.zjhz.teacher.ui.activity.LeaveApplyForListActivity;
import com.zjhz.teacher.ui.activity.MyScheduleActivity;
import com.zjhz.teacher.ui.activity.NewsActivity;
import com.zjhz.teacher.ui.activity.NewsDetailActivity;
import com.zjhz.teacher.ui.activity.OutgoingAnnouncementActivity;
import com.zjhz.teacher.ui.activity.PersonMoralEducationListActivity;
import com.zjhz.teacher.ui.activity.PersonalCalendarActivity;
import com.zjhz.teacher.ui.activity.RepairsProposerListActivity;
import com.zjhz.teacher.ui.activity.SchoolCalendarManagerActivity;
import com.zjhz.teacher.ui.activity.SchoolLocationActivity;
import com.zjhz.teacher.ui.activity.ScoreActivity;
import com.zjhz.teacher.ui.activity.SendMeetingNoticeListActivity;
import com.zjhz.teacher.ui.activity.SendMessageListActivity;
import com.zjhz.teacher.ui.activity.TearchDutyActivity;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.UpdateHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 首页
 */

// TODO: 2017/3/1 断网情况下，进入app，首页数据不显示，不退出APP重新链接网络时不会再次请求数据
public class HomeFragment extends BaseFragment {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.fragment_home_banner)
    com.zjhz.teacher.ui.view.BannerLayout bannerLayout;
    @BindView(R.id.fragment_home_gridview)
    ScrollViewWithGridView fragmentHomeGridview;
    @BindView(R.id.fragment_home_listview)
    ScrollViewWithListView fragmentHomeListview;
    @BindView(R.id.fragment_home_listview_school)
    ScrollViewWithListView schoolListview;
    @BindView(R.id.fragment_home_person)
    RelativeLayout fragment_home_person;
    @BindView(R.id.fragment_home_new_event)
    RelativeLayout fragment_home_new_event;

    List<String> images = new ArrayList<>();
    List<HomeNewDetail> homeNewDetails = new ArrayList<>();

    HomeGridViewAdapter gridAdapter;
    List<HomeGridView> grids = new ArrayList<>();

    PersonalCalendarHomeAdapter personAdapter;
    //    List<HomeListData> lists = new ArrayList<>();
    List<PersonalCalendar> lists = new ArrayList<>();

    HomeListViewSchoolAdapter homeListViewSchoolAdapter;
    List<HomeListData> listSchools = new ArrayList<>();
    boolean person = true;
    boolean event = true;
    WaitDialog dialog;
    private String day;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        titleTv.setText(getResources().getText(R.string.home));
        title_back_img.setVisibility(View.GONE);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        dialog = new WaitDialog(context);
        dialog.show();

        HomeBannerParams homeBannerParams = new HomeBannerParams("1", "4");
        NetworkRequest.request(homeBannerParams, CommonUrl.HOMEBANNER, Config.HOMEBANNER);

        HomeListParams homeListParams = new HomeListParams("1",SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey), "11", "TEACHER_APP");
        LogUtil.e("九宫格参数 = ",GsonUtils.toJson(homeListParams));

        NetworkRequest.request(homeListParams, CommonUrl.SCHOOLAPP, Config.SCHOOLAPP);

        day = TimeUtil.refFormatNowDate();
        PersonalCalendarRequest mPersonalCalendarRequest = new PersonalCalendarRequest(day);
        NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONEVENT, Config.PERSONEVENTHOME);

        NetworkRequest.request(new NewsAndAnnouncementListParams("", 3, 1, 5), CommonUrl.newsUrl, "home_news"); // OA_CATEGORY_NAME_1

        PersonalCalendarAllRequest mPersonalCalendarAllRequest = new PersonalCalendarAllRequest(TimeUtil.refFormatNowDate());
        LogUtil.e("所有日期参数",GsonUtils.toJson(mPersonalCalendarAllRequest));
        NetworkRequest.request(mPersonalCalendarRequest, CommonUrl.PERSONCALENDEREVENT, Config.FLYPERSONCALENDEREVENT);

        gridAdapter = new HomeGridViewAdapter(grids, context);
        fragmentHomeGridview.setAdapter(gridAdapter);

        personAdapter = new PersonalCalendarHomeAdapter(context, lists);
        fragmentHomeListview.setAdapter(personAdapter);
        homeListViewSchoolAdapter = new HomeListViewSchoolAdapter(listSchools, context);
        schoolListview.setAdapter(homeListViewSchoolAdapter);
        schoolListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (listSchools.get(position).categoryId.equals("OA_CATEGORY_NAME_2")){
                    intent.setClass(getActivity(),AnnounceDetailActivity.class);
                    intent.putExtra("announceId", listSchools.get(position).newsId);
                }else if (listSchools.get(position).categoryId.equals("OA_CATEGORY_NAME_1")){
                    intent.setClass(getActivity(),NewsDetailActivity.class);
                    intent.putExtra("newsId", listSchools.get(position).newsId);
                }
                startActivity(intent);
            }
        });
        fragmentHomeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.adapter_home_text);
                switch (text.getText().toString().trim()) {
                    case "校园新闻":
                        startActivity(NewsActivity.class);
                        break;
                    case "今日食谱":
                        startActivity(FoodActivity.class);
                        break;
                    case "外出公告":
                        startActivity(OutgoingAnnouncementActivity.class);
                        break;
                    case "课表查询":
                        startActivity(MyScheduleActivity.class);
                        break;
                    case "校历管理":
                        startActivity(SchoolCalendarManagerActivity.class);
                        LogUtil.e("tabjin","校历管理");
                        break;
                    case "设备报修":
                        startActivity(RepairsProposerListActivity.class);
                        break;
                    case "个人行事历":
                        startActivity(PersonalCalendarActivity.class);
                        break;
                    case "校外定位":
                        startActivity(ExtramuralLocationActivity.class);
                        break;
                    case "请假管理":
                        startActivity(LeaveApplyForListActivity.class);
                        break;
                    case "群发消息":
                        startActivity(SendMessageListActivity.class);
                        break;
                    case "学生考勤":
                        startActivity(AttendanceActivity.class);
                        break;
                    case "更多":
                        startActivity(HomeMoreActivity.class);
                        break;
                    case "会议通知":
                        startActivity(SendMeetingNoticeListActivity.class);
                        break;
                    case "校内定位":
                        startActivity(SchoolLocationActivity.class);
                        break;
                    case "通知公告":
                        startActivity(AnnouncementActivity.class);
                        break;
                    case "作业查看":
                        startActivity(HomeWorkManagerActivity.class);
                        break;
                    case "值日管理":
                        startActivity(TearchDutyActivity.class);
                        break;
                    case "个人德育":
                        startActivity(PersonMoralEducationListActivity.class);
                        break;
                    case "班级德育":
                        startActivity(ClassAndGradeEducationListActivity.class);
                        break;
                    case "成绩管理":
                        startActivity(ScoreActivity.class);
                        break;
                }
            }
        });

        UpdateHelper updateHelper = new UpdateHelper.Builder(context)
                .checkUrl(CommonUrl.LOWEST_VERSION)
                .isAutoInstall(true)
                .build();
        updateHelper.check();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        }else{
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) throws JSONException {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.toast("error");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.SCHOOLAPP:
                JSONObject dataGrid = (JSONObject) event.getData();
                LogUtil.e("首页九宫格返回数据 = ", dataGrid.toString());
                grids.clear();
                HomeListResponse homeListResponse = GsonUtils.toObject(dataGrid.toString(), HomeListResponse.class);
                if (!BaseUtil.isEmpty(homeListResponse.data)) {
                    int maxIndex = homeListResponse.data.size();
                    if (maxIndex >= 11){
                        maxIndex = 11;
                    }
                    for (int i = 0; i < maxIndex; i++) {
                        HomeGridView homeGridView = new HomeGridView();
                        homeGridView.image = homeListResponse.data.get(i).appIcon;
                        homeGridView.name = homeListResponse.data.get(i).appName;
                        grids.add(homeGridView);
                    }
                    HomeGridView homeGridView = new HomeGridView();
                    homeGridView.image = R.mipmap.home_twelve_one+"";
                    homeGridView.name = "更多";
                    grids.add(homeGridView);
                }
                gridAdapter.notifyDataSetChanged();
                break;
            case Config.HOMEBANNER:  // banner
                JSONObject data = (JSONObject) event.getData();
                homeNewDetails.clear();
                if (data != null) {
                    JSONArray data1 = data.optJSONArray("data");
                    if (data1 != null && data1.length() > 0) {
                        for (int i = 0; i < data1.length(); i++) {
                            JSONObject o = (JSONObject) data1.get(i);
                            HomeNewDetail homeNewDetail = new HomeNewDetail();
                            homeNewDetail.image = o.optString("headImg");
                            homeNewDetail.id = o.optString("imageId");
                            homeNewDetail.title = o.optString("title");
                            homeNewDetails.add(homeNewDetail);
                        }
                        if (!BaseUtil.isEmpty(homeNewDetails)) {
//                            for (int i = 0; i < homeNewDetails.size(); i++) {
//                                images.add(homeNewDetails.get(i).image);
//                            }
                            bannerLayout.setViewUrls(homeNewDetails);
                        }
                    }
                } else {
                    HomeNewDetail homeNewDetail = new HomeNewDetail();
                    homeNewDetail.image = "http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0jpg";
                    homeNewDetail.id = "";
                    homeNewDetail.title = "图片新闻";
                    homeNewDetails.add(homeNewDetail);
                    bannerLayout.setViewUrls(homeNewDetails);
//                    images.add("http://img3.imgtn.bdimg.com/it/u=2674591031,2960331950&fm=23&gp=0jpg");
//                    images.add("http://img5.imgtn.bdimg.com/it/u=3639664762,1380171059&fm=23&gp=0.jpg");
//                    bannerLayout.setViewUrls(images);
                }
                break;
            case "home_news":  //
                JSONObject news = (JSONObject) event.getData();
                JSONArray data1 = news.optJSONArray("data");
                if (data1 != null && data1.length() > 0) {
                    for (int i = 0; i < data1.length(); i++) {
                        JSONObject o = (JSONObject) data1.get(i);
                        HomeListData homeListData = new HomeListData();
                        homeListData.top = o.optString("title");
                        homeListData.center = o.optString("summary");
                        homeListData.left = o.optString("publishUserVal");
                        homeListData.right = o.optString("publishTime").substring(0,16);
                        homeListData.newsId = o.optString("newsId");
                        homeListData.categoryId = o.optString("categoryId");
                        listSchools.add(homeListData);
                    }
                }
                homeListViewSchoolAdapter.notifyDataSetChanged();
                dialog.dismiss();
                break;
            case Config.PERSONEVENTHOME:  //TODO
                dialog.dismiss();
                lists.clear();
                JSONObject person = (JSONObject) event.getData();
                if (person != null) {

                    JSONObject data9 = person.optJSONObject("data");
                    if (data9 != null) {
                        JSONArray calendar0 = data9.optJSONArray("calendar");
                        if (person != null && calendar0.length() > 0) {
                            for (int i = 0; i < calendar0.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) calendar0.get(i);
                                    PersonalCalendar mPersonalCalendar = new PersonalCalendar();
                                    switch (o.optInt("priority")) {
                                        case 3:
                                            mPersonalCalendar.image = R.mipmap.person_hight_grade_one;
                                            break;
                                        case 2:
                                            mPersonalCalendar.image = R.mipmap.person_center_grade_one;
                                            break;
                                        case 1:
                                            mPersonalCalendar.image = R.mipmap.person_low_grade_one;
                                            break;
                                        case 0:
                                            mPersonalCalendar.image = R.mipmap.person_none_grade_one;
                                            break;
                                    }
                                    mPersonalCalendar.text = o.optString("title");
                                    mPersonalCalendar.time = o.optString("scheduleTime").substring(0, 11);
                                    mPersonalCalendar.id = o.optString("calendarId");
                                    mPersonalCalendar.summary = o.optString("summary");
                                    if (lists.size() < 5) {
                                        lists.add(mPersonalCalendar);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                personAdapter.notifyDataSetChanged();
                break;
            case Config.FLYPERSONCALENDEREVENT:
                JSONObject personAllDate = (JSONObject) event.getData();
                LogUtil.e("TAG","personAllFlyHome = " + personAllDate.toString());
                if (personAllDate != null) {
                    AppContext.getInstance().persons.clear();
                    JSONObject data0 = personAllDate.optJSONObject("data");
                    if (data0 != null) {
                        JSONArray calendar0 = data0.optJSONArray("calendar");
                        if (calendar0 != null && calendar0.length() > 0) {
                            for (int i = 0; i < calendar0.length(); i++) {
                                try {
                                    JSONObject o = (JSONObject) calendar0.get(i);
                                    AppContext.getInstance().persons.add(o.optString("scheduleTime").substring(0,11).trim());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @OnClick({R.id.fragment_home_person, R.id.fragment_home_new_event,R.id.title_tv})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (v.getId()) {
            case R.id.fragment_home_person:
                startActivity(PersonalCalendarActivity.class);
                break;
            case R.id.fragment_home_new_event:
                startActivity(NewsActivity.class);
                break;
            case R.id.title_tv:  // 个人德育
//                startActivity(PersonMoralEducationListActivity.class);
//                startActivity(ClassAndGradeEducationListActivity.class);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
