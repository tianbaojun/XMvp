package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;

/**
 * 提示popwindow
 * Created by Administrator on 2016/8/4.
 */
public class HintPopwindow extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private TextView hint_tvs,sure,cancel;
    private View view_line;
    private OnClicks clicks;
    public HintPopwindow(Context context) {
        super(context);
        mContext = context;
        initView();
    }
    public void setOnclicks(OnClicks clicks){
        this.clicks = clicks;
    }
    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hintpop,null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x90000000);
        setBackgroundDrawable(dw);
        hint_tvs = (TextView) view.findViewById(R.id.hint_tvs);
        sure = (TextView) view.findViewById(R.id.sure);
        cancel = (TextView) view.findViewById(R.id.cancel);
        view_line = view.findViewById(R.id.view_line);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    public void setTitleMessage(String msg){
        hint_tvs.setText(msg);
    }
    public void setSureBtn(String msg){
        sure.setText(msg);
    }
    public void setCancleBtn(String msg){
        cancel.setText(msg);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.sure:
                clicks.sureClick();
                break;
            case R.id.cancel:
                clicks.cancelClick();
                break;
        }
    }
    public interface OnClicks{
        void sureClick();
        void cancelClick();
    }
    public void hintCancel(){
        view_line.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }
}
