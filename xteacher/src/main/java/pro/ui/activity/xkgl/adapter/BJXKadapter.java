package pro.ui.activity.xkgl.adapter;

import android.content.Context;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;

import java.util.List;

import pro.ui.activity.xkgl.testbean.BJXKBeanAdmin;

/**
 * Created by Tabjin on 2017/7/13.
 * Description:
 * What Changed:
 */
public class BJXKadapter extends CommonAdapter<BJXKBeanAdmin.SubMapBean> {
    public void setData(List<BJXKBeanAdmin.SubMapBean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    public BJXKadapter(Context context, int layoutId, List<BJXKBeanAdmin.SubMapBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder,BJXKBeanAdmin.SubMapBean item, int position) {
        if(position==0) {
            viewHolder.setText(R.id.no_select_num_tv, "选课"+item.getSelectNum()+"人");
        }else if(position == 1){
            viewHolder.setText(R.id.no_select_num_tv, "未选课"+item.getSelectNum()+"人");
        }
        ScrollViewWithListView gridView = viewHolder.getView(R.id.two_listview);
        gridView.setAdapter(new BJXKSelectdapter(mContext,R.layout.activity_class_selection_banji_list_list_item,item.getSelectList()));
    }
}
