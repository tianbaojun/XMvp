package com.zjhz.teacher.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.BaseUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.rawn_hwang.library.widgit.SmartLoadingLayout;

/**
 * Created by zzd on 2017/6/20.
 */

public abstract class ListFragment<E> extends BaseFragment implements
        RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    protected static final String IS_FIRST = "is_first";

    protected List<E> dataList = new ArrayList<>();

    protected ListViewDBAdapter<E> adapter;
    protected int listItemLayoutId;
    protected int BRId;


    protected LinearLayout top, bottom;
    protected RefreshLayout refreshLayout;
    protected ListView listView;

    protected int page = 1;

    protected SmartLoadingLayout loadingLayout;

    public ListFragment() {
        // Required empty public constructor
    }

//    /**
//     * @param listItemLayoutId Parameter 1.
//     * @return A new instance of fragment ListFragment.
//     */
//    public static <F extends ListFragment > F newInstance(Class<F> fClass, int listItemLayoutId) {
//        try {
//            Constructor<F> fConstructor = fClass.getDeclaredConstructor();
//            F listFragment = fConstructor.newInstance();
//            Bundle args = new Bundle();
//            args.putInt(ARG_PARAM1, listItemLayoutId);
//            listFragment.setArguments(args);
//            return listFragment;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            listItemLayoutId = getArguments().getInt(ARG_PARAM1);
//        }
        initParams();
        adapter = new ListViewDBAdapter<E>(getContext(), dataList, listItemLayoutId, BRId);
    }

    @Override
    public View initView(LayoutInflater inflater){
        View view = inflater.inflate(R.layout.list_view_layout, null, false);
        top = (LinearLayout)view.findViewById(R.id.list_layout_top);
        bottom = (LinearLayout)view.findViewById(R.id.list_layout_bottom);
        refreshLayout = (RefreshLayout)view.findViewById(R.id.refresh_layout);
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        return view;
    }

    @Override
    public void lazyLoad() {
        getSearcherList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataList!=null&&dataList.size()>0){
            loadingLayout.onDone();
        }else{
            loadingLayout.onEmpty();
        }
    }

    protected abstract void initParams();

    protected abstract void getSearcherList();

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) throws JSONException {
        refreshLayout.setRefreshing(false);
        refreshLayout.setLoading(false);
        int total = 0;
        if(event.getData() instanceof JSONObject) {
            total = ((JSONObject) event.getData()).optInt("total");
        }
        if(total == dataList.size()){
            refreshLayout.setCanLoad(false);
        }
    }

    protected void notifyDataSetChanged(){
        if(BaseUtil.isEmpty(dataList)){
            loadingLayout.onEmpty();
        }else {
            loadingLayout.onDone();
            adapter.notifyDataSetChanged();
        }
    }
}