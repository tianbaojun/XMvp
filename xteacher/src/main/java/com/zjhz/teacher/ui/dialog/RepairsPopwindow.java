package com.zjhz.teacher.ui.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;

/**(删除)
 * Created by Administrator on 2016/7/4.
 */
public class RepairsPopwindow{
    private static TextView cancel_tv;
    private static TextView sure_tv;
    private static TextView size_tv;
    private static EditText reason_edt;
    private static int maxSize = 25;
    private static SureListener listner;
    public RepairsPopwindow(SureListener SureListener) {
        listner = SureListener;
    }
    public interface SureListener {
        void sureClick(String reasonStr);
    }
    public static PopupWindow makePopwindow(final Context context){
        PopupWindow popupWindow = null;
        if (popupWindow == null){
            View views = LayoutInflater.from(context).inflate(R.layout.repairs_return_reason,null);
            cancel_tv = (TextView) views.findViewById(R.id.cancel_tv);
            sure_tv = (TextView) views.findViewById(R.id.sure_tv);
            size_tv = (TextView) views.findViewById(R.id.size_tv);
            reason_edt = (EditText) views.findViewById(R.id.reason_edt);
            reason_edt.addTextChangedListener(textWatcher);
            popupWindow = new PopupWindow(views, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
            popupWindow.setOutsideTouchable(false);
            popupWindow.setFocusable(true);
            final PopupWindow finalPopupWindow = popupWindow;
            cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reason_edt.setText("");
                    finalPopupWindow.dismiss();
                }
            });
            sure_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SharePreCache.isEmpty(reason_edt.getText().toString().trim())){
                        ToastUtils.showShort("审批意见不能为空字符串,请重新输入");
                        return;
                    }
                    listner.sureClick(reason_edt.getText().toString().trim());
                    reason_edt.setText("");
                    finalPopupWindow.dismiss();
                }
            });
        }
        return popupWindow;
    }
    static TextWatcher textWatcher = new TextWatcher() {
        private CharSequence temp;
        private int selectionStart;
        private int selectionEnd;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            temp = s.toString().trim();
        }
        @Override
        public void afterTextChanged(Editable s) {
            int number = maxSize - s.toString().length();
            selectionStart = reason_edt.getSelectionStart();
            selectionEnd = reason_edt.getSelectionEnd();
            size_tv.setText(number+"/"+maxSize);
            if (temp.length() > maxSize) {
                s.delete(selectionStart - 1, selectionEnd);
                int tempSelection = selectionEnd;
                reason_edt.setText(number+"/"+maxSize);
                reason_edt.setSelection(tempSelection);//设置光标在最后
            }
        }
    };
}
