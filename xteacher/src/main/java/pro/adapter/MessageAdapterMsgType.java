package pro.adapter;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.DeleteMessages;
import com.zjhz.teacher.R;
import com.zjhz.teacher.adapter.MessageAdapter;
import com.zjhz.teacher.bean.Message;
import com.zjhz.teacher.utils.DateUtil;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.zjhz.teacher.R.id.type_news;

/**
 * Created by Administrator on 2017/2/28.
 * 对所有消息的一个适配
 */

public class MessageAdapterMsgType extends MessageAdapter {

//    //要标志已读的消息集合
//    private Set<String> hasReads = new HashSet();
//    private Set<String> hasReadMsgIds = new HashSet();
    //最后显示的条目
    private int max = 0;
    //全文按钮可见的位置（存放linkId）
    private Set<String> visibilities = new HashSet();
    //全文按钮可见并且显示‘收起’的linkId
    private Set<String> ups = new HashSet();
    //右边删除按钮的宽度
    private int rightWidth;
    //删除选择了的position集合
    private Set<String> dels = new HashSet<>();
    //是否显示checkbox
    private boolean isShowCheckbox = false;
    private boolean hasReadMsg = false;

    public void setOnItemRightClickListener(IOnItemRightClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

//    /**
//     * 获取标志已读的未读消息id
//     *
//     * @return
//     */
//    public String hasReadIds() {
//        Iterator it = hasReads.iterator();
//        StringBuilder sb = new StringBuilder();
//        while (it.hasNext()) {
//            sb.append(it.next()).append(",");
//        }
//        hasReads.clear();
//        if (sb.length() == 0) {
//            return null;
//        }
//        return sb.substring(0, sb.length() - 1);
//    }

    /**
     * 获取要删除的消息ids
     *
     * @return
     */
    public String delsIds() {
        StringBuilder sb = new StringBuilder();
        for (String id : dels) {
            sb.append(id + ",");
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.substring(0, sb.length() - 1);
    }

    public List getDelsList() {
        List<String> list = new ArrayList();
        list.addAll(dels);
        return list;
    }

    public boolean isShowCheckBox() {
        return isShowCheckbox;
    }

    public void showCheckBox() {
        isShowCheckbox = true;
        notifyDataSetChanged();
    }

    public void closeCheckBox() {
        isShowCheckbox = false;
        dels.clear();
        max = 0;
        notifyDataSetChanged();
    }

    public MessageAdapterMsgType(Context context, List<Message> messages, int rightWidth) {
        super(context, messages);
        this.rightWidth = rightWidth;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        max = position >= max ? position : max;
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.type_news = (TextView) convertView.findViewById(type_news);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.oval_iv = (ImageView) convertView.findViewById(R.id.oval_iv);
            holder.show_all = (TextView) convertView.findViewById(R.id.show_all);
            holder.item_left = (View) convertView.findViewById(R.id.item_left);
            holder.item_right = (View) convertView.findViewById(R.id.item_right);
            holder.item_right_txt = (TextView) convertView.findViewById(R.id.item_right_txt);
//            holder.poster_tv = (TextView) convertView.findViewById(R.id.poster_tv);
            holder.radio = (CheckBox) convertView.findViewById(R.id.radio);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.radio.setVisibility(isShowCheckbox ? View.VISIBLE : View.GONE);
        //全文收起
        holder.show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUp = ((TextView) v).getText().toString().equals(context.getResources().getString(R.string.show_all));
                if (isUp) {
                    holder.content_tv.setMaxLines(Integer.MAX_VALUE);
                    holder.content_tv.requestLayout();
                    ((TextView) v).setText(context.getResources().getString(R.string.close_show_all));
                    ups.add(messages.get(position).getLinkId());
                } else {
                    holder.content_tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                    holder.content_tv.setMaxLines(2);
                    ((TextView) v).setText(context.getResources().getString(R.string.show_all));
                    ups.remove(messages.get(position).getLinkId());
                }
                notifyDataSetChanged();
            }
        });
        //单删
        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, position);
                }
            }
        });

        //勾选
        holder.radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dels.add(messages.get(position).getLinkId());
                }else{
                    dels.remove(messages.get(position).getLinkId());
                }
            }
        });

        if (isShowCheckbox) {
            if(dels.contains(messages.get(position).getLinkId())){
                holder.radio.setChecked(true);
            }else{
                holder.radio.setChecked(false);
            }
        }
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(rightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        Message bean = getItem(position);

        isVisionable(position, holder);
        if (bean != null) {
            //初始化相关属性，以免错乱
            if (ups.contains(messages.get(position).getLinkId())) {
                holder.content_tv.setMaxLines(Integer.MAX_VALUE);
                holder.show_all.setText(context.getResources().getString(R.string.close_show_all));
            } else {
                holder.content_tv.setMaxLines(2);
                holder.show_all.setText(context.getResources().getString(R.string.show_all));
            }
            String time = DateUtil.getStandardDate(bean.getCreateTime());
            holder.time_tv.setText("【发自："+bean.getNickName()+"】"+time);
            holder.content_tv.setText(bean.getContent());
           /* if (TextUtils.isEmpty(bean.getNickName())) {
                holder.poster_tv.setVisibility(View.GONE);
            } else {
                holder.poster_tv.setVisibility(View.VISIBLE);
                holder.poster_tv.setText("发自" + bean.getNickName());
            }*/
//            isVisionable(position, holder);
            if (bean.getStatus() == 0) {
                holder.oval_iv.setVisibility(View.VISIBLE);
                bean.setStatus(1);
//                hasReads.add(bean.getLinkId());
//                hasReadMsgIds.add(bean.getMsgId());
                Map<String, String> map = new HashMap<>();
                map.put("msgIds", bean.getLinkId());
                map.put("linkIds", bean.getMsgId());
                map.put("phoneNo", SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey));
                NetworkRequest.request(map, CommonUrl.SETHASREAD, "setHasRead");
//                if(!hasReadMsg)
//                    readMsg();
            } else {
                holder.oval_iv.setVisibility(View.GONE);
            }
            if (bean.getMsgNature() == 1) {// 1表示群发消息
                holder.icon.setImageResource(R.mipmap.message_all);
                holder.type_news.setText(R.string.message_list_adapter_type_mass_send);
            } else if (bean.getMsgNature() == 2) {// 2危险区域
                holder.icon.setImageResource(R.mipmap.dangerous);
                holder.type_news.setText(R.string.message_list_adapter_type_dangerous);
            } else if (bean.getMsgNature() == 3) {//3出入校
                holder.icon.setImageResource(R.mipmap.in_out_school);
                holder.type_news.setText(R.string.message_list_adapter_type_in_out_schoole);
            } else if (bean.getMsgNature() == 4) { //4会议通知
                holder.icon.setImageResource(R.mipmap.metting);
                holder.type_news.setText(R.string.message_list_adapter_type_metting);
            }
        }


        return convertView;
    }

//    private void readMsg(){
//        hasReadMsg = true;
//        String linkIds = "", msgIds = "";
//        for(int i = 0; i < messages.size(); i++){
//            if(!TextUtils.isEmpty(messages.get(i).getLinkId()))
//                linkIds += messages.get(i).getLinkId()+",";
//            if(!TextUtils.isEmpty(messages.get(i).getMsgId()))
//                msgIds += messages.get(i).getMsgId()+",";
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("linkIds", linkIds);
//        if(!TextUtils.isEmpty(msgIds))
//            map.put("msgIds", msgIds);
//        map.put("phoneNo", SharedPreferencesUtils.getSharePrefString(ConstantKey.PhoneNoKey));
//        NetworkRequest.request(map, CommonUrl.SETHASREAD, "setHasRead");
//    }

    /**
     * 判断全文按钮是否可见
     * 如果是第一次出现这条信息，判断内容是否显示完全
     * 如果不是第一次出现，则在set中查找是否加入过
     *
     * @param position
     * @param holder
     */
    private void isVisionable(final int position, final ViewHolder holder) {
//        if (position == max) {
            holder.content_tv.post(new Runnable() {
                @Override
                public void run() {
                    Layout l = holder.content_tv.getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0) {
                            if (l.getEllipsisCount(lines - 1) > 0) {
                                holder.show_all.setVisibility(View.VISIBLE);
                                visibilities.add(messages.get(position).getLinkId());
                            } else {
                                if(lines<3) {
                                    holder.show_all.setVisibility(View.INVISIBLE);
                                }else{
                                    holder.show_all.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        /*if(lines>3){
                            holder.show_all.setVisibility(View.VISIBLE);
                            visibilities.add(messages.get(position).getLinkId());
                        } else {
                            holder.show_all.setVisibility(View.INVISIBLE);
                        }*/
                    }
                }
            });
//        } else {
//            if (visibilities.contains(messages.get(position).getLinkId())) {
//                holder.show_all.setVisibility(View.VISIBLE);
//            } else {
//                holder.show_all.setVisibility(View.INVISIBLE);
//
//            }
//        }
    }

    //判断是否显示  全文>   按钮
    private void postRunnable(final ViewHolder holder) {
        holder.show_all.post(new Runnable() {
            @Override
            public void run() {
                Layout l = holder.content_tv.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            holder.show_all.setVisibility(View.VISIBLE);
                            holder.show_all.setOnClickListener(new View.OnClickListener() {
                                //textview是处于下拉还是上拉状态 false 下拉显示全文   TRUE 上拉收起
                                boolean isUp = false;

                                @Override
                                public void onClick(View v) {
                                    if (!isUp) {
                                        isUp = true;
                                        holder.content_tv.setEllipsize(null);
                                        holder.content_tv.requestLayout();
                                        ((TextView) v).setText(context.getResources().getString(R.string.close_show_all));
                                    } else {
                                        isUp = false;
                                        holder.content_tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                                        holder.content_tv.setLines(2);
                                        ((TextView) v).setText(context.getResources().getString(R.string.show_all));
                                    }
                                }
                            });
                        } else {
                            holder.show_all.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
    }

    class ViewHolder {
        ImageView icon, oval_iv;
        TextView content_tv;
        TextView time_tv, show_all, item_right_txt, type_news;/* poster_tv;*/
        View item_left, item_right;
        CheckBox radio;
    }
}
