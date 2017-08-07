package com.zjhz.teacher.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zjhz.teacher.NetworkRequests.Config;
import com.zjhz.teacher.NetworkRequests.ConstantKey;
import com.zjhz.teacher.NetworkRequests.response.JPushBean;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpLoadSucBean;
import com.zjhz.teacher.ui.activity.LoginActivity;
import com.zjhz.teacher.ui.activity.MessageDetailActivity;
import com.zjhz.teacher.ui.activity.ReceiveDangerousListActivity;
import com.zjhz.teacher.utils.GsonUtils;
import com.zjhz.teacher.utils.LogUtil;
import com.zjhz.teacher.utils.SharePreCache;
import com.zjhz.teacher.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by xiangxue on 2016/6/18.
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    private static void exit() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                LogUtil.e("JPushReceiver", "退出成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                LogUtil.d("JPushReceiver", "退出失败！");
            }
        });
    }

    private static void exitAlias(Context context) {
        JPushInterface.setAlias(context, "", new TagAliasCallback() {

            @Override
            public void gotResult(int code, String alias, Set<String> tags) {
                String logs;
                switch (code) {
                    case 0:
                        logs = "Set tag and alias success" + alias;
                        Log.i("NetworkRequest", logs);
                        break;
                    default:
                        logs = "Failed with errorCode = " + code;
                        Log.e("NetworkRequest", logs);
                }
            }
        });
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " +"==自定义消息: " + printBundle(bundle));
            upLoadSuc(bundle);
            receiverMessage(context, bundle,1);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " +"==通知: " + printBundle(bundle));
            receiverMessage(context, bundle,2);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //打开自定义的Activity
            String jsonObject = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JPushBean bean = GsonUtils.toObject(jsonObject, JPushBean.class);
            Intent intents = null;
            if (bean != null && bean.getForcedOffLine() != 0){
                if (bean.getmType() == 1){
                    intents = new Intent(context, ReceiveDangerousListActivity.class);
                }else {
                    intents = new Intent(context, MessageDetailActivity.class);
                    String msgId = bean.getMsgId();
                    String meetId = bean.getMeetId();
                    if (!SharePreCache.isEmpty(meetId)){
                        intents.putExtra("type",1);
                        intents.putExtra("id",meetId);
                    }else if (!SharePreCache.isEmpty(msgId)){
                        intents.putExtra("type",2);
                        intents.putExtra("id",msgId);
                    }
                    intents.putExtra("isupdate",true);
                }
            }
            if (intents != null){
                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(intents);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        }else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void upLoadSuc(Bundle bundle) {
        String jsonObject = bundle.getString(JPushInterface.EXTRA_EXTRA);
        UpLoadSucBean bean = GsonUtils.toObject(jsonObject, UpLoadSucBean.class);
        if (bean == null || bean.getAttName() == null)
            return;
        LogUtil.e("广播接收到消息 = ", "JpushReceive");
        EventBus.getDefault().post(new EventCenter<UpLoadSucBean>(Config.UPLOAD_SUC, bean));
    }

    //接收消息处理
    private void receiverMessage(Context context, Bundle bundle,int type) {
        String jsonObject = bundle.getString(JPushInterface.EXTRA_EXTRA);
        UpLoadSucBean upLoadSucBean = GsonUtils.toObject(jsonObject, UpLoadSucBean.class);
        if (upLoadSucBean != null && upLoadSucBean.getAttName() != null && upLoadSucBean.getAttPath() != null)
            return;

        JPushBean bean = GsonUtils.toObject(jsonObject, JPushBean.class);
        if (bean != null && bean.getForcedOffLine() == 0){
            if (!ConstantKey.isLogin){
                ConstantKey.isLogin = true;
                exit();
                exitAlias(context);
                SharePreCache.logOut();
                AppContext.getInstance().finishAllActivitys();
                SharedPreferencesUtils.putSharePrefBoolean("net_base",true);
                Intent intentLogin = new Intent(context, LoginActivity.class);
                if (type == 1){
                    intentLogin.putExtra("content",bundle.getString(JPushInterface.EXTRA_MESSAGE));
                }else if (type == 2){
                    intentLogin.putExtra("content",bundle.getString(JPushInterface.EXTRA_ALERT));
                }
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(intentLogin);
            }
        }else {
            LogUtil.e("广播接收到消息 = ","JpushReceive");
            EventBus.getDefault().post(new EventCenter("updateDataNum",null));
        }
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (ConstantKey.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
        }
    }
}
