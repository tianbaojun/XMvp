package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class AddTextView {

    public static View getText(Context mContext, List<TeacherBean> datas, int type,int size) {
        LinearLayout ParentLayout = new LinearLayout(mContext);
        ParentLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout linearLayout = new LinearLayout(mContext);
        float allwidth = 0;
        float eachWidth = 0;
        int screenWidth;
        if (size == 0){
            screenWidth = SharePreCache.getScreenWidth(mContext) - SharePreCache.dp2px(mContext, 20);
        }else {
            screenWidth = SharePreCache.getScreenWidth(mContext) - SharePreCache.dp2px(mContext, 120);
        }
        int idx = 0;
        for (TeacherBean lables : datas) {
            idx++;
            LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.text_item, null);
            TextView name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            if (type == 0 ){
                name_tv.setTextColor(mContext.getResources().getColor(R.color.text_color28));
                if (idx == datas.size()){
                    name_tv.setText(lables.getNickName());
                }else {
                    name_tv.setText(lables.getNickName()+"、");
                }
            }else if (type == 1){
                name_tv.setTextColor(mContext.getResources().getColor(R.color.text_coloree452f));
                name_tv.setText(lables.getNickName());
            }
            eachWidth = GetAllTextViewWidth(name_tv) + name_tv.getPaddingLeft() + name_tv.getPaddingRight() + name_tv.getScaleX() * (lables.getNickName().length() - 1)
                    + ((LinearLayout.LayoutParams) name_tv.getLayoutParams()).leftMargin + ((LinearLayout.LayoutParams) name_tv.getLayoutParams()).rightMargin;
            allwidth = allwidth + eachWidth;
            if(screenWidth>=allwidth){
                linearLayout.addView(itemView);
                eachWidth=0;
            }else{
                ParentLayout.addView(linearLayout);
                linearLayout=new LinearLayout(mContext);
                linearLayout.addView(itemView);
                allwidth=eachWidth;
                eachWidth=0;
            }
            if (idx == datas.size()) {
                ParentLayout.addView(linearLayout);
            }
        }
        return ParentLayout;
    }

    /**
     * 获取textview总长度
     */
    private static float GetAllTextViewWidth(TextView tv) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        // textView getPaint measureText
        TextPaint textPaint = tv.getPaint();
        float textPaintWidth = textPaint.measureText(tv.getText().toString());
        return textPaintWidth;
    }
}
