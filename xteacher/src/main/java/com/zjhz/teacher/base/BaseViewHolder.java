package com.zjhz.teacher.base;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-29
 * Time: 15:57
 * Description:
 */
public class BaseViewHolder {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
