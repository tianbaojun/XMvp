package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.GetParentInfoParams;
import com.zjhz.teacher.NetworkRequests.request.SendMessageParentParams;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.GradeClsBean;
import com.zjhz.teacher.NetworkRequests.response.ParentBean;
import com.zjhz.teacher.NetworkRequests.response.SendDeptBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.SendMessageParentAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.BindView;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 群发消息  学生家长
 */
public class SendMessageParentActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.send_message_teacher_expandable_list)
    ListView listView;
    @BindView(R.id.hint_text)
    public TextView hint_text;
    @BindView(R.id.all_cb)
    public ImageView all_cb;
    //没有内容时候无数据图片替换的布局
    @BindView(R.id.content_ll)
    LinearLayout content_ll;
    public boolean isSelectAll = true;
    private SendMessageParentAdapter adapter;
    private List<SendDeptBean> groupItems = new ArrayList<>(); //一级年级列表
    private List<List<ClassesBeans>> childs = new ArrayList<>();//二级年级列表
    public List<ClassesBeans> classesId = new ArrayList<>();//选择班级对象
    private String name = "";
    private final static String TAG = SendMessageParentActivity.class.getSimpleName();
    private String classesIds = "";//选择班级id
    private String selectTrueClassesIds = "";//选择班级id
    //请求参数的类型 从intent当中获取  1表示会议通知   2表示群发消息
    private String type;

    private DefaultLoadingLayout loadingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_teacher);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this,content_ll);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        initTitle();
        loadingLayout.onLoading();
//        selectTrueClassesIds = getIntent().getStringExtra("classesIds");
        getParentData();
    }
    private void initTitle() {
        if(type.equals("1")){
            titleTv.setText(R.string.send_message_parent_activity_title_mass);
        }else if(type.equals("2")){
            titleTv.setText(R.string.send_message_parent_activity_title_meeting);
        }
        right_text.setVisibility(View.VISIBLE);
        right_text.setText("完成");
    }
    private void getParentData() {
        NetworkRequest.request(new SendMessageParentParams(type), CommonUrl.LISTCLASS,Config.ADDMESSAGEPARENTCLASSLIST);
    }
    @OnClick({R.id.title_back_img,R.id.right_text,R.id.all_cb})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:  // 全选
                if (classesId.size() > 0 ){
                    for (int i = 0 ; i < classesId.size() ; i++){
                        classesIds += classesId.get(i).getClassId()+",";
                        name += classesId.get(i).getName()+"、";
                    }
                    NetworkRequest.request(new GetParentInfoParams(classesIds),CommonUrl.listByClassId,Config.ADDMESSAGEPARENTLIST);
                }else {
//                    Intent intent = new Intent();
//                    intent.putExtra("names","");
//                    intent.putExtra("userId","");
//                    intent.putExtra("phoneNo","");
//                    intent.putExtra("classesIds","");
//                    setResult(RESULT_OK,intent);
//                    finish();
                    ToastUtils.showShort("请选择发送对象");
                }
                break;
            case R.id.all_cb:
                // 全选
                if (isSelectAll){
                    all_cb.setImageResource(R.mipmap.btn_check_on);
                    isSelectAll = false;
                    hint_text.setText("取消");
                    classesId.clear();
                    for (int i = 0 ; i < groupItems.size() ; i ++){
                        groupItems.get(i).setChecked(true);
                    }
                    for (int i = 0 ; i < childs.size() ; i ++){
                        List<ClassesBeans> beanList = childs.get(i);
                        for (int j = 0 ; j < beanList.size() ; j ++){
                            classesId.add(beanList.get(j));
                        }
                    }
                }else {
                    all_cb.setImageResource(R.mipmap.btn_check_off);
                    isSelectAll = true;
                    hint_text.setText("全选");
                    for (int i = 0 ; i < groupItems.size() ; i ++){
                        groupItems.get(i).setChecked(false);
                    }
                    classesId.clear();
                    adapter.setAll(true);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()){
            case Config.ERROR:
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                break;
            case Config.ADDMESSAGEPARENTCLASSLIST:
                dialog.dismiss();
                JSONObject json = (JSONObject) eventCenter.getData();
                List<GradeClsBean> beans = GsonUtils.toArray(GradeClsBean.class,json);
                if (beans != null && beans.size() > 0){
                    loadingLayout.onDone();
                    initList(beans);
                }else{ //无数据，不显示完成按钮
                    loadingLayout.onEmpty();
                    right_text.setVisibility(View.GONE);
                }
                break;
            case Config.ADDMESSAGEPARENTLIST://通过班级获取家长
                JSONObject jsons = (JSONObject) eventCenter.getData();
                List<ParentBean> parents = GsonUtils.toArray(ParentBean.class,jsons);
                if (parents != null){
                    String userId = "";
                    String phoneNo ="";
                    for (int i = 0 ; i < parents.size();i++){
                        if (!userId.contains(parents.get(i).getUserId()+",")){
                            userId += parents.get(i).getUserId()+",";
                        }
                        if (!phoneNo.contains( parents.get(i).getPhone()+",")){
                            phoneNo += parents.get(i).getPhone()+",";
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("names",name);
                    intent.putExtra("userId",userId);
                    intent.putExtra("phoneNo",phoneNo);
                    intent.putExtra("classesIds",classesIds);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                break;
        }
    }
    private void initList(List<GradeClsBean> beans) {
        for (int i = 0 ; i < beans.size() ; i ++){
            SendDeptBean bean = new SendDeptBean();
            bean.setName(beans.get(i).getName());
            bean.setChecked(false);
            groupItems.add(bean);
            List<ClassesBeans> classesList = beans.get(i).getDetail();//班级
            childs.add(classesList);
//            int add = 0 ;
//            List<ClassesBeans> classesList = beans.get(i).getDetail();//班级
//            for (int j = 0 ; j < classesList.size() ; j ++){
//                if (!SharePreCache.isEmpty(selectTrueClassesIds) && selectTrueClassesIds.contains(classesList.get(j).getClassId())){
//                    add++;
//                    classesId.add(classesList.get(j));
//                    classesList.get(j).setChecd(true);
//                }
//            }
//            childs.add(classesList);
//            SendDeptBean bean = new SendDeptBean();
//            bean.setName(beans.get(i).getName());
//            if (classesList != null && classesList.size() > 0){
//                if (add == classesList.size()){
//                    bean.setChecked(true);
//                }else {
//                    bean.setChecked(false);
//                }
//            }else {
//                bean.setChecked(false);
//            }
//            groupItems.add(bean);
        }
//        int isall = 0;
//        for (int i = 0 ; i < groupItems.size() ; i++){
//            if (groupItems.get(i).isChecked()){
//                isall ++;
//            }
//        }
//        if (isall == groupItems.size()){
//            all_cb.setImageResource(R.mipmap.btn_check_on);
//            isSelectAll = false;
//            hint_text.setText("取消");
//        }
        initListView();
    }
    private void initListView() {
        adapter = new SendMessageParentAdapter(this,this,groupItems, childs);
        listView.setAdapter(adapter);
    }
    public void update(int index,boolean isTrue){
        if (isTrue){
            groupItems.get(index).setChecked(true);
            int isIndex = 0;
            for (int i = 0 ; i < groupItems.size() ; i++){
                if (groupItems.get(i).isChecked()){
                    isIndex ++;
                }
            }
            if (isIndex == groupItems.size()){
                all_cb.setImageResource(R.mipmap.btn_check_on);
                isSelectAll = false;
                hint_text.setText("取消");
            }
        }else {
            all_cb.setImageResource(R.mipmap.btn_check_off);
            isSelectAll = true;
            hint_text.setText("全选");
            groupItems.get(index).setChecked(false);
            adapter.setAll(false);
        }
        adapter.notifyDataSetChanged();
    }
}
