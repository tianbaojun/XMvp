package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: CustomScrollView
 */
public class CustomScrollView extends ScrollView {
    public OnScrollChangeListener onScrollChangeListener;

    public View contentView;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    protected void onFinishInflate() {
//        // TODO Auto-generated method stub
//        if (getChildCount() > 0) {
//            contentView = getChildAt(0);
//        }
//    }

    public interface OnScrollChangeListener {
        void onScrollChange(CustomScrollView view, int x, int y, int oldx, int oldy);

        void onScrollBottomListener();

        void onScrollTopListener();
    }

    /**
     * l当前水平滚动的开始位置
     * t当前的垂直滚动的开始位置
     * oldl上一次水平滚动的位置。
     * oldt上一次垂直滚动的位置。
     **/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
        if (t + getHeight() >= contentView.getHeight() && onScrollChangeListener != null) {
            onScrollChangeListener.onScrollBottomListener();
        }
        if (t == 0 || t + getHeight() > contentView.getHeight() && onScrollChangeListener != null) {
            onScrollChangeListener.onScrollTopListener();
        }
    }

}
