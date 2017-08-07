package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.HomeListParams;
import com.zjhz.teacher.NetworkRequests.response.HomeListResponse;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.HomeMoreAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.HomeGridView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;
import pro.ui.fragment.HomeFragment;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-9
 * Time: 15:57
 * Description: 更多
 */
public class HomeMoreActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private final static String TAG = HomeMoreActivity.class.getSimpleName();
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.activity_home_more_grid)
    GridView gridView;
    HomeMoreAdapter adapter;
    private List<HomeGridView> grids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more);
        AppContext.getInstance().addActivity(TAG,this);
        ButterKnife.bind(this);
        titleTv.setText("更多");
        dialog.show();

        HomeListParams homeListParams = new HomeListParams("1",SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey),"100","TEACHER_APP");
        NetworkRequest.request(homeListParams, CommonUrl.SCHOOLAPP, Config.SCHOOLAPPFLY);
        adapter = new HomeMoreAdapter(this,grids);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected boolean isBindEventBusHere() {return true;}

    @OnClick(R.id.title_back_img)
    public void clickEvent(View v){
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        if (v.getId() == R.id.title_back_img) {
            finish();
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.toast("error");
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.SCHOOLAPPFLY:
                dialog.dismiss();
                grids.clear();
                JSONObject dataGrid = (JSONObject) event.getData();
                HomeListResponse homeListResponse = GsonUtils.toObject(dataGrid.toString(), HomeListResponse.class);
                if (!BaseUtil.isEmpty(homeListResponse.data)) {
                    for (int i = 0; i < homeListResponse.data.size(); i++) {
                        HomeGridView homeGridView = new HomeGridView();
                        homeGridView.image = homeListResponse.data.get(i).appIcon;
                        homeGridView.name = homeListResponse.data.get(i).appName;
                        homeGridView.code = homeListResponse.data.get(i).code;
                        grids.add(homeGridView);
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeFragment.gotoActivity(this, grids.get(i));
//        String code = grids.get(i).code;
//        switch (code.trim()) {
//            case "xyxw":
//                startActivity(NewsActivity.class,"title",grids.get(i).name);
//                break;
//            case "jrsp":
//                startActivity(FoodActivity.class,"title",grids.get(i).name);
//                break;
//            case "wcgg":
//                startActivity(OutgoingAnnouncementActivity.class,"title",grids.get(i).name);
//                break;
//            case "kbcx":
//                startActivity(MyScheduleActivity.class,"title",grids.get(i).name);
//                break;
//            case "xlgl":
//                startActivity(SchoolCalendarManagerActivity.class,"title",grids.get(i).name);
//                break;
//            case "sbbx":
//                startActivity(RepairsProposerListActivity.class,"title",grids.get(i).name);
//                break;
//            case "grxs":
//                startActivity(PersonalCalendarActivity.class,"title",grids.get(i).name);
//                break;
//            case "xwdw":
//                startActivity(ExtramuralLocationActivity.class,"title",grids.get(i).name);
//                break;
//            case "qjgl":
//                startActivity(LeaveApplyForListActivity.class,"title",grids.get(i).name);
//                break;
//            case "qfxx":
//                startActivity(pro.ui.activity.SendMessageListActivity.class,"title",grids.get(i).name);
//                break;
//            case "xskq":
//                startActivity(AttendanceActivity.class,"title",grids.get(i).name);
//                break;
//            case "hytz":
//                startActivity(SendMeetingNoticeListActivity.class,"title",grids.get(i).name);
//                break;
//            case "xndw":
//                startActivity(SchoolLocationActivity.class,"title",grids.get(i).name);
//                break;
//            case "tzgg":
//                startActivity(AnnouncementActivity.class,"title",grids.get(i).name);
//                break;
//            case "zyck":
//                startActivity(HomeworkManagerNewActivity.class,"title",grids.get(i).name);
//                break;
//            case "zrgl":
//                startActivity(TearchDutyActivity.class,"title",grids.get(i).name);
//                break;
//            case "grdy":
//                startActivity(PersonMoralEducationListActivity.class,"title",grids.get(i).name);
//                break;
//            case "bjdy":
//                startActivity(ClassAndGradeEducationListActivity.class,"title",grids.get(i).name);
//                break;
//            case "cjgl":
//                startActivity(ScoreActivity.class,"title",grids.get(i).name);
//                break;
//            case "dytj":
//                startActivity(EducationCountActivity3.class,"title",grids.get(i).name);
//                break;
//            case "fkgl":
//                startActivity(VisitorManagerActivity.class, "title", grids.get(i).name);
//                break;
//            case "bjq":
//                startActivity(ClasszMomentsActivity.class, "title", grids.get(i).name);
//                break;
//            case "xzzx":
//                startActivity(CompanyNewsActivity.class, "title", grids.get(i).name);
//                break;
//            case "czda":
//                startActivity(GrowthArchivesSearchActivity.class, "title", grids.get(i).name);
//                break;
//        }
    }
}
