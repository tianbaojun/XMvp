package com.zjhz.teacher.ui.view.popuwindow;

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
 * Created by zzd on 2017/7/24.
 */

public class SelectPicVideoPopupWindow extends PopupWindow {
    private View conentView;

    public SelectPicVideoPopupWindow(final Context context, View parent, final PicOrVideo picOrVideo) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.select_pic_video_pw, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(conentView);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.popupwind_fade);
        TextView sdTv = (TextView) conentView
                .findViewById(R.id.pv_pic);
        TextView pcTv = (TextView) conentView
                .findViewById(R.id.pv_video);
        TextView cancelTv = (TextView) conentView
                .findViewById(R.id.pv_cancel);
        sdTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (picOrVideo != null) {
                    picOrVideo.pic();
                }
                dismiss();
            }
        });

        pcTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (picOrVideo != null) {
                    picOrVideo.video();
                }
                dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        conentView.findViewById(R.id.doc_select_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface PicOrVideo {
        void pic();
        void video();
    }
}
