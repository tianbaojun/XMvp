package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.RepairsTaskListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;

import java.util.List;

/**
 * 报修流程
 * Created by Administrator on 2016/7/15.
 */
public class RepairsListAdapter extends BaseAdapter{
    private Context context;
    private List<RepairsTaskListBean> lists;

    public RepairsListAdapter(Context context, List<RepairsTaskListBean> lists) {
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
        RepairsTaskListBean beann = lists.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_repairs_state_listview_item, viewGroup, false);
        }
        TextView state = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_text);
        CircleImageView head = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_image);
        TextView name = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_name);
        TextView content = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_content);
        TextView time = BaseViewHolder.get(convertView, R.id.adapter_repairs_state_list_item_time);
        View view = BaseViewHolder.get(convertView, R.id.view_2);
        View view1 = BaseViewHolder.get(convertView, R.id.view_1);
        state.setText(beann.getNodeName());
        name.setText(beann.getNickName());
        content.setText(beann.getContent());

        String timeF = DateUtil.getStandardDate(beann.getFlowTime().substring(0,16));
        time.setText(timeF);
        if (!SharePreCache.isEmpty(beann.getPhotoUrl())){
            GlideUtil.loadImageHead(beann.getPhotoUrl(),head);
        }
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
