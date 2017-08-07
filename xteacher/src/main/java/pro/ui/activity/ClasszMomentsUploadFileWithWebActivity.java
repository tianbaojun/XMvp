package pro.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsDocumentSave;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseActivity;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMomentGetCode;
import com.zjhz.teacher.bean.UpLoadSucBean;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.uk.co.senab.photoview.Bimp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.kit.ViewTools;

/**
 * 从电脑导入文件
 */
public class ClasszMomentsUploadFileWithWebActivity extends BaseActivity {

    //刷新校验码按钮
    @BindView(R.id.refresh_code)
    TextView refresh_code;
    //url展示tv
    @BindView(R.id.url)
    TextView url;
    //校验码
    @BindView(R.id.check_code)
    TextView check_code;
    @BindView(R.id.before)
    LinearLayout before;
    @BindView(R.id.after)
    RelativeLayout after;
    //图标
    @BindView(R.id.document_icon)
    ImageView document_icon;
    //文件名
    @BindView(R.id.document_name)
    TextView document_name;
    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.upload_status)
    ImageView upload_status;
    //上传成功的文件路径
    private String attacheUrl;
    //上传的文件名
    private String attacheName;
    //上传文件的验证码
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classz_moments_upload_file_with_web);
        ButterKnife.bind(this);
        title.setText("电脑导入");
        NetworkRequest.request(null, CommonUrl.GETCODE, Config.GETCODE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attacheName = null;
                attacheUrl = null;
                code = null;
                before.setVisibility(View.VISIBLE);
                after.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @OnClick({R.id.title_back_img,R.id.submit,R.id.refresh_code})
    public void clickEvent(View view){
        if (ViewTools.avoidRepeatClick(view)) {
            return;
        }
        Bimp.tempSelectBitmap=new ArrayList<>();
        switch (view.getId()){
            case R.id.title_back_img:
                finish();
                break;
            case R.id.refresh_code:
                NetworkRequest.request(null, CommonUrl.GETCODE, Config.GETCODE);
                break;
            case R.id.submit:
                if(code!=null&&code.length()>0){
                    if(attacheName!=null&&attacheName.length()>0){
                        if(attacheUrl!=null&&attacheUrl.length()>0){
                            ClasszMomentsDocumentSave save = new ClasszMomentsDocumentSave();
                            save.setCode(code);
                            save.setAttPath(attacheUrl);
                            save.setAttName(attacheName);

                            EventBus.getDefault().post(new EventCenter(Config.GOT_UPLOAD_URL,save));
                            finish();
                            break;
                        }
                    }
                }
                ToastUtils.showShort("还没有上传成功");
                break;

        }
    }

    @Subscribe
    public void onEventMainThread(EventCenter center){
        switch(center.getEventCode()){
            case "noSuccess":
                ToastUtils.showShort(R.string.lassz_moments_upload_document_with_web_faild_to_refresh_check_code);
                break;
            case Config.GETCODE:
                JSONObject js = (JSONObject)center.getData();
                ClasszMomentGetCode code = GsonUtils.toObjetJson(js, ClasszMomentGetCode.class);
                if(code != null){
                    check_code.setText(code.getCode());
                }
            case Config.UPLOAD_SUC:
                if(center.getData() instanceof UpLoadSucBean) {
                    UpLoadSucBean bean = (UpLoadSucBean) center.getData();
                    attacheName = bean.getAttName();
                    attacheUrl = bean.getAttPath();
                    this.code = bean.getCode();
                    before.setVisibility(View.GONE);
                    after.setVisibility(View.VISIBLE);
                    switch(bean.getFileFormat()){
                        case WORD:
                            document_icon.setImageResource(R.mipmap.word);
                            break;
                        case EXCEL:
                            document_icon.setImageResource(R.mipmap.excl);
                            break;
                        case PPT:
                            document_icon.setImageResource(R.mipmap.ppt);
                            break;
                        case PDF:
                            document_icon.setImageResource(R.mipmap.pdf);
                            break;
                        case TXT:
                            document_icon.setImageResource(R.mipmap.txt);
                            break;
                    }
                    document_name.setText(bean.getAttName());
                    upload_status.setImageResource(R.mipmap.upload_complete);
                }
                break;
        }
    }
}
