package com.zjhz.teacher.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-2
 * Time: 15:57
 * Description: 倒计时
 */
public class CountDown {
    public static final int VALIDATE_CODE = 1;		// 获取验证码倒计时
    public static final int VALIDATE_FINISH = 7;    // 获取验证码
    private int countTime = 60;
    private TextView obtain;
    private Timer timer;

    public CountDown(TextView obtain) {
        this.obtain = obtain;
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case VALIDATE_CODE:
                    Integer time = (Integer) msg.obj;
                    obtain.setClickable(false);
                    obtain.setText(time.intValue() + "秒后再次尝试");
                    break;
                case VALIDATE_FINISH:
                    obtain.setText("获取验证码");
                    obtain.setClickable(true);
                    obtain.setBackgroundResource(R.color.main_bottom_button);
                    break;
            }
        }
    };

    public void countDown(){
        timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                countTime --;
                Message msg = Message.obtain();
                msg.what = VALIDATE_CODE;
                msg.obj = countTime;
                handler.sendMessage(msg);
                if(countTime == 0){
                    countTime = 60;
                    timer.cancel();
                    handler.sendEmptyMessage(VALIDATE_FINISH);
                }
            }
        }, 0, 1000);
    }
}
