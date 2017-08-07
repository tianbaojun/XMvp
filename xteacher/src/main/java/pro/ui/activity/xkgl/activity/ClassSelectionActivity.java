package pro.ui.activity.xkgl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.RefreshLayout;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.utils.BaseUtil;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.adapter.SimpleFragmentPagerAdapter;
import pro.kit.ViewTools;
import pro.ui.activity.xkgl.adapter.WeekAdapter;
import pro.ui.activity.xkgl.fragment.admin.BanJiSelectionFramgent;
import pro.ui.activity.xkgl.fragment.admin.StudentSelectionFramgent;
import pro.ui.activity.xkgl.testbean.BZRhistory;
import pro.ui.activity.xkgl.testbean.SaiXuanbean;
import pro.ui.activity.xkgl.testbean.ZHbean;
import pro.widget.Selector.AutoLocateHorizontalView;

public class ClassSelectionActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.search_student_ed)
    EditText search_et;
    //班级切换
    @BindView(R.id.grade_change)
    ImageView gradeChange;
    //综合查看
    @BindView(R.id.zonghe_tv)
    TextView zonghe_tv;
    //班级选课
    @BindView(R.id.banji_tv)
    TextView banji_tv;
    //教师选课
    @BindView(R.id.jiaoshi_tv)
    TextView jiaoshi_tv;
    //综合查看LinearLayout
    @BindView(R.id.zonghe_ll)
    LinearLayout zonghe_ll;
    //班级选课LinearLayout
    @BindView(R.id.banji_ll)
    LinearLayout banji_ll;
    //教师选课LinearLayout
    @BindView(R.id.jiaoshi_ll)
    LinearLayout jiaoshi_ll;
    //综合查看view
    @BindView(R.id.zonghe_content)
    View zonghe_content;
    //班级选课view
    @BindView(R.id.selection_content)
    View selection_content;
    //成绩查看view
    @BindView(R.id.lesson_record_content)
    View lesson_record_content;
    //筛选drawerLayout
    @BindView(R.id.drawer_layout_selection)
    DrawerLayout drawerLayout;
    //抽屉布局
    @BindView(R.id.rl_drawer_layout)
    RelativeLayout rl_drawer_layout;

    //综合RefreshLayout
    RefreshLayout zongheRefreshLayout;
    //综合listview
    ListView zongheListView;
    //综合adapter
    CommonAdapter zongheAdapter;
    //综合page
    private int zonghePage = 1;
    //综合数据total
    private int zongheTotal = 0;
    //综合数据结合
    private List<ZHbean> zongheBeans = new ArrayList();

    //选课Tab
    TabLayout selectionTab;
    //选课viewpager
    ViewPager selectionViewpager;
    //选课Tab标题
    List<String> selectionTitles = new ArrayList<>();
    //选课Fragment
    List<Fragment> selectionFragments = new ArrayList<>();
    //选课的fragment adapter
    SimpleFragmentPagerAdapter selFragAdapter;

    //课堂记录适配器
    private CommonAdapter recordAdapter;
    //课堂记录Bean
    private BZRhistory history = new BZRhistory();
    //课堂记录listview
    private ListView recordListView;
    //课堂记录refreshLayout
    private RefreshLayout recordRefreshLayout;
    //周次选择器
    @BindView(R.id.week_tab)
    AutoLocateHorizontalView weekSelector;
    //周次列表
    private List<String> weeks = new ArrayList<>();
    private List<String> weekStrs = new ArrayList<>();
    WeekAdapter weekAdapter;
    private boolean isFirst = true;

    //筛选信息
    private SaiXuanbean saiXuanbean = new SaiXuanbean();
    //筛选Grid adapter
    private CommonAdapter typeAdapter,propAdapter;
    //筛选Grid 点击监听
    private AdapterView.OnItemClickListener drawerOnItemListener;
    //筛选数据
    private  String typeName, propName,subjectName,teacherName,supName,courseNo,classId;
    //班级id
    private String classzId;
    //当前显示的是内容
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_selection);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (!SharePreCache.isEmpty(title)) {
            title_tv.setText(title);
        }

        zonghe_tv.setTextColor(getResources().getColor(R.color.text_color_8CA404));
        handleShowContent(zonghe_content);

        initZoneHeView();
        initSelectionView();
        initLessonRecordView();
        getProp();

    }

    /**
     * 初始化综合查看
     */
    private void initZoneHeView() {
        zongheRefreshLayout = (RefreshLayout) zonghe_content.findViewById(R.id.refresh_layout);
        zongheListView = (ListView) zonghe_content.findViewById(R.id.refresh_listview);
        zongheAdapter = new CommonAdapter<ZHbean>(this, R.layout.activity_class_selection_zonghe_list_item, zongheBeans) {
            @Override
            protected void convert(ViewHolder viewHolder, ZHbean item, int position) {
                viewHolder.setText(R.id.one, item.getCourseNo());
                viewHolder.setText(R.id.two, item.getSubjectName());
                viewHolder.setText(R.id.three, item.getTeacherName());
                viewHolder.setText(R.id.four, item.getStuNum()+"/"+ item.getStuMax());
            }
        };
        zongheListView.setAdapter(zongheAdapter);
        zongheListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",zongheBeans.get(position));
                startActivity(ZoneHeDtlActivity.class,bundle);
            }
        });
        zongheRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                zongheRefreshLayout.setRefreshing(false);
                zonghePage = 1;
                getZongHeData();
//                zongheRefreshLayout.setRefreshing(true);
            }
        });
        zongheRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                zongheRefreshLayout.setLoading(false);
//                if(zongheTotal)
            }
        });
        getZongHeData();
    }


    /**
     * 初始化班级选课
     */
    private void initSelectionView() {
        selectionTab = (TabLayout) selection_content.findViewById(R.id.selection_tablayout);
        selectionViewpager = (ViewPager) selection_content.findViewById(R.id.selection_viewPager);
        selectionTitles.add("班级选课");
        selectionTitles.add("学生选课");
        selectionFragments.add(new BanJiSelectionFramgent());
        selectionFragments.add(new StudentSelectionFramgent());
        selFragAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), selectionTitles, this, selectionFragments);
        selectionViewpager.setAdapter(selFragAdapter);
        selectionViewpager.setOffscreenPageLimit(2);
        selectionTab.setupWithViewPager(selectionViewpager);
        selectionTab.setTabMode(TabLayout.MODE_FIXED);

        //设置编辑框的搜索监听
        search_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(search_et.getText().toString())) {
                        ToastUtils.showShort("请输入学生姓名");
                        return false;
                    }
                    // 先隐藏键盘
                    BaseUtil.hideSoftKeyBoard(ClassSelectionActivity.this, search_et);
                    //搜索
                    /*Map<String, String> map = new HashMap<>();
                    map.put("name", searchEd.getText().toString());
                    NetworkRequest.request(map, CommonUrl.GET_STUDENT_LIST, "student__name_list");*/
                    return true;
                }
                return false;
            }
        });

        /*selectionViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    title_tv.setVisibility(View.GONE);
                    search_et.setVisibility(View.VISIBLE);
                } else {
                    search_et.setVisibility(View.GONE);
                    title_tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    /**
     * 初始化成绩查询
     */
    private void initLessonRecordView() {
        recordListView = (ListView) lesson_record_content.findViewById(R.id.refresh_listview);
        recordRefreshLayout = (RefreshLayout) lesson_record_content.findViewById(R.id.refresh_layout);
//        // TODO: 2017/7/11 课堂记录

        recordAdapter = new CommonAdapter<BZRhistory.History>(this, R.layout.activity_class_selection_zonghe_list_item, history.getHistoryList()) {
            @Override
            protected void convert(ViewHolder viewHolder, BZRhistory.History item, int position) {
                viewHolder.setText(R.id.one, item.getName());
                viewHolder.setText(R.id.two, item.getSubjectName());
//                String level ="" ;
                String status = "";
                switch (item.getStatus()) {
                    case 0:
                        status = "出勤";
                        break;
                    case 1:
                        status = "迟到";
                        break;
                    case 2:
                        status = "缺勤";
                        break;
                }
                viewHolder.setText(R.id.three, status);
                /*switch (item.getLevel()){
                    case "0":
                        level = "优秀";
                        break;
                    case "1":
                        level = "良好";
                        break;
                    case "2":
                        level = "一般";
                        break;
                }*/
                viewHolder.setText(R.id.four, item.getLevelName());
            }
        };
        recordListView.setAdapter(recordAdapter);
        /*weekSelector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getHistoryData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    private void initWeek() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weekSelector.setLayoutManager(linearLayoutManager);
        weekSelector.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                getHistoryData( ClassSelectionActivity.this.history.getWeekList().get(pos).getIndex());
            }
        });
        weekSelector.setItemCount(5);
        weekAdapter = new WeekAdapter(this, weekStrs);
        weekSelector.setInitPos(weekStrs.size()-1);
        weekSelector.setAdapter(weekAdapter);
    }



    /**
     * c初始化抽屉
     */
    private void initDrawer() {
        final ScrollViewWithGridView subjectTypeGrid = (ScrollViewWithGridView) rl_drawer_layout.findViewById(R.id.subject_type_grid);
        final ScrollViewWithGridView subjectPropGrid = (ScrollViewWithGridView) rl_drawer_layout.findViewById(R.id.subject_prop_grid);
        //类别
        typeAdapter = new CommonAdapter<SaiXuanbean.Type>(this, R.layout.textview, saiXuanbean.getTypeList()) {

            @Override
            protected void convert(ViewHolder viewHolder, SaiXuanbean.Type item, int position) {
                viewHolder.setText(R.id.text_tv, item.getTypeName());
                viewHolder.setTextColor(R.id.text_tv, getResources().getColor(R.color.gray3));
                viewHolder.setBackgroundRes(R.id.text_tv, R.drawable.btn_rectangle_radio_gray_background);
            }
        };
        subjectTypeGrid.setAdapter(typeAdapter);
        subjectTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < saiXuanbean.getTypeList().size(); i++) {
                    TextView tv = (TextView) subjectTypeGrid.getChildAt(i);
                    if (tv == view) {
                        if( saiXuanbean.getTypeList().get(position).getOptTypeId().equals(typeName)){
                            tv.setTextColor(getResources().getColor(R.color.gray3));
                            tv.setBackgroundResource(R.drawable.btn_rectangle_radio_gray_background);
                            tv.setText(saiXuanbean.getTypeList().get(i).getTypeName());
                            typeName = "";
                        }else {
                            ViewTools.tvAppendImage(tv, saiXuanbean.getTypeList().get(position).getTypeName(), R.mipmap.term_check);
                            tv.setTextColor(getResources().getColor(R.color.white));
                            tv.setBackgroundResource(R.drawable.radio_green);
                            typeName = saiXuanbean.getTypeList().get(position).getOptTypeId();
                        }
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.gray3));
                        tv.setBackgroundResource(R.drawable.btn_rectangle_radio_gray_background);
                        tv.setText(saiXuanbean.getTypeList().get(i).getTypeName());
                    }
                }
            }
        });
        //名字
        propAdapter = new CommonAdapter<SaiXuanbean.Prop>(this, R.layout.textview, saiXuanbean.getPropList()) {

            @Override
            protected void convert(ViewHolder viewHolder, SaiXuanbean.Prop item, int position) {
                viewHolder.setText(R.id.text_tv, item.getPropName());
                viewHolder.setTextColor(R.id.text_tv, getResources().getColor(R.color.gray3));
                viewHolder.setBackgroundRes(R.id.text_tv, R.drawable.btn_rectangle_radio_gray_background);
            }
        };
        subjectPropGrid.setAdapter(propAdapter);
        subjectPropGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < saiXuanbean.getPropList().size(); i++) {
                    TextView tv = (TextView) subjectPropGrid.getChildAt(i);
                    if (tv == view) {
                        if(saiXuanbean.getPropList().get(position).getOptPropId().equals(propName)){
                            tv.setTextColor(getResources().getColor(R.color.gray3));
                            tv.setBackgroundResource(R.drawable.btn_rectangle_radio_gray_background);
                            tv.setText(saiXuanbean.getPropList().get(i).getPropName());
                            propName = "";
                        }else {
                            ViewTools.tvAppendImage(tv, saiXuanbean.getPropList().get(position).getPropName(), R.mipmap.term_check);
                            tv.setTextColor(getResources().getColor(R.color.white));
                            tv.setBackgroundResource(R.drawable.radio_green);
                            propName = saiXuanbean.getPropList().get(position).getOptPropId();
                        }
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.gray3));
                        tv.setBackgroundResource(R.drawable.btn_rectangle_radio_gray_background);
                        tv.setText(saiXuanbean.getPropList().get(i).getPropName());
                    }
                }
            }
        });

        final int keyHeight = this.getWindowManager().getDefaultDisplay().getHeight() / 3;
        rl_drawer_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    rl_drawer_layout.findViewById(R.id.bottom_ll).setVisibility(View.GONE);
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    rl_drawer_layout.findViewById(R.id.bottom_ll).setVisibility(View.VISIBLE);
                }
            }
        });
        rl_drawer_layout.findViewById(R.id.grade_et).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGradeCls();
            }
        });
        rl_drawer_layout.findViewById(R.id.submit_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subjectName = ((EditText)rl_drawer_layout.findViewById(R.id.subject_name_et)).getText().toString();
                teacherName = ((EditText)rl_drawer_layout.findViewById(R.id.teacher_name_et)).getText().toString();
                supName = ((EditText)rl_drawer_layout.findViewById(R.id.assistant_name_et)).getText().toString();
                courseNo = ((EditText)rl_drawer_layout.findViewById(R.id.subject_num_et)).getText().toString();
                getZongHeData();
                closeDrewer();
            }
        });
        rl_drawer_layout.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeName = "";
                propName = "";
                subjectName = "";
                teacherName = "";
                supName = "";
                courseNo = "";
                classzId = "";
                ((EditText)rl_drawer_layout.findViewById(R.id.subject_name_et)).setText("");
                ((EditText)rl_drawer_layout.findViewById(R.id.teacher_name_et)).setText("");
                ((EditText)rl_drawer_layout.findViewById(R.id.assistant_name_et)).setText("");
                ((EditText)rl_drawer_layout.findViewById(R.id.subject_num_et)).setText("");
                ((TextView)rl_drawer_layout.findViewById(R.id.grade_et)).setText("");
                typeAdapter.notifyDataSetChanged();
                propAdapter.notifyDataSetChanged();
            }
        });
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 获取综合数据
     */
    private void getZongHeData() {
        Map<String,String> map = new HashMap<>();
        map.put("supName", supName);
        map.put("teacherName", teacherName);
        map.put("subjectName", subjectName);
        map.put("coursePropId", propName);
        map.put("courseTypeId", typeName);
        map.put("courseNo", courseNo);
        map.put("classId", classId);
        NetworkRequest.request(map,CommonUrl.QUERY_COURSE_ADMIN,Config.QUERY_COURSE_ADMIN);
        dialog.show();
    }
    /**
     * 获取课堂记录
     */
    private void getHistoryData() {
        Map<String,String> map = new HashMap<>();
//        if (!SharePreCache.isEmpty(classzId)) {
            map.put("inClassId", classzId);
//        }
        NetworkRequest.request(map,CommonUrl.KTJL,Config.KTJL);
    }
    private void getHistoryData(int index) {
        Map<String,String> map = new HashMap<>();
//        if (!SharePreCache.isEmpty(classzId)) {
            map.put("classId", classzId);
            map.put("weekNum", String.valueOf(index));
//        }
        NetworkRequest.request(map,CommonUrl.KTJL,Config.KTJLWEEK);
        showWaitDialog("加载中...");
    }

    /**
     * 获取赛选条件
     */
    private void getProp(){
        NetworkRequest.request(null,CommonUrl.SELECTINFO,Config.SELECTINFO);
    }


    @OnClick({R.id.zonghe_ll, R.id.banji_ll, R.id.jiaoshi_ll, R.id.title_back_img, R.id.right_icon1,R.id.grade_change})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.zonghe_ll:
                setNormalColor();
                zonghe_tv.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                handleShowContent(zonghe_content);
                break;
            case R.id.banji_ll:
                setNormalColor();
                banji_tv.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                handleShowContent(selection_content);
                break;
            case R.id.jiaoshi_ll:
                setNormalColor();
                jiaoshi_tv.setTextColor(getResources().getColor(R.color.text_color_8CA404));
                handleShowContent(lesson_record_content);
                break;
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_icon1:
                openDrawer();
//                selectGradeCls();
                break;
            case R.id.grade_change:
                selectGradeCls();
                break;
        }
    }

    @Subscribe
    @Override
    public void onEventMainThread(EventCenter event) {
        super.onEventMainThread(event);
        dialog.dismiss();
        switch (event.getEventCode()) {
            /*case Config.ERROR:
                dialog.dismiss();
                break;*/
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.QUERY_COURSE_ADMIN:
                dialog.dismiss();
                JSONObject js = (JSONObject) event.getData();
                List<ZHbean> zHbeens = GsonUtils.toArray(ZHbean.class, js);
                zongheBeans.clear();
                if (!BaseUtil.isEmpty(zHbeens)) {
                    zongheBeans.addAll(zHbeens);
                }
                zongheAdapter.notifyDataSetChanged();
                break;
            case Config.KTJL:
                dialog.dismiss();
                this.history.getHistoryList().clear();
                JSONObject js2 = (JSONObject) event.getData();
                if (js2 != null) {
                    BZRhistory history =GsonUtils.toObject(js2,BZRhistory.class);
                    if(this.history!=null&&history!=null) {
                        if(this.history.getWeekList().size()==0) {
//                            this.history.getWeekList().clear();
                            this.history.getWeekList().addAll(history.getWeekList());
                        }
                        this.history.getHistoryList().addAll(history.getHistoryList());
                    }
                    if (BaseUtil.isEmpty(weekStrs) && history != null) {
                        if (!BaseUtil.isEmpty(history.getWeekList())) {
                            weekStrs.clear();
                            for (BZRhistory.Week bean : history.getWeekList()) {
                                weekStrs.add(bean.getWeek());
                            }
                        }
                        initWeek();
//                        weekAdapter.notifyDataSetChanged();
                    }
                }
                recordAdapter.notifyDataSetChanged();
                break;
            case Config.KTJLWEEK:
                dialog.dismiss();
                JSONObject js3 = (JSONObject) event.getData();
                if (js3 != null) {
                    List<BZRhistory.History> historyList =GsonUtils.toArray(BZRhistory.History.class,js3);
                    if(this.history!=null) {
                        this.history.getHistoryList().clear();
                        this.history.getHistoryList().addAll(historyList);
                    }
                }
                recordAdapter.notifyDataSetChanged();
                break;
            case Config.SELECTINFO:
                SaiXuanbean saiXuanbean = GsonUtils.toObject((JSONObject) event.getData(), SaiXuanbean.class);
                if (saiXuanbean != null) {
                    this.saiXuanbean.getTypeList().clear();
                    this.saiXuanbean.getPropList().clear();
                    this.saiXuanbean.getPropList().addAll(saiXuanbean.getPropList());
                    this.saiXuanbean.getTypeList().addAll(saiXuanbean.getTypeList());
                }
                initDrawer();
                break;
        }
    }

    /**
     * 显示
     *
     * @param view 成绩查看view  班级选课view  综合查看view
     */
    private void handleShowContent(View view) {
        zonghe_content.setVisibility(View.GONE);
        selection_content.setVisibility(View.GONE);
        lesson_record_content.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        if (view == selection_content) {
            gradeChange.setVisibility(View.VISIBLE);
            findViewById(R.id.right_icon1).setVisibility(View.GONE);
            index = 1;
           /* if (selectionTab.getSelectedTabPosition() == 1) {
                search_et.setVisibility(View.VISIBLE);
                title_tv.setVisibility(View.GONE);
            } else {
                search_et.setVisibility(View.GONE);
                title_tv.setVisibility(View.VISIBLE);
            }*/
        } /*else {    //隐藏搜索框
            search_et.setVisibility(View.GONE);
            title_tv.setVisibility(View.VISIBLE);
        }*/
        if(view == lesson_record_content){
            index = 2;
            gradeChange.setVisibility(View.VISIBLE);
            if(isFirst) {
                getHistoryData();
            }
            isFirst = false;
            findViewById(R.id.right_icon1).setVisibility(View.GONE);
        }
        if(view == zonghe_content){
            index = 0;
            findViewById(R.id.right_icon1).setVisibility(View.VISIBLE);
            gradeChange.setVisibility(View.GONE);
        }
    }

    /**
     * tv设置没有选中颜色
     */
    private void setNormalColor() {
        zonghe_tv.setTextColor(getResources().getColor(R.color.gray3));
        banji_tv.setTextColor(getResources().getColor(R.color.gray3));
        jiaoshi_tv.setTextColor(getResources().getColor(R.color.gray3));
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }



    /**
     * 打开抽屉
     */
    public void openDrawer() {
        if (drawerLayout == null)
            return;
        drawerLayout.openDrawer(rl_drawer_layout);
    }

    /**
     * 关闭抽屉
     */
    private void closeDrewer() {
        if (drawerLayout == null)
            return;
        drawerLayout.closeDrawer(rl_drawer_layout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrewer();
            return;
        }
        super.onBackPressed();
    }

    private void selectGradeCls() {
        final OptionsPickerView optionsPickerView = new OptionsPickerView(this);
        optionsPickerView.setPicker(AppContext.getInstance().grades, AppContext.getInstance().clazzs, 0, 0, 0);
        optionsPickerView.setCyclic(false);
        optionsPickerView.setCancelable(true);
        optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, int options4) {
                if (AppContext.getInstance().clazzs.get(options1) != null &&
                        AppContext.getInstance().clazzs.get(options1).size() > 0 &&
                        options2 < AppContext.getInstance().clazzs.get(options1).size()) {
                    classzId = AppContext.getInstance().gradeBeans.get(options1).getDetail().get(options2).getClassId();
                    switch (index){
                        case 0:
                            classId = classzId;
                            ((TextView)rl_drawer_layout.findViewById(R.id.grade_et)).setText(AppContext.getInstance().gradeBeans.get(options1).getDetail().get(options2).getName());
                            break;
                        case 1:
                            BanJiSelectionFramgent.getBjxkList(classzId);
                            StudentSelectionFramgent.getData(classzId);
                            break;
                        case 2:
                            getHistoryData();
                            break;
                    }
                    /*Map<String,String> map = new HashMap<>();
                    map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
                    map.put("classId", classzId);
                    NetworkRequest.request(map, CommonUrl.BJ_COURSE_ADMIN, BANJI);

                    Map<String,String> map2 = new HashMap<>();
                    map.put("teacherId", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));
                    NetworkRequest.request(map, CommonUrl.COURSE_HISTORY_S_BZR, Config.COURSE_HISTORY_S_BZR);*/
                } else {
                    ToastUtils.showShort("班级为空，不能选择");
                }
            }
        });
        optionsPickerView.show();
    }
}
