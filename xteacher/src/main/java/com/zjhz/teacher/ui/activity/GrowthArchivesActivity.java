package com.zjhz.teacher.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.GrowthTermBean;
import com.zjhz.teacher.bean.StudentBean;
import com.zjhz.teacher.ui.fragment.GrowthDailyFragment;
import com.zjhz.teacher.ui.fragment.GrowthMoralFragment;
import com.zjhz.teacher.ui.fragment.StudyRecordFragment;
import com.zjhz.teacher.ui.fragment.TermSummaryFragment;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;

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
import pro.kit.ViewTools;
import pro.widget.BubblePopupWindow.ListViewAdaptWidth;

public class GrowthArchivesActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView backTv;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.right_icon0)
    ImageView add;
    @BindView(R.id.right_icon)
    ImageView termChoose;


    private SimpleFragmentPagerAdapter pagerAdapter;
    private List<String> tabTitles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    private StudentBean studentBean;

    private Fragment dailyFragment, moralFragment, recordFragment, summaryFragment;
    //当前显示的fragment 的index
    private int index = 0;
    //学期id
    private String calendarId = "";
    //学期list
    private List<GrowthTermBean> terms = new ArrayList<>();
    //更换学期通知的code
    public static final String TERMPOST = "TERMPOST";
    //选择学期的popupwindow
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_archies);
        ButterKnife.bind(this);

        studentBean = (StudentBean) getIntent().getExtras().getSerializable("bean");


        initData();
        initView();
    }

    private void getTerm() {
        Map<String,String> map = new HashMap<>();
        map.put("pullDownFlag", String.valueOf(1));
        NetworkRequest.request(map, CommonUrl.TERMLIST, Config.TERMLIST);
    }

    private void initData() {
        getTerm();
        tabTitles.add("成长记事");
        tabTitles.add("德育表现");
        tabTitles.add("学习成绩");
        tabTitles.add("学期汇总");

        dailyFragment = GrowthDailyFragment.newInstance(studentBean.getStudentId(), studentBean.getClassId());
        moralFragment = GrowthMoralFragment.newInstance(studentBean.getStudentId());
        recordFragment = StudyRecordFragment.newInstance(studentBean.getStudentId());
        summaryFragment = TermSummaryFragment.newInstance(studentBean.getStudentId());
        fragments.add(dailyFragment);
        fragments.add(moralFragment);
        fragments.add(recordFragment);
        fragments.add(summaryFragment);
    }

    private void initView(){
        titleTv.setText(studentBean.getName()+"的成长档案");
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), tabTitles, this, fragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                switch (index) {
                    case 3:
                        add.setVisibility(View.VISIBLE);
                        break;
                    default:
                        add.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        termChoose.setImageResource(R.mipmap.term_choose);
        termChoose.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.title_back_img, R.id.right_icon, R.id.right_icon0})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_icon0:   //新增
                Bundle bundle = new Bundle();
                bundle.putString(GrowthSummaryAddActivity.STUDENTID, studentBean.getStudentId());
                bundle.putString(GrowthSummaryAddActivity.CALENDARID, calendarId);
                startActivity(GrowthSummaryAddActivity.class, bundle);
                break;
            case R.id.right_icon:    //选择学期
                if (!BaseUtil.isEmpty(terms)) {
                    String[] strs = new String[terms.size()];
                    for (int i = 0; i < terms.size(); i++) {
                        strs[i] = terms.get(i).getTitle();
                    }
                    initPopList(view, strs);
                }
                break;
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter e) {
        switch (e.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.TERMLIST:     //学期
                JSONObject js = (JSONObject) e.getData();
                List<GrowthTermBean> list = GsonUtils.toArray(GrowthTermBean.class, js);
                if (!BaseUtil.isEmpty(list)) {
                    terms.clear();
                    terms.addAll(list);
                    calendarId = terms.get(0).getId();
                }
                break;
        }
    }


    /**
     * 显示pop选择框
     *
     * @param strs 选择数组
     */
    public void initPopList(View view, final Object[] strs) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(view.getContext());
        }
        List<Spannable> spans = new ArrayList<>();
        for (int i = 0; i < terms.size(); i++) {
            if (calendarId.equals(terms.get(i).getId())) {
                SpannableString sp = new SpannableString(strs[i].toString()+" ");
                Drawable d = getResources().getDrawable(R.mipmap.term_check);
                d.setBounds(0, 0, (int)getResources().getDimension(R.dimen.dp_15), (int)getResources().getDimension(R.dimen.dp_15));
                sp.setSpan(new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE), sp.length()-1, sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spans.add(sp);
            }else{
                spans.add(new SpannableString(strs[i].toString()));
            }
        }
        ListViewAdaptWidth popListView = new ListViewAdaptWidth(view.getContext());
        popListView.setCacheColorHint(Color.WHITE);
        popListView.setDivider(new ColorDrawable(Color.WHITE));
        popListView.setDividerHeight(1);
        ArrayAdapter popAdapter = new ArrayAdapter<>(view.getContext(), R.layout.textview);
        popListView.setAdapter(popAdapter);
        popupWindow.setContentView(popListView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c4d600")));
        popupWindow.setOutsideTouchable(true);
        popAdapter.clear();
        popAdapter.addAll(spans);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calendarId = terms.get(position).getId();
                ViewTools.tvAppendImage((TextView)view,strs[position].toString(),R.mipmap.term_check);
                popupWindow.dismiss();
                //发出更换学期的通知，fragment都需要接收这个
                EventBus.getDefault().post(new EventCenter<>(TERMPOST, calendarId));
            }
        });
        popAdapter.notifyDataSetChanged();
        popupWindow.showAsDropDown(view, 0, 0);
//        return popupWindow;
    }
}
