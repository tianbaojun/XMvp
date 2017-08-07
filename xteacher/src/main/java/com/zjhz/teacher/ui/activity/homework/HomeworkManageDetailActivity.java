package com.zjhz.teacher.ui.activity.homework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.HomeworkListBeanRes;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.fragment.homework.HomeworkDetailFragment;
import com.zjhz.teacher.ui.fragment.homework.HomeworkStatisticsFragment;
import com.zjhz.teacher.ui.fragment.homework.StudentHandInFragment;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.ButterKnife;import butterknife.BindView;
import butterknife.OnClick;
import pro.adapter.SimpleFragmentPagerAdapter;

public class HomeworkManageDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        CompoundButton.OnCheckedChangeListener{

    @BindView(R.id.title_back_img)
    ImageView backImg;
    @BindView(R.id.homework_wj)
    TextView wjTv;
    @BindView(R.id.homework_detail_viewpager)
    ViewPager viewPager;
    @BindView(R.id.homework_detail_title)
    TextView titleTv;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.radio_bznr)
    RadioButton bznrRaido;
    @BindView(R.id.radio_xssj)
    RadioButton xssjRaido;
    @BindView(R.id.radio_zytj)
    RadioButton zytjRaido;

    public static final int DFB = 1;
    public static final int FB = 2;
    public static final int WJ = 3;

    private HomeworkListBeanRes bean;
    private SimpleFragmentPagerAdapter adapter;

    private HomeworkDetailFragment detailFragment;
    private StudentHandInFragment handInFragment;
    private HomeworkStatisticsFragment statisticsFragment;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<ClassesBeans> classList = new ArrayList<>();

    private List<NotifyClassList> notifyClassLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_manage_detail);
        ButterKnife.bind(this);

        bean = (HomeworkListBeanRes) getIntent().getExtras().getSerializable("bean");

        detailFragment = HomeworkDetailFragment.newInstance(bean != null ? bean.getHomeworkId() : null, bean != null ? bean.getTeacherId() : null);
        handInFragment = StudentHandInFragment.newInstance(bean != null ? bean.getHomeworkId() : null, bean != null ? bean.getHomeworkType() : null);
        statisticsFragment = HomeworkStatisticsFragment.newInstance(bean != null ? bean.getHomeworkId() : null);
        fragmentList.add(detailFragment);
        if(!"SYS_HOMEWORK_NATURE_1".equals(bean.getHomeworkNature())){//书面作业SYS_HOMEWORK_NATURE_1
            fragmentList.add(handInFragment);
            fragmentList.add(statisticsFragment);
            viewPager.setOffscreenPageLimit(3);
            radioGroup.setVisibility(View.VISIBLE);
            titleTv.setVisibility(View.GONE);
        }else {
            radioGroup.setVisibility(View.GONE);
            titleTv.setVisibility(View.VISIBLE);
        }

        adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        bznrRaido.setOnCheckedChangeListener(this);
        xssjRaido.setOnCheckedChangeListener(this);
        zytjRaido.setOnCheckedChangeListener(this);

        if(bean.getStatus() == 2){
            wjTv.setText("完结");
        }else if(bean.getStatus() == 3){
            wjTv.setText("反完结");
        }
        //发布作业老师才有完结按钮
        if(bean.getTeacherId() != null && bean.getTeacherId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey))){
            wjTv.setVisibility(View.VISIBLE);
        }else {
            wjTv.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.title_back_img, R.id.homework_wj})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.homework_wj:
                if(bean.getStatus() == 2){
                    modifyHomeworkStatus(3);
                }else if(bean.getStatus() == 3){
                    modifyHomeworkStatus(2);
                }
                break;
        }
    }

    private void modifyHomeworkStatus(int status){
        Map<String, String> map = new HashMap<>();
        map.put("homeworkId", bean.getHomeworkId());
        map.put("status", status+"");
        NetworkRequest.request(map, CommonUrl.HOMEWORK_MODIFY_STATUS, "homework_modify_status");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event)  {
        switch (event.getEventCode()){
            case "homework_modify_status":
                if(bean.getStatus() == 2){
                    bean.setStatus(3);
                    setResult(WJ);
                    ToastUtils.showShort("作业完结成功");
                    wjTv.setText("反完结");
                }else if(bean.getStatus() == 3){
                    bean.setStatus(2);
                    setResult(FB);
                    ToastUtils.showShort("作业反完结成功");
                    wjTv.setText("完结");
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                bznrRaido.setChecked(true);
                BaseUtil.hideSoftKeyBoard(this, viewPager);
                break;
            case 1:
                xssjRaido.setChecked(true);
                break;
            case 2:
                zytjRaido.setChecked(true);
                BaseUtil.hideSoftKeyBoard(this, viewPager);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            bznrRaido.setTextColor(getResources().getColor(R.color.white));
            xssjRaido.setTextColor(getResources().getColor(R.color.white));
            zytjRaido.setTextColor(getResources().getColor(R.color.white));
            switch (buttonView.getId()) {
                case R.id.radio_bznr:
                    viewPager.setCurrentItem(0);
                    bznrRaido.setTextColor(getResources().getColor(R.color.title_background_red));
                    break;
                case R.id.radio_xssj:
                    viewPager.setCurrentItem(1);
                    xssjRaido.setTextColor(getResources().getColor(R.color.title_background_red));
                    break;
                case R.id.radio_zytj:
                    viewPager.setCurrentItem(2);
                    zytjRaido.setTextColor(getResources().getColor(R.color.title_background_red));
                    break;
            }
        }
    }

    public List<ClassesBeans> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassesBeans> classList) {
        this.classList = classList;
        for(NotifyClassList notifyClassList : notifyClassLists){
            notifyClassList.notifyData(classList);
        }
    }

    public void addNotifyClassList(NotifyClassList notifyClassList){
        notifyClassLists.add(notifyClassList);
    }

    public interface NotifyClassList{
        void notifyData(List<ClassesBeans> classList);
    }
}
