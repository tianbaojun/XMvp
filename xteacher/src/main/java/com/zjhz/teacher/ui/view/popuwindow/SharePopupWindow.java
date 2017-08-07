package com.zjhz.teacher.ui.view.popuwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;

/**
 * Created by zzd on 2017/6/27.
 */

public class SharePopupWindow extends PopupWindow {

//    private static SharePopupWindow sharePopupWindow;
    private Share share;
    private View parent;

//    public static SharePopupWindow newInstance(Context context, View parent) {
//        if (sharePopupWindow == null) {
//            sharePopupWindow = new SharePopupWindow(context, parent);
//        }
//
//        return sharePopupWindow;
//    }

    public SharePopupWindow(Context context, View parent) {
        super(context);
        this.parent = parent;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.share_popupwindow_layout, null);
        setContentView(layout);
        Button bjqBtn = (Button) layout.findViewById(R.id.share_bjq);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.black_alpha_96));
        setBackgroundDrawable(dw);
        update();
        bjqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share != null) {
                    share.bjq();
                    dismiss();
                }
            }
        });
        TextView cancel = (TextView) layout.findViewById(R.id.share_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public SharePopupWindow setShare(Share share) {
        this.share = share;
        return this;
    }

    public void show(){
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public interface Share{
        void bjq();
    }
}
