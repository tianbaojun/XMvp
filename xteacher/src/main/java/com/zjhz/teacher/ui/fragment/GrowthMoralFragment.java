package com.zjhz.teacher.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.GrowthMoralBeanRes;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.GrowthArchivesActivity;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * Created by zzd on 2017/6/21.
 */

public class GrowthMoralFragment extends ListFragment<GrowthMoralBeanRes>{

    private static final String SID = "sid";

    private static final  int TOTAl = 1;
    private static final  int REDUCE = 2;
    private static final  int PLUS = 3;

    private String calendarId = "";

    private TextView totalTv, reduceTv, plusTv;

    private List<GrowthMoralBeanRes> totalList = new ArrayList<>();
    private List<GrowthMoralBeanRes> reduceList = new ArrayList<>();
    private List<GrowthMoralBeanRes> plusList = new ArrayList<>();

    private int selectType = TOTAl;
    private boolean refresh = false;

    public static GrowthMoralFragment newInstance( String studentId){
        GrowthMoralFragment fragment = new GrowthMoralFragment();
        Bundle args = new Bundle();
        args.putString(SID, studentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParams() {
        listItemLayoutId = R.layout.growth_moral_month_item;
        BRId = BR.growthMoralBean;
    }

    @Override
    protected void initViewsAndEvents() {
        refreshLayout.setOnLoadListener(null);
        listView.setBackgroundColor(getResources().getColor(R.color.white));
        LinearLayout topLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.growth_moral_top, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.dp_35));
//        lp.setMargins(0,(int)getResources().getDimension(R.dimen.dp_10),0,0);
        top.addView(topLayout, lp);

        totalTv = (TextView) topLayout.findViewById(R.id.total);
        reduceTv = (TextView) topLayout.findViewById(R.id.score_reduce);
        plusTv = (TextView) topLayout.findViewById(R.id.score_plus);

        totalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelector(totalList, TOTAl);
            }
        });
        reduceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelector(reduceList, REDUCE);
            }
        });
        plusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelector(plusList, PLUS);
            }
        });

        adapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, final int monthPosition) {
                ListViewDBAdapter<GrowthMoralBeanRes.MonthListBean> monthAdapter = new ListViewDBAdapter<GrowthMoralBeanRes.MonthListBean>
                        (getContext(), dataList.get(monthPosition).getMonthList(), R.layout.growth_moral_date_item, BR.monthListBean);
                ScrollViewWithListView listView = (ScrollViewWithListView) rootView.findViewById(R.id.growth_month_list);
                listView.setAdapter(monthAdapter);
                monthAdapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
                    @Override
                    public void item(View rootView, int dayPosition) {
                        ListViewDBAdapter<GrowthMoralBeanRes.MonthListBean.DayListBean> dateAdapter = new ListViewDBAdapter<GrowthMoralBeanRes.MonthListBean.DayListBean>
                                (getContext(), dataList.get(monthPosition).getMonthList().get(dayPosition).getDayList(),
                                        R.layout.growth_moral_date_item_item, BR.dayListBean);
                        ScrollViewWithListView dateListView = (ScrollViewWithListView) rootView.findViewById(R.id.growth_date_list);
                        dateListView.setAdapter(dateAdapter);
                    }
                });
            }
        });
    }

    private void searchList(int type){
        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("pageSize", "20");
        map.put("calendarId", calendarId);
        map.put("studentId", getArguments().getString(SID));
        switch (type){
            case TOTAl:
                map.put("viewStatus", "0");
                break;
            case PLUS:
                map.put("viewStatus", "1");
                break;
            case REDUCE:
                map.put("viewStatus", "2");
                break;
        }
        NetworkRequest.request(map, CommonUrl.GROWTH_MORAL_SCORE, "growth_moral_list");
    }

    private void changeSelector(List<GrowthMoralBeanRes> list, int type){
        totalTv.setBackgroundResource(R.drawable.growth_moral_btn_unclick);
        reduceTv.setBackgroundResource(R.drawable.growth_moral_btn_unclick);
        plusTv.setBackgroundResource(R.drawable.growth_moral_btn_unclick);
        totalTv.setTextColor(getResources().getColor(R.color.gray6));
        reduceTv.setTextColor(getResources().getColor(R.color.gray6));
        plusTv.setTextColor(getResources().getColor(R.color.gray6));
        switch (type){
            case TOTAl:
                totalTv.setBackgroundResource(R.drawable.growth_moral_btn_click);
                totalTv.setTextColor(getResources().getColor(R.color.white));
                selectType = TOTAl;
                if(list.size() > 0){
                    dataList.clear();
                    dataList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else {
                    getSearcherList();
                }
                break;
            case PLUS:
                plusTv.setBackgroundResource(R.drawable.growth_moral_btn_click);
                plusTv.setTextColor(getResources().getColor(R.color.white));
                selectType = PLUS;
                if(list.size() > 0){
                    dataList.clear();
                    dataList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else {
                    getSearcherList();
                }
                break;
            case REDUCE:
                reduceTv.setBackgroundResource(R.drawable.growth_moral_btn_click);
                reduceTv.setTextColor(getResources().getColor(R.color.white));
                selectType = REDUCE;
                if(list.size() > 0){
                    dataList.clear();
                    dataList.addAll(list);
                    adapter.notifyDataSetChanged();
                }else {
                    getSearcherList();
                }
                break;
        }
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void getSearcherList() {
        searchList(selectType);
        refreshLayout.setRefreshing(true);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) throws JSONException {
        super.onEventMainThread(event);
        switch (event.getEventCode()){
            case "growth_moral_list":
                dataList.clear();
                if(refresh) {
                    totalList.clear();
                    reduceList.clear();
                    plusList.clear();
                }
                switch (selectType) {
                    case TOTAl:
                        totalList.addAll(GsonUtils.toArray(GrowthMoralBeanRes.class, (JSONObject)event.getData()));
                        dataList.addAll(totalList);
                        break;
                    case REDUCE:
                        reduceList.addAll(GsonUtils.toArray(GrowthMoralBeanRes.class, (JSONObject)event.getData()));
                        dataList.addAll(reduceList);
                        break;
                    case PLUS:
                        plusList.addAll(GsonUtils.toArray(GrowthMoralBeanRes.class, (JSONObject)event.getData()));
                        dataList.addAll(plusList);
                        break;
                }
                notifyDataSetChanged();
                break;
            case GrowthArchivesActivity.TERMPOST:   //切换calendarid
                String id = event.getData().toString();
                if (!SharePreCache.isEmpty(id)) {
                    calendarId = id;
                    refresh = true;
                }
                getSearcherList();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getSearcherList();
        refresh = true;
    }

    @Override
    public void onLoad() {
//        page++;
//        getSearcherList();
//        refresh = false;
    }
}
