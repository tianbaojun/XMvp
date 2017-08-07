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
import com.zjhz.teacher.NetworkRequests.request.SendMessageTeacherParams;
import com.zjhz.teacher.NetworkRequests.response.DeptAndTeacherBean;
import com.zjhz.teacher.NetworkRequests.response.SendDeptBean;
import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.SendMessageTeacherAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rawn_hwang.library.widgit.DefaultLoadingLayout;
import me.rawn_hwang.library.widgit.SmartLoadingLayout;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-16
 * Time: 15:57
 * Description: 群发消息  校内老师  选择发送对象
 */
public class SendMessageTeacherActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_back_img)
    TextView title_back_img;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.hint_text)
    public TextView hint_text;
    @BindView(R.id.all_cb)
    public ImageView all_cb;
    @BindView(R.id.send_message_teacher_expandable_list)
    ListView listView;
    //没有内容时候无数据图片替换的布局
    @BindView(R.id.content_ll)
    LinearLayout content_ll;

    public  boolean isSelectAll = true;
    private SendMessageTeacherAdapter adapter;
    private List<SendDeptBean> groupItems = new ArrayList<>();//一级列表部门名称
    private List<List<TeacherBean>> childs = new ArrayList<>();//二级列表教师名称
    public List<TeacherBean> teacherBean = new ArrayList<>();
    private final static String TAG = SendMessageTeacherActivity.class.getSimpleName();
    private String userid ;

    //请求参数的类型 从intent当中获取  1表示会议通知   2表示群发消息
    private String type;

    DefaultLoadingLayout loadingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_teacher);
        ButterKnife.bind(this);
        loadingLayout = SmartLoadingLayout.createDefaultLayout(this,content_ll);
        Intent intent = getIntent();
        type = intent.getStringExtra("type"); //需要在initTitle方法被调用钱初始化
        initTitle();
        AppContext.getInstance().addActivity(TAG,this);
//        dialog.setMessage("获取部门...");
//        dialog.show();
        loadingLayout.onLoading();
//        userid = getIntent().getStringExtra("userid");
        getTeacherData();
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
    private void getTeacherData() {
        NetworkRequest.request(new SendMessageTeacherParams(type), CommonUrl.GETPUSHDEPTPERSON,Config.ADDMESSAGETEACHERLIST);
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
            case R.id.right_text:
                if (teacherBean.size() > 0){
                    String name = "";
                    String userId = "";
                    String phoneNo ="";
                    for (int i = 0 ; i < teacherBean.size() ; i ++){
                        if (!userId.contains(teacherBean.get(i).getUserId()+ ",")){
                            userId += teacherBean.get(i).getUserId()+ ",";
                            name += teacherBean.get(i).getNickName()+ "、";
                        }
                        if (!phoneNo.contains(teacherBean.get(i).getPhoneNo() + ",")){
                            phoneNo += teacherBean.get(i).getPhoneNo() + ",";
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("names",name);
                    intent.putExtra("userId",userId);
                    intent.putExtra("phoneNo",phoneNo);
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
//                    Intent intent = new Intent();
//                    intent.putExtra("names","");
//                    intent.putExtra("userId","");
//                    intent.putExtra("phoneNo","");
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
                    teacherBean.clear();
                    for (int i = 0 ; i < groupItems.size() ; i ++){
                        groupItems.get(i).setChecked(true);
                    }
                    for (int i = 0 ; i < childs.size() ; i ++){
                        List<TeacherBean> beanList = childs.get(i);
                        for (int j = 0 ; j < beanList.size() ; j ++){
                            teacherBean.add(beanList.get(j));
                        }
                    }
                }else {
                    all_cb.setImageResource(R.mipmap.btn_check_off);
                    isSelectAll = true;
                    hint_text.setText("全选");
                    for (int i = 0 ; i < groupItems.size() ; i ++){
                        groupItems.get(i).setChecked(false);
                    }
                    teacherBean.clear();
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
            case Config.ADDMESSAGETEACHERLIST:
                dialog.dismiss();
                JSONObject j = (JSONObject) eventCenter.getData();
                List<DeptAndTeacherBean> beans = GsonUtils.toArray(DeptAndTeacherBean.class,j);
                if (beans != null && beans.size() > 0){
//                    BaseUtil.saveLocalFile(this,,Config.DEPTNAMTEACHER);
                    loadingLayout.onDone();
                    initList(beans);
                }else{
                    loadingLayout.onEmpty();
                    right_text.setVisibility(View.GONE);
                }
                break;
        }
    }
    private void initList(List<DeptAndTeacherBean> beans) {
        for (int i = 0; i < beans.size(); i++) {
            SendDeptBean bean = new SendDeptBean();
            bean.setName(beans.get(i).getDeptName());
            bean.setChecked(false);
            groupItems.add(bean);
            List<TeacherBean> teachers =  beans.get(i).getUsers();
            childs.add(teachers);
//            int add = 0;
//            List<TeacherBean> teachers =  beans.get(i).getUsers();
//            for (int j = 0 ; j < teachers.size() ; j ++){
//                if (!SharePreCache.isEmpty(userid) && userid.contains(teachers.get(j).getUserId())){
//                    add++;
//                    teachers.get(j).setChecd(true);
//                    teacherBean.add(teachers.get(j));
//                }
//            }
//            childs.add(teachers);
//            SendDeptBean bean = new SendDeptBean();
//            bean.setName(beans.get(i).getDeptName());
//            if (teachers != null && teachers.size() > 0){
//                if (add == teachers.size()){
//                    bean.setChecked(true);
//                }else {
//                    bean.setChecked(false);
//                }
//            }else {
//                bean.setChecked(false);
//            }
//            groupItems.add(bean);
        }
//        int isall = 0 ;
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
        adapter = new SendMessageTeacherAdapter(this,this,groupItems, childs);
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
