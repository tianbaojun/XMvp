package com.zjhz.teacher.ui.fragment.homework;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.StudentBean;
import com.zjhz.teacher.ui.activity.homework.HomeworkCorrectActivity;
import com.zjhz.teacher.ui.adapter.ExpandListViewDBAdapter;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;



/**
 * Created by zzd on 2017/6/29.
 */

public class StudentHandInFragment extends BaseFragment{
    @BindView(R.id.search_student_ed)
    EditText searchEd;
    @BindView(R.id.student_expand_list)
    ExpandableListView expandableListView;
    @BindView(R.id.student_list_view)
    ListView listView;
    @BindView(R.id.student_search_cancel)
    TextView cancelTv;

    private static final String HOMEWORK_TYPE = "homeworkType";
    private static final String HOMEWORK_ID = "homeworkId";

    private List<ClassesBeans> groupList = new ArrayList<>();
    private List<List<List<StudentBean>>> childList = new ArrayList<>();

    private List<StudentBean> studentSearchList = new ArrayList<>();

    private ExpandListViewDBAdapter expandListViewDBAdapter;
    private ListViewDBAdapter listViewDBAdapter;

    public static StudentHandInFragment newInstance(String homeworkId,String homeworkType){
        StudentHandInFragment fragment = new StudentHandInFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HOMEWORK_TYPE, homeworkType);
        bundle.putString(HOMEWORK_ID, homeworkId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_student_hand_in, null);
    }

    @Override
    protected void initViewsAndEvents() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void lazyLoad() {
        getClassList();
        setUpSearchEd();
        setUpExpandableListView();
        setUpListView();

        expandableListView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void setUpSearchEd(){
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                cancelTv.setVisibility(View.INVISIBLE);
                searchEd.setText("");
                BaseUtil.hideSoftKeyBoard(getContext(), searchEd);
            }
        });
        searchEd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                expandableListView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                cancelTv.setVisibility(View.VISIBLE);
                BaseUtil.showSoftKeyBoard(getContext(), searchEd);
                return false;
            }
        });

        searchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(TextUtils.isEmpty(searchEd.getText().toString())) {
                        ToastUtils.showShort("请输入学生姓名");
                        return false;
                    }
                    // 先隐藏键盘
                    BaseUtil.hideSoftKeyBoard(getContext(), searchEd);
                    //搜索
                    Map<String, String> map = new HashMap<>();
                    map.put("name", searchEd.getText().toString());
                    map.put("homeworkId", getArguments().getString(HOMEWORK_ID));
                    NetworkRequest.request(map, CommonUrl.HOMEWORK_STUDENT_SUBMIT_INFO, "student__name_list");
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    }

    private void setUpExpandableListView(){
        expandListViewDBAdapter = new ExpandListViewDBAdapter<>(getContext(), groupList, childList, R.layout.student_homework_list_group, BR.classesBeans,
                R.layout.student_list_child_item_grid, 0);

        expandableListView.setAdapter(expandListViewDBAdapter);

        expandListViewDBAdapter.setCallBack(new ExpandListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, final int groupPosition, int childPosition) {
                GridView studentGrid = (GridView) rootView.findViewById(R.id.student_grid);
                studentGrid.setAdapter(new CommonAdapter<StudentBean>
                        (getContext(), R.layout.student_list_child_item_grid_item, childList.get(groupPosition).get(0)) {
                    @Override
                    protected void convert(ViewHolder viewHolder, StudentBean item, int position) {
                        viewHolder.setText(R.id.student_name, item.getName());
                        if(item.getFlag() == 1){
                            viewHolder.setTextColorRes(R.id.student_name, R.color.title_background_red);
                        }
                        CircleImageView header =  viewHolder.getView(R.id.student_header_img);
                        GlideUtil.loadImageHead(item.getPhotoUrl(), header);
                    }
                });
                studentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //页面跳转
                        gotoOtherActivity(childList.get(groupPosition).get(0).get(position));
                    }
                });
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (childList.get(groupPosition) == null || childList.get(groupPosition).get(0).size() == 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put("classId", groupList.get(groupPosition).getClassId());
                    map.put("homeworkId", getArguments().getString(HOMEWORK_ID));
                    NetworkRequest.request(map, CommonUrl.HOMEWORK_STUDENT_SUBMIT_INFO, "student_list" + groupPosition);
                }
                return false;
            }
        });
//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if(childList.get(groupPosition) == null || childList.get(groupPosition).size() == 0){
//                    Map<String, String> map = new HashMap<>();
//                    map.put("classId", groupList.get(groupPosition).getClassId());
//                    NetworkRequest.request(map, CommonUrl.GET_STUDENT_LIST, "student_list"+groupPosition);
//                }
//            }
//        });

//        expandableListView.setOnHeaderUpdateListener(this);
    }

    private void setUpListView(){
        listViewDBAdapter = new ListViewDBAdapter<StudentBean>(getContext(), studentSearchList, R.layout.growth_student_search_list, BR.studentBean);
        listView.setAdapter(listViewDBAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //页面跳转
                gotoOtherActivity(studentSearchList.get(position));
            }
        });
    }

    private void gotoOtherActivity(StudentBean bean){
        if(bean.getFlag() == 0) {
            ToastUtils.showShort("该学生未提交作业");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        bundle.putString("homeworkId",getArguments().getString(HOMEWORK_ID));
        bundle.putString("homeworkType",getArguments().getString(HOMEWORK_TYPE));
        startActivity(HomeworkCorrectActivity.class, bundle);
    }

    private void getClassList(){
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("homeworkId", getArguments().getString(HOMEWORK_ID));
        NetworkRequest.request(map, CommonUrl.HOMEWORK_STUDENT_SUBMIT_INFO_FIRST, "classList");

//        if(getActivity() instanceof HomeworkManageDetailActivity) {
//            ((HomeworkManageDetailActivity) getActivity()).addNotifyClassList(this);
//        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        dialog.dismiss();
        switch (eventCenter.getEventCode()){
            case "classList":
                groupList.clear();
                groupList.addAll(GsonUtils.toArray(ClassesBeans.class, (JSONObject) eventCenter.getData()));
                if(groupList == null)
                    return;
                for(int i = 0; i < groupList.size(); i++){
                    List<StudentBean> list = new ArrayList<>();
                    if(i == 0 && groupList.get(0).getStudentList() != null){
                        list = groupList.get(0).getStudentList();
                    }
                    List<List<StudentBean>> lists = new ArrayList<>();
                    lists.add(list);
                    childList.add(lists);
                }
                expandListViewDBAdapter.notifyDataSetChanged();
                if(groupList.size() > 0)
                    expandableListView.expandGroup(0);
                break;
            case "student__name_list":
                studentSearchList.clear();
                studentSearchList.addAll(GsonUtils.toArray(StudentBean.class, (JSONObject) eventCenter.getData()));
                listViewDBAdapter.notifyDataSetChanged();
                break;
        }
        if(eventCenter.getEventCode().contains("student_list")){
            int position = Integer.parseInt(eventCenter.getEventCode().substring(12));
            List<StudentBean> studentBeanList = GsonUtils.toArray(StudentBean.class, (JSONObject) eventCenter.getData());
            childList.get(position).set(0, studentBeanList);
            expandListViewDBAdapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public void notifyData(List<ClassesBeans> classList) {
//        if(groupList.size() > 0)
//            return;
//        groupList.clear();
//        groupList.addAll(classList);
//        for(int i = 0; i < groupList.size(); i++){
//            groupList.get(i).setName(groupList.get(i).getClassName());
//            List<List<StudentBean>> list = new ArrayList<List<StudentBean>>();
//            list.add(new ArrayList<StudentBean>());
//            childList.add(list);
//        }
//        if(expandListViewDBAdapter!=null)
//            expandListViewDBAdapter.notifyDataSetChanged();
//    }
}
