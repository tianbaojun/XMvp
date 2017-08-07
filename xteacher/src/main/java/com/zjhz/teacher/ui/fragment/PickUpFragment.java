package com.zjhz.teacher.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.MyScheduleQueryPaging;
import com.zjhz.teacher.NetworkRequests.request.PickUpRequestBean;
import com.zjhz.teacher.NetworkRequests.response.MyScheduleQueryNamePagingResponse;
import com.zjhz.teacher.NetworkRequests.response.PickUpStudentResBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.Dict;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class PickUpFragment extends BaseFragment implements RefreshLayout.OnLoadListener,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.pick_up_refresh)
    RefreshLayout refreshLayout;
    @BindView(R.id.pick_up_fragment_list_view)
    ListView listView;

    public final static int WSH = 1;
    public final static int YSH = 2;
    public final static String YSHRequest = Config.PICK_UP_SEARCHER_ISDONE+2;
    public final static String WSHRequest = Config.PICK_UP_SEARCHER_ISDONE+1;




    private static final String STATUS = "status";
//    private static final String LIST = "list";
    private static final String IS_FIRST = "is_first";
    private static final String pick_up_approval = "pick_up_approval";

    private List<Dict> teacherList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    protected int status;

//    protected OnFragmentInteractionListener mListener;
    protected ListViewDBAdapter<PickUpStudentResBean> adapter;
    protected List<PickUpStudentResBean> beanList = new ArrayList<>();
    protected PickUpRequestBean requestBean = new PickUpRequestBean();

    //总数
    private int total = 0;

    public PickUpFragment() {
        // Required empty public constructor
    }

    public static PickUpFragment newInstance(int status) {
        PickUpFragment fragment = new PickUpFragment();
        Bundle args = new Bundle();
        args.putInt(STATUS, status);
        args.putBoolean(IS_FIRST, true);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt(STATUS);
            if (status == WSH) {
                requestBean.isApprove = 0;
            }else{
                requestBean.isApprove = 1;
            }
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_pick_up, null, false);
    }

    @Override
    protected void initViewsAndEvents() {
        adapter = new ListViewDBAdapter<PickUpStudentResBean>(context, beanList, R.layout.pick_up_list_item, BR.pickupBean);

        listView.setAdapter(adapter);
        setListAdapterItemCallBack();
        requestBean.page = 1;
        requestBean.pageSize = 5;
        getSearcherList();

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
    }

    private void setListAdapterItemCallBack(){
        adapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, final int position) {
                FrameLayout frameLayoutP = (FrameLayout)rootView.findViewById(R.id.pick_up_parent);
                FrameLayout frameLayoutS = (FrameLayout)rootView.findViewById(R.id.pick_up_student);
                final Spinner teacherSp = (Spinner)rootView.findViewById(R.id.jsls_sp);
                ScrollViewWithListView listView = (ScrollViewWithListView)rootView.findViewById(R.id.pickup_approval_list);

                GlideUtil.loadImageBg(beanList.get(position).getShuttlePersonPhotoUrl(), frameLayoutP);
                GlideUtil.loadImageBg(beanList.get(position).getStudentPhotoUrl(), frameLayoutS);

                setApprovalList(listView, beanList.get(position).getCheckMsgs());

                Collections.sort(teacherList);
                ArrayAdapter spAdapter = new ArrayAdapter<Dict>(context, R.layout.support_simple_spinner_dropdown_item, teacherList);
                teacherSp.setAdapter(spAdapter);
                for(int i = 0; i < teacherList.size(); i++){
                    Dict dict = teacherList.get(i);
                    if(dict != null && dict.id != null && dict.id.equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey))){
                        teacherSp.setSelection(i);
                    }
                }

                TextView approvalBtn = (TextView)rootView.findViewById(R.id.pick_up_approval_btn);
                if(status == WSH){
                    approvalBtn.setVisibility(View.VISIBLE);
                    approvalBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 审核
                            String teacherId = teacherSp.getSelectedItem() == null ? null : ((Dict)teacherSp.getSelectedItem()).id;
                            approval(beanList.get(position).getState(), beanList.get(position).getApplyId(), position, teacherId);
                        }
                    });
                }else {
                    approvalBtn.setVisibility(View.GONE);
                }

                rootView.findViewById(R.id.pick_up_parent_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + beanList.get(position).getShuttlePersonPhone());
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void setApprovalList(ScrollViewWithListView listView, List<PickUpStudentResBean.CheckMsgsBean> dataList){
        ListViewDBAdapter<PickUpStudentResBean.CheckMsgsBean> adapter =
                new ListViewDBAdapter<>(context, dataList, R.layout.pickup_item_audit_teacher_item, BR.checkMsgsBean);
        listView.setAdapter(adapter);
    }

    //审核
    private void approval(int state, String applyId, int position, String teacherId){
        Map<String, String> map = new HashMap<>();
        map.put("__curentUser", SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey));
        map.put("schoolId", SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey));
        map.put("applyId", applyId);
        String url = null;
//        0,新增,
//        1, 核实家长入校,
//        2, 待交接,
//        3, 审核中,
//        4, 待离校,
//        5, 已完结,
//        6, 放弃执行,//未进入学校时放弃
//        7, 未执行,
//        8, 异常离校,
//        9, 放弃执行;//进入学校后放弃
        switch (state){
            case 0:
                if(teacherId == null) {
                    ToastUtils.showShort("请选择老师");
                    return;
                }
                map.put("handoverTeacherId", teacherId);
                url = CommonUrl.BZR_APPROVAL;
                break;
            case 2:
                url = CommonUrl.WM_APPROVAL_ENTER;
                break;
            case 1:
                url = CommonUrl.JJLS_APPROVAL;
                break;
            case 3:
                url = CommonUrl.SHLS_APPROVAL;
                break;
            case 4:
                url = CommonUrl.MW_APPROVAL;
                break;
        }

        if(url != null){
            NetworkRequest.request(map, url, pick_up_approval);
            dialog.show();
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    private void getSearcherList(){
        requestBean.__curentUser = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
        requestBean.schoolId = SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey);
        NetworkRequest.request(requestBean, CommonUrl.PICK_UP_SEARCHER_ISDONE, Config.PICK_UP_SEARCHER_ISDONE+status);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) throws JSONException {
        switch (event.getEventCode()){
            case Config.ERROR:
            case Config.ERROR_JSON:
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case YSHRequest:
            case WSHRequest:
//                for(int i = 0; i < 10; i++) {
////                    beanList.add(initBean());
//                    beanList.addAll(JSON.parseArray(initJson(), PickUpStudentResBean.class));
//                }
                String fromCode = event.getEventCode().substring(Config.PICK_UP_SEARCHER_ISDONE.length(),event.getEventCode().length());
                if (String.valueOf(status).equals(fromCode)) {
                    total = ((JSONObject) event.getData()).optInt("total");
                    if (requestBean.page == 1)
                        beanList.clear();

                    beanList.addAll(GsonUtils.jsonToArray(PickUpStudentResBean.class, (JSONObject) event.getData()));
                    boolean hasGetTeacherList = false;
                    for (PickUpStudentResBean bean : beanList) {
                        if (status == WSH) {
                            bean.setMeApproval(true);
                        } else if (status == YSH) {
                            bean.setMeApproval(false);
                        }
                        if (bean.getState() == 0) {
                            bean.setBZR(true);
                            if (!hasGetTeacherList) {
                                hasGetTeacherList = true;
                                getTeacherList();
                            }
                        }

                        for(int i = 0; i < bean.getCheckMsgs().size(); i++){
                            bean.getCheckMsgs().get(i).setState(i);
                        }
                    }
                    refreshLayout.setRefreshing(false);
                    refreshLayout.setLoading(false);
                    adapter.notifyDataSetChanged();
                }
                break;
            case Config.MYSCHEDULEPAGING_NEW:
                teacherList.clear();
                MyScheduleQueryNamePagingResponse mMyScheduleQueryNamePagingResponse = GsonUtils.toObject(event.getData().toString(),MyScheduleQueryNamePagingResponse.class);
                if (mMyScheduleQueryNamePagingResponse != null) {
                    for (int i = 0; i < mMyScheduleQueryNamePagingResponse.getData().size(); i++) {
                        Dict dict = new Dict();
                        dict.id = mMyScheduleQueryNamePagingResponse.getData().get(i).getTeacherId();
                        dict.value = mMyScheduleQueryNamePagingResponse.getData().get(i).getName();
                        teacherList.add(dict);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case pick_up_approval:
                dialog.dismiss();
                getSearcherList();
                break;
        }
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onRefresh() {
        requestBean.page = 1;
        getSearcherList();
    }

    @Override
    public void onLoad() {
        if(beanList.size()<total) {
            requestBean.page++;
            getSearcherList();
        }else{
            refreshLayout.setLoading(false);
        }
    }

    private void getTeacherList(){
        //用我的课表的老师列表
        NetworkRequest.request(new MyScheduleQueryPaging("1","100"), CommonUrl.MYSCHEDULEPAGING_NEW, Config.MYSCHEDULEPAGING_NEW);
    }

  /*  public PickUpStudentResBean initBean(){
        PickUpStudentResBean bean = new PickUpStudentResBean();

        bean.setApplyShuttleTime("2017-01-01 10:00");
        bean.setStudentName("小明");
        bean.setStudentClassName("一（1）班"); //所在班级
        bean.setShuttlePersonName("大明"); //接送人姓名
        bean.setShuttlePersonRelation("爸爸"); //接送人与学生的关系
//        public String state; //状态
//        public String applyPersonRelation; //申请人与学生的关系
//        public String createUser; //创建/申请人用户ID
//        public String applyPersonName; //申请人姓名
        bean.setShuttleCause("家中有事"); //接送原因
        bean.setStudentPhotoUrl("http://img.qq745.com/uploads/allimg/150104/1-150104153425.jpg"); //学生照片url
        bean.setShuttlePersonPhotoUrl("http://img.qq745.com/uploads/allimg/150104/1-150104153425.jpg"); //接送人照片URL
//        public String checkMsgs; //教师、门卫们交接/审核/核对信息

        bean.setPhoneNum("18059293976");//电话号码
        bean.setState(1);
        return bean;
    }

    private String initJson(){
        String json = "[\n" +
                "        {\n" +
                "            \"applyId\": \"387794854798692352\",\n" +
                "            \"applyPersonName\": \"汤楚琪\",\n" +
                "            \"applyPersonRelation\": \"BASE_PARENT_RELATION_2\",\n" +
                "            \"applyShuttleTime\": \"2017-05-31 16:00:00\",\n" +
                "            \"checkMsgs\": [\n" +
                "                {\n" +
                "                    \"checkDesc\": \"班主任\",\n" +
                "                    \"checkMsgId\": \"387794858569371648\",\n" +
                "                    \"checkRole\": \"0\",\n" +
                "                    \"checkState\": 1,\n" +
                "                    \"checkTime\": \"2017-06-01 18:15:26\",\n" +
                "                    \"name\": \"章银平\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"交接教师\",\n" +
                "                    \"checkMsgId\": \"387794858695200768\",\n" +
                "                    \"checkRole\": \"1\",\n" +
                "                    \"checkState\": 1,\n" +
                "                    \"checkTime\": \"2017-06-02 09:09:51\",\n" +
                "                    \"name\": \"陈国栋\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"审核教师\",\n" +
                "                    \"checkMsgId\": \"387794858959441920\",\n" +
                "                    \"checkRole\": \"2\",\n" +
                "                    \"checkState\": 1,\n" +
                "                    \"checkTime\": \"2017-06-02 09:19:52\",\n" +
                "                    \"name\": \"杨妙\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"审核教师\",\n" +
                "                    \"checkMsgId\": \"387794859055910912\",\n" +
                "                    \"checkRole\": \"2\",\n" +
                "                    \"checkState\": 0,\n" +
                "                    \"name\": \"梁棋\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"审核教师\",\n" +
                "                    \"checkMsgId\": \"387794859118825472\",\n" +
                "                    \"checkRole\": \"2\",\n" +
                "                    \"checkState\": 1,\n" +
                "                    \"checkTime\": \"2017-06-02 09:31:06\",\n" +
                "                    \"name\": \"王岚\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"审核教师\",\n" +
                "                    \"checkMsgId\": \"387794859177545728\",\n" +
                "                    \"checkRole\": \"2\",\n" +
                "                    \"checkState\": 1,\n" +
                "                    \"checkTime\": \"2017-06-02 09:31:28\",\n" +
                "                    \"name\": \"安全教师\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"checkDesc\": \"门卫放行\",\n" +
                "                    \"checkMsgId\": \"387794859236265984\",\n" +
                "                    \"checkRole\": \"3\",\n" +
                "                    \"checkState\": 0,\n" +
                "                    \"name\": \"门卫\",\n" +
                "                    \"schoolId\": \"288069341826519040\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"classTeacherUser\": \"288078455667429377\",\n" +
                "            \"createTime\": \"2017-06-01 18:13:14\",\n" +
                "            \"createUser\": \"288114334880501778\",\n" +
                "            \"flowId\": \"387336781990727680\",\n" +
                "            \"handoverTeacherUser\": \"288078455709372436\",\n" +
                "            \"haveCheckTimes\": 3,\n" +
                "            \"haveCheckUsers\": \"288078455692595202,288078455692595210,288078455738732561\",\n" +
                "            \"needCheckGroup\": \"\",\n" +
                "            \"needCheckTeacher\": \"288078455692595205\",\n" +
                "            \"schoolId\": \"288069341826519040\",\n" +
                "            \"shuttleCause\": \"培训奥数\",\n" +
                "            \"shuttlePersonId\": \"377906999343452164\",\n" +
                "            \"shuttlePersonName\": \"张三\",\n" +
                "            \"shuttlePersonPhotoUrl\": \"http://www.baidu.com\",\n" +
                "            \"shuttlePersonRelation\": \"叔侄\",\n" +
                "            \"state\": \"2\",\n" +
                "            \"studentClassId\": \"288113366604451840\",\n" +
                "            \"studentClassName\": \"一（1）班\",\n" +
                "            \"studentId\": \"288114322939318285\",\n" +
                "            \"studentName\": \"向榆汇\",\n" +
                "            \"studentPhotoUrl\": \"http://www.student.photo\",\n" +
                "            \"term\": \"2016-2017-上\",\n" +
                "            \"totalCheckTimes\": 4\n" +
                "        }\n" +
                "    ]";
        return json;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
