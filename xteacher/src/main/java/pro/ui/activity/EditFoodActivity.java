/*
 * 源文件名：EditFoodActivity
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：编辑食谱发布
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package pro.ui.activity;

import android.os.Bundle;
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
import com.zjhz.teacher.NetworkRequests.request.ReleaseFoodBean;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;


public class EditFoodActivity extends BaseActivity implements LoadFile.FileCallBack {

    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.breakfast)
    TextView breakfast;
    @BindView(R.id.lunch)
    TextView lunch;
    @BindView(R.id.dinner)
    TextView dinner;
    @BindView(R.id.ll)
    LinearLayout ll;
//    @BindView(R.id.type)
//    TextView type;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.recycler_images)
    PicturesPreviewer mLayImages;

//    private ArrayList<String> foodType = new ArrayList<>();
    private final static String TAG = EditFoodActivity.class.getSimpleName();

//    private SpinerPopWindow mSpinerPopWindow;
//    private SpinerAdapter mAdapter;
    private int pattern = 0;
    private LoadFile loadFile ;
//    private SelectPicPopwindow selectPicPopwindow ;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_publish_pro);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG,this);
        initView();
        initData();
    }


    private void initView() {
        titleTv.setText("食谱编辑");
        titleBackImg.setText("取消");
        titleBackImg.setCompoundDrawables(null, null, null, null);
        rightText.setText("发布");
        rightText.setVisibility(View.VISIBLE);
        //默认选中午餐
        ViewTools.tvShowCheck(lunch,getString(R.string.lunch));

        pattern = 2;
    }


    private void initData() {
        //初始化餐点类型
       /* String[] types = getResources().getStringArray(R.array.type);
        for (int i = 0; i < types.length; i++) {
            foodType.add(types[i]);
        }*/
        //图片
        loadFile = new LoadFile(this);
    }


    @OnClick({ R.id.title_back_img,R.id.right_text, R.id.dinner,R.id.lunch,R.id.breakfast})
    public void clickEvent(View v) {
        if (ViewTools.avoidRepeatClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                upload();
                break;
            case R.id.dinner:   //晚餐
                selectType(dinner,getString(R.string.dinner));
                pattern = 3;
                break;
            case R.id.lunch:   //午餐
                selectType(lunch,getString(R.string.lunch));
                pattern = 2;
                break;
            case R.id.breakfast:   //早餐
                selectType(breakfast,getString(R.string.breakfast));
                pattern = 1;
                break;
        }
    }


  /*  private int index;
    private OptionsPickerView optionsPickerView;
    private void selectType() {
        if (optionsPickerView == null){
            optionsPickerView = new OptionsPickerView(this);
            optionsPickerView.setPicker((ArrayList) foodType, index, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    type.setText(foodType.get(options1));
                    pattern = options1+1;
                    index = options1;
                }
            });
        }
        optionsPickerView.show();
    }*/

    private void selectType(TextView view,String str){
        //所有设置成不选中
        dinner.setText(R.string.dinner);
        lunch.setText(R.string.lunch);
        breakfast.setText(R.string.breakfast);
        dinner.setTextColor(getResources().getColor(R.color.gray3));
        lunch.setTextColor(getResources().getColor(R.color.gray3));
        breakfast.setTextColor(getResources().getColor(R.color.gray3));
        //设置选中
        ViewTools.tvShowCheck(view,str);
    }


    @Subscribe
    public void onEventMainThread (EventCenter event){
        switch (event.getEventCode()){
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort("请求网络出错");
                break;
            case Config.NOSUCCESS:
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
            case Config.FOODRELEASE:
                dialog.dismiss();
                ToastUtils.showShort("发布成功");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }


    //获取图片的网络路径
    private void upload(){
        if (pattern == 0 ){
            ToastUtils.showShort("请选择餐类型");
            return;
        }
        if (SharePreCache.isEmpty(editContent.getText().toString().trim())){
            ToastUtils.showShort("内容不能为空");
            return;
        }
        if (mLayImages.getPaths() == null){
            ToastUtils.showShort("请添加图片");
            return;
        }
        dialog.setMessage("正在发布中...");
        dialog.show();
        final List<File> imgUrls = loadFile.uriToFile(mLayImages.getPaths()); //将图片URI转换成文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFile.uploadFile(imgUrls);
            }
        }).start();
    }


    /**
     * 获取图片路径成功
     * @param response
     */
    @Override
    public void onfileComplete(final Response response) {
        dialog.dismiss();
        if (response.code() == 200){
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(),ImageUrls.class);
                String urls = "";
                if (imageUrls != null){
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)){ urls += f0+",";}
                    String f1 = imageUrls.getFile1();
                    if (!SharePreCache.isEmpty(f1)){urls += f1+","; }
                    String f2 = imageUrls.getFile2();
                    if (!SharePreCache.isEmpty(f2)){urls += f2+",";}
                    String f3 = imageUrls.getFile3();
                    if (!SharePreCache.isEmpty(f3)){urls += f3+",";}
                    String f4 = imageUrls.getFile4();
                    if (!SharePreCache.isEmpty(f4)){urls += f4+",";}
                    String f5 = imageUrls.getFile5();
                    if (!SharePreCache.isEmpty(f5)){urls += f5+",";}
                    String f6 = imageUrls.getFile6();
                    if (!SharePreCache.isEmpty(f6)){ urls += f6+",";}
                    String f7 = imageUrls.getFile7();
                    if (!SharePreCache.isEmpty(f7)){urls += f7+",";}
                    String f8 = imageUrls.getFile8();
                    if (!SharePreCache.isEmpty(f8)){urls += f8+",";}
                }
                if (!SharePreCache.isEmpty(urls)){
                    urls = urls.substring(0,urls.length() - 1);
                }
                addOtherInfo(urls);
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
     *获取图片路径失败
     * @param request
     */
    @Override
    public void onfileErr(Request request) {
        dialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                ToastUtils.showShort("图片发布失败fail");
            }
        });
    }


    private void addOtherInfo(String urls){
        if (SharePreCache.isEmpty(urls)){
            dialog.dismiss();
            ToastUtils.showShort("图片发布失败");
            return;
        }
        NetworkRequest.request(new ReleaseFoodBean(editContent.getText().toString().trim(), pattern, urls), CommonUrl.FOODADD,  Config.FOODRELEASE);
    }
}
