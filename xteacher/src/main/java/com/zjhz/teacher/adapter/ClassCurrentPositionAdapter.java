package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.Auditor;
import com.zjhz.teacher.utils.BaseUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-07-05
 * Time: 16:57
 * Description:
 */
public class ClassCurrentPositionAdapter extends BaseAdapter {

    private Context mContext;
    private List<Auditor> lists;

    public ClassCurrentPositionAdapter(Context mContext, List<Auditor> lists) {
        this.mContext = mContext;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        if (!BaseUtil.isEmpty(lists)) {
            return lists.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_students_current_position_item,parent,false);
        }
        TextView text = BaseViewHolder.get(view, R.id.adapter_students_current_position_item_text);
        text.setText(lists.get(i).studentName);
        return view;
    }
}
