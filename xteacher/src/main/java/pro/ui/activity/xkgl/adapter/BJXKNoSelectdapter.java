package pro.ui.activity.xkgl.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.ScrollViewWithGridView;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

import pro.ui.activity.xkgl.testbean.BJXKBeanAdmin;
import pro.ui.activity.xkgl.testbean.XSXKbean;

/**
 * Created by Tabjin on 2017/7/13.
 * Description:
 * What Changed:
 */
public class BJXKNoSelectdapter extends CommonAdapter<BJXKBeanAdmin.NoSubMapBean.NoSelectListBean> {
    public void setData(List<BJXKBeanAdmin.NoSubMapBean.NoSelectListBean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    public BJXKNoSelectdapter(Context context, int layoutId, List<BJXKBeanAdmin.NoSubMapBean.NoSelectListBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder,BJXKBeanAdmin.NoSubMapBean.NoSelectListBean item, int position) {
        viewHolder.setText(R.id.subject_tv, "未选课");
        viewHolder.setText(R.id.num_tv, item.getCount() + "人");
        ScrollViewWithGridView gridView = viewHolder.getView(R.id.student_grid);
        if (!BaseUtil.isEmpty(item.getStuList())) {
            gridView.setAdapter(new CommonAdapter<XSXKbean>(mContext, R.layout.header_name, item.getStuList()) {
                @Override
                protected void convert(ViewHolder viewHolder, XSXKbean item, int position) {
                    viewHolder.setText(R.id.name_tv, item.getName());
                    GlideUtil.loadImageHead(item.getPhotoUrl(),(ImageView)viewHolder.getView(R.id.header_image));
                }
            });
        }
    }
}
