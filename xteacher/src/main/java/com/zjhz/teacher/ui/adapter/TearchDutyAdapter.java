package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.response.TeacherBean;
import com.zjhz.teacher.NetworkRequests.response.TeacherListBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.List;

import static com.zjhz.teacher.R.id.duty_remark;

/**
 * 值日值周
 * Created by Administrator on 2016/6/21.
 */
public class TearchDutyAdapter extends BaseAdapter{
    private Context mContext;
    private List<TeacherListBean> lists;
    private boolean isLeader;
    public TearchDutyAdapter(Context context) {
        this.mContext = context;
    }
    public TearchDutyAdapter(Context context,boolean isLeader) {
        this.mContext = context;
        this.isLeader = isLeader;
    }
    public List<TeacherListBean> getLists() {
        return lists;
    }
    public void setLists(List<TeacherListBean> lists) {
        this.lists = lists;
    }
    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0 ){
            return lists.size();
        }
        return 0;
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
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_tearchduty,null);
            viewHolder.tearch_ll = (TextView) convertView.findViewById(R.id.tearch_ll);
            viewHolder.leader_tv = (TextView) convertView.findViewById(R.id.leader_tv);
            viewHolder.duty_time_tv = (TextView) convertView.findViewById(R.id.duty_time_tv);
            viewHolder.duty_remark = (ImageView) convertView.findViewById(duty_remark);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<TeacherBean> leadersdatas = lists.get(position).getLeaders();
//        viewHolder.leader_tv.removeAllViews();
        viewHolder.leader_tv.setText("");
        if(isLeader) {
            boolean isLeader = false;
            for(TeacherBean bean:lists.get(position).getLeaders()){
                if(bean.getTeacherId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.teacherIdKey))){
                    isLeader = true;
                }
            }
            if(isLeader) {
                viewHolder.duty_remark.setVisibility(View.VISIBLE);
                if (lists.get(position).isHasAffairs()) {
                    viewHolder.duty_remark.setImageResource(R.mipmap.has_affairs);
                } else {
                    viewHolder.duty_remark.setImageResource(R.mipmap.no_affairs);
                }
            }else{
                viewHolder.duty_remark.setVisibility(View.GONE);
            }
        }else{
            viewHolder.duty_remark.setVisibility(View.GONE);
        }
        if (leadersdatas != null && leadersdatas.size() > 0){
            String leader = "";
            for (int i = 0 ; i < leadersdatas.size(); i ++){
                leader += leadersdatas.get(i).getNickName()+"、";
            }
            if (!SharePreCache.isEmpty(leader)){
                leader = leader.substring(0,leader.length()-1);
            }
            viewHolder.leader_tv.setText(leader);
//            viewHolder.leader_tv.addView(AddTextView.getText(mContext,leadersdatas,0,1));
        }
        List<TeacherBean> datas = lists.get(position).getTeachers();
//        viewHolder.tearch_ll.removeAllViews();
        viewHolder.tearch_ll.setText("");
        if (datas != null && datas.size() > 0){
            String leader = "";
            for (int i = 0 ; i < datas.size(); i ++){
                leader += datas.get(i).getNickName()+"、";
            }
            if (!SharePreCache.isEmpty(leader)){
                leader = leader.substring(0,leader.length()-1);
            }
            viewHolder.tearch_ll.setText("成员："+leader);
//            viewHolder.tearch_ll.addView(AddTextView.getText(mContext,datas,0,0));
        }else {
            viewHolder.tearch_ll.setText("暂时没有教师值日");
//            TextView textView = new TextView(mContext);
//            textView.setText("暂时没有教师值日");
//            viewHolder.tearch_ll.addView(textView);
        }
        String dutyTime = lists.get(position).getDutyTime() ;
        String str1 = dutyTime.substring(5,10).split("-")[0];
        String month = str1.substring(0,1);
        if ("0".equals(month)){
            month = str1.substring(1)+"月";
        }else {
            month = str1 + "月";
        }
        String str2 = dutyTime.substring(5,10).split("-")[1];
        String day = str2.substring(0,1);
        if ("0".equals(day)){
            day = str2.substring(1)+"日";
        }else {
            day = str2 +"日";
        }
        int dayofweek = DateUtil.dayForWeek(dutyTime);
        if (dayofweek == 1){
            viewHolder.duty_time_tv.setText(month+day+"(周一)");
        }else if (dayofweek == 2){
            viewHolder.duty_time_tv.setText(month+day+"(周二)");
        }else if (dayofweek == 3){
            viewHolder.duty_time_tv.setText(month+day+"(周三)");
        }else if (dayofweek == 4){
            viewHolder.duty_time_tv.setText(month+day+"(周四)");
        }else if (dayofweek == 5){
            viewHolder.duty_time_tv.setText(month+day+"(周五)");
        }else if (dayofweek == 6){
            viewHolder.duty_time_tv.setText(month+day+"(周六)");
        }else if (dayofweek == 7){
            viewHolder.duty_time_tv.setText(month+day+"(周日)");
        }
        return convertView;
    }
    class ViewHolder{
        TextView tearch_ll;
        TextView duty_time_tv,leader_tv;
        ImageView duty_remark;
    }
}
