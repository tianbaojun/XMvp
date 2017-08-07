/*
 * 源文件名：HomeWorkEditFisrtActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述： 添加作业布置  第一步
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CenterParams;
import com.zjhz.teacher.NetworkRequests.request.EditWorkParams;
import com.zjhz.teacher.NetworkRequests.request.WorkDetailParams;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.NetworkRequests.response.SubjectBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.EditImageAdapter;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.adapter.EditClassAdapter;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

public class HomeWorkEditFisrtActivity extends BaseActivity implements LoadFile.FileCallBack {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
//    @BindView(R.id.right_icon)
//    ImageView right_icon;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.select_class_gridview)
    ScrollViewWithGridView selectClassGridview;
//    @BindView(R.id.homework_pic_gv)
//    ScrollViewWithGridView homeworkPicGv;
    @BindView(R.id.rl)
    LinearLayout rl;
    @BindView(R.id.deliver_time_et)
    TextView deliver_time_et;
    @BindView(R.id.subject_name_tv)
    TextView subject_name_tv;
    @BindView(R.id.costtime_et)
    EditText costtime_et;
    private EditClassAdapter adapterclasses;      //班级选择adapter
    private EditImageAdapter adapterimg;   //图片选择adapter
    private PhotoListBean bean ;           //选中的图片集合
    private LoadFile loadFile ;             //图片处理类

    private List<SubjectBeans> subObj = new ArrayList<>();//科目集合
    private List<String> subStr = new ArrayList<>();
    private List<ClassesBeans> classnames = new ArrayList<>(); // 班级集合
    private List<LocalImageHelper.LocalFile> imgUrls = new ArrayList<>();//保存的图片集合
    private final static String TAG = HomeWorkEditFisrtActivity.class.getSimpleName();
    private int index_subject;
    private String costTime;
    private String deliver;
    private String cotent;
    private String subjectId;
    private int type = 0;
    private String oldUrl = "";
    private String allImgUrls = "";
    private String homeWorkId = "";
    private  HomeWorkBean homeWorkBean;
//    private SelectPicPopwindow selectPicPopwindow ;
    private PopupWindow makePopwindow;
    String fly;
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppContext.getInstance().addActivity(TAG,this);
        setContentView(R.layout.activity_edit_homework_first);
        ButterKnife.bind(this);
        initView();
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("下一步");
        dialog.show();
        homeWorkId = getIntent().getStringExtra("homeworId");
        fly = getIntent().getStringExtra("draft");
        if (!SharePreCache.isEmpty(homeWorkId)){
            NetworkRequest.request(new WorkDetailParams(homeWorkId),"TeachHomeworkService.dtl","homeWorkdtl");
        }else {
            getSubjectData();
            getClassData();
        }
    }
    private void initView() {
        titleTv.setText("作业编辑");
        rightText.setVisibility(View.VISIBLE);
    }
    @OnClick({R.id.title_back_img,R.id.deliver_time_et,R.id.subject_name_tv,R.id.right_text})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                ViewTools.hideSoftInput(this);
                finish();
                break;
            //选择时间
            case R.id.deliver_time_et:
                ViewTools.hideSoftInput(this);
                BaseUtil.selectDate(this,deliver_time_et);
                break;
            //选择科目
            case R.id.subject_name_tv:
                selectSubject();
                break;
            //下一步
            case R.id.right_text:
                ViewTools.hideSoftInput(this);
                if(isRequest()) {
                    String classIds = getClassIds();
                    Intent intent = new Intent(HomeWorkEditFisrtActivity.this, HomeWorkEditSecondActivity.class);
                    Bundle bundle = new Bundle();//　　Bundle的底层是一个HashMap<String, Object
                    bundle.putString("classIds", classIds);
                    bundle.putString("subjectId", subjectId);
                    bundle.putString("costTime", costTime);
                    bundle.putString("deliverTime", deliver);
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, 1);
                }
                break;
        }
    }
    /**
     * 选择科目
     */
    private void selectSubject() {
        if (subStr.size() > 0){
            OptionsPickerView optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker((ArrayList) subStr, index_subject, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    subject_name_tv.setText(subObj.get(options1).getName());
                    subjectId = subObj.get(options1).getSubjectId();
                    index_subject = options1;
                }
            });
            optionsPickerView.show();
        }else {
             ToastUtils.showShort("没有数据");
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            if (requestCode == SelectPicPopwindow.REQUEST_CODE_GETIMAGE_BYCROP){
//                //获取选中的图片
//                List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
//                bean.setDatas(files);
//                adapterimg.setPicLists(files);
//                adapterimg.notifyDataSetChanged();
//            }else if (requestCode == SelectPicPopwindow.SELECT_PIC_BY_TACK_PHOTO){
//                LocalImageHelper.LocalFile files = new LocalImageHelper.LocalFile();
//                files.setThumbnailUri(SelectPicPopwindow.photoUri +"");
//                List<LocalImageHelper.LocalFile> list = adapterimg.getPicLists();
//                if (list == null){
//                    list = new ArrayList<>();
//                }
//                list.add(files);
//                bean.setDatas(list);
//                adapterimg.setPicLists(list);
//                adapterimg.notifyDataSetChanged();
//            }
//
//        }
//    }
//    //获取网络的图片路径
//    private void releaseInfo() {
//        if (isRequest()){
////            if (SharePreCache.isEmpty(oldUrl) && adapterimg.needUpDatas().size() == 0){
////                 ToastUtils.showShort("请选择图片");
////                return;
////            }
//            if (type == 1){
//                dialog.setMessage("正在发布...");
//            }else if (type == 2){
//                dialog.setMessage("正在保存...");
//            }
//            dialog.show();
//            if (adapterimg.needUpDatas().size() == 0 ){
//                if (!TextUtils.isEmpty(oldUrl)){
//                    allImgUrls = oldUrl.substring(0,oldUrl.length() - 1);
//                }
//                if (type == 1){
//                    addWorkInfo();
//                }else if (type == 2){
//                    saveWorkInfo();
//                }
//            }else {
//                final List<File> imgUrlsFile = loadFile.uriToFile(this,adapterimg.needUpDatas());//将图片URI转换成文件
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadFile.uploadFile(imgUrlsFile);
//                    }
//                }).start();
//            }
//        }
//    }
    private boolean isRequest() {
        if (SharePreCache.isEmpty(subject_name_tv.getText().toString().trim())){
             ToastUtils.showShort("请选择科目");
            return false;
        }
        if (adapterclasses == null ){
             ToastUtils.showShort("班级为空，不能发布或保存");
            return false;
        }
        if(adapterclasses.getChecked().size() == 0){
             ToastUtils.showShort("请选择班级");
            return false;
        }
        costTime = costtime_et.getText().toString().trim();
        if (SharePreCache.isEmpty(costTime)){
             ToastUtils.showShort("请输入时长");
            return false;
        }
        deliver = deliver_time_et.getText().toString().trim();
        if (SharePreCache.isEmpty(deliver)){
             ToastUtils.showShort("请选择上交时间");
            return false;
        }

        return true;
    }
    /**
     * 获取成功
     * @param response
     */
    @Override
    public void onfileComplete(final Response response) {
        if (response.code() == 200){
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
                String newUrl = "";
                if (imageUrls != null){
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)){newUrl += f0+",";}
                    String f1 = imageUrls.getFile1();
                    if (!SharePreCache.isEmpty(f1)){newUrl += f1+",";}
                    String f2 = imageUrls.getFile2();
                    if (!SharePreCache.isEmpty(f2)){newUrl += f2+",";}
                    String f3 = imageUrls.getFile3();
                    if (!SharePreCache.isEmpty(f3)){newUrl += f3+",";}
                    String f4 = imageUrls.getFile4();
                    if (!SharePreCache.isEmpty(f4)){newUrl += f4+",";}
                }
                if (!SharePreCache.isEmpty(newUrl)){
                    allImgUrls = oldUrl + newUrl;
                }
                if (!SharePreCache.isEmpty(allImgUrls)){
                    allImgUrls = allImgUrls.substring(0,allImgUrls.length() - 1);
                }
                if (type == 1){
                    addWorkInfo();
                }else if (type == 2){
                    saveWorkInfo();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                     ToastUtils.showShort("图片发布失败code="+response.code());
                }
            });
        }
    }
    /**
     * 获取失败
     * @param request
     */
    @Override
    public void onfileErr(Request request) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                 ToastUtils.showShort("图片发布失败fail");
            }
        });
    }
    /**
     * 发布作业
     */
    private void addWorkInfo(){
        String classIds = getClassIds();
        if (!SharePreCache.isEmpty(homeWorkId)){
            NetworkRequest.request(new EditWorkParams(subjectId, costTime, deliver,cotent, classIds,allImgUrls,homeWorkId), CommonUrl.modifyPublish, "releaseWorkInfo");
        }else {
            NetworkRequest.request(new EditWorkParams(subjectId, costTime, deliver,cotent, classIds,allImgUrls), CommonUrl.homeworkPublish, "releaseWorkInfo");
        }
//        if ("1".equals(fly)){
//            EventBus.getDefault().post(new EventCenter<Object>("fly",new ErrorUtils("")));
//        }
    }
    /**
     * 保存作业
     */
    private void saveWorkInfo() {
        String classIds = getClassIds();
        if (SharePreCache.isEmpty(homeWorkId)){
            NetworkRequest.request(new EditWorkParams(subjectId, costTime, deliver,cotent, classIds,allImgUrls), CommonUrl.homeworkSave, "saveWorkInfo");
        }else {
            NetworkRequest.request(new EditWorkParams(subjectId, costTime, deliver,cotent, classIds,allImgUrls,homeWorkId), CommonUrl.homeworkmodifySave, "saveWorkInfo");
        }
    }
    private String getClassIds() {
        String classIds = "";
        for (int i = 0 ; i < adapterclasses.getChecked().size() ; i ++){
            classIds += adapterclasses.getChecked().get(i) + ",";
        }
        if (!SharePreCache.isEmpty(classIds)){
            classIds = classIds.substring(0,classIds.length() - 1);
        }
        return classIds;
    }
    private void getSubjectData() {
        NetworkRequest.request(null,CommonUrl.homeworkSubjectList,"edtsubList");
    }
    private void getClassData() {
        NetworkRequest.request(new CenterParams(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey),1,100),CommonUrl.homeworkClassesList,"classList");
    }
    @Subscribe
    public void onEventMainThread(EventCenter event){
        switch (event.getEventCode()){
            case "error":
                dialog.dismiss();
                 ToastUtils.showShort("网络请求出错");
                break;
            case "noSuccess":
                dialog.dismiss();
                JSONObject o = (JSONObject) event.getData();
                if (o != null){
                    try {
                         ToastUtils.showShort(o.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "homeWorkdtl":
                dialog.dismiss();
                JSONObject jsonObject = (JSONObject) event.getData();
                try {
                    homeWorkBean = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),HomeWorkBean.class);
                    if (homeWorkBean != null){
                        costtime_et.setText(homeWorkBean.getCostTime());
                        deliver_time_et.setText(homeWorkBean.getDeliverTime().split(" ")[0]);
                        if (homeWorkBean.getImgs() != null && homeWorkBean.getImgs().size() > 0){
                            for (int i = 0; i < homeWorkBean.getImgs().size() ; i ++){
                                String url = homeWorkBean.getImgs().get(i).getDocPath();
                                oldUrl += url+",";
                                LocalImageHelper.LocalFile imgUrl = new LocalImageHelper.LocalFile();
                                imgUrl.setThumbnailUri(url);
                                imgUrls.add(imgUrl);
                            }
                            bean.setDatas(imgUrls);
                            adapterimg.setPicLists(imgUrls);
                            adapterimg.notifyDataSetChanged();
                        }
                        getSubjectData();
                        getClassData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "edtsubList":
                dialog.dismiss();
                JSONObject obj = (JSONObject) event.getData();
                subObj = GsonUtils.toArray(SubjectBeans.class,obj);
                if (subObj.size() > 0){
                    for (int i = 0 ;i < subObj.size() ; i ++){
                        subStr.add(subObj.get(i).getName());
                        if (homeWorkBean != null && !SharePreCache.isEmpty(homeWorkBean.getSubjectId())){
                            if (homeWorkBean.getSubjectId().equals(subObj.get(i).getSubjectId())){
                                subject_name_tv.setText(subObj.get(i).getName());
                                index_subject = i;
                                subjectId = homeWorkBean.getSubjectId();
                            }
                        }
                    }
                }
                break;
            case "classList":
                dialog.dismiss();
                JSONObject os = (JSONObject) event.getData();
                classnames = GsonUtils.toArray(ClassesBeans.class,os);
                if (classnames.size() > 0){
                    adapterclasses = new EditClassAdapter(this,classnames);
                    if (homeWorkBean != null && homeWorkBean.getLink().size() > 0){
                        adapterclasses.setIsChecked(homeWorkBean.getLink());
                    }
                    selectClassGridview.setAdapter(adapterclasses);
                }
                break;
            case "releaseWorkInfo":
                dialog.dismiss();
                 ToastUtils.showShort("发布成功");
                setResult(RESULT_OK);
                finish();
                break;
            case "saveWorkInfo":
                dialog.dismiss();
                 ToastUtils.showShort("保存成功");
                if (!SharePreCache.isEmpty(homeWorkId)){
                    setResult(2);
                }else {
                    startActivity(DraftActivity.class);
                }
                finish();
                break;
        }
    }
}
