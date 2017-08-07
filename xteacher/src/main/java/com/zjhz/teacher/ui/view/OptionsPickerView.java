package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.optionsviewutil.view.BasePickerView;
import com.zjhz.teacher.ui.view.optionsviewutil.view.WheelOptions;

import java.util.ArrayList;

/**
 * 滚动选择器
 */
public class OptionsPickerView<T> extends BasePickerView implements
		View.OnClickListener {
	private static final String TAG_SUBMIT = "submit";
	private static final String TAG_CANCEL = "cancel";
	private WheelOptions<T> wheelOptions;
	private View btnSubmit, btnCancel;
	private TextView tvTitle;
	private OnOptionsSelectListener optionsSelectListener;
	private OnOptionsSubmitListener optionsSubmitListener;

	public OptionsPickerView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.pickerview_options,
				contentContainer);
		// -----确定和取消按钮
		btnSubmit = findViewById(R.id.btnSubmit);
		btnSubmit.setTag(TAG_SUBMIT);
		btnCancel = findViewById(R.id.btnCancel);
		btnCancel.setTag(TAG_CANCEL);
		btnSubmit.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		// 顶部标题
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		// ----转轮
		final View optionspicker = findViewById(R.id.optionspicker);
		wheelOptions = new WheelOptions<T>(optionspicker);
	}

	public void setPicker(ArrayList<T> optionsItems,int index1,int index2,int index3) {
		wheelOptions.setPicker(optionsItems, null, null,null,index1,index2,index3,0, false);
	}

	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items,int index1,int index2,int index3) {
		wheelOptions.setPicker(options1Items, options2Items,null, null,index1,index2,index3,0, true);
	}

	public void setPicker(ArrayList<T> options1Items,
						  ArrayList<ArrayList<T>> options2Items,
						  ArrayList<ArrayList<ArrayList<T>>> options3Items,int index1,int index2,int index3,boolean linkage) {
		wheelOptions.setPicker(options1Items, options2Items, options3Items,null,index1,index2,index3,0,linkage);
	}

	public void setPicker(ArrayList<T> options1Items,
						  ArrayList<ArrayList<T>> options2Items,
						  ArrayList<ArrayList<ArrayList<T>>> options3Items,ArrayList<T> options4Items,int index1,int index2,int index3,int index4,boolean linkage) {
		wheelOptions.setPicker(options1Items, options2Items, options3Items,options4Items,index1,index2,index3,index4,linkage);
	}

	/**
	 * 设置选中的item位置
	 *
	 * @param option1
	 */
	public void setSelectOptions(int option1) {
		wheelOptions.setCurrentItems(option1, 0, 0);
	}

	/**
	 * 设置选中的item位置
	 *
	 * @param option1
	 * @param option2
	 */
	public void setSelectOptions(int option1, int option2) {
		wheelOptions.setCurrentItems(option1, option2, 0);
	}

	/**
	 * 设置选中的item位置
	 *
	 * @param option1
	 * @param option2
	 * @param option3
	 */
	public void setSelectOptions(int option1, int option2, int option3) {
		wheelOptions.setCurrentItems(option1, option2, option3);
	}

	/**
	 * 设置选项的单位
	 *
	 * @param label1
	 */
	public void setLabels(String label1) {
		wheelOptions.setLabels(label1, null, null);
	}

	/**
	 * 设置选项的单位
	 *
	 * @param label1
	 * @param label2
	 */
	public void setLabels(String label1, String label2) {
		wheelOptions.setLabels(label1, label2, null);
	}

	/**
	 * 设置选项的单位
	 *
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		wheelOptions.setLabels(label1, label2, label3);
	}

	/**
	 * 设置是否循环滚动
	 *
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wheelOptions.setCyclic(cyclic);
	}

	public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
		wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		if (tag.equals(TAG_CANCEL)) {
			dismiss();
			return;
		} else {
			if (optionsSelectListener != null) {
				int[] optionsCurrentItems = wheelOptions.getCurrentItems();
				optionsSelectListener.onOptionsSelect(optionsCurrentItems[0],
						optionsCurrentItems[1], optionsCurrentItems[2],optionsCurrentItems[3]);
				if (optionsSubmitListener != null){
					optionsSubmitListener.onOptionsSubmit();
				}
			}
			dismiss();
			return;
		}
	}

	public void setOnoptionsSelectListener(
			OnOptionsSelectListener optionsSelectListener) {
		this.optionsSelectListener = optionsSelectListener;
	}

	public void setOnoptionsSubmitListener(
			OnOptionsSubmitListener optionsSubmitListener) {
		this.optionsSubmitListener = optionsSubmitListener;
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public interface OnOptionsSelectListener {
		void onOptionsSelect(int options1, int option2, int options3, int options4);
	}


	public interface OnOptionsSubmitListener {
		void onOptionsSubmit();
	}
}
