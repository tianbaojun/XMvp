package com.zjhz.teacher.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.LeaveApplyForContentActivity;
import com.zjhz.teacher.ui.delegate.LeaveApplyForListDelegate;

/**(删除)
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 请假申请列表弹窗
 */
public class LeavePopwindow extends PopupWindow implements View.OnClickListener {

    private View conentView;
    private Context mContext;
    LeaveApplyForListDelegate delegate;
    @SuppressLint("InflateParams")
    public LeavePopwindow(Context context,LeaveApplyForListDelegate delegate) {
        this.mContext = context;
        this.delegate = delegate;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.list_leave_item, null);

        this.setContentView(conentView);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

        TextView   leave_add_tv =(TextView) conentView.findViewById(R.id.leave_add_tv);
        TextView   leave_side_tv =(TextView) conentView.findViewById(R.id.leave_side_tv);

        leave_add_tv.setOnClickListener(this);
        leave_side_tv.setOnClickListener(this);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, -10, 10);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_add_tv:
                Intent intent = new Intent(mContext,LeaveApplyForContentActivity.class);
                intent.putExtra("type","2");
                mContext.startActivity(intent);
                LeavePopwindow.this.dismiss();
                break;
            case R.id.leave_side_tv:
                delegate.openDrawer();
                LeavePopwindow.this.dismiss();
                break;
            default:
                break;
        }
    }

}
