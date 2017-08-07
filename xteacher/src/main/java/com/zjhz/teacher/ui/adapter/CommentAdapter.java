package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.response.NewsReplayBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.activity.CommentActivity;
import com.zjhz.teacher.ui.activity.FoodDetailActivity;
import com.zjhz.teacher.ui.activity.NewsDetailActivity;
import com.zjhz.teacher.ui.view.CircleImageView;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻评论列表
 * Created by xiangxue on 2016/6/16.
 */
public class CommentAdapter extends BaseAdapter{
    private Context context;
    public List<NewsReplayBean> datas = new ArrayList<>();
    private String userId = "";
    private int type;

    public ItemCallBack itemCallBack;

//    private CommentActivity commentActivity;
//    private NewsDetailActivity newsDetailActivity;
//    private FoodDetailActivity foodDetailActivity;

    public CommentAdapter(Context context) {
        this.context = context;
        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
    }

//    public CommentAdapter(Context context,NewsDetailActivity newsDetailActivity) {
//        this.context = context;
//        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
//        this.newsDetailActivity = newsDetailActivity;
//    }
//    public CommentAdapter(Context context,FoodDetailActivity foodDetailActivity) {
//        this.context = context;
//        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
//        this.foodDetailActivity = foodDetailActivity;
//    }
//    public CommentAdapter(Context context,CommentActivity commentActivity,int type) {
//        this.context = context;
//        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
//        this.type = type;
//        this.commentActivity = commentActivity;
//    }
    public List<NewsReplayBean> getDatas() {
        return datas;
    }
    public void setDatas(List<NewsReplayBean> datas) {
        this.datas = datas;

    }
    public void addDatas(List<NewsReplayBean> datas){
        this.datas.addAll(datas);
    }
    public void addData(int position,NewsReplayBean data){
        this.datas.add(position, data);
    }
    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_comment_item,null);
            viewHolder.comment_name_tv = (TextView) convertView.findViewById(R.id.comment_name_tv);
            viewHolder.comment_content_tv = (TextView) convertView.findViewById(R.id.comment_content_tv);
            viewHolder.comment_time_tv = (TextView) convertView.findViewById(R.id.comment_time_tv);
            viewHolder.comment_time_delete = (ImageView) convertView.findViewById(R.id.comment_time_delete);
            viewHolder.user_head = (CircleImageView) convertView.findViewById(R.id.user_head);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final NewsReplayBean bean = datas.get(position);
        if (bean != null){
            final String newsUser =  bean.getUserName();
            final String foodUser =  bean.getName();
            String replyUser = bean.getReplyUser();
            if (!SharePreCache.isEmpty(newsUser)){
                viewHolder.comment_name_tv.setText(newsUser);
            }else if (!SharePreCache.isEmpty(foodUser)){
                viewHolder.comment_name_tv.setText(foodUser);
            }
            if (userId.equals(replyUser)){
                viewHolder.comment_time_delete.setVisibility(View.VISIBLE);
            }else {
                viewHolder.comment_time_delete.setVisibility(View.GONE);
            }
            viewHolder.comment_content_tv.setText(bean.getMsgContect());
//            String date = DateUtil.getStandardDate(bean.getReplyTime());
//            viewHolder.comment_time_tv.setText(date);
            viewHolder.comment_time_tv.setText(bean.getReplyTime());
            String url = bean.getUserPhotoUrl();
            GlideUtil.loadImageHead("",viewHolder.user_head);
            if (!SharePreCache.isEmpty(url)){
                GlideUtil.loadImageHead(url,viewHolder.user_head);
            }else {
                GlideUtil.loadImageHead("",viewHolder.user_head);
            }
            viewHolder.comment_time_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCallBack.delete(bean.getReplyId());
//                    if (newsDetailActivity != null){
//                        newsDetailActivity.deleteComment(bean.getReplyId(), CommonUrl.removeNewReply);
//                    }else if (foodDetailActivity != null){
//                        foodDetailActivity.deleteComment(bean.getReplyId(), CommonUrl.removeCookBookReply);
//                    }else if (commentActivity != null){
//                        if (type == 1){
//                            commentActivity.deleteComment(bean.getReplyId(), CommonUrl.removeNewReply);
//                        }else if (type == 2){
//                            commentActivity.deleteComment(bean.getReplyId(), CommonUrl.removeCookBookReply);
//                        }
//                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder{
        TextView comment_name_tv,comment_time_tv,comment_content_tv;
        ImageView comment_time_delete;
        CircleImageView user_head;
    }

    public void setItemCallBack(ItemCallBack itemCallBack){
        this.itemCallBack = itemCallBack;
    }

    public interface ItemCallBack{
        void delete(String id);
    }
}
