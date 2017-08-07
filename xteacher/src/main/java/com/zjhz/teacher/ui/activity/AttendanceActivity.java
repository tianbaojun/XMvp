package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.AttendanceListParams;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.response.AttendanceListBean;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.SchoolRfidBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.AttendanceAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.delegate.AttendanceDelegate;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;import butterknife.BindView;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 考勤
 */
public class AttendanceActivity extends BaseActivity  implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.title_back_img)
    public TextView title_back_img;
    @BindView(R.id.right_icon0)
    public ImageView right_icon0;
    @BindView(R.id.activity_attendance_drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.refresh_listview)
    public ListView listview;
    @BindView(R.id.refresh_layout)
    public RefreshLayout swipeLayout;
    @BindView(R.id.navigation_header_left)
    public TextView left;
    @BindView(R.id.navigation_header_title)
    public TextView title;
    @BindView(R.id.navigation_header_right)
    public TextView right;
    @BindView(R.id.drawer_layout_classes_name)
    public TextView name;
    @BindView(R.id.classname_tv)
    public TextView classnameTv;
    @BindView(R.id.drawer_layout_subject_name)
    public TextView drawerLayoutSubjectName;
    @BindView(R.id.subjectname_tv)
    public TextView subjectnameTv;
    @BindView(R.id.subjectname_tv_time)
    public TextView time;
    @BindView(R.id.drawer_relative)
    public RelativeLayout relativeLayout;
    @BindView(R.id.drawer_layout_time)
    public LinearLayout linearLayout;
    @BindView(R.id.drawer_layout_clear)
    public TextView clear;
    @BindView(R.id.nv_drawer_layout)
    public LinearLayout nvDrawerLayout;
    AttendanceDelegate delegate;
    AttendanceAdapter adapter;

    private DefaultLoadingLayout loadingLayout;
//    public List<GradeClsBean> beans;
    private final static String TAG = AttendanceActivity.class.getSimpleName();

//    public ArrayList<String> grades = new ArrayList<>();
//    public ArrayList<ArrayList<String>> clazzs = new ArrayList<ArrayList<String>>();

    private List<AttendanceListBean> AttendanceDatas = new ArrayList<>();//获取考勤数据
    private List<SchoolRfidBean> rfidDatas = new ArrayList<>();//获取卡号集合
    public String  date ;
    private String cardNums = "";
    public int status = 2;
    public int flag = 0;
    private int page = 1 ;
    private int pageSize = 50;
    private int total = 0;
//    private boolean isNext = true;
    public boolean isChange = false;
    private String classId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        delegate = new AttendanceDelegate(this);
        delegate.initialize();
        adapter = new AttendanceAdapter(this,AttendanceDatas);
        listview.setAdapter(adapter);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this, swipeLayout);
        initRefreshLayout();
        loadingLayout.onEmpty();
        MainActivity.getClassz();
        dialog.setMessage("正在初始化");
        dialog.show();
//        getGradeClsLisData();
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)-1);
        date = TimeUtil.getYMD(new Date());
        Map<String,String> map = new HashMap<>();
        map.put("date",date);
        map.put("initLoad","true");
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
//        activity.status = 1;
//        activity.flag = 0;
        map.put("function1", String.valueOf(status));
        map.put("flag", String.valueOf(flag));
        Log.e("main", map.toString());
        NetworkRequest.request(map, CommonUrl.AttendanceList,"AttendanceList");
//        getData();
        //默认项目
        BaseUtil.index_leave = 0;
        //默认班级
        NetworkRequest.request(new CenterParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey), 1, 100), CommonUrl.homeworkClassesList, "classList");

    }
    //获取所有班级年级
   /* private void getGradeClsLisData() {
        NetworkRequest.request(null, CommonUrl.gradeClsList,"gradeClsList");
    }*/
    //获取班级所有学生的卡号
    public void getSchoolRfidData(String gradeId,String classId) {
        dialog.setMessage("正在获取中");
        dialog.show();
        this.classId = classId;
        isChange = true;
        page = 1;
        Log.d("main",new AttendanceListParams(date,classId,status+"",flag+"",page,pageSize).toString());
        NetworkRequest.request(new AttendanceListParams(date,classId,status+"",flag+"",page,pageSize), CommonUrl.AttendanceList,"AttendanceList");
    }
    public void getData(){

        Log.d("main",new AttendanceListParams(date,classId,status+"",flag+"",page,pageSize).toString());
        NetworkRequest.request(new AttendanceListParams(date,classId,status+"",flag+"",page,pageSize), CommonUrl.AttendanceList,"AttendanceList");
    }
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                stopRefresh();
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                stopRefresh();
                loadingLayout.onEmpty();
                dialog.dismiss();
                break;

            case "AttendanceList":
                dialog.dismiss();
                stopRefresh();
                JSONObject json2 = (JSONObject) event.getData();
                total = json2.optInt("total");
                List<AttendanceListBean> datas = GsonUtils.toArray(AttendanceListBean.class,json2);
                if (datas != null && datas.size() > 0){
                    if (page == 1){
                        AttendanceDatas.clear();
                        AttendanceDatas.addAll(datas);
                    }else {
                        AttendanceDatas.addAll(datas);
                    }
                    loadingLayout.onDone();
                    adapter.notifyDataSetChanged();
                }else {
                    if (isChange && page == 1){
                        AttendanceDatas.clear();
                        adapter.notifyDataSetChanged();
                    }
                    loadingLayout.onEmpty();
                }
                break;
            case "classList":    //获取教师任课班级
                JSONObject os = (JSONObject) event.getData();
                List<ClassesBeans> classBeans = GsonUtils.toArray(ClassesBeans.class, os);
                if (classBeans.size() > 0) {
                    for (int i = 0; i < classBeans.size(); i++) {
                        for(int a=0;a<AppContext.getInstance().gradeBeans.size();a++){
                            if(AppContext.getInstance().gradeBeans.get(a).getDetail()!=null)
                            for(int b = 0;b<AppContext.getInstance().gradeBeans.get(a).getDetail().size();b++){
                                if (!TextUtils.isEmpty(AppContext.getInstance().gradeBeans.get(a).getDetail().get(b).getName())) {
                                    //找到任课班级在
                                    if(AppContext.getInstance().gradeBeans.get(a).getDetail().get(b).getClassId().equals(classBeans.get(i).getClassId())){
                                        delegate.index1 = a;
                                        delegate.index2 = b;
                                        classnameTv.setText(classBeans.get(i).getName());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                break;

        }
    }

    private void getDatas() {
        cardNums ="";
        if (total <= pageSize){
            for (int i = 0 ; i < total ; i++){
                if (!SharePreCache.isEmpty(rfidDatas.get(i).getRfidCard())){
                    cardNums += rfidDatas.get(i).getRfidCard()+",";
                }
            }
            getData();
        }else {
            for (int i = 0 ; i < pageSize ; i ++){
                if (!SharePreCache.isEmpty(rfidDatas.get(i).getRfidCard())){
                    cardNums += rfidDatas.get(i).getRfidCard()+",";
                }
            }
            getData();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            delegate.closeDrawer();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onLoad() {
        isChange = false;
        if (AttendanceDatas.size() < total){
            page ++;
            getData();
        }else {
            swipeLayout.setLoading(false);
        }
    }
    @Override
    public void onRefresh() {
        isChange = false;
        page = 1;
        getData();
//        getDatas();
    }
    private void stopRefresh(){
        if (swipeLayout != null){
            swipeLayout.setLoading(false);
            swipeLayout.setRefreshing(false);
        }
    }
    private void initRefreshLayout() {
        swipeLayout.post(new Thread(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        }));
        swipeLayout.setColorSchemeResources(Constance.colors);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        delegate = null;
    }
}
