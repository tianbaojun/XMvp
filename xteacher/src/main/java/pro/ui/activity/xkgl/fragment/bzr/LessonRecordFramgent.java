package pro.ui.activity.xkgl.fragment.bzr;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.ui.activity.xkgl.adapter.WeekAdapter;
import pro.ui.activity.xkgl.testbean.BZRhistory;
import pro.widget.Selector.AutoLocateHorizontalView;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class LessonRecordFramgent extends BaseFragment implements RefreshLayout.OnLoadListener, RefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    @BindView(R.id.refresh_listview)
    ListView listView;
    //周次选择器
    @BindView(R.id.week_tab)
    AutoLocateHorizontalView weekSelector;
    //周次列表
    private List<String> weeks = new ArrayList<>();
    private SmartLoadingLayout loadingLayout;
    private CommonAdapter adapter;

    private BZRhistory mBZRhistory = new BZRhistory();

    private List<String> weekStrs = new ArrayList<>();
    WeekAdapter weekAdapter;

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_lesson_record, null, false);
    }

    private void getDate() {
        Map<String, String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        NetworkRequest.request(map, CommonUrl.COURSE_HISTORY_BZR, Config.COURSE_HISTORY_BZR);
    }

    private void getDate(int week) {
        Map<String, String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        map.put("weekNum", String.valueOf(week));
        NetworkRequest.request(map, CommonUrl.COURSE_HISTORY_BZR, Config.COURSE_HISTORY_BZR_WEEK);
        dialog.show();
    }

    @Override
    protected void initViewsAndEvents() {
        getDate();
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        adapter = new CommonAdapter<BZRhistory.History>(getActivity(), R.layout.activity_class_selection_zonghe_list_item, mBZRhistory.getHistoryList()) {
            @Override
            protected void convert(ViewHolder viewHolder, BZRhistory.History item, int position) {
                viewHolder.setText(R.id.one, item.getName());
                viewHolder.setText(R.id.two, item.getSubjectName());
//                String level ="" ;
                String status = "";
                switch (item.getStatus()) {
                    case 0:
                        status = "出勤";
                        break;
                    case 1:
                        status = "迟到";
                        break;
                    case 2:
                        status = "缺勤";
                        break;
                }
                viewHolder.setText(R.id.three, status);
                /*switch (item.getLevel()){
                    case "0":
                        level = "优秀";
                        break;
                    case "1":
                        level = "良好";
                        break;
                    case "2":
                        level = "一般";
                        break;
                }*/
                viewHolder.setText(R.id.four, item.getLevelName());
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getDate();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    @Override
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.ERROR_JSON:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.COURSE_HISTORY_BZR:
                dialog.dismiss();
                JSONObject js = (JSONObject) eventCenter.getData();
                BZRhistory history = GsonUtils.toObject(js, BZRhistory.class);
                if (history != null) {
                    mBZRhistory.getHistoryList().clear();
                    mBZRhistory.getHistoryList().addAll(history.getHistoryList());
                    mBZRhistory.getWeekList().clear();
                    mBZRhistory.getWeekList().addAll(history.getWeekList());
                    if (!BaseUtil.isEmpty(mBZRhistory.getWeekList())) {
                        weekStrs.clear();
                        for (BZRhistory.Week bean : mBZRhistory.getWeekList()) {
                            weekStrs.add(bean.getWeek());
                        }
                    }
                    initWeek();
                    weekSelector.moveToPosition(weekStrs.size() - 1);
//                    weekAdapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
                break;
            case Config.COURSE_HISTORY_BZR_WEEK:
                dialog.dismiss();
                JSONObject js1 = (JSONObject) eventCenter.getData();
                List<BZRhistory.History> historyList = GsonUtils.toArray(BZRhistory.History.class, js1);
                mBZRhistory.getHistoryList().clear();
                mBZRhistory.getHistoryList().addAll(historyList);
                adapter.notifyDataSetChanged();
                break;

        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoad() {
        refreshLayout.setLoading(false);
    }

    private void initWeek() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weekSelector.setLayoutManager(linearLayoutManager);
        weekSelector.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                getDate(mBZRhistory.getWeekList().get(pos).getIndex());
            }
        });
        weekSelector.setItemCount(5);
        weekAdapter = new WeekAdapter(getActivity(), weekStrs);
        weekSelector.setInitPos(weekStrs.size()-1);
        weekSelector.setAdapter(weekAdapter);
    }


}
