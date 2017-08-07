package com.zjhz.teacher.utils.media;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zjhz.teacher.R;

import android.widget.ImageView;

import java.util.UUID;

/**
 * Created by zzd on 2017/7/21.
 */

public class AudioRecordView extends FrameLayout {

    private AudioRecord audioRecord;
    private Context context;
    private MediaUtils mediaUtils;
    private boolean isCanRecord = true;
    private long time;

    public AudioRecordView(Context context) {
        this(context, null);
    }

    public AudioRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public AudioRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    private void initView() {
        mediaUtils = new MediaUtils((Activity) context);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_AUDIO);

        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.audio_record_view_layout, this, true);
        final ImageView imageView = (ImageView) frameLayout.findViewById(R.id.voice_recorder);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanRecord)
                    return;
                AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                if (!mediaUtils.isRecording()) {
                    initMedia();
                    animationDrawable.start();
                    mediaUtils.record();
                    time = System.currentTimeMillis();
                } else {
                    mediaUtils.stopRecordSave();
                    String path = mediaUtils.getTargetFilePath();
                    animationDrawable.stop();

                    if (audioRecord != null)
                        audioRecord.complete(path, (int) ((System.currentTimeMillis() - time)/1000 + 1));
                }
            }
        });
    }

    private void initMedia() {

        mediaUtils.setTargetDir(Environment.getExternalStorageDirectory().getPath() + "/千校云/audio");
        mediaUtils.setTargetName(UUID.randomUUID() + ".mp3");
    }

    public void setAudioRecord(AudioRecord audioRecord) {
        this.audioRecord = audioRecord;
    }

    public boolean isCanRecord() {
        return isCanRecord;
    }

    public void setCanRecord(boolean canRecord) {
        isCanRecord = canRecord;
    }

    public interface AudioRecord {
        void complete(String path, int duration);
    }
}
