package pro.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CommentParams;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.CompanyNewsCommentDetail;
import com.zjhz.teacher.ui.dialog.WaitDialog;
import com.zjhz.teacher.ui.view.HintPopwindow;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;
import com.zjhz.teacher.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zjhz.teacher.R.id.dianzan_iv;


/**
 * Created by Administrator on 2017/6/14.
 */

public class CompanyNewsCommentReplyAdapter extends CommonAdapter<CompanyNewsCommentDetail.ReplyListBean> {

    private WaitDialog dialog;

    private HintPopwindow hintPopwindow;
    private final String SEPRATOR = ">>>";


    public CompanyNewsCommentReplyAdapter(Context context, int layoutId, List<CompanyNewsCommentDetail.ReplyListBean> datas) {
        super(context, layoutId, datas);
        dialog = new WaitDialog(context);
        hintPopwindow = new HintPopwindow(context);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void convert(final ViewHolder viewHolder, final CompanyNewsCommentDetail.ReplyListBean item, final int position) {
        GlideUtil.loadImageHead(item.getPhotoUrl(), (ImageView) viewHolder.getView(R.id.comment_header_iv));
        viewHolder.setText(R.id.comment_username_tv, item.getUserName());
        viewHolder.setText(R.id.comment_time_tv, item.getCreateTime());
        if (!SharePreCache.isEmpty(item.getRefReplyId()) && !SharePreCache.isEmpty(item.getRootId())) {
            if (item.getRefReplyId().equals(item.getRootId())) {
                viewHolder.setText(R.id.comment_content_tv, item.getReplyContent());
            } else {
                viewHolder.setText(R.id.comment_content_tv, "回复 " + item.getReplyUserName() + ":" + item.getReplyContent());
            }
        } else {
            viewHolder.setText(R.id.comment_content_tv, item.getReplyContent());
        }
        viewHolder.setText(R.id.comment_praise_num, String.valueOf(item.getPraiseNum()));
        if(item.getReplyNum()>0){
            viewHolder.setText(R.id.reply_num,"回复 · "+item.getReplyNum());
            viewHolder.setVisible(R.id.reply_num, true);
        }else{
            viewHolder.setVisible(R.id.reply_num, false);
        }
        if (item.getPraiseStatus() == 1) {
            viewHolder.setImageResource(dianzan_iv, R.mipmap.dianzan2_pre_1);
        } else {
            viewHolder.setImageResource(dianzan_iv, R.mipmap.dianzan2_nor_1);
        }
        //删除图标是否显示
        if (item.getCreateUser().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))) {
            viewHolder.setVisible(R.id.comment_time_delete, true);
        } else {
            viewHolder.setVisible(R.id.comment_time_delete, false);
        }
        //点赞
        viewHolder.setOnClickListener(dianzan_iv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("newsId", item.getNewsId());
                map.put("replyId", item.getReplyId());
                if (item.getPraiseStatus() == 1) {
                    map.put("praiseStatus", String.valueOf(0));
                } else {
                    map.put("praiseStatus", String.valueOf(1));
                }
                NetworkRequest.request(map, CommonUrl.COMPANYNEWSPRAISEOCANCEL, Config.COMPANYNEWSPRAISEOCANCEL + SEPRATOR + item.getReplyId());
                dialog.show();
            }
        });
        //删除操作
        viewHolder.setOnClickListener(R.id.comment_time_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintPopwindow.setTitleMessage("确认删除此评论吗?");
                hintPopwindow.setOnclicks(new HintPopwindow.OnClicks() {
                    @Override
                    public void sureClick() {
                        Map<String, String> map = new HashMap<>();
                        map.put("replyId", item.getReplyId());
                        map.put("newsId", item.getNewsId());
                        NetworkRequest.request(map, CommonUrl.COMPANYNEWSREMOVEREPLY, Config.COMPANYNEWSREMOVEREPLY + SEPRATOR + item.getReplyId());
                        dialog.show();
                        hintPopwindow.dismiss();
                    }

                    @Override
                    public void cancelClick() {
                        hintPopwindow.dismiss();
                    }
                });
                if (!hintPopwindow.isShowing()) {
                    hintPopwindow.showAtLocation(viewHolder.getConvertView().getRootView(), Gravity.CENTER, 0, 0);
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        JSONObject js = (JSONObject) event.getData();
        switch (event.getEventCode()) {
            case Config.ERROR:
                dialog.dismiss();
                ToastUtils.showShort("无网络");
                break;
            case Config.NOSUCCESS:
                dialog.dismiss();
                ToastUtils.showShort("请求错误");
                try {
                    if (Config.COMPANYNEWSREMOVEREPLY.equals(js.getString("config"))) {
                        ToastUtils.showShort("删除失败");
                    }
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        //点赞
        if (event.getEventCode().startsWith(Config.COMPANYNEWSPRAISEOCANCEL + SEPRATOR)) {
            dialog.dismiss();
            String replyId = event.getEventCode().substring((Config.COMPANYNEWSPRAISEOCANCEL + SEPRATOR).length());
            for (CompanyNewsCommentDetail.ReplyListBean bean : mDatas) {
                if (bean.getReplyId().equals(replyId)) {
                    if (bean.getPraiseStatus() == 1) {
                        bean.setPraiseStatus(0);
                    } else {
                        bean.setPraiseStatus(1);
                    }

                    try {
                        bean.setPraiseNum(Integer.valueOf(js.getJSONObject("data").optString("praiseNum")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();
                }
            }
        }
        //回复
        if (event.getEventCode().startsWith(Config.COMPANYNEWSREMOVEREPLY)) {
            dialog.dismiss();
            String replyId = event.getEventCode().substring((Config.COMPANYNEWSREMOVEREPLY + SEPRATOR).length());
            CompanyNewsCommentDetail.ReplyListBean deleteBean = null;
            for (CompanyNewsCommentDetail.ReplyListBean bean : mDatas) {
                if (bean.getReplyId().equals(replyId)) {
                    deleteBean = bean;
                }
            }
            if (deleteBean != null) {
                mDatas.remove(deleteBean);
            }
            NetworkRequest.request(new CommentParams(deleteBean.getNewsId(), 1, 4), CommonUrl.COMPANYNEWSREPLYLIST, Config.NEWSREPLAYLIST);
            notifyDataSetChanged();
        }
    }
}
