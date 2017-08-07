package pro.ui.fragment;

import android.app.Activity;
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
import com.zjhz.teacher.ui.activity.GrowthArchivesSearchActivity;
import com.zjhz.teacher.ui.activity.HomeMoreActivity;
import com.zjhz.teacher.ui.activity.LeaveApplyForListActivity;
import com.zjhz.teacher.ui.activity.MyScheduleActivity;
import com.zjhz.teacher.ui.activity.NewsActivity;
import com.zjhz.teacher.ui.activity.NewsDetailActivity;
import com.zjhz.teacher.ui.activity.OutgoingAnnouncementActivity;
import com.zjhz.teacher.ui.activity.PersonMoralEducationListActivity;
import com.zjhz.teacher.ui.activity.PersonalCalendarActivity;
import com.zjhz.teacher.ui.activity.PickUpStudentActivity;
import com.zjhz.teacher.ui.activity.RepairsProposerListActivity;
import com.zjhz.teacher.ui.activity.SchoolCalendarManagerActivity;
import com.zjhz.teacher.ui.activity.SchoolLocationActivity;
import com.zjhz.teacher.ui.activity.ScoreActivity;
import com.zjhz.teacher.ui.activity.SendMeetingNoticeListActivity;
import com.zjhz.teacher.ui.activity.TearchDutyActivity;
import com.zjhz.teacher.ui.activity.VisitorManagerActivity;
import com.zjhz.teacher.ui.activity.homework.HomeworkManagerNewActivity;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

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
import pro.ui.activity.ClasszMomentsActivity;
import pro.ui.activity.CompanyNewsActivity;
import pro.ui.activity.EducationCountActivity3;
import pro.ui.activity.SendMessageListActivity;
import pro.ui.activity.xkgl.activity.ClassSelectionActivity;
import pro.ui.activity.xkgl.activity.LessonRecordActivity;
import pro.ui.activity.xkgl.activity.MineSelectionActivity;

//import com.zjhz.teacher.ui.activity.SendMessageListActivity;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 首页
 */

//// TODO: 2017/3/1 断网情况下，进入app，首页数据不显示，不退出APP重新链接网络不会再次请求数据
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
    @BindView(R.id.title)
    View title;

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
//        title.setVisibility(View.GONE);
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

//        bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(context, NewsDetailActivity.class);
//                intent.putExtra("newsId", homeNewDetails.get(position).id);
////                intent.putExtra("type", "img");
//                startActivity(intent);
//            }
//        });
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
                gotoActivity(getActivity(), grids.get(i));
            }
        });
    }

    public static void gotoActivity(Activity activity, HomeGridView gridsItem){
        switch (gridsItem.code){
            case "xyxw":
                startActivity(activity, NewsActivity.class,"title", gridsItem.name);
                break;
            case "jrsp":
                startActivity(activity,FoodActivity.class,"title", gridsItem.name);
                break;
            case "wcgg":
                startActivity(activity,OutgoingAnnouncementActivity.class,"title",gridsItem.name);
                break;
            case "kbcx":
                startActivity(activity,MyScheduleActivity.class,"title",gridsItem.name);
                break;
            case "xlgl":
                startActivity(activity,SchoolCalendarManagerActivity.class,"title",gridsItem.name);
                break;
            case "sbbx":
                startActivity(activity,RepairsProposerListActivity.class,"title",gridsItem.name);
                break;
            case "grxs":
                startActivity(activity,PersonalCalendarActivity.class,"title",gridsItem.name);
                break;
            case "xwdw":
                startActivity(activity,ExtramuralLocationActivity.class,"title",gridsItem.name);
                break;
            case "qjgl":
                startActivity(activity,LeaveApplyForListActivity.class,"title",gridsItem.name);
                break;
            case "qfxx":
                startActivity(activity,SendMessageListActivity.class,"title",gridsItem.name);
                break;
            case "xskq":
                startActivity(activity,AttendanceActivity.class,"title",gridsItem.name);
                break;
            case "gd":
                startActivity(activity,HomeMoreActivity.class,"title",gridsItem.name);
                break;
            case "hytz":
                startActivity(activity,SendMeetingNoticeListActivity.class,"title",gridsItem.name);
                break;
            case "xndw":
                startActivity(activity,SchoolLocationActivity.class,"title",gridsItem.name);
                break;
            case "tzgg":
                startActivity(activity,AnnouncementActivity.class,"title",gridsItem.name);
                break;
            case "zyck":
                startActivity(activity,HomeworkManagerNewActivity.class,"title",gridsItem.name);
                break;
            case "zrgl":
                startActivity(activity,TearchDutyActivity.class,"title",gridsItem.name);
                break;
            case "grdy":
                startActivity(activity,PersonMoralEducationListActivity.class,"title",gridsItem.name);
                break;
            case "bjdy":
                startActivity(activity,ClassAndGradeEducationListActivity.class,"title",gridsItem.name);
                break;
            case "cjgl":
                startActivity(activity,ScoreActivity.class,"title",gridsItem.name);
                break;
            case "dytj":
                startActivity(activity,EducationCountActivity3.class,"title",gridsItem.name);
                break;
            case "fkgl":
                startActivity(activity,VisitorManagerActivity.class, "title", gridsItem.name);
                break;
            case "bjq":
                startActivity(activity,ClasszMomentsActivity.class, "title", gridsItem.name);
                break;
            case "xzzx":
                startActivity(activity,CompanyNewsActivity.class, "title", gridsItem.name);
                break;
            case "lsjs":
                startActivity(activity,PickUpStudentActivity.class, "title", gridsItem.name);
                break;
            case "czda":
                startActivity(activity,GrowthArchivesSearchActivity.class, "title", gridsItem.name);
                break;
            case "xkgl":
                switch (SharedPreferencesUtils.getSharePrefString(ConstantKey.RoleType)){
                    case "3"://班主任
                        startActivity(activity,LessonRecordActivity.class, "title", gridsItem.name);
                        break;
                    case "4"://普通教师
                        startActivity(activity,MineSelectionActivity.class, "title", gridsItem.name);
                        break;
                    case "5"://管理员
                        startActivity(activity,ClassSelectionActivity.class, "title", gridsItem.name);
                        break;
                }
                break;
        }
    }

    private static void  startActivity(Activity activity, Class<?> clazz, String key, String value) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtra(key,value);
        activity.startActivity(intent);
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
                if (homeListResponse != null && !BaseUtil.isEmpty(homeListResponse.data)) {
                    int maxIndex = homeListResponse.data.size();
                    if (maxIndex >= 11) {
                        maxIndex = 11;
                    }
                    for (int i = 0; i < maxIndex; i++) {
                        HomeGridView homeGridView = new HomeGridView();
                        homeGridView.image = homeListResponse.data.get(i).appIcon;
                        homeGridView.name = homeListResponse.data.get(i).appName;
                        homeGridView.code = homeListResponse.data.get(i).code;
                        grids.add(homeGridView);
                    }
                    HomeGridView homeGridView = new HomeGridView();
                    homeGridView.image = R.mipmap.home_twelve_one + "";
                    homeGridView.name = "更多";
                    homeGridView.code = "gd";
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
            case Config.PERSONEVENTHOME:
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
