package pro.ui.fragment;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsMomentList;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.ClasszMomentType;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.utils.Constance;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.adapter.ClasszMomentsAdapter;
import pro.adapter.itemDelagate.ClasszMomentsDocument;
import pro.adapter.itemDelagate.ClasszMomentsHonour;
import pro.adapter.itemDelagate.ClasszMomentsShare;
import pro.kit.ViewTools;
import pro.ui.activity.ClasszMomentsActivity;

/**
 * Created by Tabjin on 2017/4/13.
 * Description:
 * What Changed:
 */

public class ClasszMomentsAllFragment extends BaseFragment implements RefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    //    RecyclerView recyclerView;
    @BindView(R.id.listView)
    ListView listView;
    //ListView的适配器
    ClasszMomentsAdapter adapter;
    //Share 班级动态集合
    List<ClasszMoment> list = new ArrayList<>();
    //    private static ClasszMomentsAllFragment instance;
    //资源类型集合
    private List<ClasszMomentType> classzMomentTypes = new ArrayList<>();
    //该老师的班级集合
    private List<ClassesBeans> classnames = new ArrayList<>();
    //当前选中的班级index
    private int index = 0;
    //当前选中的classid
    private String classId;
    //当前请求
    private int page = 1;
    private ClasszMomentsMomentList listParam;
    //动态总条数
    private int total = 0;
    private DefaultLoadingLayout loadingLayout;
    private View mFragmentView;
    private static final String REFRESHLASTEST = Config.REFRESHLASTEST + "ALL";
    private static final String REFRESHMOMENTLIST = Config.REFRESHMOMENTLIST + "ALL";
    private boolean mFull;

    public ClasszMomentsAllFragment() {
    }

    public static ClasszMomentsAllFragment newInstance() {
        Log.e("tabjin", "ClasszMomentsAllFragment");
        ClasszMomentsAllFragment fragment = new ClasszMomentsAllFragment();
        Bundle args = new Bundle();
        args.putBoolean("isFistVisit", true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return null;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("tabjin", "initView");
        if (null == mFragmentView) {
            mFragmentView = inflater.inflate(R.layout.fragment_classz_moments_all_fragment, container, false);
        }
//        if(adapter != null)
//            adapter.notifyDataSetChanged();
        return mFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("tabjin", "onDestroyView");
        if (null != mFragmentView) {
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
        }
        adapter.regesterEventBus(false);
    }

    @Override
    protected void initViewsAndEvents() {
        Log.e("tabjin", "initViewsAndEvents");
        classzMomentTypes = ((ClasszMomentsActivity) activity).getClasszMomentType();
        classnames.clear();
        classnames.addAll(((ClasszMomentsActivity) activity).getClassNames());
//        String dcCode = getArguments().getString("dcCode");
        String classId = (classnames.size()>index)?classnames.get(index).getClassId():"";
        listParam = new ClasszMomentsMomentList(classId, String.valueOf(page));
        getMomentsList();
        /*switch (dcCode){
            case "0":
                break;
            case "1001":
                initParams("1001");
                break;
            case "1002":
                initParams("1002");
                break;
            case "1003":
                initParams("1003");
                break;
        }*/
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(), refreshLayout);
        loadingLayout.onLoading();
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        listView.setDividerHeight(0);
        if (adapter == null) {
            adapter = new ClasszMomentsAdapter(getActivity(), list, "ALL");
            adapter.addItemViewDelegate(new ClasszMomentsShare(adapter, list, "ALL"));
            adapter.addItemViewDelegate(new ClasszMomentsHonour());
            adapter.addItemViewDelegate(new ClasszMomentsDocument());
            adapter.regesterEventBus(true);
            /*
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);*/
            listView.setAdapter(adapter);
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == (MotionEvent.ACTION_DOWN)) {
                        EventBus.getDefault().post(new EventCenter(Config.HIDDENINPUTWINDOW, null));
                        ViewTools.hideSoftInput(getActivity());
                    }
                    return false;
                }
            });

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    //大于0说明有播放
                    if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                        //当前播放的位置
                        int position = GSYVideoManager.instance().getPlayPosition();
                        //对应的播放列表TAG
                        if (GSYVideoManager.instance().getPlayTag().equals(ClasszMomentsShare.TAG)
                                && (position < firstVisibleItem || position > lastVisibleItem)) {
                            //如果滑出去了上面和下面就是否，和今日头条一样
                            GSYVideoPlayer.releaseAllVideos();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }

    /**
     * 通过dcode初始化请求参数
     *
     * @param dcode dcCode
     */
    private void initParams(String dcode) {
        for (ClasszMomentType type : classzMomentTypes) {
            if (dcode.equals(type.getDcCode())) {
                listParam.setDcId(type.getDcId());
            }
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private void getMomentsList() {
        Log.e("tabjin", "getMomentsList");
        NetworkRequest.request(listParam, CommonUrl.MOMENTLIST, Config.MOMENTLIST_ALL);
        if (listParam.getPage().equals("1")) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("tabjin", "setUserVisibleHint");
        boolean isFistVisit = true;
        if (getArguments() != null)
            isFistVisit = getArguments().getBoolean("isFistVisit");
        if (isVisibleToUser && isFistVisit) {
//            getMomentsList();
            if (getArguments() != null)
                getArguments().putBoolean("isFistVisit", false);
        }
       /* if(isVisibleToUser){
            if(adapter!=null){
                adapter.regesterEventBus(true);
            }
        }else{
            if(adapter!=null){
                adapter.regesterEventBus(false);
            }
        }*/
//        super.onHiddenChanged(isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        switch (eventCenter.getEventCode()) {
            case "noSuccess":
                break;
            case Config.MOMENTLIST_ALL:
                if (null != refreshLayout) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setLoading(false);
                }
                JSONObject jo = (JSONObject) eventCenter.getData();
                total = jo.getInt("total");
                List<ClasszMoment> moments = GsonUtils.toArray(ClasszMoment.class, jo);
                if (moments != null || moments.size() > 0) {
                    loadingLayout.onDone();
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(filterData(moments, "1001", "1002", "1003"));
                    adapter.notifyDataSetChanged();
                } else {
                    loadingLayout.onEmpty();
                }
                break;
            case Config.REFRESHMOMENTLIST:
                index = Integer.parseInt(eventCenter.getData().toString());
                listParam.setClassId(classnames.get(index).getClassId());
                getMomentsList();
                break;
            case Config.REFRESHLASTEST:
                refreshLastest();
                listView.setSelection(0);
                break;
        }

    }

    /**
     * 筛选数据
     *
     * @param moments 被筛选的数据集合
     * @param dcCodes 需要被筛选的数据的code
     * @return
     */
    public static List<ClasszMoment> filterData(List<ClasszMoment> moments, String... dcCodes) {
        List<ClasszMoment> list = new ArrayList<>();
        List<String> codes = new ArrayList<>();
        for (String str : dcCodes) {
            codes.add(str);
        }
        for (ClasszMoment moment : moments) {
            if (codes.contains(moment.getDcCode())) {
                list.add(moment);
            }
        }
        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tabjin", "onResume");

        GSYVideoManager.onResume();
//        refreshLastest();
//        getMomentsList();
    }

    private void refreshLastest() {
        page = 1;
        listParam.setPage(String.valueOf(page));
        getMomentsList();
    }

    @Override
    public void onRefresh() {
        refreshLastest();
    }

    @Override
    public void onLoad() {
        if (list != null && list.size() < total) {
            page++;
            listParam.setPage(String.valueOf(page));
            getMomentsList();
        } else {
            refreshLayout.setLoading(false);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("tabjin", "onPause");
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tabjin", "onDestroy");
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("tabjin", "onConfigurationChanged");
        //如果旋转了就全屏
        if (newConfig.orientation != ActivityInfo.SCREEN_ORIENTATION_USER) {
            mFull = false;
        } else {
            mFull = true;
        }

    }

}
