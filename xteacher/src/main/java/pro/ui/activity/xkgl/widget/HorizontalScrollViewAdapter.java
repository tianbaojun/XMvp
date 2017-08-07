package pro.ui.activity.xkgl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.util.List;

public class HorizontalScrollViewAdapter
{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> mDatas;

	public HorizontalScrollViewAdapter(Context context, List<String> mDatas)
	{
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.textview, parent, false);
			viewHolder.mText = (TextView) convertView
					.findViewById(R.id.text_tv);

			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mText.setText(mDatas.get(position));
		viewHolder.mText.setTextColor(mContext.getResources().getColor(R.color.gray_d));

		return convertView;
	}

	private class ViewHolder
	{
		TextView mText;
	}

}
