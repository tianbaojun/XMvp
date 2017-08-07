package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.bean.RepairsProposerState;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-30
 * Time: 8:57
 * Description: 申请人请假申请状态适配器
 */
public class LeaveApplyStateListAdapter extends BaseAdapter {

    private Context context;
    private List<RepairsProposerState> lists;

    public LeaveApplyStateListAdapter(Context context, List<RepairsProposerState> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        RepairsProposerState beann = lists.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_repairs_state_listview_item, viewGroup, false);
        }
        TextView state = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_text);
        ImageView head = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_image);
        TextView name = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_name);
        TextView content = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_content);
        TextView time = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_time);
        View view1 = BaseViewHolder.get(convertView, R.id.view_1);
        View view = BaseViewHolder.get(convertView, R.id.view_2);

        state.setText(beann.state);
        name.setText(beann.name);
        content.setText(beann.content);
        time.setText(beann.time);
        GlideUtil.loadImageHead(beann.image,head);
        if (position == 0 ){
            view1.setVisibility(View.INVISIBLE);
        }else {
            view1.setVisibility(View.VISIBLE);
        }
        if ((lists.size() - 1) == position) {
            view.setVisibility(View.INVISIBLE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
