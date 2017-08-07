package com.zjhz.teacher.utils.media;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zjhz.teacher.R;

import java.io.IOException;

public class VideoPreviewActivity extends Activity implements SurfaceHolder.Callback{

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private ImageView imageView;
    private String videoUrl;
    private boolean isCreate = false, isPrepareAsync = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        imageView = (ImageView)findViewById(R.id.video_play);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        videoUrl = getIntent().getStringExtra("video_path");

        initView();
        initMediaPlayer();
    }

    private void initView(){
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        // 设置播放时打开屏幕
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);

        findViewById(R.id.frame_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imageView.setVisibility(View.VISIBLE);
                }else {
                    mediaPlayer.start();
                    imageView.setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.vide_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
    }

    private void initMediaPlayer(){
        // 创建MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            Uri uri = Uri.parse(videoUrl);
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepareAsync = true;
                if(isCreate)
                    start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageView.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("error",what+" & "+extra);
                return false;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        isCreate = true;
        if(isPrepareAsync)
            start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void start(){
        // 首先取得video的宽和高
        int vWidth = mediaPlayer.getVideoWidth();
        int vHeight = mediaPlayer.getVideoHeight();
        if(vWidth > vHeight) {
            // 该LinearLayout的父容器 android:orientation="vertical" 必须
            FrameLayout linearLayout = (FrameLayout) findViewById(R.id.frame_layout);
            int lw = linearLayout.getWidth();
            int lh = linearLayout.getHeight();

            if (vWidth > lw || vHeight > lh) {
                // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
                float wRatio = (float) vWidth / (float) lw;
                float hRatio = (float) vHeight / (float) lh;

                // 选择大的一个进行缩放
                float ratio = Math.max(wRatio, hRatio);
                vWidth = (int) Math.ceil((float) vWidth / ratio);
                vHeight = (int) Math.ceil((float) vHeight / ratio);

                // 设置surfaceView的布局参数
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(vWidth, vHeight);
                lp.gravity = Gravity.CENTER;
                surfaceView.setLayoutParams(lp);
            }
        }
        // 然后开始播放视频
        mediaPlayer.start();
    }
}