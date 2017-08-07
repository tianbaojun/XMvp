/*
 * 源文件名：PersonMoralEducationListAdapter
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/3
 * 修改作者：captailgodwin
 * 修改日期：2016/11/3
 * 文件描述：个人德育适配器
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
import com.zjhz.teacher.bean.PersonMoralEducationListBean;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

public class PersonMoralEducationListAdapter extends BaseAdapter {

    private List<PersonMoralEducationListBean> datas;
    private Context context;
    private boolean isAnimation = false;
    public List<String> lists = new ArrayList<>();
    public List<PersonMoralEducationListBean> positions = new ArrayList<>();
    private String userId;
    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        this.isAnimation = animation;
    }

    public void clearList(){
        lists.clear();
        positions.clear();
    }

    public PersonMoralEducationListAdapter(List<PersonMoralEducationListBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_person_moral_education_item, viewGroup, false);
        }
        TextView time = BaseViewHolder.get(convertView, R.id.adapter_person_moral_education_item_time);
        TextView clazz = BaseViewHolder.get(convertView, R.id.adapter_person_moral_education_item_clazz);
        TextView name = BaseViewHolder.get(convertView, R.id.adapter_person_moral_education_name);
        TextView item = BaseViewHolder.get(convertView, R.id.adapter_person_moral_education_item);
        final ImageView imageView = BaseViewHolder.get(convertView, R.id.adapter_person_education_checked_iv);
        LinearLayout linearLayout = BaseViewHolder.get(convertView, R.id.adapter_person_education_ll);
        time.setText(datas.get(i).time.substring(2,datas.get(i).time.length()));
        clazz.setText(datas.get(i).clazz);
        name.setText(datas.get(i).name);
        item.setText(datas.get(i).sub);

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
        if (lists.contains(datas.get(i).id)){
            imageView.setImageResource(R.mipmap.selecttrue_icon);
        }else {
            imageView.setImageResource(R.mipmap.selectfalse_icon);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.contains(datas.get(i).id)){
                    imageView.setImageResource(R.mipmap.selectfalse_icon);
                    lists.remove(datas.get(i).id);
                    positions.remove(datas.get(i));
                }else {
                    lists.add(datas.get(i).id);
                    positions.add(datas.get(i));
                    imageView.setImageResource(R.mipmap.selecttrue_icon);
                }
            }
        });
        return convertView;
    }
}
