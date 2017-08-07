/*
 * 源文件名：PreviewerViewPager
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：适配ImagePreviewerView的使用
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class PreviewerViewPager extends ViewPager {

    private boolean isInterceptable = false;
    private boolean isTransition = false;
    private int mScrollState = SCROLL_STATE_IDLE;

    public PreviewerViewPager(Context context) {
        this(context, null);
    }

    public PreviewerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(new PageChangeListener());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mScrollState != SCROLL_STATE_IDLE) {
            return super.onInterceptTouchEvent(ev);
        }

        // 移动到边界改变拦截方式时
        if (isTransition){
            int action = ev.getAction();
            ev.setAction(MotionEvent.ACTION_DOWN);
            super.onInterceptTouchEvent(ev);
            ev.setAction(action);
            isTransition = false;
        }

        boolean b = false;

        int action  = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN){
            isInterceptable = false;
        }

        if (action != MotionEvent.ACTION_MOVE || isInterceptable){
            b = super.onInterceptTouchEvent(ev);
        }

        return isInterceptable && b;
    }

    public void isInterceptable(boolean b){
        if (!isInterceptable && b) isTransition = true;
        this.isInterceptable = b;
    }

    private class PageChangeListener extends SimpleOnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }
    }
}
