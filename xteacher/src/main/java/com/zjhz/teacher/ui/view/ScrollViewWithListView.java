package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-6
 * Time: 9:57
 * Description: ListView
 */
public class ScrollViewWithListView extends ListView {

	public ScrollViewWithListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewWithListView(Context context) {
		super(context);
	}

	public ScrollViewWithListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/***
	 *
	 * 改变高度 其中onMeasure函数决定了组件显示的高度与宽度；
	 * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
	 * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
	 * 之后在ScrollView中添加这个组件就OK了，同样的道理，ListView也适用。
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
