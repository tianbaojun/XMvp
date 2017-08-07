/*
 * 源文件名：ListPopupWindow 
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/3
 * 修改作者：captailgodwin
 * 修改日期：2016/11/3
 * 文件描述：列表管理统一弹出框，兼容‘筛选’、‘删除’等等其它操作功能
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.Constants;
import com.zjhz.teacher.ui.delegate.ClassAndGradeEducationListDelegate;
import com.zjhz.teacher.ui.delegate.PersonMoralEducationListDelegate;
import com.zjhz.teacher.ui.delegate.HomeWorkManagerDrawerLayoutPresenter;

public class ListPopupWindow extends PopupWindow implements View.OnClickListener {

    private View conentView;
    private Context mContext;
    private String moduleName = "";
    PersonMoralEducationListDelegate   personAction;
    ClassAndGradeEducationListDelegate classAction;
    HomeWorkManagerDrawerLayoutPresenter homeWorkAction;

    @SuppressLint("InflateParams")
    public ListPopupWindow(Context context, Object action, String module, DeleteAction deleteAction) {

        this.mContext = context;
        this.moduleName =  module;
        this.deleteAction =  deleteAction;
        if( moduleName.equals(Constants.MODULE_HOMEWORK )) {

            this.homeWorkAction =  (HomeWorkManagerDrawerLayoutPresenter)action;

        }else if( moduleName.equals(Constants.MODULE_PERSONMORAL )) {

            this.personAction =  (PersonMoralEducationListDelegate)action;

        }else if ( moduleName.equals(Constants.MODULE_CALSSMORAL )) {

            this.classAction =  (ClassAndGradeEducationListDelegate)action;
        }
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.dialog_popup_item, null);

        this.setContentView(conentView);
        this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);

        TextView popup_search_tv =
                (TextView) conentView.findViewById(R.id.popup_search_tv);
        TextView popup_delete_tv =
                (TextView) conentView.findViewById(R.id.popup_delete_tv);

        popup_search_tv.setOnClickListener(this);
        popup_delete_tv.setOnClickListener(this);
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
            case R.id.popup_search_tv:
                if( moduleName.equals(Constants.MODULE_HOMEWORK)) {

                    homeWorkAction.openDrawer();

                }else if(moduleName.equals(Constants.MODULE_PERSONMORAL)) {

                    personAction.openDrawer();

                }else if(moduleName.equals(Constants.MODULE_CALSSMORAL)) {

                    classAction.openDrawer();
                }
                ListPopupWindow.this.dismiss();
                break;
            case R.id.popup_delete_tv:
                deleteAction.deleteOnClick();
                ListPopupWindow.this.dismiss();
                break;
            default:
                break;
        }
    }

    //父子窗口传递消息
    private DeleteAction deleteAction;
    public interface DeleteAction {
        void deleteOnClick();
    }

}

