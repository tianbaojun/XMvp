package com.zjhz.teacher.utils.media;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zjhz.teacher.R;

import java.util.UUID;

/**
 * Created by wanbo on 2017/1/18.
 */

public class VideoRecorderActivity extends AppCompatActivity {

    private MediaUtils mediaUtils;
    private boolean isCancel;
    private VideoProgressBar progressBar;
    private int mProgress;
    private TextView btnInfo , btn;
    private TextView view;
    private SendView send;
    private RelativeLayout recordLayout, switchLayout;

    private final int TIME = 10; //60s

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.main_surface_view);
        // setting
        mediaUtils = new MediaUtils(this);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
        mediaUtils.setTargetDir(Environment.getExternalStorageDirectory().getPath()+"/千校云/video");
        mediaUtils.setTargetName(UUID.randomUUID() + ".mp4");
        mediaUtils.setSurfaceView(surfaceView);
        // btn
        send = (SendView) findViewById(R.id.view_send);
//        view = (TextView) findViewById(R.id.view);
        btnInfo = (TextView) findViewById(R.id.tv_info);
        btn = (TextView) findViewById(R.id.main_press_control);
//        btn.setOnTouchListener(btnTouch);
        btn.setOnClickListener(onClickListener);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.backLayout.setOnClickListener(backClick);
        send.selectLayout.setOnClickListener(selectClick);
        recordLayout = (RelativeLayout) findViewById(R.id.record_layout);
        switchLayout = (RelativeLayout) findViewById(R.id.btn_switch);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isRecording = false;
                if(mediaUtils.isRecording()) {
                    isRecording = true;
                    mediaUtils.stopRecordUnSave();
                }
                mediaUtils.switchCamera();
                if(isRecording){
                    startView();
                    mediaUtils.record();
                }
            }
        });
        // progress
        progressBar = (VideoProgressBar) findViewById(R.id.main_progress_bar);
        progressBar.setOnProgressEndListener(listener);
        progressBar.setCancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setCancel(true);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!mediaUtils.isRecording()){
                startView();
                mediaUtils.record();
            }else {
                stopView(true);
                mediaUtils.stopRecordSave();
            }
        }
    };

    View.OnTouchListener btnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            float downY = 0;
            int action = event.getAction();

            switch (v.getId()) {
                case R.id.main_press_control: {
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            mediaUtils.record();
                            startView();
                            ret = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            if (!isCancel) {
                                if (mProgress == 0) {
                                    stopView(false);
                                    break;
                                }
                                if (mProgress < 10) {
                                    //时间太短不保存
                                    mediaUtils.stopRecordUnSave();
                                    Toast.makeText(VideoRecorderActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                                    stopView(false);
                                    break;
                                }
                                //停止录制
                                mediaUtils.stopRecordSave();
                                stopView(true);
                            } else {
                                //现在是取消状态,不保存
                                mediaUtils.stopRecordUnSave();
                                Toast.makeText(VideoRecorderActivity.this, "取消保存", Toast.LENGTH_SHORT).show();
                                stopView(false);
                            }
                            ret = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float currentY = event.getY();
                            isCancel = downY - currentY > 10;
                            moveView();
                            break;
                    }
                }

            }
            return ret;
        }
    };

    VideoProgressBar.OnProgressEndListener listener = new VideoProgressBar.OnProgressEndListener() {
        @Override
        public void onProgressEndListener() {
            progressBar.setCancel(true);
            mediaUtils.stopRecordSave();
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(mProgress <= 100) {
                        progressBar.setProgress(mProgress);
                    }else {
                        stopView(true);
                        mediaUtils.stopRecordSave();
                    }
                    if (mediaUtils.isRecording()) {
                        mProgress = mProgress + 1;
                        sendMessageDelayed(handler.obtainMessage(0), 10 * TIME);
                    }
                    break;
            }
        }
    };

    private void startView(){
        startAnim();
        mProgress = 0;
        handler.removeMessages(0);
        handler.sendMessage(handler.obtainMessage(0));
    }

    private void moveView(){
        if(isCancel){
            btnInfo.setText("松手取消");
        }else {
            btnInfo.setText("上滑取消");
        }
    }

    private void stopView(boolean isSave){
        stopAnim();
        progressBar.setCancel(true);
        mProgress = 0;
        handler.removeMessages(0);
        btnInfo.setText("双击放大");
        if(isSave) {
            recordLayout.setVisibility(View.GONE);
            send.startAnim();
        }
    }

    private void startAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btn,"scaleX",1,0.5f),
                ObjectAnimator.ofFloat(btn,"scaleY",1,0.5f),
                ObjectAnimator.ofFloat(progressBar,"scaleX",1,1.3f),
                ObjectAnimator.ofFloat(progressBar,"scaleY",1,1.3f)
        );
        set.setDuration(250).start();
    }

    private void stopAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btn,"scaleX",0.5f,1f),
                ObjectAnimator.ofFloat(btn,"scaleY",0.5f,1f),
                ObjectAnimator.ofFloat(progressBar,"scaleX",1.3f,1f),
                ObjectAnimator.ofFloat(progressBar,"scaleY",1.3f,1f)
        );
        set.setDuration(250).start();
    }

    private View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            send.stopAnim();
            recordLayout.setVisibility(View.VISIBLE);
            mediaUtils.deleteTargetFile();
        }
    };

    private View.OnClickListener selectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String videoPath = mediaUtils.getTargetFilePath();
            String picturePath = mediaUtils.getVideoThumb();
            Toast.makeText(VideoRecorderActivity.this, "文件以保存至：" + videoPath, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("video_path", videoPath);
            intent.putExtra("picture_path", picturePath);
            setResult(RESULT_OK, intent);
            finish();
//            send.stopAnim();
//            recordLayout.setVisibility(View.VISIBLE);
        }
    };

}
