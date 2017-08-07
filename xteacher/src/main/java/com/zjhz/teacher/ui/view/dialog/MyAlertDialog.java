package com.zjhz.teacher.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;

/**
 * Created by asus on 2016/11/22.
 */
public class MyAlertDialog extends Dialog {

    public MyAlertDialog(Context context) {
        super(context);
    }

    public MyAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnClickListener {
        void onClick();
    }

    public static class Builder {
        private Context context;
        private String titleStr;//从外界设置的title文本
        private String messageStr;//从外界设置的消息文本
        //确定文本和取消文本的显示内容
        private String yesStr, noStr;
        private LinearLayout layout;

        private OnClickListener noOnClickListener;//取消按钮被点击了的监听器
        private OnClickListener yesOnClickListener;//确定按钮被点击了的监听器


        public Builder(Context context) {
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflater.inflate(R.layout.my_alert_dialog_layout, null);
        }

        public MyAlertDialog create() {
            final MyAlertDialog dialog = new MyAlertDialog(context, R.style.Dialog);

            Button yes = (Button) layout.findViewById(R.id.yes);
            Button no = (Button) layout.findViewById(R.id.no);
            TextView titleTv = (TextView) layout.findViewById(R.id.title);
            TextView messageTv = (TextView) layout.findViewById(R.id.message);

            if (titleStr != null)
                titleTv.setText(titleStr);

            if (messageStr != null)
                messageTv.setText(messageStr);
            else
                messageTv.setVisibility(View.GONE);

            if (yesStr != null) {
                yes.setText(yesStr);
            }
            if (noStr != null) {
                no.setText(noStr);
            }

            //设置确定按钮被点击后，向外界提供监听
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (yesOnClickListener != null) {
                        yesOnClickListener.onClick();
                    }
                    dialog.dismiss();
                }
            });
            //设置取消按钮被点击后，向外界提供监听
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (noOnClickListener != null) {
                        noOnClickListener.onClick();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }

        public void setTitle(String title) {
            titleStr = title;
        }

        public Builder setMessage(String message) {
            messageStr = message;
            return this;
        }

        public Builder setNoOnClickListener(String str, OnClickListener listener) {
            if (str != null)
                noStr = str;
            this.noOnClickListener = listener;
            return this;
        }

        public Builder setYesOnClickListener(String str, OnClickListener listener) {
            if (str != null)
                yesStr = str;
            this.yesOnClickListener = listener;
            return this;
        }
    }
}  