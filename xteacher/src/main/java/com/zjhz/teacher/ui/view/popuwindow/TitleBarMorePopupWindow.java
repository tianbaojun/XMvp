package com.zjhz.teacher.ui.view.popuwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.zjhz.teacher.R;

/**
 * Created by zzd on 2017/5/8.
 */

public class TitleBarMorePopupWindow extends PopupWindow {

    @SuppressLint("InflateParams")
    public TitleBarMorePopupWindow(Context mContext, final MoreCallBack moreCallBack) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.dialog_popup_item, null);

        this.setContentView(conentView);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

        conentView.findViewById(R.id.popup_search_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreCallBack.filterAction();
                dismiss();
            }
        });
        conentView.findViewById(R.id.popup_delete_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreCallBack.deleteAction();
                dismiss();
            }
        });
    }

    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {
            this.showAsDropDown(parent, -10, 10);
        } else {
            this.dismiss();
        }
    }


    public interface MoreCallBack {
        void deleteAction();

        void filterAction();
    }

}
