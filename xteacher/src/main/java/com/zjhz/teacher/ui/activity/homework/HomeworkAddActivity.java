package com.zjhz.teacher.ui.activity.homework;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.EditWorkParams;
import com.zjhz.teacher.NetworkRequests.response.ClassesBeans;
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.NetworkRequests.response.HomeworkListBeanRes;
import com.zjhz.teacher.NetworkRequests.response.ImageBean;
import com.zjhz.teacher.NetworkRequests.response.SubjectBeans;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewer;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeworkAddActivity extends BaseActivity implements LoadFile.FileCallBackNew {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_text)
    TextView rightText;
    @BindView(R.id.homework_title_ed)
    EditText titleEd;
    @BindView(R.id.homework_duration)
    TextView durationTv;
    @BindView(R.id.homework_form_layout)
    LinearLayout formLayout;
    @BindView(R.id.qwz_layout)
    LinearLayout qwzLayout;
    @BindView(R.id.qwz_tv)
    TextView qwzTv;
    @BindView(R.id.qwz_img)
    ImageView qwzImg;
    @BindView(R.id.zp_layout)
    LinearLayout zpLayout;
    @BindView(R.id.zp_tv)
    TextView zpTv;
    @BindView(R.id.zp_img)
    ImageView zpImg;
    @BindView(R.id.twjh_layout)
    LinearLayout twjhLayout;
    @BindView(R.id.twjh_tv)
    TextView twjhTv;
    @BindView(R.id.twjh_img)
    ImageView twjhImg;
    @BindView(R.id.yy_layout)
    LinearLayout yyLayout;
    @BindView(R.id.yy_tv)
    TextView yyTv;
    @BindView(R.id.yy_img)
    ImageView yyImg;
    @BindView(R.id.deadline_tv)
    TextView deadLineTv;
    @BindView(R.id.homework_content)
    EditText contentEd;
    @BindView(R.id.homework_subject_grid)
    ScrollViewWithGridView subjectGrid;
    @BindView(R.id.homework_class_grid)
    ScrollViewWithGridView classGrid;
    @BindView(R.id.homework_picture)
    PicturesPreviewer picturesView;
    @BindView(R.id.sync_czda)
    CheckBox syncCzdaCk;
    @BindView(R.id.homework_title)
    LinearLayout homeworkTitle;
    @BindView(R.id.homework_form_ll)
    LinearLayout homeworkFormLL;

    private EditWorkParams editWorkParams = new EditWorkParams();
    private LoadFile loadFile;

    private List<SubjectBeans> subjectBeansList = new ArrayList<>();
    private List<List<ClassesBeans>> classBeansLists = new ArrayList<>();
    private List<ClassesBeans> classesBeans = new ArrayList<>();

    private CommonAdapter subjectAdapter, classAdapter;

    private int subjectPosition = 0;

    private HomeworkListBeanRes listBeanRes;
    private HomeWorkBean detailBean = new HomeWorkBean();

    private String homeworkNature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_add);
        ButterKnife.bind(this);

        classBeansLists.add(new ArrayList<ClassesBeans>());

        if(getIntent().getExtras() != null) {
            homeworkNature = getIntent().getExtras().getString("nature");
            int type = getIntent().getExtras().getInt("type", -1);
            if (type == 1) {  //1表示修改
                getDetail();
            }
        }
        initView();
    }

    private void getDetail(){
        listBeanRes = (HomeworkListBeanRes) getIntent().getExtras().getSerializable("bean");
        if(listBeanRes == null)
            return;
        Map<String, String> map = new HashMap<>();
        map.put("homeworkId", listBeanRes.getHomeworkId());
        NetworkRequest.request(map, CommonUrl.HOMEWORK_PUBLISH_DETAIL, "homework_detail");
    }

    private void bindData(){
        if("SYS_HOMEWORK_NATURE_1".equals(detailBean.getHomeworkNature())){
            homeworkNature = "SYS_HOMEWORK_NATURE_1";
            initSMWork();
            titleTv.setText("书面作业");
        }else {
            titleTv.setText("电子作业");
        }
        titleEd.setText(detailBean.getHomeworkName());
        durationTv.setText(detailBean.getCostTime());
        deadLineTv.setText(detailBean.getDeliverTime());
        contentEd.setText(detailBean.getContent());
        switch (detailBean.getHomeworkType()){
            case "SYS_HOMEWORK_TYPE_1":
                qwzLayout.performClick();
                break;
            case "SYS_HOMEWORK_TYPE_2":
                zpLayout.performClick();
                break;
            case "SYS_HOMEWORK_TYPE_3":
                twjhLayout.performClick();
                break;
            case "SYS_HOMEWORK_TYPE_4":
                yyLayout.performClick();
                break;
        }
        List<String> imgList = new ArrayList<>();
        for(ImageBean imageBean: detailBean.getImgs()){
            imgList.add(imageBean.getDocPath());
        }

        picturesView.setHasLoadPicture(imgList);
    }

    private void bindClassList(List<ClassesBeans> link, List<ClassesBeans> classesBeansList){
        if(classesBeansList == null || classesBeansList.size() == 0 || link == null || link.size() == 0)
            return;
        int i = 0;
        for(ClassesBeans beans1 : classesBeansList){
            for(ClassesBeans beans2 : link){
                if(beans1.getClassId().equals(beans2.getClassId())){
                    classGrid.performItemClick(null, i, 0);
                    break;
                }
            }
            i++;
        }
    }

    //书面作业
    private void initSMWork(){
        homeworkTitle.setVisibility(View.GONE);
        homeworkFormLL.setVisibility(View.GONE);
        syncCzdaCk.setVisibility(View.GONE);
    }

    private void initView(){
        if("SYS_HOMEWORK_NATURE_1".equals(homeworkNature)){
            initSMWork();
            titleTv.setText("书面作业");
        }else {
            titleTv.setText("电子作业");
        }

        rightText.setVisibility(View.VISIBLE);
        rightText.setText("发布");
        loadFile = new LoadFile(this, this);
        picturesView.maxCount=5;//最多5张

        editWorkParams.setHomeworkType("SYS_HOMEWORK_TYPE_3");//默认图文结合

        subjectGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(SubjectBeans subjectBeans : subjectBeansList){
                    subjectBeans.setCheck(false);
                }
                subjectBeansList.get(position).setCheck(true);
                subjectPosition = position;
                if(classBeansLists.get(position).size() == 0 ) {
                    getClassData(subjectBeansList.get(position).getSubjectId(), position);
                }else {
                    classesBeans.clear();
                    classesBeans.addAll(classBeansLists.get(position));
                    classAdapter.notifyDataSetChanged();
                }
                editWorkParams.setSubjectId(subjectBeansList.get(position).getSubjectId());
                subjectAdapter.notifyDataSetChanged();
            }
        });

        subjectAdapter = new CommonAdapter<SubjectBeans>(this,R.layout.homework_add_subject_class_grid_item,subjectBeansList) {
            @Override
            protected void convert(final ViewHolder viewHolder, final SubjectBeans item, final int position) {
                if(viewHolder == null || item == null)
                    return;
                viewHolder.setText(R.id.name, item.getName() == null ? "" : item.getName());
                if(subjectBeansList.get(position).isCheck()){
                    viewHolder.setTextColorRes(R.id.name, R.color.title_background_red);
                    viewHolder.setVisible(R.id.img, true);
                }else {
                    viewHolder.setTextColorRes(R.id.name, R.color.gray6);
                    viewHolder.setVisible(R.id.img, false);
                }
//                viewHolder.setOnClickListener(R.id.layout, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        changeColor(subjectGrid);
//                        viewHolder.setTextColorRes(R.id.name, R.color.title_background_red);
//                        viewHolder.setVisible(R.id.img, true);
//                        subjectPosition = position;
//                        if(classBeansLists.get(position).size() == 0 ) {
//                            getClassData(item.getSubjectId(), position);
//                        }else {
//                            classesBeans.clear();
//                            classesBeans.addAll(classBeansLists.get(position));
//                            classAdapter.notifyDataSetChanged();
//                        }
//                        editWorkParams.setSubjectId(item.getSubjectId());
//                    }
//                });
            }
        };

        subjectGrid.setAdapter(subjectAdapter);

        classGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean check = classBeansLists.get(subjectPosition).get(position).isChecd();
                classBeansLists.get(subjectPosition).get(position).setChecd(!check);
                classAdapter.notifyDataSetChanged();
            }
        });
        classAdapter = new CommonAdapter<ClassesBeans>(this,R.layout.homework_add_subject_class_grid_item, classesBeans) {
            @Override
            protected void convert(final ViewHolder viewHolder, final ClassesBeans item, final int position) {
                if(viewHolder == null || item == null)
                    return;
                viewHolder.setText(R.id.name, item.getName() == null ? "" : item.getClassName());
//                viewHolder.setOnClickListener(R.id.layout, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean check = classBeansLists.get(subjectPosition).get(position).isChecd();
//                        classBeansLists.get(subjectPosition).get(position).setChecd(!check);
//
//                        if(classBeansLists.get(subjectPosition).get(position).isChecd()){
//                            viewHolder.setTextColorRes(R.id.name, R.color.title_background_red);
//                            viewHolder.setVisible(R.id.img, true);
//                        }else {
//                            viewHolder.setTextColorRes(R.id.name, R.color.gray6);
//                            viewHolder.setVisible(R.id.img, false);
//                        }
//                    }
//                });
                if(classBeansLists.get(subjectPosition).get(position).isChecd()){
                    viewHolder.setTextColorRes(R.id.name, R.color.title_background_red);
                    viewHolder.setVisible(R.id.img, true);
                }else {
                    viewHolder.setTextColorRes(R.id.name, R.color.gray6);
                    viewHolder.setVisible(R.id.img, false);
                }
            }
        };

        classGrid.setAdapter(classAdapter);

        getSubjectData();
    }

    @OnClick({R.id.title_back_img, R.id.right_text, R.id.qwz_layout, R.id.zp_layout, R.id.twjh_layout, R.id.yy_layout, R.id.deadline_tv})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.right_text:
                submit();
                break;
            case R.id.qwz_layout:
                changeColor(formLayout);
                qwzTv.setTextColor(getResources().getColor(R.color.title_background_red));
                qwzImg.setVisibility(View.VISIBLE);
//                contentEd.setVisibility(View.VISIBLE);
//                picturesView.setVisibility(View.GONE);
                editWorkParams.setHomeworkType("SYS_HOMEWORK_TYPE_1");
                break;
            case R.id.zp_layout:
                changeColor(formLayout);
                zpTv.setTextColor(getResources().getColor(R.color.title_background_red));
                zpImg.setVisibility(View.VISIBLE);
//                contentEd.setVisibility(View.GONE);
//                picturesView.setVisibility(View.VISIBLE);
                editWorkParams.setHomeworkType("SYS_HOMEWORK_TYPE_2");
                break;
            case R.id.twjh_layout:
                changeColor(formLayout);
                twjhTv.setTextColor(getResources().getColor(R.color.title_background_red));
                twjhImg.setVisibility(View.VISIBLE);
//                contentEd.setVisibility(View.VISIBLE);
//                picturesView.setVisibility(View.VISIBLE);
                editWorkParams.setHomeworkType("SYS_HOMEWORK_TYPE_3");
                break;
            case R.id.yy_layout:
                changeColor(formLayout);
                yyTv.setTextColor(getResources().getColor(R.color.title_background_red));
                yyImg.setVisibility(View.VISIBLE);
                editWorkParams.setHomeworkType("SYS_HOMEWORK_TYPE_4");
                break;
            case R.id.deadline_tv:
                BaseUtil.selectDateYMDHM(this,deadLineTv);
                break;

        }
    }

    private void submit() {
        final List<File> imgUrls = loadFile.uriToFile(picturesView.getPaths()); //将图片URI转换成文件
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
    }

    private void addWorkInfo() {
        if(!checkSubmitData()){
            return;
        }

        editWorkParams.setHomeworkName(titleEd.getText().toString());
        editWorkParams.setContent(contentEd.getText().toString());
        editWorkParams.setCostTime(durationTv.getText().toString());
        editWorkParams.setDeliverTime(deadLineTv.getText().toString());
        if(!TextUtils.isEmpty(homeworkNature))
            editWorkParams.setHomeworkNature(homeworkNature);

        String classIds = "";
        for(ClassesBeans classesBeans : classBeansLists.get(subjectPosition)){
            if(classesBeans.isChecd())
                classIds += classesBeans.getClassId()+",";
        }
        if(classIds.length() > 0)
            classIds = classIds.substring(0, classIds.length() - 1);
        editWorkParams.setClassIds(classIds);
//        editWorkParams.setCostTime();
        if (listBeanRes != null && !TextUtils.isEmpty(listBeanRes.getHomeworkId())){//修改
            editWorkParams.setHomeworkId(listBeanRes.getHomeworkId());
            NetworkRequest.request(editWorkParams, CommonUrl.modifyPublish, "releaseWorkInfo");
        }
        else {
            if(syncCzdaCk.isChecked())
                editWorkParams.setShareFlag("2");//同步到成长档案
            NetworkRequest.request(editWorkParams, CommonUrl.homeworkPublish, "releaseWorkInfo");
        }
    }

    private boolean checkSubmitData(){
        if(TextUtils.isEmpty(titleEd.getText().toString()) && !"SYS_HOMEWORK_NATURE_1".equals(homeworkNature)){
            ToastUtils.showShort("标题不能为空");
            return false;
        }
        if(TextUtils.isEmpty(editWorkParams.getSubjectId())){
            ToastUtils.showShort("科目不能为空");
            return false;
        }
        if(classBeansLists.get(subjectPosition).size() == 0){
            ToastUtils.showShort("班级不能为空");
            return false;
        }
        if("请选择时间".equals(deadLineTv.getText().toString())){
            ToastUtils.showShort("截止时间不能为空");
            return false;
        }
        if(TextUtils.isEmpty(contentEd.getText().toString()) && TextUtils.isEmpty(editWorkParams.getImages())){
            ToastUtils.showShort("作业内容不能为空");
            return false;
        }
        return true;
    }

    //还原作业形式选择颜色
    private void changeColor(ViewGroup viewGroup){
        for(int i = 0; i < viewGroup.getChildCount(); i++){
            if(viewGroup.getChildAt(i) instanceof ViewGroup){
                changeColor((ViewGroup) viewGroup.getChildAt(i));
            }
            if(viewGroup.getChildAt(i) instanceof TextView){
                ((TextView)viewGroup.getChildAt(i)).setTextColor(getResources().getColor(R.color.gray6));
            }
            if(viewGroup.getChildAt(i) instanceof ImageView){
                viewGroup.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void getSubjectData() {
        Map<String, String> map = new HashMap<>();
        map.put("subjectNature", "0");//0必修  1选修
        NetworkRequest.request(map, CommonUrl.homeworkSubjectList, "subject_list");
    }
    private void getClassData(String subjectId, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("subjectId", subjectId);
        NetworkRequest.request(map, CommonUrl.HOMEWORK_ADD_CLASS,"class_List"+position);
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }


    @Subscribe
    public void onEventMainThread(EventCenter event) {
        if(event.getEventCode() ==  null){
            return;
        }
        switch (event.getEventCode()){
            case "subject_list":
                    subjectBeansList.addAll(GsonUtils.toArray(SubjectBeans.class, (JSONObject)event.getData()));
                    subjectAdapter.notifyDataSetChanged();
                    if(subjectBeansList.size() > 0) {
                        for (int i = 0; i < subjectBeansList.size(); i++) {
                            classBeansLists.add(new ArrayList<ClassesBeans>());
                        }
                        if(listBeanRes != null){
                            int i = 0;
                            for(SubjectBeans subjectBeans: subjectBeansList){
                                if (listBeanRes.getSubjectId().equals(subjectBeans.getSubjectId())) {
                                    break;
                                }
                                i++;
                            }
                            subjectGrid.performItemClick(null, i, 0);
                        }else {
                            subjectGrid.performItemClick(null, 0, 0);
                        }
                    }
                break;
            case "releaseWorkInfo":
                ToastUtils.showShort("发布成功");
                setResult(RESULT_OK);
                finish();
                break;
            case "homework_detail":
                detailBean = GsonUtils.toObject((JSONObject) event.getData(), HomeWorkBean.class);
                if(detailBean != null) {
                    bindData();
                    if(classBeansLists.size() > subjectPosition)
                        bindClassList(detailBean.getLink(), classBeansLists.get(subjectPosition));
                }
                break;
        }
        if(event.getEventCode().contains("class_List")){
            subjectPosition = Integer.parseInt(event.getEventCode().substring("class_List".length()));
            classBeansLists.set(subjectPosition, GsonUtils.toArray(ClassesBeans.class, (JSONObject)event.getData()));
            classesBeans.clear();
            classesBeans.addAll(GsonUtils.toArray(ClassesBeans.class, (JSONObject)event.getData()));
            classAdapter.notifyDataSetChanged();

            if(getIntent().getExtras().getInt("type", -1) != 1)//修改就不要默认点击第一项了
                classGrid.performItemClick(null, 0, 0);
            bindClassList(detailBean.getLink(), classBeansLists.get(subjectPosition));
        }
    }


    @Override
    public void onFileComplete(String urls) {
        editWorkParams.setImages(urls);
        addWorkInfo();
    }

    @Override
    public void onFileErr(Request request) {

    }
}
