package pro.ui.activity.xkgl.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.List;

import pro.ui.activity.xkgl.testbean.XSXKbean;

/**
 * Created by Tabjin on 2017/7/13.
 * Description:
 * What Changed:
 */
public class XSXKdapter extends CommonAdapter<XSXKbean> {
    public XSXKdapter(Context context, int layoutId, List<XSXKbean> datas) {
        super(context, layoutId, datas);
    }

    public void setData(List<XSXKbean> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(ViewHolder viewHolder, XSXKbean item, int position) {
        viewHolder.setText(R.id.name_tv, item.getName());
        GlideUtil.loadImageHead(item.getPhotoUrl(),(ImageView)viewHolder.getView(R.id.header_image));
    }
}
