package com.zjhz.teacher.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zjhz.teacher.BR;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.GrowthDailyBeanRes;
import com.zjhz.teacher.NetworkRequests.response.HomeworkShareBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.AudioBean;
import com.zjhz.teacher.ui.activity.GrowthArchivesActivity;
import com.zjhz.teacher.ui.adapter.ListViewDBAdapter;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.ui.view.popuwindow.SharePopupWindow;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.ToastUtils;
import com.zjhz.teacher.utils.media.AudioPlayerView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.listener.SampleListener;
import pro.widget.NineGridLayout.HoldScaleImageView;
import pro.widget.NineGridLayout.NineGridlayout;

/**
 * Created by zzd on 2017/6/20.
 */

public class GrowthDailyFragment extends ListFragment<GrowthDailyBeanRes> {

    private static final String SID = "sid";
    private static final String CID = "cid";

    private String calendarId = "";

    private List<HomeworkShareBean> shareListBeenList = new ArrayList<>();

    private int cachedHeight;
    //是否全屏
    private boolean isFullVideo;

    public static GrowthDailyFragment newInstance(String studentId, String classId) {
        GrowthDailyFragment fragment = new GrowthDailyFragment();
        Bundle args = new Bundle();
        args.putString(SID, studentId);
        args.putSerializable(CID, classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initParams() {
        listItemLayoutId = R.layout.growth_daily_layout;
        BRId = BR.growthDailyBean;
    }

    @Override
    protected void initViewsAndEvents() {
        listView.setDividerHeight((int) getResources().getDimension(R.dimen.dp_10));

        adapter.addListVariable(BR.homeworkshareBean, shareListBeenList);
        adapter.setItemCallBack(new ListViewDBAdapter.ItemCallBack() {
            @Override
            public void item(View rootView, final int position) {
                rootView.findViewById(R.id.pic_grid).setVisibility(View.GONE);
                rootView.findViewById(R.id.image_view).setVisibility(View.GONE);
                rootView.findViewById(R.id.video_item_player).setVisibility(View.GONE);
                rootView.findViewById(R.id.audio_play_list).setVisibility(View.GONE);
                if (dataList.get(position).getDetailList() != null && dataList.get(position).getDetailList().size() > 0) {
                    if ("mp4".equals(dataList.get(position).getDetailList().get(0).getAttExtName().trim()))
                        showVideo(rootView, dataList.get(position).getDetailList().get(0), position);
                    if ("mp3".equals(dataList.get(position).getDetailList().get(0).getAttExtName().trim()))
                        showAudio(rootView, dataList.get(position).getDetailList(), position);
                    if (!"mp3".equals(dataList.get(position).getDetailList().get(0).getAttExtName().trim())
                            && !"mp4".equals(dataList.get(position).getDetailList().get(0).getAttExtName().trim()))
                        showPic(rootView, dataList.get(position));
                }
                ImageView deleteImg = (ImageView) rootView.findViewById(R.id.growth_daily_delete);
                if ("我的成长日记".equals(dataList.get(position).getCategoryTitle())) {
                    deleteImg.setVisibility(View.VISIBLE);
                    deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(dataList.get(position), position);
                        }
                    });
                } else {
                    deleteImg.setVisibility(View.GONE);
                    deleteImg.setOnClickListener(null);
                }
                if ("来自电子作业".equals(dataList.get(position).getCategoryTitle())) {
                    rootView.findViewById(R.id.homework_comment_layout).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.homework_comment_layout).setVisibility(View.GONE);
                }
                rootView.findViewById(R.id.growth_daily_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SharePopupWindow(getContext(), listView)
                                .setShare(new SharePopupWindow.Share() {
                                    @Override
                                    public void bjq() {
                                        shareBqj(dataList.get(position));
                                    }
                                }).show();
                    }
                });
                LinearLayout layoutClick = (LinearLayout) rootView.findViewById(R.id.homework_comment_layout_click);
                final LinearLayout layoutShow = (LinearLayout) rootView.findViewById(R.id.homework_comment_layout_show);
                layoutClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (layoutShow.getVisibility() == View.GONE) {
                            layoutShow.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(shareListBeenList.get(position).getHomeworkName())) {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("taskId", dataList.get(position).getMemoryId());
                                NetworkRequest.request(map, CommonUrl.HOMEWORK_SHARE_SELECT, "homework_share_select" + position);
                            }
                        } else
                            layoutShow.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void showAudio(View rootView, List<GrowthDailyBeanRes.DetailListBean> audioPath, int position) {
        ScrollViewWithListView listView = (ScrollViewWithListView) rootView.findViewById(R.id.audio_play_list);
        listView.setVisibility(View.VISIBLE);
        CommonAdapter audioPlayerAdapter = new CommonAdapter<GrowthDailyBeanRes.DetailListBean>(context, R.layout.audio_player_list_item, audioPath) {
            @Override
            protected void convert(ViewHolder viewHolder, GrowthDailyBeanRes.DetailListBean item, int position) {
                AudioPlayerView audioPlayerView = viewHolder.getView(R.id.audio_play_view);
                audioPlayerView.setAudioPath(item.getPath());
                int time = 0;
                if (!TextUtils.isEmpty(item.getRemarks())) {
                    time = Integer.parseInt(item.getRemarks());
                }
                audioPlayerView.setAudioLength(time);
                audioPlayerView.setCanDelete(false);
            }
        };
        listView.setAdapter(audioPlayerAdapter);
    }

    /**
     * 显示视频
     *
     * @param rootView
     * @param listAttachmentBean
     */
    private void showVideo(View rootView, final GrowthDailyBeanRes.DetailListBean listAttachmentBean, int position) {
        final StandardGSYVideoPlayer player = (StandardGSYVideoPlayer) rootView.findViewById(R.id.video_item_player);
        player.setVisibility(View.VISIBLE);
        final View mediaLayout = rootView.findViewById(R.id.media_layout);
        final ImageView imageView = (mediaLayout.getTag() != null) ? (ImageView) mediaLayout.getTag() : new ImageView(mediaLayout.getContext());
        mediaLayout.setTag(imageView);
        player.getThumbImageViewLayout().removeAllViews();
        player.setThumbImageView(imageView);
        //通过获取缩略图控制视频宽高
        Glide.with(mediaLayout.getContext()).
                load(listAttachmentBean.getPicPath()).
                asBitmap().
                into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        params.width = resource.getWidth();
                        params.height = resource.getHeight();
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        imageView.setLayoutParams(params);
                        imageView.setImageBitmap(resource);
                        /*setVideoAreaSize(mediaLayout, player,
                                listAttachmentBean.getPath(),
                                Float.valueOf(resource.getHeight() / new Float(resource.getWidth())));*/
                        setVideoAreaSize(mediaLayout, player,
                                resource.getHeight(),
                                resource.getWidth());
                    }
                });
        player.setUp(listAttachmentBean.getPath(), true, null, null);
        player.getTitleTextView().setVisibility(View.GONE);
        player.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(player);
            }
        });
        player.setRotateViewAuto(true);
        player.setLockLand(false);
//        player.setPlayTag(TAG);
        player.setShowFullAnimation(false);
        //循环
        //player.setLooping(true);
        player.setNeedLockFull(true);
        player.setNeedShowWifiTip(true);
        //player.setSpeed(2);
        player.setPlayPosition(position);

        player.setStandardVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                if (!player.isIfCurrentIsFullscreen()) {
                    //静音
                    GSYVideoManager.instance().setNeedMute(false);
                }

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                //全屏不静音
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
            }
        });

    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(standardGSYVideoPlayer.getContext(), false, true);
        isFullVideo = true;
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize(final View view, final StandardGSYVideoPlayer videoView, final int picWidth, final int picHeight) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (picHeight>picWidth) {
                    int width = view.getWidth() / 2;
                    cachedHeight = (width * picHeight/picWidth);
                } else {
                    int width = view.getWidth();
                    cachedHeight = (width * picHeight/picWidth);
                }
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
                ViewGroup.LayoutParams videoLayoutParams = videoView.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                videoView.setLayoutParams(videoLayoutParams);
                videoView.requestFocus();
            }
        });
    }

    private void shareBqj(GrowthDailyBeanRes beanRes) {
        Map<String, String> map = new HashMap<>();
        map.put("sectionCode", "1001");
        map.put("classId", getArguments().getString(CID));
        map.put("ccdContent", beanRes.getContent());

        map.put("isshare", "1");
        map.put("parentId", beanRes.getMemoryId());
        if ("我的成长日记".equals(beanRes.getCategoryTitle())) {
            map.put("type", "1");//0	原生;  1 成长日记;  2	电子作业
        } else if ("来自电子作业".equals(beanRes.getCategoryTitle())) {
            map.put("type", "2");
        }

        if (beanRes.getDetailList() != null && beanRes.getDetailList().size() > 0) {
            if ("mp4".equals(dataList.get(0).getDetailList().get(0).getAttExtName().trim())) {
                map.put("attPicPath", dataList.get(0).getDetailList().get(0).getPicPath());
                map.put("attPath", dataList.get(0).getDetailList().get(0).getPath());
            }
            if ("mp3".equals(dataList.get(0).getDetailList().get(0).getAttExtName().trim())) {
                List<AudioBean> audioBeanList = new ArrayList<>();
                for (GrowthDailyBeanRes.DetailListBean detailListBean : beanRes.getDetailList()) {
                    int time = 0;
                    if (!TextUtils.isEmpty(detailListBean.getRemarks())) {
                        time = Integer.parseInt(detailListBean.getRemarks());
                    }
                    AudioBean audio = new AudioBean(detailListBean.getPath(), time);
                    audioBeanList.add(audio);
                }
                map.put("voiceList", GsonUtils.toJson(audioBeanList));
            }
            if (!"mp3".equals(dataList.get(0).getDetailList().get(0).getAttExtName().trim())
                    && !"mp4".equals(dataList.get(0).getDetailList().get(0).getAttExtName().trim())) {
                String picUrls = "";

                for (GrowthDailyBeanRes.DetailListBean bean : beanRes.getDetailList()) {
                    picUrls += bean.getPath() + ",";
                }

                if (!TextUtils.isEmpty(picUrls)) {
                    picUrls = picUrls.substring(0, picUrls.length() - 1);
                }
                map.put("picUrls", picUrls);
            }
        }

        NetworkRequest.request(map, CommonUrl.MOMENTSAVE, "growth_daily_share");
    }

    private void delete(GrowthDailyBeanRes beanRes, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("memoryId", beanRes.getMemoryId());
        if (!TextUtils.isEmpty(beanRes.getContent()) && beanRes.getDetailList().size() == 0) {
            map.put("type", "SYS_HOMEWORK_TYPE_1");//全文字
        }
        if (TextUtils.isEmpty(beanRes.getContent()) && beanRes.getDetailList().size() > 0) {
            map.put("type", "SYS_HOMEWORK_TYPE_2");//图片
        }
        if (!TextUtils.isEmpty(beanRes.getContent()) && beanRes.getDetailList().size() > 0) {
            map.put("type", "SYS_HOMEWORK_TYPE_3");//图文结合
        }
        map.put("shareFlag", beanRes.getShareFlag() + "");
        NetworkRequest.request(map, CommonUrl.GROWTH_DAILY_DELETE, "growth_daily_delete" + position);
    }

    private void showPic(View rootView, GrowthDailyBeanRes beanRes) {
        NineGridlayout pic_grid = (NineGridlayout) rootView.findViewById(R.id.pic_grid);
        HoldScaleImageView image_view = (HoldScaleImageView) rootView.findViewById(R.id.image_view);
        List<GrowthDailyBeanRes.DetailListBean> picUrls = beanRes.getDetailList();
        final List<String> urlList = new ArrayList<>();
        if (picUrls != null) {
            for (GrowthDailyBeanRes.DetailListBean detailListBean : picUrls) {
                urlList.add(detailListBean.getPath());
            }
        } else {
            pic_grid.setVisibility(View.GONE);
            image_view.setVisibility(View.GONE);
        }
        if (urlList.size() == 1) {
            pic_grid.setVisibility(View.GONE);
            image_view.setVisibility(View.VISIBLE);
            image_view.setImageUrl(urlList.get(0));
            image_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] urls = {urlList.get(0)};
                    ImageGalleryActivity.show(v.getContext(), urls, 0, false);
                }
            });
        } else if (urlList.size() > 1) {
            pic_grid.setVisibility(View.VISIBLE);
            image_view.setVisibility(View.GONE);
            pic_grid.setImagesData(urlList);
            pic_grid.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
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

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void getSearcherList() {
        boolean isFirst = getArguments().getBoolean(IS_FIRST);
        if (isFirst && !refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(true);
        }
        Map<String, String> map = new HashMap<>();
        map.put("studentId", getArguments().getString(SID));
        map.put("pageSize", "10");
        map.put("page", page + "");
        map.put("calendarId", calendarId);
        NetworkRequest.request(map, CommonUrl.GROWTH_DAILY_LIST, "growth_daily_list");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    @Subscribe
    public void onEventMainThread(EventCenter event) throws JSONException {
        super.onEventMainThread(event);
        switch (event.getEventCode()) {
            case "growth_daily_list":
                if (page == 1) {
                    dataList.clear();
                    shareListBeenList.clear();
                }
                List<GrowthDailyBeanRes> list = GsonUtils.toArray(GrowthDailyBeanRes.class, (JSONObject) event.getData());
                dataList.addAll(list);
                for (int i = 0; i < list.size(); i++) {
                    shareListBeenList.add(new HomeworkShareBean());
                }
                notifyDataSetChanged();
                break;
            case "growth_daily_delete":
                ToastUtils.showShort("删除成功");
                break;
            case "growth_daily_share":
                ToastUtils.showShort("分享成功");
                break;
            case GrowthArchivesActivity.TERMPOST:   //切换calendarid
                String id = event.getData().toString();
                if (!SharePreCache.isEmpty(id)) {
                    calendarId = id;
                }
                getSearcherList();
                break;
        }
        if (event.getEventCode().contains("growth_daily_delete")) {
            int length = "growth_daily_delete".length();
            int position = Integer.parseInt(event.getEventCode().substring(length));
            dataList.remove(position);
            adapter.notifyDataSetChanged();
        }
        if (event.getEventCode().startsWith("homework_share_select")) {
            int position = Integer.parseInt(event.getEventCode().substring("homework_share_select".length()));
            shareListBeenList.set(position, GsonUtils.toObjetJson((JSONObject) event.getData(), HomeworkShareBean.class));
            notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        getSearcherList();
    }

    @Override
    public void onLoad() {
        page++;
        getSearcherList();
    }

}
