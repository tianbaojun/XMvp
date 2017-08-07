package com.zjhz.teacher.ui.fragment.homework;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.activity.homework.HomeworkManageDetailActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pro.adapter.SimpleFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeworkStatisticsFragment extends BaseFragment implements HomeworkManageDetailActivity.NotifyClassList{

    @BindView(R.id.class_tab)
    TabLayout tabLayout;
    @BindView(R.id.homework_statistics_vp)
    ViewPager viewPager;

    private static final String HOMEWORK_ID = "homework_id";
    private String homeworkId;

    private List<ClassesBeans> classList = new ArrayList<>();
    private List<String> classNameList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private SimpleFragmentPagerAdapter pagerAdapter;

    public HomeworkStatisticsFragment() {
        // Required empty public constructor
    }

    public static HomeworkStatisticsFragment newInstance(String homeworkId) {
        HomeworkStatisticsFragment fragment = new HomeworkStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(HOMEWORK_ID, homeworkId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_homework_statistics, null, false);
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        homeworkId = getArguments().getString(HOMEWORK_ID);
    }

    @Override
    protected void lazyLoad(){
        notifyData(((HomeworkManageDetailActivity) getActivity()).getClassList());
        pagerAdapter = new SimpleFragmentPagerAdapter(getActivity().getSupportFragmentManager(), classNameList, getActivity(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if(getActivity() instanceof HomeworkManageDetailActivity) {
            ((HomeworkManageDetailActivity) getActivity()).addNotifyClassList(this);
        }
    }

    private void addFragment(String classId){
        HwsWebViewFragment fragment = HwsWebViewFragment.newInstance(homeworkId, classId);
        fragmentList.add(fragment);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {

    }

    @Override
    public void notifyData(List<ClassesBeans> list) {
        if(classList.size() > 0)
            return;
        classList.clear();
        classList.addAll(list);
        classNameList.add("全部");
        addFragment("");
        for(ClassesBeans beans : classList){
            classNameList.add(beans.getClassName());
            addFragment(beans.getClassId());
        }
        if(pagerAdapter != null)
            pagerAdapter.changed();
    }
}
