package com.zjhz.teacher.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.GrowthTermSummary;
import com.zjhz.teacher.ui.activity.GrowthArchivesActivity;
import com.zjhz.teacher.ui.activity.GrowthSummaryAddActivity;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.log.XLog;

public class TermSummaryFragment extends BaseFragment {

    //寄语内容
    @BindView(R.id.root_listview)
    ListView root_listview;


    //是否第一次展示args  key
    private static final String ARG_FIRST_VISIT = "isFistVisit";
    //是否第一次展示
    private boolean isFistVisist = false;
    //当前学生id
    private String studentId = "";
    //当前学生id
    private String calendarId = "";
    //学期汇总列表
    private List<GrowthTermSummary> beans = new ArrayList<>();
    //汇总列表adapter
//    private ListViewDBAdapter adapter;
    private CommonAdapter adapter;

    private SmartLoadingLayout loadingLayout;

    public TermSummaryFragment() {

    }

    public static TermSummaryFragment newInstance() {
        TermSummaryFragment fragment = new TermSummaryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_VISIT, true);
        fragment.setArguments(args);
        return fragment;
    }

    public static TermSummaryFragment newInstance(String studentId) {
        TermSummaryFragment fragment = new TermSummaryFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_VISIT, true);
        args.putString("studentId", studentId);
        fragment.setArguments(args);
        return fragment;
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("studentId", studentId);
        if(!SharePreCache.isEmpty(calendarId)) {
            map.put("calendarId", calendarId);
        }
        XLog.e("main", GsonUtils.toJson(map));
        NetworkRequest.request(map, CommonUrl.TERMAPPRAISElISTSTUDENTID, Config.TERMAPPRAISElISTSTUDENTID);
        dialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isFistVisist = getArguments().getBoolean(ARG_FIRST_VISIT);
            studentId = getArguments().getString("studentId");
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_term_summary, null);
    }

    @Override
    protected void initViewsAndEvents() {
        getData();
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), root_listview);
        adapter = new CommonAdapter<GrowthTermSummary>(context, R.layout.item_term_summary_fragment, beans){

            @Override
            protected void convert(ViewHolder viewHolder, GrowthTermSummary item, int position) {
                viewHolder.setText(R.id.summary_title_tv,item.getTitle());
                ScrollViewWithListView jiangcheng_listview = viewHolder.getView(R.id.summary_listview);
                CommonAdapter jiangchengAdapter = new CommonAdapter<GrowthTermSummary.ListBean>(context, R.layout.item_db_textview_term_summary, beans.get(position).getList()) {
                    @Override
                    protected void convert(ViewHolder viewHolder, GrowthTermSummary.ListBean item, int position) {
                        viewHolder.setText(R.id.content_tv, item.getContent());
                    }
                };
                jiangcheng_listview.setAdapter(jiangchengAdapter);
                ImageView imageView = viewHolder.getView(R.id.jiangcheng_iv);
                switch (beans.get(position).getTitle()) {
                    case "学期寄语":
                        imageView.setImageResource(R.mipmap.jiyu);
                        break;
                    case "学期奖惩":
                        imageView.setImageResource(R.mipmap.jiangcheng);
                        break;
                    case "自我总结":
                        imageView.setImageResource(R.mipmap.zongjie);
                        break;
                    case "班主任评语":
                        imageView.setImageResource(R.mipmap.pingyu);
                        break;
                }
            }
        };
        root_listview.setAdapter(adapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort(R.string.tip_no_internet);
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.TERMAPPRAISElISTSTUDENTID:  //列表数据
                dialog.dismiss();
                JSONObject js = (JSONObject) eventCenter.getData();
                List<GrowthTermSummary> list = GsonUtils.toArray(GrowthTermSummary.class, js);
                beans.clear();
                if (list != null && list.size() > 0) {
                    beans.addAll(list);
                    adapter.notifyDataSetChanged();
                    loadingLayout.onDone();
                }else{
                    loadingLayout.onEmpty();
                }
                break;
            case GrowthArchivesActivity.TERMPOST:   //切换calendarid
                String id = eventCenter.getData().toString();
                if (!SharePreCache.isEmpty(id)) {
                    calendarId = id;
                }
                getData();
                break;
            case GrowthSummaryAddActivity.TERMMARYSUMMARYREFRESH:
                getData();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(beans!=null&&beans.size()>0){
            loadingLayout.onDone();
        }else{
            loadingLayout.onEmpty();
        }
    }
}
