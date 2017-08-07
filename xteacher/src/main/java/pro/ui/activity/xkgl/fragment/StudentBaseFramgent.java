package pro.ui.activity.xkgl.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.ui.activity.xkgl.testbean.XSXKbean;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public abstract class StudentBaseFramgent extends BaseFragment implements RefreshLayout.OnLoadListener,RefreshLayout.OnRefreshListener{

    @BindView(R.id.refresh_layout)
    protected RefreshLayout refreshLayout;
    @BindView(R.id.refresh_listview)
    protected ListView listView;

    protected SmartLoadingLayout loadingLayout;
    protected CommonAdapter adapter;
    protected List<XSXKbean> beans = new ArrayList<>();

    public static final String STUDENT = "STUDENT";

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.refresh_layout, null,false);
    }

    @Override
    protected void initViewsAndEvents() {
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(false);
        adapter = new CommonAdapter<XSXKbean>(getContext(),R.layout.activity_class_selection_student_list_item,beans) {
            @Override
            protected void convert(ViewHolder viewHolder, XSXKbean item, int position) {
                viewHolder.setText(R.id.name_tv, item.getName());
                viewHolder.setText(R.id.subject_tv, item.getSubjectName().equals("0")?"未选课":item.getSubjectName());
                GlideUtil.loadImageHead( item.getPhotoUrl(),(ImageView) viewHolder.getView(R.id.header_img));
                if (position % 2 == 0) {
                    viewHolder.setVisible(R.id.line, false);
                }else{
                    viewHolder.setVisible(R.id.line, true);
                }
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
        switch (eventCenter.getEventCode()){
            case Config.ERROR:
            case Config.ERROR_JSON:
            case Config.NOSUCCESS:
                refreshLayout.setRefreshing(false);
            case  STUDENT:
                refreshLayout.setRefreshing(false);
                beans.clear();
                beans.addAll(GsonUtils.toArray(XSXKbean.class, (JSONObject)eventCenter.getData()));
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

    protected abstract void getDate();
}
