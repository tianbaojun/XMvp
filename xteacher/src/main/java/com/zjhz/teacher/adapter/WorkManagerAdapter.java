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
import com.zjhz.teacher.NetworkRequests.response.HomeWorkBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.dialog.ListPopupWindow;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-20
 * Time: 15:57
 * Description: 作业管理
 */
public class WorkManagerAdapter extends BaseAdapter {

    private Context context;
    private boolean isAnimation = false;
    private List<HomeWorkBean> datas;
    private boolean isShowParisAndReply;
    ListPopupWindow window;

    private List<String> lists = new ArrayList<>();
    public List<String> getLists() {
        return lists;
    }
    private String userId;
    public void setLists() {
        this.lists.clear();
    }

    public WorkManagerAdapter(Context context, List<HomeWorkBean> datas,boolean isShowParisAndReply) {
        this.context = context;
        this.isShowParisAndReply = isShowParisAndReply;
        this.datas = datas;
        userId = SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey);
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
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_work_manager_item,null,false);
            holder.person_tv = (TextView) view.findViewById(R.id.person_tv);
            holder.work_title_tv = (TextView) view.findViewById(R.id.work_title_tv);
            holder.release_time_tv = (TextView) view.findViewById(R.id.release_time_tv);
            holder.data_ll = (LinearLayout) view.findViewById(R.id.data_ll);
            holder.checked_iv = (ImageView) view.findViewById(R.id.checked_iv);
            holder.left_tv_fly = (TextView) view.findViewById(R.id.left_tv_fly); // 点赞数
            holder.right_tv_fly = (TextView) view.findViewById(R.id.right_tv_fly); // 评论数
            holder.worktime_tv = (TextView) view.findViewById(R.id.worktime_tv);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        final String homeWorkId = datas.get(i).getHomeworkId();
        String className = "";
        if (datas.get(i).getClasses() != null && datas.get(i).getClasses().size() > 0){
            for (int index = 0 ; index < datas.get(i).getClasses().size() ; index++){
                className+=datas.get(i).getClasses().get(index).getClassName()+"、";
            }
        }
        //去掉最后的逗号
        if(className.length()>0){
            className=className.substring(0,className.length()-1);
        }
        holder.person_tv.setText("发送对象:"+className);
        holder.work_title_tv.setText(datas.get(i).getHomeworkName());
        String time = DateUtil.getStandardDate(datas.get(i).getPublishTime());
        holder.release_time_tv.setText(time);
        if (isShowParisAndReply){
            holder.worktime_tv.setVisibility(View.GONE);
            holder.left_tv_fly.setVisibility(View.VISIBLE);
            holder.right_tv_fly.setVisibility(View.VISIBLE);
            holder.left_tv_fly.setText(datas.get(i).getPraiseNum());
            holder.right_tv_fly.setText(datas.get(i).getReplyNum());
        }else {
            holder.worktime_tv.setText("作业时长："+datas.get(i).getCostTime()+"分钟");
            holder.worktime_tv.setVisibility(View.VISIBLE);
            holder.left_tv_fly.setVisibility(View.GONE);
            holder.right_tv_fly.setVisibility(View.GONE);
        }
        if (datas.get(i).getCreateUser().equals(userId)){
            if (isAnimation){
                int px = ToolsUtil.dip2px(context, 56);
                TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, -px,Animation.ABSOLUTE, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f);
                anim.setDuration(500);
                holder.data_ll.startAnimation(anim);
                holder.checked_iv.setVisibility(View.VISIBLE);
            }else {
                holder.checked_iv.setVisibility(View.GONE);
            }
        }else {
            holder.checked_iv.setVisibility(View.GONE);
        }

        if (lists.contains(homeWorkId)){
            holder.checked_iv.setImageResource(R.mipmap.selecttrue_icon);
        }else {
            holder.checked_iv.setImageResource(R.mipmap.selectfalse_icon);
        }
        holder.checked_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.contains(homeWorkId)){
                    holder.checked_iv.setImageResource(R.mipmap.selectfalse_icon);
                    lists.remove(homeWorkId);
                }else {
                    lists.add(homeWorkId);
                    holder.checked_iv.setImageResource(R.mipmap.selecttrue_icon);
                }
            }
        });
        return view;
    }
    class ViewHolder{
        ImageView checked_iv;
        LinearLayout data_ll;
        TextView person_tv,work_title_tv,release_time_tv,left_tv_fly,right_tv_fly,worktime_tv;
    }
}
