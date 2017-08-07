package com.zjhz.teacher.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: Fragment基类
 */
public abstract class Common extends Fragment {

    protected Context context;
    protected View view;
    protected Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        activity = this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
       return view;
    }

    @Nullable @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndEvents();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    /**
     * bind layout resource file
     * @return id of layout resource
     */
    protected abstract View initView(LayoutInflater inflater);

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();

    protected  abstract void initData(Bundle savedInstanceState);

    /**
     * when event comming
     * @param eventCenter
     */
    @Subscribe
    public abstract void onEventMainThread(EventCenter eventCenter) throws JSONException;

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz,String key,int value) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key,value);
        startActivity(intent);
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void startActivity(Class<?> clazz,String key,String value) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key,value);
        startActivity(intent);
    }

}
