package com.zjhz.teacher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.utils.DateUtil;

import java.util.List;
public class MessageAdapter extends BaseAdapter {
	protected Context context;
	protected List<Message> messages;
	protected int type = 0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public MessageAdapter(Context context, List<Message> messages) {
		super();
		this.context = context;
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Message getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.adapter_item_message, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
			holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
			holder.oval_iv = (ImageView) convertView.findViewById(R.id.oval_iv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Message bean = getItem(position);
		if (bean != null ){
			String time = DateUtil.getStandardDate(bean.getCreateTime());
			holder.time_tv.setText(time);
			if (type == 1){// 1表示会议通知
				holder.content_tv.setText(bean.getTitle());
				if (bean.getStatus() == 0){
					holder.oval_iv.setVisibility(View.VISIBLE);
				}else {
					holder.oval_iv.setVisibility(View.GONE);
				}
			}else if (type == 2){// 2表示消息群发
				holder.content_tv.setText(bean.getContent());
				if (bean.getStatus() == 0){
					holder.oval_iv.setVisibility(View.VISIBLE);
				}else {
					holder.oval_iv.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}

	class ViewHolder{
		ImageView icon,oval_iv;
		TextView content_tv;
		TextView time_tv;
	}
}
