package pro.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.LoadFile;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsDocumentSave;
import com.zjhz.teacher.NetworkRequests.response.ImageUrls;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMementDocumentSectionDic;
import com.zjhz.teacher.bean.ClasszMomentDocumentDicChild;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.popuwindow.DocumentSelectPopupWindow;
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
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pro.kit.ViewTools;
import pro.widget.BubblePopupWindow.BubblePopupWindow;

/**
 * 新增班级圈资源共享
 */
public class ClasszMomentsAddDocumentActivity extends BaseActivity implements View.OnTouchListener {

    @BindView(R.id.listView)
    ScrollViewWithListView listView;
    //提交按钮
    @BindView(R.id.right_text)
    TextView title_right_text;
    //想法编辑框
    @BindView(R.id.content_et)
    EditText content_et;
    @BindView(R.id.document_icon)
    ImageView document_icon;
    @BindView(R.id.document_name)
    TextView document_name;
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.document_label)
    ImageView document_label;

    private final static int SD = 1;
    private final static int PC = 2;
    //标签的默认颜色
//    private TagContainerLayout.ViewColor mDefaultViewColor;
    //标签选中颜色
//    private TagContainerLayout.ViewColor mClickViewColor;
    //上传成功的文件路径
    private String attacheUrl;
    //上传的文件名
    private String attacheName;
    //上传文件的验证码
    private String code;

    private ClasszMomentsDocumentSave params = new ClasszMomentsDocumentSave();

    //标签集合
    private List<ClasszMementDocumentSectionDic> dicChilds = new ArrayList<>();
    //标签标题字符列表
    private List<String> list = new ArrayList<>();
    //选中的标签字符列表
    private List<String> labels = new ArrayList<>();
    //已选中文件路径
    private ArrayList<String> docPaths = new ArrayList<>();
    private int sdOrPc = 2;
    private BubblePopupWindow popupWindow;

    private LoadFile loadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments_add_document);
        ButterKnife.bind(this);
        title_tv.setText(R.string.activity_classz_moments_add_document_title);
        title_right_text.setVisibility(View.VISIBLE);
        title_right_text.setText(R.string.activity_classz_moments_add_document_submit);
        getData();
        //解决滑动冲突
        content_et.setOnTouchListener(this);
        cancel.setVisibility(View.GONE);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String dcId = bundle.getString("dcId");
        String classId = bundle.getString("classId");
        params.setDcId(dcId);
        params.setClassId(classId);
        params.setSectionCode("1002");

        loadFile = new LoadFile(new LoadFile.FileCallBack() {
            @Override
            public void onfileComplete(Response response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    ImageUrls urls = GsonUtils.toObject(jsonObject.getJSONObject("data").toString(), ImageUrls.class);
                    if (urls != null) {
                        String f0 = urls.getFile0();
                        attacheUrl = f0;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                document_name.setText(attacheName);
                                cancel.setVisibility(View.VISIBLE);
                                setDocIcon(attacheName);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onfileErr(Request request) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("上传失败");
                        document_name.setText("请重新选择");
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void getData(){
        NetworkRequest.request(null,CommonUrl.SECTIONDIC,Config.SECTIONDIC);
    }

    @Override
    protected boolean isBindEventBusHere(){
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    if (docPaths.size() > 0) {
                        String file = docPaths.get(0);
                        List<File> imgUrls = loadFile.uriToFile(docPaths); //将图片URI转换成文件
                        if (imgUrls.size() > 0) {  //如果存在图片
                            long len = imgUrls.get(0).length();
                            if(len<=2*1024*1024) {
                                dialog.setMessage("正在提交");
                                dialog.show();
                                loadFile.uploadFile(imgUrls);
                                int length = file.split(File.separator).length;
                                attacheName = file.split(File.separator)[length - 1];
                            }else{
                                ToastUtils.showShort("文件太大，无法上传");
                            }
                        }
                    }
                }
                break;
        }
    }

    @OnClick({R.id.right_text,R.id.title_back_img,R.id.document_label,R.id.cancel})
    public void clickEvent(View v) {
        if(ViewTools.avoidRepeatClick(v)){
            return;
        }
        switch (v.getId()){
            case R.id.title_back_img:      //返回
                finish();
                break;
            case R.id.right_text:
                submit();
                break;
            case R.id.document_label:
                new DocumentSelectPopupWindow(ClasszMomentsAddDocumentActivity.this,
                        findViewById(R.id.doc_bottom), new DocumentSelectPopupWindow.SdOrPcUpLoad() {
                    @Override
                    public void sd() {
                        sdOrPc = SD;
                        String[] doc = {".doc", ".docx"};
                        String[] xls = {".xls", ".xlsx"};
                        String[] ppt = {".ppt", ".pptx"};
                        String[] pdf = {".pdf"};
                        String[] txt = {".txt"};
                        new FilePickerBuilder().setMaxCount(1)
                                .setSelectedFiles(docPaths)
                                .setActivityTheme(R.style.FilePickerTheme)
                                .addFileSupport("DOC", doc)
                                .addFileSupport("XLS", xls)
                                .addFileSupport("PPT", ppt)
                                .addFileSupport("PDF", pdf)
                                .addFileSupport("TXT", txt)
                                .enableDocSupport(false)
                                .pickFile(ClasszMomentsAddDocumentActivity.this);
                    }

                    @Override
                    public void pc() {
                        sdOrPc = PC;
                        startActivity(ClasszMomentsUploadFileWithWebActivity.class);
                    }
                });
                break;
            case R.id.cancel:
                attacheUrl = "";
                attacheName = "";
                document_name.setText("");
                cancel.setVisibility(View.GONE);
                document_label.setClickable(true);
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        dialog.dismiss();
        switch (event.getEventCode()) {
            case Config.ERROR:
            case Config.NOSUCCESS:
                ToastUtils.showShort("请求失败");
                break;
            case Config.SECTIONDIC:
                JSONObject o = (JSONObject) event.getData();
                List<ClasszMementDocumentSectionDic> dics = GsonUtils.toArray(ClasszMementDocumentSectionDic.class,o);
                if(dics!=null&&dics.size()>0){
                    dicChilds.clear();
                    dicChilds.addAll(dics);
                    initTagData();
                }
                break;
            case Config.GOT_UPLOAD_URL:
                if(event.getData() instanceof ClasszMomentsDocumentSave){
                    ClasszMomentsDocumentSave save = (ClasszMomentsDocumentSave)event.getData();
                    attacheUrl = save.getAttPath();
                    attacheName = save.getAttName();
                    code = save.getCode();
                    setDocIcon(attacheName);
                    document_name.setText(save.getAttName());
                    cancel.setVisibility(View.VISIBLE);
                    document_label.setClickable(false);
                }
                break;
            case Config.DOCUMENTSAVE:
                setResult(101);
                finish();
        }
    }

    private void setDocIcon(String fileName) {
        int length = fileName.length();
        if (fileName.substring(length - 3, length).equals("doc") || fileName.substring(length - 4, length).equals("docx"))
            document_icon.setImageResource(R.mipmap.word);
        else if (fileName.substring(length - 3, length).equals("xls") || fileName.substring(length - 4, length).equals("xlsx"))
            document_icon.setImageResource(R.mipmap.excl);
        else if (fileName.substring(length - 3, length).equals("ppt") || fileName.substring(length - 4, length).equals("pptx"))
            document_icon.setImageResource(R.mipmap.ppt);
        else if (fileName.substring(length - 3, length).equals("pdf"))
            document_icon.setImageResource(R.mipmap.pdf);
        else if (fileName.substring(length - 3, length).equals("txt"))
            document_icon.setImageResource(R.mipmap.txt);
    }


    /**
     * 从电脑导入提交
     */
    private void submit(){
        if (!isAllow()) {
            return;
        }

//        Log.d("attache", params.toString());
        NetworkRequest.request(params, CommonUrl.DOCUMENTSAVE, Config.DOCUMENTSAVE);
        dialog.setMessage("正在提交");
        dialog.show();
    }

    private boolean isAllow() {
        if(labels.size()>0){ //判断类型是否选择
            StringBuilder keywords = new StringBuilder();
            for(String str:labels){
                for(ClasszMementDocumentSectionDic dic:dicChilds){
                    List<ClasszMomentDocumentDicChild> list = dic.getSectionDicChildren();
                    for(ClasszMomentDocumentDicChild child:list) {
                        if (child.getParamValue().equals(str)) {
                            keywords.append(child.getParamKey() + ",");
                            break;
                        }
                    }
                }
            }
            if(labels.size() == dicChilds.size()) {
                params.setKeywords(keywords.substring(0, keywords.length() - 1));
            }else{
                ToastUtils.showShort("类型没有选择完全！");
                return false;
            }
        }else{
            ToastUtils.showShort("请选择类型！");
            return false;
        }
        String content = content_et.getText().toString();
        if(!SharePreCache.isEmpty(content)){  //判断标题是否输入
            params.setCcdContent(content);
        }else{
            ToastUtils.showShort("请输入内容！");
            return false;
        }
        //判断上传是否成功
        if (SharePreCache.isEmpty(attacheUrl)||SharePreCache.isEmpty(attacheName)) {
            ToastUtils.showShort("文件信息错误");
            return false;
        }else{
            params.setAttName(attacheName);
            params.setAttPath(attacheUrl);
//            params.setCode(code);
        }
        return true;
    }


    /**
     * 初始化Tag相关数据和组件
     */
    private void initTagData(){
       /* mDefaultViewColor=new TagContainerLayout.ViewColor(ContextCompat.getColor(this,
                R.color.classz_moments_add_document_tag_background),
                ContextCompat.getColor(this,R.color.gray),
                ContextCompat.getColor(this,R.color.classz_moments_add_document_tag_name));
        mClickViewColor=new TagContainerLayout.ViewColor(ContextCompat.getColor(this,
                R.color.classz_moments_add_document_tag_background_click),
                ContextCompat.getColor(this,R.color.gray),
                ContextCompat.getColor(this,R.color.white));*/
        listView.setDivider(null);
        listView.setAdapter(new CommonAdapter<ClasszMementDocumentSectionDic>(this,R.layout.activity_classz_moments_add_document_add_list_item,dicChilds) {
            @Override
            protected void convert(ViewHolder viewHolder, ClasszMementDocumentSectionDic item, int position) {
                viewHolder.setText(R.id.title,item.getParamValue());
                final ScrollViewWithGridView containerLayout = viewHolder.getView(R.id.listview);
                containerLayout.setHorizontalSpacing((int)getResources().getDimension(R.dimen.dp_20));
                containerLayout.setVerticalSpacing((int)getResources().getDimension(R.dimen.dp_10));
                containerLayout.setTag(position);
                containerLayout.setAdapter(new CommonAdapter<ClasszMomentDocumentDicChild>(ClasszMomentsAddDocumentActivity.this,R.layout.mmc_textview,item.getSectionDicChildren()) {
                    @Override
                    protected void convert(ViewHolder viewHolder, final ClasszMomentDocumentDicChild item, final int position) {
                        viewHolder.setText(R.id.text_tv,item.getParamValue());
//                        viewHolder.setBackgroundRes(R.id.text_tv,R.drawable.btn_rectangle_shape_gray);
                        viewHolder.setOnClickListener(R.id.text_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for(int i = 0;i<containerLayout.getCount();i++){
                                    TextView tv = (TextView)containerLayout.getChildAt(i);
                                    if(tv!=v) {
                                        tv.setTextColor(getResources().getColor(R.color.gray9));
                                        int p1 = (int) containerLayout.getTag();
                                        tv.setText(dicChilds.get(p1).getSectionDicChildren().get(i).getParamValue());
//                                        SpannableString sp = new SpannableString("");
//                                        tv.setBackgroundResource(R.drawable.btn_rectangle_shape_gray);
                                        labels.remove(dicChilds.get(p1).getSectionDicChildren().get(i).getParamValue());
                                    }
                                }
                                TextView tv = (TextView)v;
                                if(labels.contains(item.getParamValue())){
                                    labels.remove(item.getParamValue());
                                    tv.setTextColor(getResources().getColor(R.color.gray9));
                                    tv.setText(item.getParamValue());
//                                    tv.setBackgroundResource(R.drawable.btn_rectangle_shape_gray);
                                }else{
                                    labels.add(item.getParamValue());
                                    ViewTools.tvShowCheck(tv,item.getParamValue());
//                                    tv.setBackgroundResource(R.drawable.btn_rectangle_shape_little_green);
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    /*//自定义TagView的监听
    class MyTagListener implements TagView.OnTagClickListener{

        private List<TagView> tagViews;

        public MyTagListener(List<TagView> tagViews) {
            this.tagViews = tagViews;
        }

        @Override
        public void onTagClick(TagView view, int position, String text) {
            tagClick(view,tagViews,text);
        }

        @Override
        public void onTagLongClick(int position, String text) {

        }

        @Override
        public void onTagCrossClick(int position) {

        }
    }*/

    /**
     * 标签选择器的点击公用方法
     * 对没有点击的都恢复默认
     * @param view
     * @param tagViews
     */
   /* private void tagClick(TagView view , List<TagView> tagViews,String text) {
        if (view.getEnabled()==true){
            view.playSoundEffect(SoundEffectConstants.CLICK);
            if (!view.getIsClick()){
                for (TagView tagView : tagViews) {
                    if (tagView.getEnabled()==true) {
                        tagView.setTagViewColor(mDefaultViewColor);
                        labels.remove(tagView.getText());
                        tagView.setIsClick(false);
                    }
                }
                view.setTagViewColor(mClickViewColor);
                labels.add(text);
                view.setIsClick(true);
            }else{
                view.setTagViewColor(mDefaultViewColor);
                view.setIsClick(false);
                labels.remove(text);
            }
        }
    }*/

    /**
     * 初始化Tag的默认颜色
     * @param
     */
   /* private void initTags(List<TagView> mChildViews) {
        for (int i=0;i<mChildViews.size();i++){
            mChildViews.get(i).setTagViewColor(mDefaultViewColor);
            mChildViews.get(i).postInvalidate();
        }
    }*/

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.content_et && canVerticalScroll(content_et))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }
}
