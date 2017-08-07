package com.zjhz.teacher.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.selectmorepicutil.LocalImageHelper;
import com.zjhz.teacher.utils.GlideUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by qts on 2016/3/25.
 */
public class FolderAdapter extends BaseAdapter {

    List<String> folderNames;
    Map<String, List<LocalImageHelper.LocalFile>> folders;
    Context context;

    public FolderAdapter(Context context, final LocalImageHelper helper, final Map<String, List<LocalImageHelper.LocalFile>> folders) {
        this.folders = folders;
        this.context = context;
        folderNames = new ArrayList<>();
        Iterator iter = folders.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            folderNames.add(key);
        }
        //根据文件夹内的图片数量降序显示
        Collections.sort(folderNames, new Comparator<String>() {
            public int compare(String arg0, String arg1) {
                Integer num1 = helper.getFolder(arg0).size();
                Integer num2 = helper.getFolder(arg1).size();
                return num2.compareTo(num1);
            }
        });
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int i) {
        return folderNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_albumfoler, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String name = folderNames.get(i);
        List<LocalImageHelper.LocalFile> files = folders.get(name);
        viewHolder.textView.setText(name + "(" + files.size() + ")");
        if (files.size() > 0) {
            GlideUtil.loadImage(files.get(0).getThumbnailUri(),viewHolder.imageView);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}