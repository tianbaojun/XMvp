package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.WorkPreviewBean;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业（详情）预览 评论适配器
 */
public class WorkPreviewAdapter extends BaseAdapter {

    private List<WorkPreviewBean> lists;
    private Context context;
    private OnDeleteClickListener mOnDeleteClickListener;

    public WorkPreviewAdapter(List<WorkPreviewBean> lists, Context context,OnDeleteClickListener onDeleteClickListener) {
        this.lists = lists;
        this.context = context;
        this.mOnDeleteClickListener = onDeleteClickListener;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_comment_item,null);
            viewHolder.comment_name_tv = (TextView) convertView.findViewById(R.id.comment_name_tv);
            viewHolder.comment_content_tv = (TextView) convertView.findViewById(R.id.comment_content_tv);
            viewHolder.comment_time_tv = (TextView) convertView.findViewById(R.id.comment_time_tv);
            viewHolder.user_head = (CircleImageView) convertView.findViewById(R.id.user_head);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.comment_time_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String newsUser =  lists.get(position).name.trim();
        viewHolder.comment_name_tv.setText(newsUser);
        String nick_name = SharedPreferencesUtils.getSharePrefString(ConstantKey.NickNameKey).trim();
        if (nick_name.equals(newsUser)) {
            viewHolder.delete.setVisibility(View.VISIBLE);
        }else{
            viewHolder.delete.setVisibility(View.GONE);
        }

        viewHolder.comment_content_tv.setText(lists.get(position).content);
        String date = DateUtil.getStandardDate(lists.get(position).time);
        viewHolder.comment_time_tv.setText(date);

        String url = lists.get(position).headImage;
        if (!SharePreCache.isEmpty(url)){
            GlideUtil.loadImage(url,viewHolder.user_head);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDeleteClickListener.onDeleteClickListener(position,lists.get(position).replyId);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView comment_name_tv,comment_time_tv,comment_content_tv;
        ImageView delete;
        CircleImageView user_head;
    }

    public interface OnDeleteClickListener{
        void onDeleteClickListener(int position,String replyId);
    }
}
