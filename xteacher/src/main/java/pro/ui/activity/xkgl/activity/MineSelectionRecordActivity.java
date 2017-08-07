package pro.ui.activity.xkgl.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.XKHistoryListBean;
import com.zjhz.teacher.NetworkRequests.response.XKWeekListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.utils.BaseUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.ui.activity.xkgl.adapter.WeekAdapter;
import pro.ui.activity.xkgl.testbean.CourseRk;
import pro.widget.Selector.AutoLocateHorizontalView;

/**
 * Created by Tabjin on 2017/7/3.
 * Description:
 * What Chang
 */
public class MineSelectionRecordActivity extends BaseActivity {

    @BindView(R.id.week_tab)
    AutoLocateHorizontalView weekSelector;
    @BindView(R.id.history_list)
    ListView historyListView;
    //科目名
    @BindView(R.id.subject_name_tv)
    TextView subject_name_tv;
    //科目编号
    @BindView(R.id.subject_no_tv)
    TextView subject_no_tv;
    //科目上课地址
    @BindView(R.id.subject_place_tv)
    TextView subject_place_tv;

    //周次列表
    private List<XKWeekListBean> weeks = new ArrayList<>();

    private List<XKHistoryListBean> historyList = new ArrayList<>();
    private ListViewDBAdapter adapter;

    private CourseRk courseRk;
    private List<String> weekStrs = new ArrayList<>();
    WeekAdapter weekAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_selection_record);
        ButterKnife.bind(this);
        courseRk = (CourseRk) getIntent().getExtras().getSerializable("bean");
        getHistory();
        initView();
    }

    private void getHistory() {
        Map<String, String> map = new HashMap<>();
        map.put("optId", courseRk.getOptId());
        NetworkRequest.request(map, CommonUrl.COURSE_PT_HISTORY, "COURSE_PT_HISTORY");
    }

    private void initView() {
        setMyTitle("选课管理");
        subject_name_tv.setText(courseRk.getSubjectName());
        subject_no_tv.setText(courseRk.getCourseNo());
        subject_place_tv.setText(courseRk.getOptAddr());

        adapter = new ListViewDBAdapter<XKHistoryListBean>(this, historyList, R.layout.xk_history_item, BR.historyListBean);
        historyListView.setAdapter(adapter);
      /*  weekTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        weekTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getPosition();
                Map<String ,String> map = new HashMap<>();
                map.put("optId", courseRk.getOptId());
                map.put("weekNum", weeks.get(tab.getPosition()).getIndex()+"");
                NetworkRequest.request(map, CommonUrl.COURSE_PT_HISTORY, "COURSE_PT_HISTORY");
                dialog.show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (dialog.isShowing())
            dialog.dismiss();
        switch (eventCenter.getEventCode()) {
            case "COURSE_PT_HISTORY":
                try {
                    JSONArray jsonObject = ((JSONObject) eventCenter.getData()).getJSONObject("data").getJSONArray("historyList");
                    List<XKHistoryListBean> listBeen = JSON.parseArray(jsonObject.toString(), XKHistoryListBean.class);
                    historyList.clear();
                    historyList.addAll(listBeen);
                    adapter.notifyDataSetChanged();

                    if (weeks.size() == 0) {
                        weeks.clear();
                        JSONArray jsonObject1 = ((JSONObject) eventCenter.getData()).getJSONObject("data").getJSONArray("weekList");
                        List<XKWeekListBean> weekListBeen = JSON.parseArray(jsonObject1.toString(), XKWeekListBean.class);
                        weeks.addAll(weekListBeen);
                        if (BaseUtil.isEmpty(weekStrs)) {
                            if (!BaseUtil.isEmpty(weeks)) {
                                weekStrs.clear();
                                for (XKWeekListBean bean : weeks) {
                                    weekStrs.add(bean.getWeek());
                                }
                            }
                            initWeek();
                        }
                        weekAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONArray jsonObject = ((JSONObject) eventCenter.getData()).getJSONArray("data");
                        List<XKHistoryListBean> listBeen = JSON.parseArray(jsonObject.toString(), XKHistoryListBean.class);
                        historyList.clear();
                        historyList.addAll(listBeen);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void initWeek() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weekSelector.setLayoutManager(linearLayoutManager);
        weekSelector.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                Map<String, String> map = new HashMap<>();
                map.put("optId", courseRk.getOptId());
                map.put("weekNum", weeks.get(pos).getIndex()+"");
                NetworkRequest.request(map, CommonUrl.COURSE_PT_HISTORY, "COURSE_PT_HISTORY");
                dialog.show();
            }
        });
        weekSelector.setItemCount(5);
        weekAdapter = new WeekAdapter(this, weekStrs);
        weekSelector.setInitPos(weekStrs.size()-1);
        weekSelector.setAdapter(weekAdapter);
    }
}
