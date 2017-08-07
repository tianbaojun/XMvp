package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.PhotoListBean;
import com.zjhz.teacher.ui.activity.ImageDetailActivity;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.uk.co.senab.photoview.Bimp;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class MyAdapter extends BaseAdapter{
    private Context m_context;
     ImageDetailActivity activity;
     List<LocalImageHelper.LocalFile> paths;
    List<LocalImageHelper.LocalFile> checkedItems = new ArrayList<>();
     int totalCount;

    public MyAdapter(Context context, List<LocalImageHelper.LocalFile> path, LocalImageHelper helper, PhotoListBean bean) {
        m_context = context;
        activity = (ImageDetailActivity) context;
        paths = path;
        if (bean.getDatas() != null){
            checkedItems = bean.getDatas();
        }
        totalCount = bean.getTotalCount();
    }

    public List<LocalImageHelper.LocalFile> getCheckedItems() {
        return checkedItems;
    }

    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public LocalImageHelper.LocalFile getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.simple_list_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.checkBox = (ImageView) convertView.findViewById(R.id.checkbox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final LocalImageHelper.LocalFile localFile = paths.get(i);
        GlideUtil.loadImage(localFile.getThumbnailUri(),viewHolder.imageView);
        viewHolder.checkBox.setTag(localFile);
        boolean isremove = false;
        int in = -1;
        for (int index = 0 ; index < checkedItems.size() ; index++ ) {
            LocalImageHelper.LocalFile index_localFile = checkedItems.get(index);
            if (localFile.getThumbnailUri().equals(index_localFile.getThumbnailUri())) {
                isremove = true;
                in = index;
                break;
            }
        }
        if (isremove){
            checkedItems.remove(checkedItems.get(in));
            localFile.setSelect(true);
            checkedItems.add(in,localFile);
            viewHolder.checkBox.setImageResource(R.mipmap.albumset_selected);
        }else {
            localFile.setSelect(false);
            viewHolder.checkBox.setImageResource(R.mipmap.albumset_preselected);
        }
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localFile.isSelect()){
                    localFile.setSelect(false);
                    viewHolder.checkBox.setImageResource(R.mipmap.albumset_preselected);
                    if (checkedItems.contains(localFile)){
                        checkedItems.remove(localFile);
                        Bimp.tempSelectBitmap.remove(localFile);
                        activity.updateCurrentSize(checkedItems.size());
                    }
                }else {
                    if (checkedItems.size() >= totalCount){
                        localFile.setSelect(false);
                        viewHolder.checkBox.setImageResource(R.mipmap.albumset_preselected);
//                        Toast.makeText(m_context,"最多"+totalCount+"个",Toast.LENGTH_SHORT).show();
                        ToastUtils.showShort("最多"+totalCount+"个");
                    }else {
                        localFile.setSelect(true);
                        viewHolder.checkBox.setImageResource(R.mipmap.albumset_selected);
                        if (!checkedItems.contains(localFile)){
                            checkedItems.add(localFile);
                            Bimp.tempSelectBitmap.add(localFile);
                            activity.updateCurrentSize(checkedItems.size());
                        }
                    }
                }
            }
        });
        return convertView;
    }
    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(m_context);
    }
    private class ViewHolder {
        ImageView imageView;
        ImageView checkBox;
    }
}
