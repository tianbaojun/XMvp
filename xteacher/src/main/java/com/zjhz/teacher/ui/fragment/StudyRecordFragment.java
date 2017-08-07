package com.zjhz.teacher.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.GrowthStudyRecord;
import com.zjhz.teacher.ui.activity.GrowthArchivesActivity;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
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

public class StudyRecordFragment extends BaseFragment {

    //分数列表
    @BindView(R.id.listview)
    ListView listview;

    //是否第一次展示args  key
    private static final String ARG_FIRST_VISIT = "isFistVisit";
    //是否第一次展示
    private boolean isFistVisist = false;
    //学期id  392120972393582592
    private String calendarId = "";
    //当前学生名  288114323320999961
    private String studentId = "";
    //成绩结果列表
    private List<GrowthStudyRecord> beans = new ArrayList<>();

    private ListViewDBAdapter adapter;

    private SmartLoadingLayout loadingLayout;

    public StudyRecordFragment() {

    }

    public static StudyRecordFragment newInstance() {
        StudyRecordFragment fragment = new StudyRecordFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_VISIT, true);
        fragment.setArguments(args);
        return fragment;
    }
    public static StudyRecordFragment newInstance(String studentId) {
        StudyRecordFragment fragment = new StudyRecordFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_VISIT, true);
        args.putString("studentId",studentId);
        fragment.setArguments(args);
        return fragment;
    }

    private void getData(){
        Map<String,String> map = new HashMap<>();
        if(!SharePreCache.isEmpty(calendarId)) {
            map.put("calendarId", calendarId);
        }
        map.put("studentId",studentId);
        NetworkRequest.request(map,CommonUrl.STUDYRECORDCOUNT, Config.STUDYRECORDCOUNT);
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
        return inflater.inflate(R.layout.fragment_study_record,null);
    }

    @Override
    protected void initViewsAndEvents() {
        getData();
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), listview);
        adapter = new ListViewDBAdapter<>(context,beans,R.layout.item_db_study_record,BR.study_record);
        adapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, int position) {
                ScrollViewWithGridView gridView = (ScrollViewWithGridView)rootView.findViewById(R.id.record_grid);
                ListViewDBAdapter gridViewAdapter = new ListViewDBAdapter<>(context,beans.get(position).getList(),R.layout.study_record_grid, BR.record_subject);
                final int rootPosition = position;
                gridViewAdapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
                    @Override
                    public void item(View rootView, int position) {
//                        语文  #c4e700
//                        数学  #e59e01
//                        英语  #4cda5d
//                        化学  #ff7005
//                        体育  #7c90e0
//                        美术  #5fb6da
//                        音乐  #ff3399
//                        科学   #99ffcc
//                        信息技术  #9933ff
//                        品德  #66cc00
                        switch(beans.get(rootPosition).getList().get(position).getSubjectName()){
                            case "语文":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.chinese);
                                break;
                            case "数学":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.math);
                                break;
                            case "英语":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.english);
                                break;
                            case "化学":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.chemistry);
                                break;
                            case "体育":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.pe);
                                break;
                            case "美术":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.art);
                                break;
                            case "音乐":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.music);
                                break;
                            case "科学":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.science);
                                break;
                            case "信息技术":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.information_tec);
                                break;
                            case "品德":
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.morality);
                                break;
                            default:
                                rootView.findViewById(R.id.subject_name).setBackgroundResource(R.color.gray3);
                                break;
                        }
                    }
                });
                gridView.setAdapter(gridViewAdapter);
            }
        });
        listview.setAdapter(adapter);
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
            case Config.STUDYRECORDCOUNT:  //列表
                dialog.dismiss();
                JSONObject js = (JSONObject) eventCenter.getData();
                List<GrowthStudyRecord> list = GsonUtils.toArray(GrowthStudyRecord.class,js);
                if(list!=null&&list.size()>0){
                    beans.clear();
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
