package com.zjhz.teacher.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.zjhz.teacher.R;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-04
 * Time: 15:57
 * Description: 请假申请状态弹窗
 */
public class DialogLeaveState extends Dialog implements View.OnClickListener {

    private Window window = null;
    private Context context;
    private EditText editText;
    private String id;
    private String type;
    private WaitDialog dialog;
    private SureListener listener;

//    public DialogLeaveState(Context context,String type,String id,WaitDialog dialog) {
//        super(context);
//        this.context = context;
//        this.dialog = dialog;
//        this.id = id;
//        this.type = type;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        showDialog();
//    }

    public DialogLeaveState(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog();
    }

    public void showDialog() {
        setContentView(R.layout.dialog_leave_state);
        windowDeploy();
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_leave_state_cancle).setOnClickListener(this);
        findViewById(R.id.dialog_leave_state_ok).setOnClickListener(this);
        editText = (EditText) findViewById(R.id.dialog_leave_state_ed);
        show();
    }

    public void windowDeploy() {
        window = getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    public void setSureListener(SureListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_leave_state_cancle:
                dismiss();
                break;
            case R.id.dialog_leave_state_ok:  // TODO 提交网络
                listener.sureClick(editText.getText().toString().trim());
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    dismiss();
                }
//                if ("1".equals(type)) {
//                    if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
//                        dialog.show();
////                        String teacherId = SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey);
////                        String schoolId = SharedPreferencesUtils.getSharePrefString(ConstantKey.SchoolIdKey);
////                        NetworkRequest.request(new LeaveTipsayAddRequest(schoolId,id,teacherId,"2","1","1","2"), CommonUrl.LEAVE_TIPSAY_ADD, Config.LEAVE_TIPSAY_ADD);
//                        NetworkRequest.request(new AgreeRequest(id,editText.getText().toString().trim()), CommonUrl.AGREE, Config.AGREE);
//                        dismiss();
//                    }else{
//                       ToastUtils.toast("审批意见不能为空");
//                    }
//                }else{
//                    if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
//                        dialog.show();
//                        NetworkRequest.request(new AgreeRequest(id,editText.getText().toString().trim()), CommonUrl.UNAGREE, Config.UNAGREE);
//                        dismiss();
//                    }else{
//                        ToastUtils.toast("审批意见不能为空");
//                    }
//                }
                break;
        }
    }

    public interface SureListener {
        void sureClick(String sure);
    }
}
