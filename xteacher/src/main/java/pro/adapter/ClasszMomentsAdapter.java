package pro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhy.adapter.abslistview.MultiItemTypeAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentCommentListParams;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentDelete;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentPraiseListParam;
import com.zjhz.teacher.NetworkRequests.request.ClasszMomentsPraiseOrCancel;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.ClasszMomentCommentBean;
import com.zjhz.teacher.bean.ClasszMomentPariseBean;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.dialog.MyAlertDialog;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.widget.BubblePopupWindow.BubblePopupWindow;

public class ClasszMomentsAdapter extends MultiItemTypeAdapter<ClasszMoment> {

    public static final String SHARE = "1001";
    public static final String DOCUMENT = "1002";
    public static final String HONOUR = "1003";

    private static String TAG;

    private String PRAISE;
    private String CANCEL;
    private String PRAISELIST;
    private String COMMENT;
    private String COMMENTLIST;
    private String DELETECOMMENT;
    private String SHOWCOMMENTDIALOG;
    private String SHOWCOMMENTDIALOGINLIST;

    //当前操作的position
    private int currentPosition;

    public ClasszMomentsAdapter(Context context, List<ClasszMoment> datas, String tag) {
        super(context, datas);
        this.TAG = tag;
        PRAISE = Config.PRAISE + TAG;
        CANCEL = Config.CANCEL + TAG;
        PRAISELIST = Config.PRAISELIST + TAG;
        COMMENT = Config.COMMENT + TAG;
        COMMENTLIST = Config.COMMENTLIST + TAG;
        DELETECOMMENT = Config.DELETECOMMENT+TAG;
        if (TAG.equals("SEARCH")) {
            SHOWCOMMENTDIALOG = Config.SHOWCOMMENTDIALOG+TAG;
            SHOWCOMMENTDIALOGINLIST = Config.SHOWCOMMENTDIALOGINLIST+TAG;
        }else{
            SHOWCOMMENTDIALOG = Config.SHOWCOMMENTDIALOG;
            SHOWCOMMENTDIALOGINLIST = Config.SHOWCOMMENTDIALOGINLIST;
        }
//        EventBus.getDefault().register(this);
    }

    public void regesterEventBus(boolean isRegester){
        if(!isRegester) {
            EventBus.getDefault().unregister(this);
        }else{
            EventBus.getDefault().register(this);
        }
        Log.d("main","-----------------------------------------regesterEventBus   "+isRegester+TAG);
    }

    @Override
    protected void convert(final ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        //设置时间标题用户名等
        holder.setText(R.id.post_name, classzMoment.getPublishName());
        holder.setText(R.id.post_date, classzMoment.getPublishTime().substring(2,classzMoment.getPublishTime().length()-3));
        GlideUtil.loadImageHead(classzMoment.getPhotoUrl(), (ImageView) holder.getView(R.id.header));
        if(!SharePreCache.isEmpty(classzMoment.getCcdTitle())){
            SpannableString sp = new SpannableString("【"+classzMoment.getCcdTitle()+"】"+classzMoment.getCcdContent());
            sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 1, classzMoment.getCcdTitle().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
            ((TextView)holder.getView(R.id.content)).setText(sp);
        }else{
            holder.setText(R.id.content, classzMoment.getCcdContent());
        }
        //评论或点赞弹窗的弹出
        holder.setOnClickListener(R.id.comment_and_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, classzMoment, holder, position);
            }
        });
        //设置点赞
        if((classzMoment.getListComment()==null||classzMoment.getListComment().size()==0)&&(classzMoment.getListPraise()==null||classzMoment.getListPraise().size()==0)){
            holder.setVisible(R.id.comment_like_ll,false);
        }else{
            holder.setVisible(R.id.comment_like_ll, true);
        }
        String userNames = getPraiseUserName(classzMoment.getListPraise());
        if (userNames != null) {
            holder.setVisible(R.id.liked_user_ll, true);
            holder.setText(R.id.liked_user, userNames);
        } else {
            holder.setVisible(R.id.liked_user_ll, false);
        }
        //设置评论列表
        initCommentListView(holder, classzMoment, position);
        //点赞和评论的分割线
        if(classzMoment.getListPraise()!=null&&classzMoment.getListPraise().size()>0&&classzMoment.getListComment()!=null&&classzMoment.getListComment().size()>0){
            holder.setVisible(R.id.comment_like_cutoff,true);
        }else{
            holder.setVisible(R.id.comment_like_cutoff,false);
        }
//        getHomeworkPy(holder, classzMoment, position);
        showDelete(holder, classzMoment, position);
        super.convert(holder, classzMoment, position);
    }


    private void showDelete(final ViewHolder holder, final ClasszMoment classzMoment, final int position){
        if(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey).equals(classzMoment.getPublishUser())){
            holder.setVisible(R.id.delete_image,true);
            holder.getView(R.id.delete_image).requestFocus();
            holder.setOnClickListener(R.id.delete_image, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MyAlertDialog.Builder builder = new MyAlertDialog.Builder(holder.getConvertView().getContext());
                    builder.setMessage("确认删除？");
                    builder.setYesOnClickListener("确定",new MyAlertDialog.OnClickListener(){

                        @Override
                        public void onClick() {
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("did",classzMoment.getdId());
                            NetworkRequest.request(map,CommonUrl.MOMENTDEL,CommonUrl.MOMENTDEL);
                            mDatas.remove(classzMoment);
                            notifyDataSetChanged();
                        }
                    });
                    builder.create().show();
                }
            });
        }else{
            holder.setVisible(R.id.delete_image,false);
        }
    }


    /**
     * 评论列表初始化
     *
     * @param holder
     * @param classzMoment
     * @param position
     */
    private void initCommentListView(final ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        //设置评论列表
        ScrollViewWithListView commentListView = holder.getView(R.id.comments_list);
        commentListView.setDividerHeight(0);
        List<CharSequence> commentList = getComments(classzMoment.getListComment());
        if (commentList != null) {
            commentListView.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> commentAdapter = new ArrayAdapter<>(holder.getConvertView().getContext(), R.layout.classz_moment_comment_textview);
            commentAdapter.addAll(commentList);
            commentListView.setAdapter(commentAdapter);
        }else{
            commentListView.setVisibility(View.GONE);
        }
        commentListView.setTag(position);
        //回复
        commentListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            int listViewPosition = position;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(classzMoment.getListComment().get(position).getUserId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))){
                    final PopupWindow bw = new PopupWindow(holder.getConvertView().getContext());
                    View v = View.inflate(view.getContext(),R.layout.center_textview,null);
                    TextView tv = (TextView)v.findViewById(R.id.tv);
                    tv.setTextColor(view.getContext().getResources().getColor(R.color.gray));
                    tv.setText("删除");
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NetworkRequest.request(new ClasszMomentDelete(classzMoment.getListComment().get(position).getDcId()), CommonUrl.DELETECOMMENT, DELETECOMMENT);
                            bw.dismiss();
                        }
                    });
                    bw.setContentView(v);
                    bw.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.btn_rectangle_radio_gray_background));
                    bw.setOutsideTouchable(true);
                    bw.showAtLocation(holder.getConvertView().getRootView(),Gravity.CENTER,0,0);
                }else {
                    EventBus.getDefault().post(new EventCenter<Object>(SHOWCOMMENTDIALOGINLIST, classzMoment.getListComment().get(position)));
                }
                currentPosition = listViewPosition;
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        switch (event.getEventCode()){
            case CommonUrl.MOMENTDEL:

        }
        pariseAndCommentEventHandler(event);
    }

    /**
     * 评论点赞的弹窗处理
     *
     * @param view
     */
    private void showPopupWindow(View view, final ClasszMoment moment, final ViewHolder holder, final int position) {
        LogUtil.d("showPopupWindow:" + position);
        final BubblePopupWindow popupWindow = new BubblePopupWindow(view.getContext());
        View popView = View.inflate(view.getContext(), R.layout.popupwindow_classz_comment_and_like, null);
        //评论
        popView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventCenter<Object>(SHOWCOMMENTDIALOG, moment.getdId()));
                currentPosition = position;
                popupWindow.dismiss();
            }
        });
        if ("1".equals(moment.getCurrentUserPraiseStatus()))
            ((TextView) popView.findViewById(R.id.like)).setText(R.string.classz_moments_cancel_like);
        else if ("2".equals(moment.getCurrentUserPraiseStatus()))
            ((TextView) popView.findViewById(R.id.like)).setText(R.string.classz_moments_like);
        //点赞点击事件
        popView.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            private int positionOfMoment = position;

            @Override
            public void onClick(View v) {
                currentPosition = positionOfMoment;
                String str = moment.getCurrentUserPraiseStatus();
                if ("2".equals(str)) {
                    //发送点赞任务
                    NetworkRequest.request(new ClasszMomentsPraiseOrCancel(moment.getdId(), String.valueOf(1), String.valueOf(0)),
                            CommonUrl.PRAISEORCANCEL, PRAISE);
//                    moment.setCurrentUserPraiseStatus("1");
                } else if ("1".equals(str)) {
                    //取消点赞
                    NetworkRequest.request(new ClasszMomentsPraiseOrCancel(moment.getdId(), String.valueOf(2), String.valueOf(0)),
                            CommonUrl.PRAISEORCANCEL, CANCEL);
//                    moment.setCurrentUserPraiseStatus("2");
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setBubbleView(popView, Color.TRANSPARENT);
        popupWindow.show(view, Gravity.LEFT);
    }

    /**
     * 点赞和评论的处理
     */
    private void pariseAndCommentEventHandler(EventCenter event) {
        if (event.getEventCode().equals(PRAISE))  //点赞
            NetworkRequest.request(new ClasszMomentPraiseListParam(mDatas.get(currentPosition).getdId()), CommonUrl.PRAISELIST, PRAISELIST);
        if (event.getEventCode().equals(CANCEL))
            NetworkRequest.request(new ClasszMomentPraiseListParam(mDatas.get(currentPosition).getdId()), CommonUrl.PRAISELIST, PRAISELIST);
        if (event.getEventCode().equals(PRAISELIST)) {
            JSONObject praiseList = (JSONObject) event.getData();
            List<ClasszMomentPariseBean> pariseBeans = GsonUtils.toArray(ClasszMomentPariseBean.class, praiseList);
            //判断当前用户是否已赞
            mDatas.get(currentPosition).setCurrentUserPraiseStatus("2");
            for (ClasszMomentPariseBean bean : pariseBeans) {
                if (bean.getUserId().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))) {
                    mDatas.get(currentPosition).setCurrentUserPraiseStatus("1");
                }
            }
            mDatas.get(currentPosition).setListPraise(pariseBeans);
            notifyDataSetChanged();
        }
        if (event.getEventCode().equals(COMMENT))
            NetworkRequest.request(new ClasszMomentCommentListParams(mDatas.get(currentPosition).getdId()), CommonUrl.COMMENTLIST, COMMENTLIST);
        if (event.getEventCode().equals(COMMENTLIST)) {
            JSONObject js = (JSONObject) event.getData();
            List<ClasszMomentCommentBean> commentBeanList = GsonUtils.toArray(ClasszMomentCommentBean.class, js);
            mDatas.get(currentPosition).setListComment(commentBeanList);
            notifyDataSetChanged();
        }
        if(event.getEventCode().equals(DELETECOMMENT)){
            NetworkRequest.request(new ClasszMomentCommentListParams(mDatas.get(currentPosition).getdId()), CommonUrl.COMMENTLIST, COMMENTLIST);
        }
//        if(event.getEventCode().startsWith( "homework_share_select"+TAG)){
//            int position = Integer.parseInt(event.getEventCode().substring(("homework_share_select"+TAG).length()));
//            HomeworkShareBean been = GsonUtils.toObjetJson((JSONObject)event.getData(), HomeworkShareBean.class);
//            if(been != null) {
//                mDatas.get(position).setHomeworkName(been.getHomeworkName());
//                mDatas.get(position).setContent(been.getContent());
//                mDatas.get(position).setPraiseLevelName(been.getPraiseLevelName());
//                mDatas.get(position).setPraiseContent(been.getPraiseContent());
//                mDatas.get(position).setPraiseLevel(been.getPraiseLevel());
//                mDatas.get(position).setPypf(been.getPypf());
//            }
//            notifyDataSetChanged();
//        }
    }

    /**
     * 从点赞集合当中获取点赞人的名字并拼接成字符串
     *
     * @param beans
     * @return
     */
    private String getPraiseUserName(List<ClasszMomentPariseBean> beans) {
        if (beans != null && beans.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (ClasszMomentPariseBean bean : beans) {
                if (!SharePreCache.isEmpty(bean.getUserName())) {
                    sb.append(bean.getUserName() + ",");
                }
                else if (!SharePreCache.isEmpty(bean.getName())) {
                    sb.append(bean.getUserName() + ",");
                }
            }
            String str;
            if(sb.length()==0)
                return null;
            return sb.substring(0, sb.length() - 1);
        } else {
            return null;
        }
    }

    /**
     * 获取评论的拼接字符串列表
     *
     * @param beans
     * @return
     */
    private List<CharSequence> getComments(List<ClasszMomentCommentBean> beans) {
        if (beans != null) {
            List<CharSequence> list = new ArrayList<>();
            for (ClasszMomentCommentBean bean : beans) {
                SpannableString sp = getSpannableStringInComment(bean);
                list.add(sp);
            }
            return list;
        }
        return null;
    }

    /**
     * 拼接评论
     *
     * @param bean
     * @return
     */
    @NonNull
    private SpannableString getSpannableStringInComment(ClasszMomentCommentBean bean) {
        String str;
        SpannableString sp = null;
        if (SharePreCache.isEmpty(bean.getBrepUserName())) {
            if (!SharePreCache.isEmpty(bean.getUserName())) {
                str = bean.getUserName() + ":" + bean.getCommentContent();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (!SharePreCache.isEmpty(bean.getName())) {
                str = bean.getName() + ":" + bean.getCommentContent();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (!SharePreCache.isEmpty(bean.getUserName())) {
                str = bean.getUserName() + " 回复 " + bean.getBrepUserName() + ":" + bean.getCommentContent();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")),
                        bean.getUserName().length() + 4,
                        bean.getUserName().length() + 4 + bean.getBrepUserName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (!SharePreCache.isEmpty(bean.getName())) {
                str = bean.getName() + " 回复 " + bean.getBrepUserName() + ":" + bean.getCommentContent();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")),
                        bean.getName().length() + 4,
                        bean.getName().length() + 4 + bean.getBrepUserName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }

}