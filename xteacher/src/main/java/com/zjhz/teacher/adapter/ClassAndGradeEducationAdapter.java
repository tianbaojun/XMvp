/*
 * 源文件名：PersonMoralEducationListAdapter
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/3
 * 修改作者：captailgodwin
 * 修改日期：2016/11/3
 * 文件描述：班级德育列表适配器
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.ClassAndGradeEducation;
import com.zjhz.teacher.bean.DeleteBean;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

public class ClassAndGradeEducationAdapter extends BaseAdapter{
    private Context context;
    private boolean isAnimation = false;
    private List<ClassAndGradeEducation> datas;
    public List<DeleteBean> lists = new ArrayList<>();
    public List<String> grades = new ArrayList<>();
    public List<String> schems = new ArrayList<>();
    public List<String> times = new ArrayList<>();
    public List<ClassAndGradeEducation> positions = new ArrayList<>();
    private String userId;

    public ClassAndGradeEducationAdapter(Context context, List<ClassAndGradeEducation> datas) {
        this.context = context;
        this.datas = datas;
        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
    }

    public void clearList(){
        lists.clear();
        grades.clear();
        schems.clear();
        times.clear();
        positions.clear();
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_classandcrade_education, viewGroup, false);
        }
        LinearLayout linearLayout = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_ll);
        final ImageView imageView = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_checked_iv);
        TextView clazz = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_person_tv);  // 班级
        TextView projectCheck = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_tv);  // 专项检查
        TextView time = BaseViewHolder.get(view, R.id.adapter_classandcrade_education_time_tv);  // 时间

        clazz.setText(datas.get(i).gradeName);
        projectCheck.setText(datas.get(i).schemeName);
        time.setText(datas.get(i).checkTime.substring(0,11));

        if (datas.get(i).createUser.equals(userId)){
            if (isAnimation){
                int px = ToolsUtil.dip2px(context, 56);
                TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, -px,Animation.ABSOLUTE, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f);
                anim.setDuration(500);
                linearLayout.startAnimation(anim);
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }
        }else {
            imageView.setVisibility(View.GONE);
        }

        final DeleteBean delete = new DeleteBean();
        delete.gradeId = datas.get(i).gradeId;
        delete.schemeId = datas.get(i).schemeId;
        delete.checkTime = datas.get(i).checkTime;

        if (grades.contains(datas.get(i).gradeId) && schems.contains(datas.get(i).schemeId) && times.contains(datas.get(i).checkTime)){
            imageView.setImageResource(R.mipmap.selecttrue_icon);
        }else {
            imageView.setImageResource(R.mipmap.selectfalse_icon);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grades.contains(datas.get(i).gradeId) && schems.contains(datas.get(i).schemeId) && times.contains(datas.get(i).checkTime)){
                    imageView.setImageResource(R.mipmap.selectfalse_icon);
                    grades.remove(datas.get(i).gradeId);
                    schems.remove(datas.get(i).schemeId);
                    times.remove(datas.get(i).checkTime);
                    lists.remove(delete);
                    positions.remove(datas.get(i));
                }else {
                    lists.add(delete);
                    times.add(datas.get(i).checkTime);
                    grades.add(datas.get(i).gradeId);
                    schems.add(datas.get(i).schemeId);
                    positions.add(datas.get(i));
                    imageView.setImageResource(R.mipmap.selecttrue_icon);
                }
            }
        });
        return view;
    }

}
