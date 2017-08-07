package com.zjhz.teacher.ui.view.popuwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;

/**
 * Created by zzd on 2017/5/3.
 */

public class DocumentSelectPopupWindow extends PopupWindow {
    private View conentView;

    public DocumentSelectPopupWindow(Activity context, View parent, final SdOrPcUpLoad sdOrPcUpLoad) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.doc_select_pop_window, null);
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
//        // 设置SelectPicPopupWindow的View
//        this.setContentView(conentView);
//        // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(w / 2 + 50);
//        // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        // 刷新状态
//
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable();
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        this.setBackgroundDrawable(dw);
//
//        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//
//        this.update();
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(conentView);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupwind_fade);
        TextView sdTv = (TextView) conentView
                .findViewById(R.id.doc_sd);
        TextView pcTv = (TextView) conentView
                .findViewById(R.id.doc_pc);
        TextView cancelTv = (TextView) conentView
                .findViewById(R.id.doc_cancel);
        sdTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sdOrPcUpLoad != null) {
                    sdOrPcUpLoad.sd();
                }
                DocumentSelectPopupWindow.this.dismiss();
            }
        });

        pcTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sdOrPcUpLoad != null) {
                    sdOrPcUpLoad.pc();
                }
                DocumentSelectPopupWindow.this.dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSelectPopupWindow.this.dismiss();
            }
        });
        conentView.findViewById(R.id.doc_select_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentSelectPopupWindow.this.dismiss();
            }
        });
    }

    public interface SdOrPcUpLoad {
        void sd();

        void pc();
    }
}
