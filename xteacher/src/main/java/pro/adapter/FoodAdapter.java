package pro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.PraiseParamTrueFood;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.FoodProBean;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.utils.GlideUtil;
import com.zjhz.teacher.utils.GsonUtils;
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
import pro.widget.NineGridLayout.CustomImageView;
import pro.widget.NineGridLayout.NineGridlayout;

import static com.zjhz.teacher.NetworkRequests.Config.SHOWCOMMENTDIALOG;

public class FoodAdapter extends CommonAdapter<FoodProBean> {

    //当前操作的position
    private int currentPosition;

    public FoodAdapter(Context context, int layoutId, List<FoodProBean> datas) {
        super(context, layoutId, datas);
        regesterEventBus(true);
        showingWindow = new BubblePopupWindow(context);
        View popView = View.inflate(context, R.layout.popupwindow_classz_comment_and_like, null);
        showingWindow.setBubbleView(popView);
    }

    public void regesterEventBus(boolean isRegester) {
        if (!isRegester) {
            EventBus.getDefault().unregister(this);
        } else {
            EventBus.getDefault().register(this);
        }
        Log.d("main", "-----------------------------------------regesterEventBus   " + isRegester);
    }

    @Override
    protected void convert(final ViewHolder holder, final FoodProBean foodbean, final int position) {
        //设置时间标题用户名等
        holder.setText(R.id.post_name, foodbean.getName());
        holder.setText(R.id.post_date, foodbean.getPublishTime().substring(2, foodbean.getPublishTime().length() - 3));
        GlideUtil.loadImageHead(foodbean.getPhotoUrl(), (ImageView) holder.getView(R.id.header));
        holder.setVisible(R.id.type_image,false);
        TextView type_tv = holder.getView(R.id.type_tv);
        type_tv.setText("");
        switch (foodbean.getPattern()){
            case "1":
                type_tv.setBackgroundResource(R.mipmap.food_icon1);
                break;
            case "2":
                type_tv.setBackgroundResource(R.mipmap.food_icon2);
                break;
            case "3":
                type_tv.setBackgroundResource(R.mipmap.food_icon3);
                break;

        }
        holder.setText(R.id.content, foodbean.getContent());
        //评论或点赞弹窗的弹出
        holder.setOnClickListener(R.id.comment_and_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, foodbean, holder, position);
            }
        });
        //设置点赞
        if ((foodbean.getListComment() == null || foodbean.getListComment().size() == 0) && (foodbean.getListPraise() == null || foodbean.getListPraise().size() == 0)) {
            holder.setVisible(R.id.comment_like_ll, false);
        } else {
            holder.setVisible(R.id.comment_like_ll, true);
        }
        String userNames = getPraiseUserName(foodbean.getListPraise());
        if (userNames != null) {
            holder.setVisible(R.id.liked_user_ll, true);
            holder.setText(R.id.liked_user, userNames);
        } else {
            holder.setVisible(R.id.liked_user_ll, false);
        }
        //设置评论列表
        initCommentListView(holder, foodbean, position);
        //点赞和评论的分割线
        if (foodbean.getListPraise() != null && foodbean.getListPraise().size() > 0 && foodbean.getListComment() != null && foodbean.getListComment().size() > 0) {
            holder.setVisible(R.id.comment_like_cutoff, true);
        } else {
            holder.setVisible(R.id.comment_like_cutoff, false);
        }
        //展示图片
        showPic(holder,foodbean);
    }

    /**
     * 评论列表初始化
     *
     * @param holder
     * @param foodbean
     * @param position
     */
    private void initCommentListView(final ViewHolder holder, final FoodProBean foodbean, final int position) {
        //设置评论列表
        ScrollViewWithListView commentListView = holder.getView(R.id.comments_list);
        commentListView.setDividerHeight(0);
        List<CharSequence> commentList = getComments(foodbean.getListComment());
        if (commentList != null) {
            commentListView.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> commentAdapter = new ArrayAdapter<>(holder.getConvertView().getContext(), R.layout.classz_moment_comment_textview);
            commentAdapter.addAll(commentList);
            commentListView.setAdapter(commentAdapter);
        } else {
            commentListView.setVisibility(View.GONE);
        }
        commentListView.setTag(position);
        //回复
        commentListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            int listViewPosition = position;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (foodbean.getListComment().get(position).getCreateUser().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))) {
                    final PopupWindow bw = new PopupWindow(holder.getConvertView().getContext());
                    View v = View.inflate(view.getContext(), R.layout.center_textview, null);
                    TextView tv = (TextView) v.findViewById(R.id.tv);
                    tv.setTextColor(view.getContext().getResources().getColor(R.color.gray));
                    tv.setText("删除");
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            NetworkRequest.request(new ClasszMomentDelete(foodbean.getListComment().get(position).getReplyId()), CommonUrl.DELETECOMMENT, Config.DELETECOMMENT);
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("replyId",foodbean.getListComment().get(position).getReplyId());
                            map.put("cookbookId", foodbean.getCookbookId());
                            NetworkRequest.request(map,CommonUrl.removeCookBookReply, Config.DELETECOMMENT);
                            bw.dismiss();
                        }
                    });
                    bw.setContentView(v);
                    bw.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.btn_rectangle_radio_gray_background));
                    bw.setOutsideTouchable(true);
                    bw.showAtLocation(holder.getConvertView().getRootView(), Gravity.CENTER, 0, 0);
                } /*else {
                    EventBus.getDefault().post(new EventCenter<Object>(SHOWCOMMENTDIALOGINLIST, foodbean.getListComment().get(position)));
                }*/
                currentPosition = listViewPosition;
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        pariseAndCommentEventHandler(event);
    }


    private BubblePopupWindow showingWindow;
    /**
     * 评论点赞的弹窗处理
     *
     * @param view
     */
    private void showPopupWindow(View view, final FoodProBean foodBean, final ViewHolder holder, final int position) {
//        LogUtil.d("showPopupWindow:" + position);
        final BubblePopupWindow popupWindow = showingWindow;
        View popView = popupWindow.getContentView();
        //评论
        popView.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventCenter<Object>(SHOWCOMMENTDIALOG, foodBean.getCookbookId()));
                currentPosition = position;
                popupWindow.dismiss();
            }
        });
        if ("1".equals(foodBean.getCurrentUserPraiseStatus()))
            ((TextView) popView.findViewById(R.id.like)).setText(R.string.classz_moments_cancel_like);
        else if ("2".equals(foodBean.getCurrentUserPraiseStatus()))
            ((TextView) popView.findViewById(R.id.like)).setText(R.string.classz_moments_like);
        //点赞点击事件
        popView.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            private int positionOfMoment = position;

            @Override
            public void onClick(View v) {
                v.setClickable(false);
                currentPosition = positionOfMoment;
                String str = String.valueOf(foodBean.getCurrentUserPraiseStatus());
                if ("2".equals(str)) {
                    NetworkRequest.request(new PraiseParamTrueFood(true ,foodBean.getCookbookId()), CommonUrl.foodPraise, Config.PRAISE);
//                    foodBean.setCurrentUserPraiseStatus("1");
                } else if ("1".equals(str)) {
                    Map<String, String> map = new HashMap<>();
                    NetworkRequest.request(new PraiseParamTrueFood(false ,foodBean.getCookbookId()), CommonUrl.foodPraise,  Config.PRAISE);
//                    foodBean.setCurrentUserPraiseStatus("2");
                }
//                popupWindow.dismiss();
            }
        });
//        popupWindow.setBubbleView(popView, Color.TRANSPARENT);
        popupWindow.show(view, Gravity.LEFT);
        showingWindow = popupWindow;
    }

    /**
     * 点赞和评论的处理
     */
    private void pariseAndCommentEventHandler(EventCenter event) {
        if (event.getEventCode().equals(Config.PRAISE))  //点赞
        {
            Map<String,String> map = new HashMap<>();
            map.put("msgId",mDatas.get(currentPosition).getCookbookId());
            map.put("schoolId",mDatas.get(currentPosition).getSchoolId());
            NetworkRequest.request(map, CommonUrl.COOKBOOKPARISELIST, Config.PRAISELIST);
        }
        /*if (event.getEventCode().equals(Config.CANCEL))
            NetworkRequest.request(new ClasszMomentPraiseListParam(mDatas.get(currentPosition).getCookbookId()), CommonUrl.COOKBOOKPARISELIST,Config.PRAISELIST);*/
        if (event.getEventCode().equals(Config.PRAISELIST)) {
            JSONObject praiseList = (JSONObject) event.getData();
            List<FoodProBean.ListPraiseBean> pariseBeans = GsonUtils.toArray(FoodProBean.ListPraiseBean.class, praiseList);
            //判断当前用户是否已赞
            mDatas.get(currentPosition).setCurrentUserPraiseStatus("2");
            for (FoodProBean.ListPraiseBean bean : pariseBeans) {
                if (bean.getPraiseUser().equals(SharedPreferencesUtils.getSharePrefString(ConstantKey.UserIdKey))) {
                    mDatas.get(currentPosition).setCurrentUserPraiseStatus("1");
                }
            }
            mDatas.get(currentPosition).setListPraise(pariseBeans);
            notifyDataSetChanged();
            showingWindow.dismiss();
            showingWindow.getContentView().findViewById(R.id.like).setClickable(true);
        }
        if (event.getEventCode().equals(Config.COMMENT)||event.getEventCode().equals(Config.DELETECOMMENT)) {
            Map<String, String> map = new HashMap<>();
            map.put("msgId", mDatas.get(currentPosition).getCookbookId());
            NetworkRequest.request(map, CommonUrl.COOKBOOKCOMMENTLIST, Config.COMMENTLIST);
        }
        if (event.getEventCode().equals(Config.COMMENTLIST)) {
            JSONObject js = (JSONObject) event.getData();
            List<FoodProBean.ListCommentBean> commentBeanList = GsonUtils.toArray(FoodProBean.ListCommentBean.class, js);
            mDatas.get(currentPosition).setListComment(commentBeanList);
            notifyDataSetChanged();
        }
    }

    /**
     * 从点赞集合当中获取点赞人的名字并拼接成字符串
     *
     * @param beans
     * @return
     */
    private String getPraiseUserName(List<FoodProBean.ListPraiseBean> beans) {
        if (beans != null && beans.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (FoodProBean.ListPraiseBean bean : beans) {
                if (!SharePreCache.isEmpty(bean.getUserName())) {
                    sb.append(bean.getUserName() + ",");
                }/* else if (!SharePreCache.isEmpty(bean.get())) {
                    sb.append(bean.getUserName() + ",");
                }*/
            }
            if (sb.length() == 0)
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
    private List<CharSequence> getComments(List<FoodProBean.ListCommentBean> beans) {
        if (beans != null) {
            List<CharSequence> list = new ArrayList<>();
            for (FoodProBean.ListCommentBean bean : beans) {
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
    private SpannableString getSpannableStringInComment(FoodProBean.ListCommentBean bean) {
        String str;
        SpannableString sp = null;
        if (SharePreCache.isEmpty(bean.getBrepUserName())) {
            if (!SharePreCache.isEmpty(bean.getUserName())) {
                str = bean.getUserName() + ":" + bean.getMsgContect();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } /*else if (!SharePreCache.isEmpty(bean.getName())) {
                str = bean.getName() + ":" + bean.getCommentContent();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }*/
        } else {
            if (!SharePreCache.isEmpty(bean.getUserName())) {
                str = bean.getUserName() + " 回复 " + bean.getBrepUserName() + ":" + bean.getMsgContect();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")),
                        bean.getUserName().length() + 4,
                        bean.getUserName().length() + 4 + bean.getBrepUserName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } /*else if (!SharePreCache.isEmpty(bean.getName())) {
                str = bean.getName() + " 回复 " + bean.getBrepUserName() + ":" + bean.getMsgContect();
                sp = new SpannableString(str);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")), 0, bean.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp.setSpan(new ForegroundColorSpan(Color.parseColor("#576B95")),
                        bean.getName().length() + 4,
                        bean.getName().length() + 4 + bean.getBrepUserName().length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }*/
        }
        return sp;
    }

    /**
     * 显示图片
     *
     * @param holder
     * @param foodbean
     */
    private void showPic(ViewHolder holder, FoodProBean foodbean) {
        NineGridlayout picView = holder.getView(R.id.pic_grid);
        List<FoodProBean.ImageUrlsBean> picUrls = foodbean.getImageUrls();
        final List<String> urlList = new ArrayList<>();
        if (picUrls != null) {
            for (FoodProBean.ImageUrlsBean imageUrl : picUrls) {
                urlList.add(imageUrl.getDocPath());
            }
        } else {
            picView.setVisibility(View.GONE);
            holder.getView(R.id.image_view).setVisibility(View.GONE);
        }
        if (urlList.size() == 1) {
            picView.setVisibility(View.GONE);
            holder.getView(R.id.image_view).setVisibility(View.VISIBLE);
            ((CustomImageView) holder.getView(R.id.image_view)).setImageUrl(urlList.get(0));
            holder.setOnClickListener(R.id.image_view, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] urls = {urlList.get(0)};
                    ImageGalleryActivity.show(v.getContext(), urls, 0, false);
                }
            });
        } else if (urlList.size() > 1) {
            picView.setVisibility(View.VISIBLE);
            holder.setVisible(R.id.image_view, false);
            picView.setImagesData(urlList);
            picView.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, View view) {
                    String[] urls = new String[urlList.size()];
                    for (int i = 0; i < urlList.size(); i++) {
                        urls[i] = urlList.get(i);
                    }
                    ImageGalleryActivity.show(view.getContext(), urls, position, false);
                }
            });
        }
    }
}