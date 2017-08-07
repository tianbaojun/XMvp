package com.zjhz.teacher.utils.media;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.BaseUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by zzd on 2017/7/25.
 */

public class AudioPlayerView extends LinearLayout {
    public final int MIN_TIME_LENGTH = 0;
    public final int MAX_TIME_LENGTH = 600;
    public final int MIN_LENGTH = 100;
    public final int MAX_LENGTH = 200;
    private Context context;
    private TextView textViewBg, timeTv;
    private ImageView imageView, deleteImg;
    private MediaPlayer mediaPlayer;
    private OnDeleteListener onDeleteListener;
    private String audioPath;

    private boolean hasData = false;
    private boolean isComplete = false;

    private AnimationDrawable animationDrawable;

    public AudioPlayerView(Context context) {
        this(context, null);
    }

    public AudioPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AudioPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                animationDrawable.stop();
                animationDrawable.selectDrawable(2);
            }
        });

        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.audio_player_view_layout, this, true);
        textViewBg = (TextView) linearLayout.findViewById(R.id.audio_play_bg_tv);
        timeTv = (TextView) linearLayout.findViewById(R.id.time_tv);
        imageView = (ImageView) linearLayout.findViewById(R.id.audio_play_icon);
        animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.selectDrawable(2);
        deleteImg = (ImageView) linearLayout.findViewById(R.id.audio_delete);
        textViewBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        if(!hasData) {
                            Uri uri = Uri.parse(audioPath);
                            mediaPlayer.setDataSource(context, uri);
                            mediaPlayer.prepareAsync();
                        }
                        hasData = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(audioPath)) {
                        return;
                    }
                    if(!isComplete) {
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                                animationDrawable.start();
                                isComplete = true;
                            }
                        });
                    }
                    else {
                        animationDrawable.start();
                        mediaPlayer.start();
                    }
                } else {
                    mediaPlayer.pause();
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(2);
                }
            }
        });

        deleteImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPath != null) {
                    File file = new File(audioPath);
                    file.delete();
                }
                if (onDeleteListener != null) {
                    onDeleteListener.delete(audioPath);
                }
            }
        });
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public void setAudioLength(int timeLength) {
//        int length = 0;
//        if (timeLength > MIN_TIME_LENGTH && timeLength <= MAX_TIME_LENGTH) {
//            length = timeLength - 10;
//        } else if (timeLength > MAX_TIME_LENGTH) {
//            length = MAX_TIME_LENGTH - MIN_TIME_LENGTH;
//        }
//        textViewBg.setWidth(BaseUtil.dp2px(context, (float) (MAX_LENGTH - MIN_LENGTH) / (MAX_TIME_LENGTH - MIN_TIME_LENGTH) * length + MIN_LENGTH));
//
//        String time = timeLength / 60 + "'" + timeLength % 60 + "''";
        timeTv.setText(timeLength + " s");
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setCanDelete(boolean canDelete) {
        if (canDelete) {
            deleteImg.setVisibility(VISIBLE);
        } else {
            deleteImg.setVisibility(GONE);
        }
    }

    public interface OnDeleteListener {
        void delete(String audioPath);
    }
}
