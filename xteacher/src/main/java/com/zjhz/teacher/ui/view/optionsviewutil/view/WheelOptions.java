package com.zjhz.teacher.ui.view.optionsviewutil.view;

import java.util.ArrayList;
import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.optionsviewutil.adapter.ArrayWheelAdapter;
import com.zjhz.teacher.ui.view.optionsviewutil.lib.WheelView;
import com.zjhz.teacher.ui.view.optionsviewutil.listener.OnItemSelectedListener;

public class WheelOptions<T> {
	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;
	private WheelView wv_option3;
	private WheelView wv_option4;
	private ArrayList<T> mOptions1Items;
	private ArrayList<ArrayList<T>> mOptions2Items;
	private ArrayList<ArrayList<ArrayList<T>>> mOptions3Items;
	private ArrayList<T> mOptions4Items;

	private boolean linkage = false;
	private OnItemSelectedListener wheelListener_option1;
	private OnItemSelectedListener wheelListener_option2;
	// 初始化下标
	private int index1, index2, index3,index4;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelOptions(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void setPicker(ArrayList<T> optionsItems) {
		setPicker(optionsItems, null, null, null,index1, index2, index3,index4, false);
	}

	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items, boolean linkage) {
		setPicker(options1Items, options2Items,null, null, index1, index2, index3,index4,
				linkage);
	}

	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items,
			ArrayList<ArrayList<ArrayList<T>>> options3Items,ArrayList<T> options4Items ,int index1,
			int index2, int index3,int index4, boolean linkage) {
		this.linkage = linkage;
		this.mOptions1Items = options1Items;
		this.mOptions2Items = options2Items;
		this.mOptions3Items = options3Items;
		this.mOptions4Items = options4Items;
		this.index1 = index1;
		this.index2 = index2;
		this.index3 = index3;
		this.index4 = index4;
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		if (this.mOptions3Items == null)
			len = 8;
		if (this.mOptions2Items == null)
			len = 12;
		// 选项1
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
		wv_option1.setCurrentItem(index1);// 初始化时显示的数据
		// 选项2
		wv_option2 = (WheelView) view.findViewById(R.id.options2);
		if (mOptions2Items != null)
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(index1)));// 设置显示数据
		wv_option2.setCurrentItem(index2);// 初始化时显示的数据
		//wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
		// 选项3
		wv_option3 = (WheelView) view.findViewById(R.id.options3);
		if (mOptions3Items != null)
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(index1).get(index2)));// 设置显示数据
		wv_option3.setCurrentItem(index3);// 初始化时显示的数据
//		wv_option3.setCurrentItem(wv_option3.getCurrentItem());// 初始化时显示的数据
		// 选项4
		wv_option4 = (WheelView) view.findViewById(R.id.options4);
		if (mOptions4Items != null){
			wv_option4.setAdapter(new ArrayWheelAdapter(mOptions4Items, len));
			wv_option4.setCurrentItem(index4);
		}

		int textSize = 16;

		wv_option1.setTextSize(textSize);
		wv_option2.setTextSize(textSize);
		wv_option3.setTextSize(textSize);
		wv_option4.setTextSize(textSize);

		if (this.mOptions2Items == null)
			wv_option2.setVisibility(View.GONE);
		if (this.mOptions3Items == null)
			wv_option3.setVisibility(View.GONE);
		if (this.mOptions4Items == null)
			wv_option4.setVisibility(View.GONE);

		// 联动监听器
		wheelListener_option1 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				int opt2Select = 0;
				if (mOptions2Items != null) {
					opt2Select = wv_option2.getCurrentItem();// 上一个opt2的选中位置
					// 新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
					opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items
							.get(index).size() - 1 : opt2Select;

					wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
							.get(index)));
					wv_option2.setCurrentItem(opt2Select);
				}
				if (mOptions3Items != null) {
					wheelListener_option2.onItemSelected(opt2Select);
				}
			}
		};
		wheelListener_option2 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				if (mOptions3Items != null) {
					int opt1Select = wv_option1.getCurrentItem();
					opt1Select = opt1Select >= mOptions3Items.size() - 1 ? mOptions3Items
							.size() - 1 : opt1Select;
					index = index >= mOptions2Items.get(opt1Select).size() - 1 ? mOptions2Items
							.get(opt1Select).size() - 1 : index;
					int opt3 = wv_option3.getCurrentItem();// 上一个opt3的选中位置
					// 新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
					opt3 = opt3 >= mOptions3Items.get(opt1Select).get(index)
							.size() - 1 ? mOptions3Items.get(opt1Select)
							.get(index).size() - 1 : opt3;

					wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items
							.get(wv_option1.getCurrentItem()).get(index)));
					wv_option3.setCurrentItem(opt3);

				}
			}
		};

		// // 添加联动监听
		if (options2Items != null && linkage)
			wv_option1.setOnItemSelectedListener(wheelListener_option1);
		if (options3Items != null && linkage)
			wv_option2.setOnItemSelectedListener(wheelListener_option2);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
		if (label3 != null)
			wv_option3.setLabel(label3);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
		wv_option3.setCyclic(cyclic);
		wv_option4.setCyclic(cyclic);
	}

	/**
	 * 分别设置第一二三级是否循环滚动
	 * 
	 * @param cyclic1
	 *            ,cyclic2,cyclic3
	 */
	public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
		wv_option1.setCyclic(cyclic1);
		wv_option2.setCyclic(cyclic2);
		wv_option3.setCyclic(cyclic3);
	}

	/**
	 * 设置第二级是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setOption2Cyclic(boolean cyclic) {
		wv_option2.setCyclic(cyclic);
	}

	/**
	 * 设置第三级是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setOption3Cyclic(boolean cyclic) {
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
	 * 
	 * @return
	 */
	public int[] getCurrentItems() {
		int[] currentItems = new int[4];
		currentItems[0] = wv_option1.getCurrentItem();
		currentItems[1] = wv_option2.getCurrentItem();
		currentItems[2] = wv_option3.getCurrentItem();
		currentItems[3] = wv_option4.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1, int option2, int option3) {
		if (linkage) {
			itemSelected(option1, option2, option3);
		}
		wv_option1.setCurrentItem(option1);
		wv_option2.setCurrentItem(option2);
		wv_option3.setCurrentItem(option3);
	}

	private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
		if (mOptions2Items != null) {
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
					.get(opt1Select)));
			wv_option2.setCurrentItem(opt2Select);
		}
		if (mOptions3Items != null) {
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(
					opt1Select).get(opt2Select)));
			wv_option3.setCurrentItem(opt3Select);
		}
	}

}
