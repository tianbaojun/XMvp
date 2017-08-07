package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ExecuteTaskRepairParams;
import com.zjhz.teacher.NetworkRequests.request.RepairsDetailParams;
import com.zjhz.teacher.NetworkRequests.request.ResubmitRepairsParams;
import com.zjhz.teacher.NetworkRequests.response.DeptBean;
import com.zjhz.teacher.NetworkRequests.response.DeptNameAndIdBean;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.NetworkRequests.response.RepairsDetailBean;
import com.zjhz.teacher.NetworkRequests.response.RepairsTaskListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.adapter.RepairsListAdapter;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 重新提交报修单
 * Created by Administrator on 2016/7/18.
 */
public class RepairReSubmitActivity extends BaseActivity implements LoadFile.FileCallBack {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.activity_repairs_proposer_state_time)
    TextView time;
    @BindView(R.id.activity_repairs_proposer_state_name)
    TextView userName;
    @BindView(R.id.activity_repairs_proposer_state_image)
    CircleImageView head_image;
    @BindView(R.id.goodsName)
    EditText goodsName;
    @BindView(R.id.goodsAddress)
    EditText goodsAddress;
    @BindView(R.id.goodsDept)
    TextView goodsDept;
    @BindView(R.id.goodsDescir)
    EditText goodsDescir;
//    @BindView(R.id.pic_grid)
//    ScrollViewWithGridView picGrid;
    @BindView(R.id.recycler_images)
    PicturesPreviewer picturesPreviewer;
    @BindView(R.id.task_list)
    ScrollViewWithListView taskList;
    @BindView(R.id.ll)
    LinearLayout ll;
    private String repairId = "";
//    private SelectPicPopwindow selectPicPopwindow;
//    private PopupWindow makePopwindow;
//    private LoadFile loadFile;
//    private PhotoListBean imgbean;
//    private EditImageAdapter adapterimg;
//    private String oldImgUrls = "";
    private String allImgUrls = "";
//    private List<LocalImageHelper.LocalFile> imgUrls = new ArrayList<>();//保存的图片集合
    private final static String TAG = RepairReSubmitActivity.class.getSimpleName();
    //提交数据
    private String date = "";
    private String goodsNames = "";
    private String addresss = "";
    private String dutyDepts = "";
    private String describs = "";
    private String dutyDeptId = "";
    private int update_index;
    private List<String> managerStr = new ArrayList<>();
    private List<String> managerId = new ArrayList<>();
    private int pageNum = 1,pageSize = 9,taskTotal = 0;
    @BindView(R.id.more_ll)
    LinearLayout more_ll;
    private List<RepairsTaskListBean> listBeen = new ArrayList<>();
    private  RepairsListAdapter adapter;


    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resubmitrepairs);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        repairId = getIntent().getStringExtra("repairId");
        update_index = getIntent().getIntExtra("update_index",-1);
        if (SharePreCache.isEmpty(repairId)) {
            ToastUtils.showShort("报修ID丢失");
            return;
        }
        DeptNameAndIdBean bean = (DeptNameAndIdBean) BaseUtil.getLocalFile(this, Config.DEPTNAMEANDID);
        if (bean == null) {
            getSpinerData();
        } else {
            managerStr = bean.getDeptNames();
            managerId = bean.getDeptIds();
        }
        initView();
    }

    //获取部门列表
    private void getSpinerData() {
        NetworkRequest.request(null, CommonUrl.Deptlist,Config.REPAIRDEPT);
    }
    private void initView() {

        rightText.setVisibility(View.VISIBLE);
        rightText.setText("提交");
        picturesPreviewer.maxCount = 5;
//        userName = SharedPreferencesUtils.getSharePrefString(ConstantKey.NickNameKey);
//        selectPicPopwindow = new SelectPicPopwindow(this);
//        loadFile = new LoadFile(this);
//        imgbean = new PhotoListBean();
//        imgbean.setTotalCount(5);
//        adapterimg = new EditImageAdapter(this);
//        adapterimg.setTotalCount(5);
//        picGrid.setAdapter(adapterimg);
//        picGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (adapterimg.getPicLists() != null && position < adapterimg.getPicLists().size()){
//                }else {
//                    if (makePopwindow == null){
//                        makePopwindow = selectPicPopwindow.makePopwindow(imgbean);
//                    }
//                    makePopwindow.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
//                }
//            }
//        });
//        picGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                if (adapterimg.getPicLists() != null && position < adapterimg.getPicLists().size()) {
//                    final int deleteIndex = position;
//                    View view1 = LayoutInflater.from(RepairReSubmitActivity.this).inflate(R.layout.hintpop,null);
//                    TextView textView = (TextView) view1.findViewById(R.id.hint_tvs);
//                    textView.setText("确认放弃这张图片吗?");
//                    final PopupWindow  popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
//                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
//                    popupWindow.setOutsideTouchable(false);
//                    view1.findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            popupWindow.dismiss();
//                            String url = adapterimg.getPicLists().get(deleteIndex).getThumbnailUri()+",";
//                            if (oldImgUrls.contains(url)){
//                                oldImgUrls = oldImgUrls.replace(url,"");
//                            }
//                            adapterimg.getPicLists().remove(deleteIndex);
//                            adapterimg.notifyDataSetChanged();
//                        }
//                    });
//                    view1.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {popupWindow.dismiss();}
//                    });
//                    popupWindow.showAtLocation(ll, Gravity.CENTER,0,0);
//                }
//                return false;
//            }
//        });
        adapter = new RepairsListAdapter(this, listBeen);
        taskList.setAdapter(adapter);
        taskList.setSelector(new ColorDrawable(Color.TRANSPARENT));
        getDetail();
    }
    @OnClick({R.id.title_back_img,R.id.right_text,R.id.goodsDept,R.id.more_ll})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                SubmitInfo();
                break;
            case R.id.goodsDept:
                selectType();
                break;
            case R.id.more_ll:
                pageNum++;
                getDetail();
                break;
        }
    }
    private int index = 0;
    private OptionsPickerView optionsPickerView;
    private void selectType() {
        if (managerStr.size() > 0 ){
            if (optionsPickerView == null){
                optionsPickerView = new OptionsPickerView(this);
                optionsPickerView.setPicker((ArrayList) managerStr, index, 0, 0);
                optionsPickerView.setCyclic(false);
                optionsPickerView.setCancelable(true);
                optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                        goodsDept.setText(managerStr.get(options1));
                        dutyDeptId = managerId.get(options1);
                        index = options1;
                    }
                });
            }
            optionsPickerView.show();
        }else {
            ToastUtils.showShort("没有数据");
        }
    }
    private void SubmitInfo() {
        if (isRequest()){
            dialog.setMessage("正在提交...");
            dialog.show();
//            if (adapterimg.needUpDatas().size() == 0 ){
//                if (!SharePreCache.isEmpty(oldImgUrls)){
//                    allImgUrls = oldImgUrls.substring(0,oldImgUrls.length() - 1);
//                }
//                reSubmitRepairs();
//            }else {
//                final List<File> imgUrlsFile = loadFile.uriToFile(this,adapterimg.needUpDatas());//将图片URI转换成文件
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadFile.uploadFile(imgUrlsFile);
//                    }
//                }).start();
//            }

            int old = 0;
            int length = 0;
            if(picturesPreviewer.getPaths() != null) {
                length = picturesPreviewer.getPaths().length;
            }
            List<String> localFileList = new ArrayList<>();
            for(int i = 0; i < length; i++){
                if(picturesPreviewer.getPaths()[i].startsWith("http")){
                    old++;
                    allImgUrls += picturesPreviewer.getPaths()[i]+",";
                }else {
                    localFileList.add(picturesPreviewer.getPaths()[i]);
                }
            }
            if(old == length){
                reSubmitRepairs();
            }else {
                final LoadFile loadFile = new LoadFile(this);
                final List<File> imgUrlsFile = loadFile.uriToFile(localFileList);//将图片URI转换成文件
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadFile.uploadFile(imgUrlsFile);
                    }
                }).start();
            }

//            if (adapterimg.needUpDatas().size() == 0 ){
//                if (!SharePreCache.isEmpty(oldImgUrls)){
//                    allImgUrls = oldImgUrls.substring(0,oldImgUrls.length() - 1);
//                }
//                reSubmitRepairs();
//            }else {
//                final List<File> imgUrlsFile = loadFile.uriToFile(this,adapterimg.needUpDatas());//将图片URI转换成文件
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        loadFile.uploadFile(imgUrlsFile);
//                    }
//                }).start();
//            }
        }
    }
    private boolean isRequest() {
//        if (SharePreCache.isEmpty(date)){
//            ToastUtils.showShort("日期为空，不能发布");
//            return false;
//        }
        goodsNames = goodsName.getText().toString().trim();
        if (SharePreCache.isEmpty(goodsNames)){
            ToastUtils.showShort("请输入物品名称");
            return false;
        }
        addresss = goodsAddress.getText().toString().trim();
        if (SharePreCache.isEmpty(addresss)){
            ToastUtils.showShort("请输入物品地址");
            return false;
        }
        dutyDepts = goodsDept.getText().toString().trim();
        if (SharePreCache.isEmpty(dutyDepts)){
            ToastUtils.showShort("请输入部门");
            return false;
        }
//        if (SharePreCache.isEmpty(allImgUrls)){
//            ToastUtils.showShort("请添加图片");
//            return false;
//        }
        return true;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            if (requestCode == SelectPicPopwindow.REQUEST_CODE_GETIMAGE_BYCROP){
//                //获取选中的图片
//                List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
//                imgbean.setDatas(files);
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
//                imgbean.setDatas(list);
//                adapterimg.setPicLists(list);
//                adapterimg.notifyDataSetChanged();
//            }
//        }
//    }
    private void getDetail() {
        NetworkRequest.request(new RepairsDetailParams(repairId,true,pageNum,pageSize), CommonUrl.repairDtl, Config.REREPAIRDETAIL);
    }
    private void reSubmitRepairs() {
        describs = goodsDescir.getText().toString().trim();
//        date = TimeUtil.getYMD(new Date());
        NetworkRequest.request(new ResubmitRepairsParams(date,goodsNames,addresss,dutyDeptId,describs,allImgUrls,repairId),CommonUrl.modify,Config.REREPAIRresubmit);
    }
    private void alignreSubmitRepairs() {
        NetworkRequest.request(new ExecuteTaskRepairParams(repairId,"重新提交"),CommonUrl.alignTask,Config.REREPAIRalignsubmit);
    }
    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()) {
            case Config.ERROR:
                ToastUtils.showShort("请求错误");
                dialog.dismiss();
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
//                JSONObject o = (JSONObject) event.getData();
//                if (o.optInt("code") == 1) {
//                    SharedPreferencesUtils.putSharePrefString(ConstantKey.TokenKey, "");
//                    toLoginActivity();
//                }
                break;
            case Config.REREPAIRDETAIL:
                JSONObject js = (JSONObject) event.getData();
                try {
                    RepairsDetailBean bean = GsonUtils.toObject(js.getJSONObject("data").toString(), RepairsDetailBean.class);
                    if (pageNum == 1){
                        initData(bean);
                    }else {
                        taskListData(bean.getTaskList());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                break;
            case Config.REREPAIRalignsubmit:
                Intent intent = new Intent();
                intent.setAction("MyRepairsFragment");
                intent.putExtra("isupdate",true);
                intent.putExtra("update_index",update_index);
//                intent.putExtra("update_time",date);
                intent.putExtra("describs","报修地点"+addresss+"需要维修"+goodsNames);
                intent.putExtra("OrginAddress",addresss);
                intent.putExtra("ItemName",goodsNames);
                intent.putExtra("status",1);
                sendBroadcast(intent);
                dialog.dismiss();
                finish();
                break;
            case Config.REREPAIRresubmit:
                alignreSubmitRepairs();
                break;
            case Config.REPAIRDEPT:
                JSONObject obj = (JSONObject) event.getData();
                List<DeptBean> beans =GsonUtils.toArray(DeptBean.class,obj);
                if (beans != null && beans.size() > 0){
                    DeptNameAndIdBean bean = new DeptNameAndIdBean();
                    for (int i = 0 ; i < beans.size() ; i ++){
                        managerStr.add(beans.get(i).getDeptName());
                        managerId.add(beans.get(i).getDeptId());
                    }
                    bean.setDeptNames(managerStr);
                    bean.setDeptIds(managerId);
                    BaseUtil.saveLocalFile(this,bean,Config.DEPTNAMEANDID);
                }
                break;

        }
    }
    private void initData(RepairsDetailBean bean) {
        if (bean != null) {
            taskTotal = bean.getDtlTotal();
            dutyDeptId = bean.getDutyDept();
            if (!SharePreCache.isEmpty(bean.getPhotoUrl())){
                GlideUtil.loadImage(bean.getPhotoUrl(),head_image);
            }
            userName.setText(bean.getNickName());
            date = bean.getApplyTime();
            time.setText(SharePreCache.isEmpty(bean.getApplyTime()) ? "报修时间：" : "报修时间："+bean.getApplyTime().split(" ")[0]);
            goodsName.setText(SharePreCache.isEmpty(bean.getItemName()) ? "" : bean.getItemName());
            goodsAddress.setText(SharePreCache.isEmpty(bean.getOrginAddress()) ? "" : bean.getOrginAddress());
            goodsDept.setText(SharePreCache.isEmpty(bean.getDutyDept()) ? "" :bean.getDutyDeptVal());
            goodsDescir.setText(SharePreCache.isEmpty(bean.getSummary()) ? "" :bean.getSummary());
            List<ImageBean> imgs = bean.getImgs();
//            if (imgs != null && imgs.size() > 0) {
//                for (int i = 0; i < imgs.size()  ; i ++){
//                    String url = imgs.get(i).getDocPath();
//                    oldImgUrls += url+",";
//                    LocalImageHelper.LocalFile imgUrl = new LocalImageHelper.LocalFile();
//                    imgUrl.setThumbnailUri(url);
//                    imgUrls.add(imgUrl);
//                }
////                imgbean.setDatas(imgUrls);
////                adapterimg.setPicLists(imgUrls);
////                adapterimg.notifyDataSetChanged();
//            }
            String[] imgPaths = new String[10];
            for(int i = 0; i < imgs.size(); i++){
                String url = imgs.get(i).getDocPath();
                imgPaths[i] = url;
//                oldImgUrls += url+",";
            }

            picturesPreviewer.setHasLoadPicture(imgPaths);

            taskListData(bean.getTaskList());
        }
        dialog.dismiss();
    }
    private void taskListData( List<RepairsTaskListBean> tasklist){
        if ( tasklist != null && tasklist.size() > 0){
            if (pageNum == 1){
                listBeen.clear();
            }
            for (int i = 0 ; i < tasklist.size() ; i ++){
                if (tasklist.get(i).getStatus() != 0){
                    listBeen.add(tasklist.get(i));
                }
            }
            if (listBeen.size() == 0 ){
                more_ll.setVisibility(View.GONE);
            }else {
                if (pageNum * pageSize < taskTotal){
                    more_ll.setVisibility(View.VISIBLE);
                }else {
                    more_ll.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }else {
            more_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public void onfileComplete(final Response response) {
        if (response.code() == 200){
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
                String newImgUrls = "";
                if (imageUrls != null){
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)){newImgUrls += f0+",";}
                    String f1 = imageUrls.getFile1();
                    if (!SharePreCache.isEmpty(f1)){newImgUrls += f1+",";}
                    String f2 = imageUrls.getFile2();
                    if (!SharePreCache.isEmpty(f2)){newImgUrls += f2+",";}
                    String f3 = imageUrls.getFile3();
                    if (!SharePreCache.isEmpty(f3)){newImgUrls += f3+",";}
                    String f4 = imageUrls.getFile4();
                    if (!SharePreCache.isEmpty(f4)){newImgUrls += f4+",";}
                }
                if (!SharePreCache.isEmpty(newImgUrls)){
                    allImgUrls = allImgUrls + newImgUrls;
                }
                if (!SharePreCache.isEmpty(allImgUrls)){
                    allImgUrls = allImgUrls.substring(0,allImgUrls.length() - 1);
                }
                reSubmitRepairs();
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
}
