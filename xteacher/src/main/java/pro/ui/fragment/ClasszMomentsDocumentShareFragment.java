package pro.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class ClasszMomentsDocumentShareFragment extends BaseFragment implements RefreshLayout.OnRefreshListener,RefreshLayout.OnLoadListener{

    @BindView(R.id.refresh_layout)
    RefreshLayout refreshLayout;
    //    RecyclerView recyclerView;
    @BindView(R.id.listView)
    ListView listView;

    //ListView的适配器
    ClasszMomentsAdapter adapter;
    //Share 班级动态集合
    List<ClasszMoment> list = new ArrayList<>();
    //资源类型集合
    private List<ClasszMomentType> classzMomentTypes = new ArrayList<>();
    //该老师的班级集合
    private List<ClassesBeans> classnames = new ArrayList<>();
    //当前选中的班级index
    private int index = 0;
    //当前选中的classid
    private String classId ;
    //当前请求
    private int page = 1;

    private ClasszMomentsMomentList listParam ;
    //动态总条数
    private int total = 0;

    private DefaultLoadingLayout loadingLayout;
    private static final String  REFRESHLASTEST = Config.REFRESHLASTEST+"DOC";
    private static final String  REFRESHMOMENTLIST = Config.REFRESHMOMENTLIST+"DOC";

    private View mFragmentView;
//    private static ClasszMomentsAllFragment instance;


    private String dcId;


    public ClasszMomentsDocumentShareFragment() {
    }

    public static ClasszMomentsDocumentShareFragment newInstance() {
        ClasszMomentsDocumentShareFragment fragment = new ClasszMomentsDocumentShareFragment();
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

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        if (null != mFragmentView) {
//            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
//        }
//    }

    @Override
    protected void initViewsAndEvents() {
        classzMomentTypes = ((ClasszMomentsActivity)activity).getClasszMomentType();
        classnames.clear();
        classnames.addAll(((ClasszMomentsActivity) activity).getClassNames());
//        String dcCode = getArguments().getString("dcCode");
        String classId = (classnames.size()>index)?classnames.get(index).getClassId():"";
        listParam = new ClasszMomentsMomentList(classId, String.valueOf(page));
        for(ClasszMomentType type:classzMomentTypes){
            if("1002".equals(type.getDcCode())){
                listParam.setDcId(type.getDcId());
                dcId = type.getDcId();
            }
        }
        getMomentsList();
        loadingLayout = SmartLoadingLayout.createDefaultLayout(getActivity(),refreshLayout);
        loadingLayout.onLoading();
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setColorSchemeResources(Constance.colors);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        listView.setDividerHeight(0);
        if(adapter == null) {
            adapter = new ClasszMomentsAdapter(getActivity(), list,"DOC");
            adapter.addItemViewDelegate(new ClasszMomentsShare(adapter, list, "DOC"));
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
                    if(event.getAction()==(MotionEvent.ACTION_DOWN)){
                        EventBus.getDefault().post(new EventCenter(Config.HIDDENINPUTWINDOW,null));
                        ViewTools.hideSoftInput(getActivity());
                    }
                    return false;
                }
            });
        }
    }

    private void getMomentsList(){
        if(dcId!=null)
            listParam.setDcId(dcId);
        NetworkRequest.request(listParam, CommonUrl.MOMENTLIST, Config.MOMENTLIST_DOC);
        if (listParam.getPage().equals("1")) {
            refreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        boolean isFistVisit = true;
        if (getArguments() != null)
            isFistVisit = getArguments().getBoolean("isFistVisit");
        if (isVisibleToUser && isFistVisit) {
//            getMomentsList();
            if (getArguments() != null)
                getArguments().putBoolean("isFistVisit", false);

        }
        /*if(isVisibleToUser){
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
            case Config.MOMENTLIST_DOC:
                if(null != refreshLayout) {
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setLoading(false);
                }
                JSONObject jo = (JSONObject)eventCenter.getData();
                total = jo.getInt("total");
                List<ClasszMoment> moments = GsonUtils.toArray(ClasszMoment.class, jo);
                if(moments != null || moments.size()>0){
                    loadingLayout.onDone();
                    if(page == 1){
                        list.clear();
                    }
                    list.addAll(ClasszMomentsAllFragment.filterData(moments,"1001","1002","1003"));
                    adapter.notifyDataSetChanged();
                }else{
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
        if(list != null&& list.size()<total){
            page++;
            listParam.setPage(String.valueOf(page));
            getMomentsList();
        }else{
            refreshLayout.setLoading(false);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
}
