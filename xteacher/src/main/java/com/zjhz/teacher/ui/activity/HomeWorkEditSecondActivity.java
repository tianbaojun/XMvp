/*
 * 源文件名：HomeWorkEditSecondActivity
 * 文件版本：1.0.0
 * 创建作者： fei.wang
 * 创建日期：2016-06-20
 * 修改作者：yyf
 * 修改日期：2016/11/3
 * 文件描述： 添加作业布置  第二步  作业录音  图片浏览删除
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.EditWorkParams;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.uk.co.senab.photoview.Bimp;

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

public class HomeWorkEditSecondActivity extends BaseActivity implements LoadFile.FileCallBack {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.rl)
    LinearLayout rl;
    @BindView(R.id.content_et)
    EditText content_et;
//    private EditImageAdapter adapterimg;   //图片选择adapter
//    private PhotoListBean bean ;           //选中的图片集合
    private LoadFile loadFile ;             //图片处理类

    private List<LocalImageHelper.LocalFile> imgUrls = new ArrayList<>();//保存的图片集合
    private final static String TAG = HomeWorkEditSecondActivity.class.getSimpleName();

    private String content="";
    private String oldUrl = "";
    private String allImgUrls = "";
    private String homeWorkId = "";
    private  HomeWorkBean homeWorkBean;
    String fly;


    @BindView(R.id.recycler_images)
    PicturesPreviewer mLayImages;
    EditWorkParams editWorkParams;//编辑作业数据
    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        AppContext.getInstance().addActivity(TAG,this);
        setContentView(R.layout.activity_edit_homework_second_pro);
        ButterKnife.bind(this);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(R.string.submit);
        //    得到跳转到该Activity的Intent对象
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        String classIds = bundle.getString("classIds");
        String subjectId = bundle.getString("subjectId");
        String costTime = bundle.getString("costTime");
        String deliverTime = bundle.getString("deliverTime");
        editWorkParams = new EditWorkParams(subjectId, costTime, deliverTime, classIds);
        initView();
//        dialog.show();
//        homeWorkId = getIntent().getStringExtra("homeworId");
//        if (!SharePreCache.isEmpty(homeWorkId)){
//            NetworkRequest.request(new WorkDetailParams(homeWorkId),"TeachHomeworkService.dtl","homeWorkdtl");
//        }
    }
    private void initView() {
        titleTv.setText("作业编辑");
        loadFile = new LoadFile(this);
        mLayImages.maxCount=5;//最多5张
    }
    @OnClick({R.id.title_back_img,R.id.right_text})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        Bimp.tempSelectBitmap=new ArrayList<LocalImageHelper.LocalFile>();
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                ViewTools.hideSoftInput(this);
                break;
            //发布
            case R.id.right_text:
                releaseInfo();
                ViewTools.hideSoftInput(this);
                break;
        }
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int ss= Bimp.tempSelectBitmap.size();
        if (resultCode == RESULT_OK){
            if (requestCode == SelectPicPopwindow.REQUEST_CODE_GETIMAGE_BYCROP){
                //获取选中的图片
                List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();
                bean.setDatas(files);
                adapterimg.setPicLists(files);
                adapterimg.notifyDataSetChanged();
            }else if (requestCode == SelectPicPopwindow.SELECT_PIC_BY_TACK_PHOTO){//拍照
                LocalImageHelper.LocalFile files = new LocalImageHelper.LocalFile();
                files.setThumbnailUri(SelectPicPopwindow.photoUri +"");
                //图片查看使用
                files.setImgPath(getRealPathFromURI(SelectPicPopwindow.photoUri));

                Bimp.tempSelectBitmap.add(files);
                List<LocalImageHelper.LocalFile> list = Bimp.tempSelectBitmap;
//                List<LocalImageHelper.LocalFile> list = adapterimg.getPicLists();
                if (list == null){
                    list = new ArrayList<>();
                }
//                list.add(files);
                bean.setDatas(list);
                adapterimg.setPicLists(list);
                adapterimg.notifyDataSetChanged();
            }

        }else{
            List<LocalImageHelper.LocalFile> files = Bimp.tempSelectBitmap;
            bean.setDatas(files);
            adapterimg.setPicLists(files);
            adapterimg.notifyDataSetChanged();
        }
    }*/


//    Uri获取真实路径转换成File
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    //获取网络的图片路径
    private void releaseInfo() {
        if (isRequest()){
//            if (SharePreCache.isEmpty(oldUrl) && adapterimg.needUpDatas().size() == 0){
//                 ToastUtils.showShort("请选择图片");
//                return;
//            }
            editWorkParams.setContent(content);
            dialog.setMessage("正在提交...");
            dialog.show();
            final List<File> imgUrls = loadFile.uriToFile(mLayImages.getPaths()); //将图片URI转换成文件
            if(imgUrls.size()>0) {  //如果存在图片
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadFile.uploadFile(imgUrls);
                    }
                }).start();
            }else{
                addWorkInfo();
            }
            /*if (adapterimg.needUpDatas().size() == 0 ){
                if (!TextUtils.isEmpty(oldUrl)){
                    allImgUrls = oldUrl.substring(0,oldUrl.length() - 1);
                }

                    addWorkInfo();
                   // saveWorkInfo();

            }else {
                final List<File> imgUrlsFile = loadFile.uriToFile(this,adapterimg.needUpDatas());//将图片URI转换成文件
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadFile.uploadFile(imgUrlsFile);
                    }
                }).start();
            }*/
        }
    }
    private boolean isRequest() {

        content = content_et.getText().toString();
        LogUtil.e("提交作业：",content_et.getText().toString());
        if (SharePreCache.isEmpty(content)){
             ToastUtils.showShort("请输入内容");
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

                    addWorkInfo();

                  //  saveWorkInfo();

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

        editWorkParams.setImages(allImgUrls);
        if (!SharePreCache.isEmpty(homeWorkId)){
            NetworkRequest.request(editWorkParams, CommonUrl.modifyPublish, "releaseWorkInfo");
        }else {
            NetworkRequest.request(editWorkParams, CommonUrl.homeworkPublish, "releaseWorkInfo");
        }
//        if ("1".equals(fly)){
//            EventBus.getDefault().post(new EventCenter<Object>("fly",new ErrorUtils("")));
//        }
    }
    /**
     * 保存作业
     */
//    private void saveWorkInfo() {
//
//        if (SharePreCache.isEmpty(homeWorkId)){
//            NetworkRequest.request(editWorkParams, CommonUrl.homeworkSave, "saveWorkInfo");
//        }else {
//            NetworkRequest.request(editWorkParams, CommonUrl.homeworkmodifySave, "saveWorkInfo");
//        }
//    }


    @Subscribe
    public void onEventMainThread(EventCenter event){
        Bimp.tempSelectBitmap=new ArrayList<LocalImageHelper.LocalFile>();
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
//                        costtime_et.setText(homeWorkBean.getCostTime());
//                        deliver_time_et.setText(homeWorkBean.getDeliverTime().split(" ")[0]);
                        content_et.setText(homeWorkBean.getContent());
                        if (homeWorkBean.getImgs() != null && homeWorkBean.getImgs().size() > 0){
                            for (int i = 0; i < homeWorkBean.getImgs().size() ; i ++){
                                String url = homeWorkBean.getImgs().get(i).getDocPath();
                                oldUrl += url+",";
                                LocalImageHelper.LocalFile imgUrl = new LocalImageHelper.LocalFile();
                                imgUrl.setThumbnailUri(url);
                                imgUrls.add(imgUrl);
                            }
//                            bean.setDatas(imgUrls);
//                            adapterimg.setPicLists(imgUrls);
//                            adapterimg.notifyDataSetChanged();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "edtsubList":
                dialog.dismiss();
                JSONObject obj = (JSONObject) event.getData();
//                subObj = GsonUtils.toArray(SubjectBeans.class,obj);
//                if (subObj.size() > 0){
//                    for (int i = 0 ;i < subObj.size() ; i ++){
//                        subStr.add(subObj.get(i).getName());
//                        if (homeWorkBean != null && !SharePreCache.isEmpty(homeWorkBean.getSubjectId())){
//                            if (homeWorkBean.getSubjectId().equals(subObj.get(i).getSubjectId())){
//                                subject_name_tv.setText(subObj.get(i).getName());
//                                index_subject = i;
//                                subjectId = homeWorkBean.getSubjectId();
//                            }
//                        }
//                    }
//                }
                break;

            case "releaseWorkInfo":
                dialog.dismiss();
                 ToastUtils.showShort("发布成功");
                setResult(RESULT_OK);
                Bimp.tempSelectBitmap=new ArrayList<LocalImageHelper.LocalFile>();
                finish();
                break;
          /*  case "saveWorkInfo":
                dialog.dismiss();
                 ToastUtils.showShort("保存成功");
                if (!SharePreCache.isEmpty(homeWorkId)){
                    setResult(2);
                }else {
                    startActivity(DraftActivity.class);
                }
                finish();
                break;*/
        }
    }
}
