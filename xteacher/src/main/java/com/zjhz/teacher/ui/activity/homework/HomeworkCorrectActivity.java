package com.zjhz.teacher.ui.activity.homework;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsMomentSave;
import com.zjhz.teacher.NetworkRequests.response.HomeworkUploadDetailBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.AudioBean;
import com.zjhz.teacher.bean.StudentBean;
import com.zjhz.teacher.databinding.ActivityHomeworkCorrectBinding;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.ui.view.popuwindow.SharePopupWindow;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.media.AudioPlayerView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.widget.NineGridLayout.HoldScaleImageView;
import pro.widget.NineGridLayout.NineGridlayout;

public class HomeworkCorrectActivity extends BaseActivity {

    private ActivityHomeworkCorrectBinding binding;
    private HomeworkUploadDetailBean detailBean;
    private StudentBean studentBean;
    private String homeworkId, homeworkType;
    private OptionsPickerView<String> optionsPickerView;

    private SharePopupWindow popupWindow;

    @BindView(R.id.title_back_img)
    TextView backTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView right_text;
    @BindView(R.id.homework_level)
    TextView levelTv;
    @BindView(R.id.student_header_img)
    CircleImageView headerImg;
    @BindView(R.id.pic_grid)
    NineGridlayout pic_grid;
    @BindView(R.id.image_view)
    HoldScaleImageView image_view;
    @BindView(R.id.homework_share)
    ImageView shareImg;
    @BindView(R.id.audio_play_list)
    ListView audioListView;

    private List<AudioBean> audioPath = new ArrayList<>();
    private CommonAdapter audioPlayerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_homework_correct);
        ButterKnife.bind(this);

        studentBean = (StudentBean) getIntent().getExtras().getSerializable("bean");
        homeworkId = getIntent().getExtras().getString("homeworkId");
        homeworkType = getIntent().getExtras().getString("homeworkType");

        right_text.setVisibility(View.VISIBLE);
        right_text.setText("提交");
        titleTv.setText(studentBean.getName()+"的作业");

        GlideUtil.loadImageHead(studentBean.getPhotoUrl(), headerImg);

        getHomeworkUploadDetail();

        popupWindow = new SharePopupWindow(this, shareImg);

        audioPlayerAdapter = new CommonAdapter<AudioBean>(this, R.layout.audio_player_list_item, audioPath) {
            @Override
            protected void convert(ViewHolder viewHolder, AudioBean item, int position) {
                AudioPlayerView audioPlayerView = viewHolder.getView(R.id.audio_play_view);
                audioPlayerView.setAudioPath(item.attPath);
                audioPlayerView.setAudioLength(item.time);
                audioPlayerView.setCanDelete(false);
            }
        };
        audioListView.setAdapter(audioPlayerAdapter);
    }

    @OnClick({R.id.title_back_img, R.id.right_text, R.id.homework_level, R.id.homework_share})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                correctHomework();
                break;
            case R.id.homework_level:
                selectLevel();
                break;
            case R.id.homework_share:
                popupWindow.setShare(new SharePopupWindow.Share() {
                    @Override
                    public void bjq() {
                        shareToBjq();
                    }
                }).show();

                break;
        }
    }

    private void shareToBjq(){
        if(detailBean == null)
            return;
        ClasszMomentsMomentSave params  = new ClasszMomentsMomentSave();
        params.setClassId(studentBean.getClassId());
//        params.setDcId(dcId);
        params.setSectionCode("1001");
        params.setCcdContent(detailBean.getContent());
        String imgs = "";
        if(detailBean.getImgList() != null) {
            for (HomeworkUploadDetailBean.ImgListBean bean : detailBean.getImgList()) {
                imgs += bean.getDocPath() + ",";
            }
        }
        params.setPicUrls(imgs);
        params.setType("2");//0	原生;  1 成长日记;  2	电子作业
        params.setIsshare("1");
        params.setParentId(detailBean.getTaskId());

        if(detailBean.getVoiceList() != null && detailBean.getVoiceList().size() > 0){
            List<AudioBean> audioBeanList = new ArrayList<>();
            for(HomeworkUploadDetailBean.voiceListBean bean : detailBean.getVoiceList()) {
                AudioBean audio = new AudioBean(bean.getAttPath(), bean.getRemarks());
                audioBeanList.add(audio);
            }
            params.setVoiceList(audioBeanList);
        }

        NetworkRequest.request(params, CommonUrl.MOMENTSAVE, Config.MOMENTSAVE);
//        Map<String, String> params = new HashMap<>();
//        params.put("content", detailBean.getContent());
//        params.put("studentId", studentBean.getStudentId());
//        String imgs = "";
//        for(HomeworkUploadDetailBean.ImgListBean bean :detailBean.getImgList()){
//            imgs += bean.getDocPath()+",";
//        }
//        params.put("picUrls", imgs);
////        if (checkBox.isChecked()) {
////            params.put("flag", "1");
////        }
//        if (!TextUtils.isEmpty(detailBean.getContent()) && TextUtils.isEmpty(imgs)) {
//            params.put("type", "SYS_HOMEWORK_TYPE_1");//全文字
//        }
//        if (TextUtils.isEmpty(detailBean.getContent()) && !TextUtils.isEmpty(imgs)) {
//            params.put("type", "SYS_HOMEWORK_TYPE_2");//图片
//        }
//        if (!TextUtils.isEmpty(detailBean.getContent()) && !TextUtils.isEmpty(imgs)) {
//            params.put("type", "SYS_HOMEWORK_TYPE_3");//图文结合
//        }
//        NetworkRequest.request(params, CommonUrl.GROWTH_DAILY_ADD, "GROWTH_DAILY_ADD");
    }

    private void selectLevel(){
        if (optionsPickerView == null) {
            final ArrayList<String> levelList = new ArrayList<>();
            levelList.add("优");
            levelList.add("良");
            levelList.add("中");
            levelList.add("及格");
            levelList.add("不及格");

            optionsPickerView = new OptionsPickerView<String>(this);
            optionsPickerView.setPicker(levelList, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2, int options3, int options4) {
                    levelTv.setText(levelList.get(options1));
                }
            });
        }
        optionsPickerView.show();
    }

    private void getHomeworkUploadDetail(){
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("studentId", studentBean.getStudentId());
        map.put("homeworkId", homeworkId);
        map.put("homeworkType", homeworkType);
        map.put("appFlag", "1");//0 家长端 1教师端
        NetworkRequest.request(map, CommonUrl.HOMEWORK_UPLOAD_DETAIL, "homework_upload_detail");
    }

    private void correctHomework(){
        if(detailBean.getEditFlag() != 1){
            ToastUtils.showShort("发布作业老师才可以批改作业");
            return;
        }
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("taskId",detailBean.getTaskId());//作业详情id
        map.put("appFlag","1");//0 家长端 1教师端
        map.put("praiseUser", SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey));//教师id
        map.put("praiseLevel", detailBean.getPraiseLevel());//评分等级
        map.put("praiseContent", detailBean.getPraiseContent());//评价内容

        NetworkRequest.request(map, CommonUrl.HOMEWORK_CORRECT, "homework_correct");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Subscribe
    public void onEventMainThread(EventCenter event){
        dialog.dismiss();
        switch (event.getEventCode()){
            case "homework_upload_detail":
                detailBean = GsonUtils.toObject((JSONObject) event.getData(), HomeworkUploadDetailBean.class);
                if(detailBean != null) {
                    detailBean.setPraiseUserName(studentBean.getName());
                    binding.setWorkDetailBean(detailBean);
                    showPic(detailBean.getImgList());
//                    if(detailBean.getEditFlag() != 1){
//                        right_text.setVisibility(View.GONE);
//                    }
                    if (detailBean.getVoiceList() != null ) {
                        for (HomeworkUploadDetailBean.voiceListBean bean : detailBean.getVoiceList()) {
                            audioPath.add(new AudioBean(bean.getAttPath(), bean.getRemarks()));
                        }
                    }
                    audioPlayerAdapter.notifyDataSetChanged();
                }
                break;
            case "homework_correct":
                ToastUtils.showShort("批改成功");
                finish();
                break;
            case Config.MOMENTSAVE:
                ToastUtils.showShort("分享成功");
                break;
        }
    }

    private void showPic(List<HomeworkUploadDetailBean.ImgListBean> beanList) {
        final List<String> imgList = new ArrayList<>();
        if(beanList != null) {
            for (HomeworkUploadDetailBean.ImgListBean imgListBean: beanList) {
                imgList.add(imgListBean.getDocPath());
            }
        }else{
            pic_grid.setVisibility(View.GONE);
            image_view.setVisibility(View.GONE);
        }
        if (imgList.size() == 1) {
            pic_grid.setVisibility(View.GONE);
            image_view.setVisibility(View.VISIBLE);
            image_view.setImageUrl(imgList.get(0));
            image_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] urls = {imgList.get(0)};
                    ImageGalleryActivity.show(v.getContext(), urls, 0, false);
                }
            });
        }else if(imgList.size()>1) {
            pic_grid.setVisibility(View.VISIBLE);
            image_view.setVisibility(View.GONE);
            pic_grid.setImagesData(imgList);
            pic_grid.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, View view) {
                    String[] urls = new String[imgList.size()];
                    for (int i = 0; i < imgList.size(); i++) {
                        urls[i] = imgList.get(i);
                    }
                    ImageGalleryActivity.show(view.getContext(), urls, position, false);
                }
            });
        }
    }
}
