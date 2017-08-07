package pro.ui.activity.xkgl.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseFragment;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pro.ui.activity.xkgl.adapter.BJXKadapter;
import pro.ui.activity.xkgl.testbean.BJXKBeanAdmin;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public abstract class BanJiBaseFramgent extends BaseFragment {
    @BindView(R.id.refresh_listview)
    public ListView refresh_listview;

    public final static String BANJI = "xk_bjxk_list";
    protected BJXKadapter adapter;
    protected BJXKBeanAdmin bjxkBean;
    protected List<BJXKBeanAdmin.SubMapBean> selectList = new ArrayList<>();


    @Override
    protected View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_list_view, null);
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getBjxkList();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) throws JSONException {
        super.onEventMainThread(eventCenter);
        switch (eventCenter.getEventCode()) {
            case BANJI:
                dialog.dismiss();
                selectList.clear();
                JSONObject js = (JSONObject) eventCenter.getData();
                bjxkBean = GsonUtils.toObject(js, BJXKBeanAdmin.class);
                if(bjxkBean!=null) {
                    selectList.add(bjxkBean.getSubMap());
                    selectList.add(NoToSub(bjxkBean.getNoSubMap()));
                    if(adapter == null){
                        adapter = new BJXKadapter(getActivity(),R.layout.bj_footer,selectList);
                        refresh_listview.setAdapter(adapter);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    protected abstract void getBjxkList();

    protected BJXKBeanAdmin.SubMapBean NoToSub(BJXKBeanAdmin.NoSubMapBean noBean) {
        BJXKBeanAdmin.SubMapBean bean= new BJXKBeanAdmin.SubMapBean();
        List<BJXKBeanAdmin.SubMapBean.SelectListBean> selList = new ArrayList<>();
        for(BJXKBeanAdmin.NoSubMapBean.NoSelectListBean noSelectListBean:noBean.getNoSelectList()){
            BJXKBeanAdmin.SubMapBean.SelectListBean a = new BJXKBeanAdmin.SubMapBean.SelectListBean();
            a.setCount(noSelectListBean.getCount());
            a.setStuList(noSelectListBean.getStuList());
            a.setSubjectName(noSelectListBean.getSubjectName());
            selList.add(a);
        }
        bean.setSelectList(selList);
        bean.setSelectNum(noBean.getNoSelectNum());
        return bean;
    }
}
