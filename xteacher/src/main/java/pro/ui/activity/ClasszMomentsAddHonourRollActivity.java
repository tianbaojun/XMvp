package pro.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsHonourSave;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.GsonUtils;
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

/**
 * 添加班级红榜
 */
public class ClasszMomentsAddHonourRollActivity extends BaseActivity implements LoadFile.FileCallBack  {

    //图片选择控件
    @BindView(R.id.recycler_images)
    PicturesPreviewer mLayImages;
    //主题编辑et
    @BindView(R.id.theme_et)
    EditText theme_et;
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
    //班级红榜主题
    private String theme;
    //上传图片的url
    private String allImgUrls = "";
    //LoadFile工具类
    private LoadFile loadFile ;

    private String dcId,classzId;

    private final String honourCode = "1003";

    private ClasszMomentsHonourSave params = new ClasszMomentsHonourSave();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments_add_honour_roll);
        ButterKnife.bind(this);
        initTitle();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String dcId = bundle.getString("dcId");
        String classId = bundle.getString("classId");
        params.setDcId(dcId);
        params.setClassId(classId);
        params.setSectionCode("1003");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    private void initTitle() {
        rightText.setVisibility(View.VISIBLE);
        title_tv.setText(R.string.activity_classz_moments_add_honour_roll_title);
        rightText.setText(R.string.submit);
        loadFile = new LoadFile(this);
    }

    private void saveHonour(){

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
            case R.id.right_text:            //发布
                releaseInfo();
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        dialog.dismiss();
        switch (event.getEventCode()) {
            case "noSuccess":
                break;
            case Config.HONOURSAVE:
                ToastUtils.showShort(R.string.classz_moments_submit_success);
                setResult(101);
                finish();
                break;

        }
    }

    private void releaseInfo() {
        if (isRequest()){
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
                addShare();
            }
        }
    }


    private boolean isRequest() {
        theme= theme_et.getText().toString();
        content = content_et.getText().toString();
        if (SharePreCache.isEmpty(content)){
            ToastUtils.showShort(R.string.activity_classz_moments_add_honour_roll_please_input_content);
            return false;
        }
        if (SharePreCache.isEmpty(theme)) {
            ToastUtils.showShort(R.string.activity_classz_moments_add_honour_roll_please_input_theme);
            return false;
        }
        params.setCcdTitle(theme);
        params.setCcdContent(content);
        return true;
    }

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
                    String f5 = imageUrls.getFile5();
                    if (!SharePreCache.isEmpty(f5)){newUrl += f5+",";}
                    String f6 = imageUrls.getFile6();
                    if (!SharePreCache.isEmpty(f6)){newUrl += f6+",";}
                    String f7 = imageUrls.getFile7();
                    if (!SharePreCache.isEmpty(f7)){newUrl += f7+",";}
                    String f8 = imageUrls.getFile8();
                    if (!SharePreCache.isEmpty(f8)){newUrl += f8+",";}
                }
                if (!SharePreCache.isEmpty(newUrl)){
                    allImgUrls =  newUrl;
                }
                if (!SharePreCache.isEmpty(allImgUrls)){
                    allImgUrls = allImgUrls.substring(0,allImgUrls.length() - 1);
                    params.setPicUrls(allImgUrls);
                }
                addShare();
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
                    ToastUtils.showShort("图片发布失败");
                }
            });
        }
    }

    /**
     * 发送添加红榜的请求
     */
    private void addShare() {
        NetworkRequest.request(params, CommonUrl.HONOURSAVE, Config.HONOURSAVE);
    }

    @Override
    public void onfileErr(Request request) {
        dialog.dismiss();
        ToastUtils.showShort("图片发布失败");
    }
}
