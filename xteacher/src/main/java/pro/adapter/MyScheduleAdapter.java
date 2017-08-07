package pro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.response.TeachSchduleBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-28
 * Time: 15:57
 * Description: 我的课表适配器
 * Modify :fei.wang 2016.7.20
 */
public class MyScheduleAdapter extends BaseAdapter {
    private Context mContext;
    private List<TeachSchduleBean> allDatas = new ArrayList<>();
    private List<TeachSchduleBean> netDatas = new ArrayList<>();

    public MyScheduleAdapter(Context mContext) {
        this.mContext = mContext;
        int[] types = mContext.getResources().getIntArray(R.array.scheduledata);
        for (int i = 0; i < types.length; i++) {
            TeachSchduleBean bean = new TeachSchduleBean();
            bean.setFlag(types[i] + "");
            allDatas.add(bean);
        }
    }

    public void setNetDatas(List<TeachSchduleBean> lists) {
        netDatas.clear();
        //设置网络获取的集合当中每个bean的课程在哪里（第几星期第几节）
        if (lists != null && lists.size() > 0) {
            for (int i = 0; i < lists.size(); i++) {
                TeachSchduleBean bean = lists.get(i);
                bean.setFlag(bean.getWeek() + bean.getClazz());
                netDatas.add(bean);
            }
        }
    }

    @Override
    public int getCount() {
        return 63;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_my_schedule_grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.adapter_my_schedule_grid_item_tv_item);
        int height = parent.getHeight() - mContext.getResources().getDimensionPixelSize(R.dimen.dp_30);
        int width = parent.getWidth();


        if(position<7){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width / 3,
                    mContext.getResources().getDimensionPixelSize(R.dimen.dp_30) );
            tv.setLayoutParams(params);
            tv.setBackgroundColor(mContext.getResources().getColor(R.color.schedule_griditems_bg));
            try {
                tv.setText(setWeek(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width / 3,
                    height / 8);
            tv.setLayoutParams(params);
            TeachSchduleBean bean = allDatas.get(position-7);
            if (netDatas.size() > 0) {
                for (int i = 0; i < netDatas.size(); i++) {
                    if (bean.getFlag().equals(netDatas.get(i).getFlag())) {
                        tv.setText(netDatas.get(i).getClassName());
                        break;
                    } else {
                        tv.setText("");
                    }
                }
            } else {
                tv.setText("");
            }
        }
        return convertView;
    }

    /**
     * 在position<7的情况下调用，将7以下的数字转换成星期几
     * @param position
     * @return
     */
    private String setWeek(int position) throws Exception{
        switch (position){
            case 0:
                return "一";
            case 1:
                return "二";
            case 2:
                return "三";
            case 3:
                return "四";
            case 4:
                return "五";
            case 5:
                return "六";
            case 6:
                return "七";
            default:
                throw new Exception("position 必须在0-6之间");
       }
    }

}
