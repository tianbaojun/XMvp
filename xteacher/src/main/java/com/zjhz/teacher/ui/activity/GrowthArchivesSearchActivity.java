package com.zjhz.teacher.ui.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.StudentBean;
import com.zjhz.teacher.ui.adapter.ExpandListViewDBAdapter;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.listview.PinnedHeaderExpandableListView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrowthArchivesSearchActivity extends BaseActivity implements PinnedHeaderExpandableListView.OnHeaderUpdateListener {

    @BindView(R.id.title_back_img)
    ImageView backImg;
    @BindView(R.id.growth_search_student_ed)
    EditText searchEd;
    @BindView(R.id.growth_archives_student_list)
    ExpandableListView expandableListView;
    @BindView(R.id.student_list_view)
    ListView listView;

    private List<ClassesBeans> groupList = new ArrayList<>();
    private List<List<List<StudentBean>>> childList = new ArrayList<>();

    private List<StudentBean> studentSearchList = new ArrayList<>();

    private ExpandListViewDBAdapter expandListViewDBAdapter;
    private ListViewDBAdapter listViewDBAdapter;
    private ViewDataBinding headViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_archives_search);
        ButterKnife.bind(this);

        getClassList();

        setUpBackImg();
        setUpSearchEd();
        setUpExpandableListView();
        setUpListView();


        expandableListView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        if(back())
            super.onBackPressed();
    }

    private void setUpBackImg(){
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private boolean back(){
        if(expandableListView.getVisibility() == View.GONE){
            expandableListView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            searchEd.setText("");
            BaseUtil.hideSoftKeyBoard(GrowthArchivesSearchActivity.this, searchEd);
            return false;
        }else {
            finish();
            return true;
        }
    }

    private void setUpSearchEd(){
        searchEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableListView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                BaseUtil.showSoftKeyBoard(GrowthArchivesSearchActivity.this, searchEd);
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
                    BaseUtil.hideSoftKeyBoard(GrowthArchivesSearchActivity.this, searchEd);
                    //搜索
                    Map<String, String> map = new HashMap<>();
                    map.put("name", searchEd.getText().toString());
                    NetworkRequest.request(map, CommonUrl.GET_STUDENT_LIST, "student__name_list");
                    return true;
                }
                return false;
            }
        });
    }

    private void setUpExpandableListView(){
        expandListViewDBAdapter = new ExpandListViewDBAdapter<>(this, groupList, childList, R.layout.student_list_group, BR.classesBeans,
                R.layout.student_list_child_item_grid, 0);

        expandableListView.setAdapter(expandListViewDBAdapter);

        expandListViewDBAdapter.setCallBack(new ExpandListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, final int groupPosition, int childPosition) {
                GridView studentGrid = (GridView) rootView.findViewById(R.id.student_grid);
                studentGrid.setAdapter(new CommonAdapter<StudentBean>
                        (GrowthArchivesSearchActivity.this, R.layout.student_list_child_item_grid_item, childList.get(groupPosition).get(0)) {
                    @Override
                    protected void convert(ViewHolder viewHolder, StudentBean item, int position) {
                        viewHolder.setText(R.id.student_name, item.getName());
                        CircleImageView header =  viewHolder.getView(R.id.student_header_img);
                        GlideUtil.loadImageHead(item.getPhotoUrl(), header);
                    }
                });
                studentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //页面跳转
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bean", childList.get(groupPosition).get(0).get(position));
                        startActivity(GrowthArchivesActivity.class, bundle);
                    }
                });
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Map<String, String> map = new HashMap<>();
                map.put("classId", groupList.get(groupPosition).getClassId());
                NetworkRequest.request(map, CommonUrl.GET_STUDENT_LIST, "student_list"+groupPosition);
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
        listViewDBAdapter = new ListViewDBAdapter<StudentBean>(this, studentSearchList, R.layout.growth_student_search_list, BR.studentBean);
        listView.setAdapter(listViewDBAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //页面跳转
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", studentSearchList.get(position));
                startActivity(GrowthArchivesActivity.class, bundle);
            }
        });
    }

    private void getClassList(){
        Map<String, String> map = new HashMap<>();
        map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
        map.put("page", "1");
        map.put("pageSize", "100");
        map.put("queryStatus", "1");
        NetworkRequest.request(map, CommonUrl.homeworkClassesList, "classList");

    }


    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        if(event.getEventCode() == null)
            return;
        switch (event.getEventCode()){
            case "classList":
                groupList.clear();
                groupList.addAll(GsonUtils.toArray(ClassesBeans.class, (JSONObject) event.getData()));
                expandListViewDBAdapter.notifyDataSetChanged();
                for(int i = 0; i < groupList.size(); i++){
                    List<List<StudentBean>> list = new ArrayList<List<StudentBean>>();
                    list.add(new ArrayList<StudentBean>());
                    childList.add(list);
                }
                break;
            case "student__name_list":
                studentSearchList.clear();
                studentSearchList.addAll(GsonUtils.toArray(StudentBean.class, (JSONObject) event.getData()));
                listViewDBAdapter.notifyDataSetChanged();
                break;
        }
        if(event.getEventCode().contains("student_list")){
            int position = Integer.parseInt(event.getEventCode().substring(12));
            List<StudentBean> studentBeanList = GsonUtils.toArray(StudentBean.class, (JSONObject) event.getData());
            childList.get(position).set(0, studentBeanList);
            expandListViewDBAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public View getPinnedHeader() {
        headViewBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.student_list_group, expandableListView, false);
        return headViewBinding.getRoot();
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        if (firstVisibleGroupPos < 0)
            return;
        headViewBinding.setVariable(BR.classesBeans, expandListViewDBAdapter.getGroup(firstVisibleGroupPos));
        headViewBinding.notifyChange();
        expandableListView.expandGroup(firstVisibleGroupPos);
    }
}
