package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.GrowthAppraiseBean;
import com.zjhz.teacher.bean.GrowthAppraiseTypeBean;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

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
import pro.kit.ViewTools;

public class GrowthSummaryAddActivity extends BaseActivity {
    @BindView(R.id.right_text)
    TextView submit;
    @BindView(R.id.title_tv)
    TextView title_tv;
    //寄语编辑框
    @BindView(R.id.appraise_et)
    EditText appraise_et;
    //寄语类型选择
    @BindView(R.id.listview)
    ScrollViewWithGridView listview;
    //默认寄语列表
    private List<GrowthAppraiseBean> appraiseBeens = new ArrayList<>();
    //教师寄语类型列表
    private List<GrowthAppraiseTypeBean> typeBeens = new ArrayList<>();
    //寄语类型id
    private String appraiseId = "";
    //学生id
    private String studentId = "";
    //学期id
    private String calendarId = "";
    //默认寄语类型列表adapter
    private CommonAdapter typeAdapter ;
    //被选中的寄语类型的index
    private int typeIndex = 0;

    //appraiseId  在intent中的key
    public static final String APPRAISEID = "APPRAISEID";
    //学生  在intent中的key
    public static final String STUDENTID = "STUDENTID";
    //学期  在intent中的key
    public static final String CALENDARID = "CALENDARID";
    //学期汇总刷新
    public static final String TERMMARYSUMMARYREFRESH = "TERMMARYSUMMARYREFRESH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_summart_add);
        ButterKnife.bind(this);
        initViewAndData();
    }

    /**
     * 初始化添加请求参数
     * 初始化标题栏
     * 请求默认寄语数据
     */
    private void initViewAndData() {
        Intent intent = getIntent();
        if (intent != null) {
//            appraiseId = intent.getStringExtra(APPRAISEID);
            studentId = intent.getStringExtra(STUDENTID);
            calendarId = intent.getStringExtra(CALENDARID);
        }
        submit.setText("提交");
        submit.setVisibility(View.VISIBLE);
        title_tv.setText("学期汇总");
        typeAdapter = new CommonAdapter<GrowthAppraiseTypeBean>(this,R.layout.mmc_textview,typeBeens) {
            @Override
            protected void convert(ViewHolder viewHolder, final GrowthAppraiseTypeBean item, final int position) {
                viewHolder.setText(R.id.text_tv,item.getAppraiseName());
                if(appraiseId.equals(item.getAppraiseId())){
                    ViewTools.tvShowCheck((TextView) viewHolder.getView(R.id.text_tv),item.getAppraiseName());
                }
                viewHolder.setOnClickListener(R.id.text_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i = 0;i<listview.getCount();i++){
                            TextView tv = (TextView)listview.getChildAt(i);
                            if(tv!=v) {
                                tv.setTextColor(getResources().getColor(R.color.gray9));
                                tv.setText(typeBeens.get(i).getAppraiseName());
                            }else{
                                appraiseId = item.getAppraiseId();
                                ViewTools.tvShowCheck((TextView) v,item.getAppraiseName());
                            }
                        }
                    }
                });
            }
        };
        listview.setAdapter(typeAdapter);
        getAppraiseType();
    }

    private void getAppraiseType(){
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", String.valueOf(10));
        map.put("page", String.valueOf(1));
        map.put("appType", String.valueOf(0));
        map.put("startStatus", String.valueOf(1));
        NetworkRequest.request(map, CommonUrl.TERMAPPRAISETYPELIST, Config.TERMAPPRAISETYPELIST);
        dialog.show();
    }

    /**
     * 获取默认寄语列表
     */
    private void getDefault() {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", String.valueOf(10));
        map.put("page", String.valueOf(1));
        NetworkRequest.request(map, CommonUrl.APPRAISEDEFAULTLIST, Config.APPRAISEDEFAULTLIST);
        dialog.show();
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        if (event.getEventCode() == null)
            return;
        switch (event.getEventCode()) {
            case Config.ERROR_JSON:
                dialog.dismiss();
                ToastUtils.showShort("服务器数据异常...");
                break;
            case Config.ERROR:   //无网络访问
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:    //有网络,但是操作失败
                dialog.dismiss();
                JSONObject jsonObject = (JSONObject) event.getData();
                String errorStr = "";
                //判断失败的动作
                switch (jsonObject.optString(NetworkRequest.FROMCODE)) {
                    case Config.TERMAPPRAISEADD:
                        errorStr = "新增失败";
                        break;
                    case Config.TERMAPPRAISETYPELIST:
                        errorStr = "查询类型失败";
                }
                ToastUtils.showShort(errorStr);
                break;
            case Config.APPRAISEDEFAULTLIST:   //获取默认寄语列表
                dialog.dismiss();
                JSONObject js = (JSONObject) event.getData();
                List<GrowthAppraiseBean> list = GsonUtils.toArray(GrowthAppraiseBean.class, js);
                if (!BaseUtil.isEmpty(list)) {
                    appraiseBeens.clear();
                    appraiseBeens.addAll(list);
                    appraise_et.setText(appraiseBeens.get(0).getContent());
                    appraiseId = appraiseBeens.get(0).getAppraiseId();
                }
                break;
            case Config.TERMAPPRAISEADD:   //添加
                dialog.dismiss();
                EventBus.getDefault().post(new EventCenter<>(TERMMARYSUMMARYREFRESH));
                finish();
                break;
            case Config.TERMAPPRAISETYPELIST:   //寄语类型列表
                dialog.dismiss();
                JSONObject jsonObject1 = (JSONObject) event.getData();
                List<GrowthAppraiseTypeBean> list1 =GsonUtils.toArray(GrowthAppraiseTypeBean.class,jsonObject1);
                if (!BaseUtil.isEmpty(list1)) {
                    typeBeens.clear();
                    typeBeens.addAll(list1);
                    appraiseId = typeBeens.get(0).getAppraiseId();
                    typeAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @OnClick({R.id.right_text,R.id.title_back_img})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.right_text:   //提交
                Map<String, String> map = new HashMap<>();
                if (!SharePreCache.isEmpty(appraise_et.getText().toString().trim())) {
                    map.put("content", appraise_et.getText().toString().trim());
                }else{
                    ToastUtils.showShort("请输入内容...");
                    break;
                }
                if (!SharePreCache.isEmpty(appraiseId)) {
                    map.put("appraiseId", appraiseId);
                } else {
                    ToastUtils.showShort("请选择类型...");
                    break;
                }
                map.put("studentId", studentId);
                map.put("calendarId", calendarId);
                NetworkRequest.request(map, CommonUrl.TERMAPPRAISEADD, Config.TERMAPPRAISEADD);
                dialog.show();
                break;
            case R.id.title_back_img:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
