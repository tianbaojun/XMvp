package com.zjhz.teacher.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.WeekNumDataParams;
import com.zjhz.teacher.NetworkRequests.response.SortClass;
import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.NetworkRequests.response.TeacherListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.TeacherDutyAddActivity;
import com.zjhz.teacher.ui.activity.TeacherDutyDetailActivity;
import com.zjhz.teacher.ui.activity.TearchDutyActivity;
import com.zjhz.teacher.ui.adapter.TearchDutyAdapter;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * Created by Administrator on 2016/8/8.
 */
public class TeacherDutyFragment extends BaseFragment {
    @BindView(R.id.week_tv)
    TextView weekTv;
    @BindView(R.id.loading_tv)
    TextView loading_tv;
    @BindView(R.id.text_ll)
    TextView textLl;
    @BindView(R.id.listview)
    ListView listview;

    private DefaultLoadingLayout loadingLayout;

    private List<TeacherBean> data = new ArrayList<>();
    private List<TeacherListBean> daybeans;//获取配置信息
    private TearchDutyAdapter adapter;
    private TearchDutyActivity activity;
    private int week;
    private boolean hasStarted = false;

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.list, null);
        return view;
    }

    @Override
    protected void initViewsAndEvents() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), listview);
        if (activity == null) {
            activity = (TearchDutyActivity) getActivity();
        }
        adapter = new TearchDutyAdapter(activity, ((TearchDutyActivity) getActivity()).getLeader());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isLeader = false;
                for(TeacherBean bean:daybeans.get(position).getLeaders()){
                    if(bean.getTeacherId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey))){
                        isLeader = true;
                    }
                }
                if(isLeader) {
                    Bundle bundle = new Bundle();
                    if (daybeans.get(position).getDutyTime().length() > 10) {
                        bundle.putString("dutyTime", daybeans.get(position).getDutyTime().substring(0, 10));
                    }
                    if (((TearchDutyActivity) getActivity()).getLeader()) {
                        if (daybeans.get(position).isHasAffairs()) {
                            startActivity(TeacherDutyDetailActivity.class, bundle);
                        } else {
                            bundle.putString("dutyId", ((TearchDutyActivity) getActivity()).getDutyId());
                            startActivity(TeacherDutyAddActivity.class, bundle);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            hasStarted = true;
            lazyLoad();
        } else {
            if (hasStarted) {
                hasStarted = false;
            }
        }
    }

    protected void lazyLoad() {
        if (data != null && data.size() > 0) {
            data.clear();
        }
        NetworkRequest.request(new WeekNumDataParams(activity.dataWeek + "", activity.dutyId), CommonUrl.currentDutyData, Config.tearchduty_curr);

    }

    public void initFragmentData(TearchDutyActivity mActivity) {
        activity = mActivity;
    }


    @Override
    protected boolean isBindEventBusHere() {
        System.out.println("isBindEventBusHere");
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        switch (eventCenter.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                initDatas();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                initDatas();
                break;
            case Config.tearchduty_curr:
                dialog.dismiss();
                JSONObject o = (JSONObject) eventCenter.getData();
                LogUtil.e("值日数据 = ", o.toString());
                if (o != null) {
                    daybeans = GsonUtils.toArray(TeacherListBean.class, o);
                    initDatas();
                }
                break;
        }
    }

    private void initDatas() {
        if (weekTv != null && textLl != null) {
            weekTv.setText("第" + activity.dataWeek + "周");
//            textLl.removeAllViews();
            textLl.setText("");
            if (data != null && data.size() > 0) {
                data.clear();
            }
            if (daybeans != null && daybeans.size() > 0) {
                int totalSize = daybeans.size();
                TeacherListBean tearchbean = daybeans.get(totalSize - 1);
                data = tearchbean.getWleaders();
                if (data != null && data.size() > 0) {
                    String leader = "";
                    for (int i = 0; i < data.size(); i++) {
                        leader += data.get(i).getNickName() + "、";
                    }
                    if (!SharePreCache.isEmpty(leader)) {
                        leader = leader.substring(0, leader.length() - 1);
                    }
                    textLl.setText(leader);
//                    textLl.addView(AddTextView.getText(getActivity(),data,1,1));
                }
                if (!SharePreCache.isEmpty(daybeans.get(totalSize - 1).getWeek())) {
                    daybeans.remove(totalSize - 1);
                }
                SortClass sortClass = new SortClass();
                Collections.sort(daybeans, sortClass);
                adapter.setLists(daybeans);
                loadingLayout.onDone();
                adapter.notifyDataSetChanged();
            } else {
                adapter.setLists(null);
                loadingLayout.onEmpty();
                adapter.notifyDataSetChanged();
            }
        }
        if (loading_tv != null) {
            loading_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    public String listToString(ArrayList<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(","); // 分隔符
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }
}
