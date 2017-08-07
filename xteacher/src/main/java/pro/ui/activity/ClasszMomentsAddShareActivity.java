package pro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsMomentSave;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.uk.co.senab.photoview.Bimp;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 班级动态发布
 */
public class ClasszMomentsAddShareActivity extends BaseActivity implements LoadFile.FileCallBackNew {

//    //图片选择控件
//    @BindView(R.id.add_img)
//    net.qiujuer.genius.ui.widget.ImageView addImg;
//    //图片选择控件
//    @BindView(R.id.video_fl)
//    FrameLayout videoFl;
//    @BindView(R.id.video_img)
//    ImageView videoImg;
    //图片选择控件
    @BindView(R.id.recycler_images)
    PicturesPreviewer mLayImages;
    //内容编辑et
    @BindView(R.id.content_et)
    EditText content_et;
    //提交按钮
    @BindView(R.id.right_text)
    TextView rightText;
    //标题Title
    @BindView(R.id.title_tv)
    TextView title_tv;

    //班级动态的内容
    private String content;
    //LoadFile工具类
    private LoadFile loadFile ;

    //发布班级动态的请求参数
    private ClasszMomentsMomentSave params  = new ClasszMomentsMomentSave();
    //班级id   dcId
    private String classId,dcId;
    //sectionCode
    private final String sectionCode = "1001";

    private String videoPath, picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments_add_share);
        ButterKnife.bind(this);
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(R.string.submit);
        title_tv.setText(R.string.classz_moments_add_share_title);
        loadFile = new LoadFile(this, this);
        mLayImages.setType(PicturesPreviewer.PIC_AND_VIDEO);

        initParams();
    }

    //初始化请求参数，一些intent过来的数据
    private void initParams() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String dcId = bundle.getString("dcId");
        String classId = bundle.getString("classId");
        if(!SharePreCache.isEmpty(classId)){
            params.setClassId(classId);
        }
        if (!SharePreCache.isEmpty(dcId)) {
            params.setDcId(dcId);
        }
        params.setSectionCode(sectionCode);
    }

    @OnClick({R.id.title_back_img,R.id.right_text})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        Bimp.tempSelectBitmap=new ArrayList<>();
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            //发布
            case R.id.right_text:
                releaseInfo();
                break;
//            case R.id.add_img:
//                new SelectPicVideoPopupWindow(this, addImg, new SelectPicVideoPopupWindow.PicOrVideo() {
//                    @Override
//                    public void pic() {
//                        mLayImages.setVisibility(View.VISIBLE);
//                        addImg.setVisibility(View.GONE);
//                        videoFl.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void video() {
//                        videoFl.setVisibility(View.VISIBLE);
//                        addImg.setVisibility(View.GONE);
//                        mLayImages.setVisibility(View.GONE);
//                    }
//                });
//                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "noSuccess":
                break;
            case Config.MOMENTSAVE:
                ToastUtils.showShort(R.string.classz_moments_submit_success);
                setResult(101);
                finish();
                break;
        }
    }

    private void releaseInfo() {
        if (isRequest()){
            dialog.setMessage("正在提交");
            dialog.show();
            if(mLayImages.getSelectType() == PicturesPreviewer.SELECT_PIC) {
                final List<File> imgUrls = loadFile.uriToFile(mLayImages.getPaths()); //将图片URI转换成文件
                if (imgUrls.size() > 0) {  //如果存在图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadFile.uploadFile(imgUrls);
                        }
                    }).start();
                } else {
                    addShare();
                }
            }else {
                List<String> videoList = new ArrayList<>();
                videoList.add(videoPath);
                List<String> picList = new ArrayList<>();
                picList.add(picturePath);
                final List<File> videoFile = loadFile.uriToAttrFile(videoList);
                final List<File> picFile = loadFile.uriToFile(picList); //将图片URI转换成文件
                if (videoFile.size() > 0 || picFile.size()> 0) {  //如果存在图片
                    if(videoFile.size() > 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                loadFile.uploadFile(videoFile);
                            }
                        }).start();
                    }
                    if(picFile.size()> 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                loadFile.uploadFile(picFile);
                            }
                        }).start();
                    }
                } else {
                    addShare();
                }
            }
        }
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private boolean isRequest() {
        content = content_et.getText().toString();
        if (SharePreCache.isEmpty(content)){
            ToastUtils.showShort("请输入内容");
            return false;
        }else{
            params.setCcdContent(content);
        }
        return true;
    }

    /**
     * 发布班级动态，发送添加班级动态的网络请求
     */
    private void addShare(){
        NetworkRequest.request(params, CommonUrl.MOMENTSAVE, Config.MOMENTSAVE);
    }

//    @Override
//    public void onfileComplete(final Response response) {
//        if (response.code() == 200){
//            try {
//                JSONObject jsonObject = new JSONObject(response.body().string());
//                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
//                String newUrl = "";
//                if (imageUrls != null){
//                    String f0 = imageUrls.getFile0();
//                    if (!SharePreCache.isEmpty(f0)){newUrl += f0+",";}
//                    String f1 = imageUrls.getFile1();
//                    if (!SharePreCache.isEmpty(f1)){newUrl += f1+",";}
//                    String f2 = imageUrls.getFile2();
//                    if (!SharePreCache.isEmpty(f2)){newUrl += f2+",";}
//                    String f3 = imageUrls.getFile3();
//                    if (!SharePreCache.isEmpty(f3)){newUrl += f3+",";}
//                    String f4 = imageUrls.getFile4();
//                    if (!SharePreCache.isEmpty(f4)){newUrl += f4+",";}
//                    String f5 = imageUrls.getFile5();
//                    if (!SharePreCache.isEmpty(f5)){newUrl += f5+",";}
//                    String f6 = imageUrls.getFile6();
//                    if (!SharePreCache.isEmpty(f6)){newUrl += f6+",";}
//                    String f7 = imageUrls.getFile7();
//                    if (!SharePreCache.isEmpty(f7)){newUrl += f7+",";}
//                    String f8 = imageUrls.getFile8();
//                    if (!SharePreCache.isEmpty(f8)){newUrl += f8+",";}
//                }
//                if (!SharePreCache.isEmpty(newUrl)){
//                    allImgUrls =  newUrl;
//                }
//                if (!SharePreCache.isEmpty(allImgUrls)){
//                    allImgUrls = allImgUrls.substring(0,allImgUrls.length() - 1);
//                    params.setPicUrls(allImgUrls);
//                }
//                addShare();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }else {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    dialog.dismiss();
//                    ToastUtils.showShort("图片发布失败code="+response.code());
//                }
//            });
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;
        videoPath = data.getStringExtra("video_path");
        picturePath = data.getStringExtra("picture_path");

        mLayImages.setHasLoadPicture(picturePath);
        mLayImages.setVideoPath(videoPath);
    }

    @Override
    public void onFileComplete(String urls) {
        if(mLayImages.getSelectType() == PicturesPreviewer.SELECT_PIC) {
            params.setPicUrls(urls);
            addShare();
        }else if(mLayImages.getSelectType() == PicturesPreviewer.SELECT_VIDEO){
            if(urls.endsWith("mp4")){
                params.setAttPath(urls);
                loadFile.clearPathUrls();
            }else {
                params.setAttPicPath(urls);
                loadFile.clearPathUrls();
            }
            if(params.getAttPath() != null && params.getAttPicPath() != null)
                addShare();
        }
    }

    @Override
    public void onFileErr(Request request) {

    }
}
