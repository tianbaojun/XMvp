package com.zjhz.teacher.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.SubmitRepairsParams;
import com.zjhz.teacher.NetworkRequests.response.DeptBean;
import com.zjhz.teacher.NetworkRequests.response.DeptNameAndIdBean;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.TimeUtil;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-30
 * Time: 8:57
 * Description: 申请人报修申请内容
 */
public class RepairsProposerActivity extends BaseActivity implements LoadFile.FileCallBack {
    @BindView(R.id.title_back_img)
    TextView titleBackImg;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.repairs_time_tv)
    TextView repairs_time_tv;
    @BindView(R.id.repairs_name_tv)
    TextView repairs_name_tv;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.repairsdepartment_manager_tv)
    TextView repairsdepartment_manager_tv;
    @BindView(R.id.repairsgoods_name_tv)
    EditText repairsgoodsNameTv;
    @BindView(R.id.repairsaddress_tv)
    EditText repairsaddressTv;
    @BindView(R.id.repairsdescri_content_tv)
    EditText repairsdescriContentTv;
    private LoadFile loadFile;
    private final static String TAG = RepairsProposerActivity.class.getSimpleName();
    private List<String> managerStr = new ArrayList<>();
    private List<String> managerId = new ArrayList<>();
    private String manager;
    private String userName = "";
    private String date = "";
    private String goodsName = "";
    private String address = "";
    private String dutyDept = "";
    private String describ = "";
    @BindView(R.id.recycler_images)
    PicturesPreviewer mLayImages;

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairs_apply_for);
        ButterKnife.bind(this);
        AppContext.getInstance().addActivity(TAG, this);
        initView();
        DeptNameAndIdBean bean = (DeptNameAndIdBean) BaseUtil.getLocalFile(this, Config.DEPTNAMEANDID);
        if (bean == null) {
            dialog.setMessage("获取部门数据");
            dialog.show();
            getSpinerData();
        } else {
            managerStr = bean.getDeptNames();
            managerId = bean.getDeptIds();
        }
    }

    //获取部门列表
    private void getSpinerData() {
        NetworkRequest.request(null, CommonUrl.Deptlist, Config.REPAIRDEPT);
    }

    private void initView() {
        titleBackImg.setCompoundDrawables(null, null, null, null);
        titleBackImg.setText("取消");
        titleTv.setText("报修申请");
        rightText.setVisibility(View.VISIBLE);
        rightText.setText("提交");
        loadFile = new LoadFile(this);
        mLayImages.maxCount = 5;
        userName = SharedPreferencesUtils.getSharePrefString(ConstantKey.NickNameKey);
        repairs_name_tv.setText(SharePreCache.isEmpty(userName) ? "报修人： " + SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey) : "报修人: " + userName);
        date = TimeUtil.getYMD(new Date());
        repairs_time_tv.setText("报修时间: " + date);
    }

    @OnClick({R.id.title_back_img, R.id.department_manager_ll, R.id.right_text})
    public void clickEvent(View view) {
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.title_back_img:
                finish();
                ViewTools.hideSoftInput(this);
                break;
            case R.id.right_text:
                ViewTools.hideSoftInput(this);
                upload();
                break;
            case R.id.department_manager_ll:
//                showSpinWindow();
                ViewTools.hideSoftInput(this);
                selectType();
                break;
        }
    }

    private int index = 0;
    private OptionsPickerView optionsPickerView;

    private void selectType() {
        if (managerStr.size() > 0) {
            if (optionsPickerView == null) {
                optionsPickerView = new OptionsPickerView(this);
                optionsPickerView.setPicker((ArrayList) managerStr, index, 0, 0);
                optionsPickerView.setCyclic(false);
                optionsPickerView.setCancelable(true);
                optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                        repairsdepartment_manager_tv.setText(managerStr.get(options1));
                        dutyDept = managerId.get(options1);
                        index = options1;
                    }
                });
            }
            optionsPickerView.show();
        } else {
            ToastUtils.showShort("没有数据");
        }
    }

    private void upload() {
        if (isRequest()) {
            dialog.setMessage("正在提交...");
            dialog.show();
            final List<File> imgUrls = loadFile.uriToFile(mLayImages.getPaths()); //将图片URI转换成文件
            if(imgUrls.size()>0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadFile.uploadFile(imgUrls);
                    }
                }).start();
            }else{
                addOtherInfo("");
            }
        }
    }

    private boolean isRequest() {
        if (SharePreCache.isEmpty(date)) {
            ToastUtils.showShort("日期为空，不能提交");
            return false;
        }
        goodsName = repairsgoodsNameTv.getText().toString().trim();
        if (SharePreCache.isEmpty(goodsName)) {
            ToastUtils.showShort("请输入物品名称");
            return false;
        }
        address = repairsaddressTv.getText().toString().trim();
        if (SharePreCache.isEmpty(address)) {
            ToastUtils.showShort("请输入物品地址");
            return false;
        }
        manager = repairsdepartment_manager_tv.getText().toString().trim();
        if (SharePreCache.isEmpty(manager)) {
            ToastUtils.showShort("请选择部门");
            return false;
        }
        return true;
    }

    @Override
    public void onfileComplete(final Response response) {
        if (response.code() == 200) {
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                ImageUrls imageUrls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(), ImageUrls.class);
                String urls = "";
                if (imageUrls != null) {
                    String f0 = imageUrls.getFile0();
                    if (!SharePreCache.isEmpty(f0)) {
                        urls += f0 + ",";
                    }
                    String f1 = imageUrls.getFile1();
                    if (!SharePreCache.isEmpty(f1)) {
                        urls += f1 + ",";
                    }
                    String f2 = imageUrls.getFile2();
                    if (!SharePreCache.isEmpty(f2)) {
                        urls += f2 + ",";
                    }
                    String f3 = imageUrls.getFile3();
                    if (!SharePreCache.isEmpty(f3)) {
                        urls += f3 + ",";
                    }
                    String f4 = imageUrls.getFile4();
                    if (!SharePreCache.isEmpty(f4)) {
                        urls += f4 + ",";
                    }
                }
                if (!SharePreCache.isEmpty(urls)) {
                    urls = urls.substring(0, urls.length() - 1);
                    addOtherInfo(urls);
                } else {
                    ToastUtils.showShort("图片提交失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    ToastUtils.showShort("图片提交失败code=" + response.code());
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
                ToastUtils.showShort("图片提交失败fail");
            }
        });
    }

    private void addOtherInfo(String urls) {
        describ = repairsdescriContentTv.getText().toString().trim();
        String time = TimeUtil.getNowYMDHMSTime(new Date());
        NetworkRequest.request(new SubmitRepairsParams(time, goodsName, address, dutyDept, describ, urls), CommonUrl.add, Config.REREPAIRRELEASE);
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
                break;
            case Config.REREPAIRRELEASE:
                dialog.dismiss();
                setResult(RESULT_OK);
                finish();
                break;
            case Config.REPAIRDEPT:
                dialog.dismiss();
                JSONObject obj = (JSONObject) event.getData();
                List<DeptBean> beans = GsonUtils.toArray(DeptBean.class, obj);
                if (beans != null && beans.size() > 0) {
                    DeptNameAndIdBean bean = new DeptNameAndIdBean();
                    for (int i = 0; i < beans.size(); i++) {
                        managerStr.add(beans.get(i).getDeptName());
                        managerId.add(beans.get(i).getDeptId());
                    }
                    bean.setDeptNames(managerStr);
                    bean.setDeptIds(managerId);
                    BaseUtil.saveLocalFile(this, bean, Config.DEPTNAMEANDID);
                }
                break;
        }
    }
}
