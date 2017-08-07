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

import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.ui.dialog.WaitDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;

import butterknife.ButterKnife;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: Fragment基类
 */
public abstract class BaseFragment extends Fragment {

    protected Context context;
    protected View view;
    protected Activity activity;
    public WaitDialog dialog;

    private static final String IS_FIRST = "is_first";
    private boolean isShowDialog = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        activity = this.getActivity();
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
            dialog = new WaitDialog(context);
            if(isShowDialog){
                dialog.show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater);
        //当需要container时  initView(inflater)返回null 重写initView(inflater, container, savedInstanceState)方法
        if(view == null){
            view = initView(inflater, container, savedInstanceState);
        }
       return view;
    }



    @Nullable @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViewsAndEvents();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if(getArguments() == null){
            Bundle bundle = new Bundle();
            setArguments(bundle);
        }
        boolean isFirst = getArguments().getBoolean(IS_FIRST, true);
        if (isFirst && isVisibleToUser) {
            lazyLoad();
            getArguments().putBoolean(IS_FIRST, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * bind layout resource file
     * @return id of layout resource
     */
    protected abstract View initView(LayoutInflater inflater);

    protected View initView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        return null;
    }

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents();

    protected  abstract void initData(Bundle savedInstanceState);

    protected void lazyLoad(){

    }

    /**
     * is bind eventBus
     * @return
     */
    protected abstract boolean isBindEventBusHere();

    /**
     * when event comming
     * @param eventCenter
     */
    @Subscribe
    protected void onEventMainThread(EventCenter eventCenter) throws JSONException{
        switch (eventCenter.getEventCode()) {
            case Config.ERROR_JSON:
                dialog.dismiss();
                break;
            case Config.ERROR:
                dialog.dismiss();
                break;
        }
    }

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
    protected void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void showDialog(){
        if(dialog == null){
            isShowDialog = true;
        }else {
            dialog.show();
        }
    }

//    /**
//     * startActivity
//     *
//     * @param clazz
//     */
//    protected void startActivity(Class<?> clazz,String key,String value) {
//        Intent intent = new Intent(getActivity(), clazz);
//        intent.putExtra(key,value);
//        startActivity(intent);
//    }

}
