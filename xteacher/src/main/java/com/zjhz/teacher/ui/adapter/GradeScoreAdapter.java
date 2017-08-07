package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.GradeScoreBean;
import com.zjhz.teacher.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29.
 */
public class GradeScoreAdapter extends BaseAdapter{
    private ArrayList<GradeScoreBean> data;
    private Context context;
    private boolean isshow = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isshow = false;
        }
    };

    public GradeScoreAdapter(ArrayList<GradeScoreBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_grade_score,null);
            viewHolder.type_tv = (TextView) convertView.findViewById(R.id.type_tv);
            viewHolder.subject_tv = (TextView) convertView.findViewById(R.id.subject_tv);
            viewHolder.class_tv = (TextView) convertView.findViewById(R.id.class_tv);
//            viewHolder.pic_iv = (ImageView) convertView.findViewById(R.id.pic_iv);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final GradeScoreBean bean = data.get(position);
        if (bean != null){
            viewHolder.type_tv.setText(bean.getCertificateId());
            viewHolder.subject_tv.setText(bean.getStudentName());
            viewHolder.class_tv.setText(bean.getScore());
//            if (bean.getImages().size() >  0){
//                viewHolder.pic_iv.setImageResource(R.mipmap.score_pic);
//            }else {
//                viewHolder.pic_iv.setImageResource(R.mipmap.score_unpic);
//            }
//            viewHolder.pic_iv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (bean.getImages().size() >  0){
//                        Intent intent0 = new Intent(context, ScoreLookPicActivity.class);
//                        intent0.putExtra("images",bean);
//                        context.startActivity(intent0);
//                    }else {
//                        if (!isshow){
//                            isshow = true;
//                            ToastUtils.showShort("没有图片");
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        Thread.sleep(2000);
//                                        Message message = new Message();
//                                        handler.sendMessage(message);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
//                    }
//                }
//            });
        }
        return convertView;
    }

    class ViewHolder{
        TextView type_tv,subject_tv,class_tv;
//        ImageView pic_iv;
    }
}
