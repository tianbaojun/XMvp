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
import com.zjhz.teacher.ui.delegate.ClassAndGradeEducationListDelegate;
import com.zjhz.teacher.ui.delegate.LeaveApplyForListDelegate;

/**(删除)
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28()
 * Time: 15:57
 * Description: 请假申请列表弹窗(删除)
 */
public class EducationPopwindow extends PopupWindow implements View.OnClickListener{

    private View conentView;
    private Context mContext;
    ClassAndGradeEducationListDelegate dalegate;
    @SuppressLint("InflateParams")
    public EducationPopwindow(Context context,ClassAndGradeEducationListDelegate dalegate,Delete delete) {
        this.mContext = context;
        this.dalegate = dalegate;
        this.delete = delete;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.leave_item_class, null);

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
                EducationPopwindow.this.dismiss();
                dalegate.openDrawer();
                break;
            case R.id.leave_side_tv:
                delete.deleteOnClick();
                EducationPopwindow.this.dismiss();
                break;
            default:
                break;
        }
    }

    private Delete delete;
    public interface Delete{
        void deleteOnClick();
    }
}
