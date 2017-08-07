package pro.adapter.itemDelagate;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.zhy.adapter.abslistview.base.ItemViewDelegate;
import com.zjhz.teacher.NetworkRequests.CommonUrl;
import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.response.HomeworkShareBean;
import com.zjhz.teacher.R;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.ClasszMomentDocumentAttachment;
import com.zjhz.teacher.bean.ImageUrl;
import com.zjhz.teacher.ui.view.ScrollViewWithListView;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;
import com.zjhz.teacher.utils.BaseUtil;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.media.AudioPlayerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.listener.SampleListener;
import pro.widget.NineGridLayout.CustomImageView;
import pro.widget.NineGridLayout.NineGridlayout;

/**
 * Created by Tabjin on 2017/4/14.
 * Description:
 * What Changed:
 */

public class ClasszMomentsShare implements ItemViewDelegate<ClasszMoment> {

    public static final String SHARE = "1001";
    public static String TAG;
    private BaseAdapter adapter;
    private List<ClasszMoment> mDatas;

    private int cachedHeight;
    //是否全屏
    private boolean isFullVideo;

    public ClasszMomentsShare(BaseAdapter adapter, List<ClasszMoment> mDatas, String tag) {
        this.adapter = adapter;
        this.mDatas = mDatas;
        this.TAG = tag;
        EventBus.getDefault().register(this);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_classz_moments_share;
    }

    @Override
    public boolean isForViewType(ClasszMoment item, int position) {
        if (SHARE.equals(item.getDcCode()))
            return true;
        return false;
    }

    @Override
    public void convert(final ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        if (!BaseUtil.isEmpty(classzMoment.getImageUrls())) {
            showPic(holder, classzMoment);
        } else if (!BaseUtil.isEmpty(classzMoment.getListAttachment())) {
            if ("2".equals(classzMoment.getDynamicType())) {
                showVideo(holder, classzMoment, position);
            } else if ("3".equals(classzMoment.getDynamicType())) {
                showVoice(holder, classzMoment, position);
            }
        }else {
            holder.getView(R.id.pic_grid).setVisibility(View.GONE);
            holder.getView(R.id.image_view).setVisibility(View.GONE);
            holder.getView(R.id.video_item_player).setVisibility(View.GONE);
            holder.getView(R.id.voice_list).setVisibility(View.GONE);
        }
        homework(holder, classzMoment, position);
        showFrom(holder, classzMoment, position);
    }

    /**
     * 展示来之哪里
     * 班级圈动态 type：
     * 0	原生
     * 1 	成长日记
     * 2	电子作业
     *
     * @param holder
     * @param classzMoment
     * @param position
     */
    private void showFrom(final ViewHolder holder, final ClasszMoment classzMoment, final int position){
        if (String.valueOf(0).equals(classzMoment.getType())) {
            holder.setVisible(R.id.from, false);
        } else {
            holder.setVisible(R.id.from, true);
            if(String.valueOf(1).equals(classzMoment.getType())){
                holder.setText(R.id.from, "来自成长日记");
            }else{
                holder.setText(R.id.from, "来自电子作业");
            }
        }
    }

    /**
     * 音频
     * @param holder
     * @param classzMoment
     * @param position
     */
    private void showVoice(ViewHolder holder, final ClasszMoment classzMoment, int position){
        holder.getView(R.id.pic_grid).setVisibility(View.GONE);
        holder.getView(R.id.image_view).setVisibility(View.GONE);
        holder.getView(R.id.video_item_player).setVisibility(View.GONE);
        holder.getView(R.id.voice_list).setVisibility(View.VISIBLE);
        ScrollViewWithListView listView = holder.getView(R.id.voice_list);
        listView.setAdapter(new CommonAdapter<ClasszMomentDocumentAttachment>(listView.getContext(), R.layout.audio_player_list_item, classzMoment.getListAttachment()) {
            @Override
            protected void convert(ViewHolder viewHolder, ClasszMomentDocumentAttachment item, int position) {
                AudioPlayerView audioPlayerView = viewHolder.getView(R.id.audio_play_view);
                audioPlayerView.setAudioPath(item.getAttPath());
                audioPlayerView.setAudioLength(Integer.valueOf(item.getRemarks()));
                audioPlayerView.setCanDelete(false);
            }
        } );

    }

    /**
     * 显示视频
     *
     * @param holder
     * @param classzMoment
     */
    private void showVideo(ViewHolder holder, final ClasszMoment classzMoment, int position) {
        holder.getView(R.id.pic_grid).setVisibility(View.GONE);
        holder.getView(R.id.image_view).setVisibility(View.GONE);
        holder.getView(R.id.voice_list).setVisibility(View.GONE);
        final StandardGSYVideoPlayer player = holder.getView(R.id.video_item_player);
        player.setVisibility(View.VISIBLE);
        final View mediaLayout = holder.getView(R.id.media_layout);
        final ImageView imageView = (mediaLayout.getTag() != null) ? (ImageView) mediaLayout.getTag() : new ImageView(mediaLayout.getContext());
        mediaLayout.setTag(imageView);
        player.getThumbImageViewLayout().removeAllViews();
        player.setThumbImageView(imageView);
        //通过获取缩略图控制视频宽高
        Glide.with(mediaLayout.getContext()).
                load(classzMoment.getListAttachment().get(0).getPicPath()).
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
                        setVideoAreaSize(mediaLayout, player,
                                classzMoment.getListAttachment().get(0).getAttPath(),
                                Float.valueOf(resource.getHeight() / new Float(resource.getWidth())));
                    }
                });
        player.getTitleTextView().setVisibility(View.GONE);
        player.getBackButton().setVisibility(View.GONE);
        player.refreshVideo();
        player.setUp(classzMoment.getListAttachment().get(0).getAttPath(), true, null, null);
        //设置全屏按键功能
        player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(player);
            }
        });
        player.setRotateViewAuto(true);
        player.setLockLand(false);
        player.setPlayTag(TAG);
        player.setNeedShowWifiTip(true);
        player.setShowFullAnimation(false);
        //循环
//        player.setLooping(true);
        player.setNeedLockFull(false);

        //player.setSpeed(2);
        player.setPlayPosition(position);

        player.setStandardVideoAllCallBack(new SampleListener(){
            @Override
            public void onClickStartIcon(String url, Object... objects) {
                super.onClickStartIcon(url, objects);
            }

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
    private void setVideoAreaSize(final View view, final StandardGSYVideoPlayer videoView, final String url, final float bili) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (bili > 1) {
                    int width = view.getWidth() / 2;
                    cachedHeight = (int) (width * bili);
                } else {
                    int width = view.getWidth();
                    cachedHeight = (int) (width * bili);
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

    /**
     * 显示图片
     *
     * @param holder
     * @param classzMoment
     */
    private void showPic(ViewHolder holder, ClasszMoment classzMoment) {
        holder.getView(R.id.video_item_player).setVisibility(View.GONE);
        holder.getView(R.id.voice_list).setVisibility(View.GONE);
        NineGridlayout picView = holder.getView(R.id.pic_grid);
        List<ImageUrl> picUrls = classzMoment.getImageUrls();
        final List<String> urlList = new ArrayList<>();
        if (picUrls != null) {
            for (ImageUrl imageUrl : picUrls) {
                urlList.add(imageUrl.getPicPath());
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

    private void homework(ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        holder.setText(R.id.homework_name, classzMoment.getHomeworkName());
        holder.setText(R.id.homework_content, classzMoment.getContent());
        holder.setText(R.id.homework_pfpy, classzMoment.getPypf());
        if ("2".equals(classzMoment.getType())) {
            holder.setVisible(R.id.homework_comment_layout, true);
        } else {
            holder.setVisible(R.id.homework_comment_layout, false);
        }
        LinearLayout layoutClick = holder.getView(R.id.homework_comment_layout_click);
        final LinearLayout layoutShow = holder.getView(R.id.homework_comment_layout_show);
        if (classzMoment.isOpen()) {
            layoutShow.setVisibility(View.VISIBLE);
        } else {
            layoutShow.setVisibility(View.GONE);
        }
        layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutShow.getVisibility() == View.GONE) {
                    layoutShow.setVisibility(View.VISIBLE);
                    classzMoment.setOpen(true);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("taskId", classzMoment.getParentId());
                    NetworkRequest.request(map, CommonUrl.HOMEWORK_SHARE_SELECT, "homework_share_select" + TAG + position);
                } else {
                    layoutShow.setVisibility(View.GONE);
                    classzMoment.setOpen(false);
                }
            }
        });
    }

    @Subscribe
    public void onEventMainThread(EventCenter event) {
        if (event.getEventCode().startsWith("homework_share_select")) {
            if (event.getEventCode().startsWith("homework_share_select" + TAG)) {
                int position = Integer.parseInt(event.getEventCode().substring(("homework_share_select" + TAG).length()));
                HomeworkShareBean been = GsonUtils.toObjetJson((JSONObject) event.getData(), HomeworkShareBean.class);
                if (been != null) {
                    mDatas.get(position).setHomeworkName(been.getHomeworkName());
                    mDatas.get(position).setContent(been.getContent());
                    mDatas.get(position).setPraiseLevelName(been.getPraiseLevelName());
                    mDatas.get(position).setPraiseContent(been.getPraiseContent());
                    mDatas.get(position).setPraiseLevel(been.getPraiseLevel());
                    mDatas.get(position).setPypf(been.getPypf());
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

}
