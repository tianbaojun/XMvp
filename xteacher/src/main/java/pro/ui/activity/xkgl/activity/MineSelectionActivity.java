package pro.ui.activity.xkgl.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
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
import pro.ui.activity.xkgl.fragment.rk.MineCallFragment;
import pro.ui.activity.xkgl.fragment.rk.RecordFragment;
import pro.ui.activity.xkgl.testbean.CourseRk;
import pro.widget.BubblePopupWindow.ListViewAdaptWidth;

public class MineSelectionActivity extends BaseActivity {

    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.selection_mine_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.selection_mine_viewPager)
    ViewPager viewPager;
    //科目图片
    @BindView(R.id.subject_img)
    ImageView subject_img;
    //科目名
    @BindView(R.id.subject_name_tv)
    TextView subject_name_tv;
    //科目编号
    @BindView(R.id.subject_no_tv)
    TextView subject_no_tv;
    //科目上课地址
    @BindView(R.id.subject_place_tv)
    TextView subject_place_tv;
    //展示类型
    @BindView(R.id.type_tv)
    TextView type_tv;
    @BindView(R.id.week_num)
    TextView weekNumTv;

    //tab标题
    private List<String> tabTitles;
    //fragments
    private List<Fragment> fragments;

    private SimpleFragmentPagerAdapter fragmentPagerAdapter;
    private PopupWindow popupWindow;
    public static final String COURSECHANGE = "course_CHANGE";

    //课程
    private List<CourseRk> courseRks = new ArrayList<>();
    private int courseIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_selection);
        ButterKnife.bind(this);
//        initView();
        getCourse();
        setMyTitle("选课管理");
    }

    private void getCourse(){
        Map<String, String> map = new HashMap<>();
        map.put("subjectNature", "1");//0必修  1选修
        NetworkRequest.request(map, CommonUrl.COURSE_RK, Config.COURSE_RK);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private void initView() {
        if(courseRks.size() > 1) {
            right_text.setVisibility(View.VISIBLE);
            right_text.setText("筛选");
        }
        tabTitles = new ArrayList<>();
        fragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        tabTitles.add("课堂点名");
        tabTitles.add("成绩记录");
        fragments = new ArrayList<>();
        MineCallFragment rollcall = new MineCallFragment();
        RecordFragment recordFrag = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("optId", courseRks.get(0).getOptId());
        rollcall.setArguments(bundle);
        recordFrag.setArguments(bundle);
        fragments.add(rollcall);
        fragments.add(recordFrag);
        fragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),tabTitles, this,fragments);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @OnClick({R.id.type_tv,R.id.right_text})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.type_tv:
                if(courseRks == null || courseRks.size() == 0)
                    return;
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", courseRks.get(courseIndex));
                startActivity(MineSelectionRecordActivity.class, bundle);
                break;
            case R.id.right_text:
                initPopupWindow();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter){
        super.onEventMainThread(eventCenter);
        switch (eventCenter.getEventCode()){
            case Config.COURSE_RK:
                List<CourseRk> list = GsonUtils.toArray(CourseRk.class, (JSONObject) eventCenter.getData());
                if(!BaseUtil.isEmpty(list)){
                    courseRks.clear();
                    for (CourseRk rk : list) {
                        if(rk.getTeacherId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey))){
                            courseRks.add(rk);
                        }
                    }
                    if(courseRks.size() > 0) {
                        subject_name_tv.setText(courseRks.get(0).getSubjectName());
                        subject_no_tv.setText(courseRks.get(0).getCourseNo());
                        subject_place_tv.setText(courseRks.get(0).getOptAddr());
                        weekNumTv.setText("第"+courseRks.get(0).getWeekNum()+"周");
                        courseIndex = 0;
                        initView();
                    }
                }
                break;
        }
    }

    private void initPopupWindow() {
        popupWindow = new PopupWindow(this);
//        View bubbleView = inflater.inflate(R.layout.listview,null);
//        ListView listView = (ListView) bubbleView.findViewById(R.id.listview);
        ListViewAdaptWidth popListView = new ListViewAdaptWidth(this);
        popListView.setCacheColorHint(Color.WHITE);
        popListView.setDivider(new ColorDrawable(Color.WHITE));
        popListView.setDividerHeight(1);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                courseIndex = position;
                subject_name_tv.setText(courseRks.get(position).getSubjectName());
                subject_no_tv.setText(courseRks.get(position).getCourseNo());
                subject_place_tv.setText(courseRks.get(position).getOptAddr());
                weekNumTv.setText(courseRks.get(position).getWeekNum());
                EventBus.getDefault().post(new EventCenter<>(COURSECHANGE,courseRks.get(position).getOptId()));
                courseIndex = position;
            }
        });
        ArrayAdapter<String> popAdapter = new ArrayAdapter<>(this, R.layout.textview);
        List<String> list = new ArrayList<>();
        for (CourseRk courseRk : courseRks) {
            if(courseRk!=null) {
                list.add(courseRk.getSubjectName());
            }
        }
        popAdapter.addAll(list);
        popListView.setAdapter(popAdapter);
        popAdapter.notifyDataSetChanged();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popListView);
        popupWindow.showAsDropDown(right_text, 0, 0);
    }
}
