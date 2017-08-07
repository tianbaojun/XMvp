package com.zjhz.teacher.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.bean.DownloadBean;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.ui.view.dialog.MyAlertDialog;

import java.io.File;


/**
 * Created by zzd on 2017/4/20.
 */

public class UpdateManager implements DownLoadAsyncTask.DownLoadFileListener{
    public static final int INSTALL_REQUEST_CODE = 0x4;
    private static final int UPDATE_NOTIFICATION_PROGRESS = 0x1;
    private static final int COMPLETE_DOWNLOAD_APK = 0x2;
    private static final int DOWNLOAD_NOTIFICATION_ID = 0x3;
    private static final String PATH = Environment
            .getExternalStorageDirectory().getPath();
    private static final String SUFFIX = ".apk";
    private static final String APK_PATH = "APK_PATH";
    private static final String APP_NAME = "APP_NAME";
    private DownLoadAsyncTask asyncDownLoad;
    private Context mContext;
    private UpdateInfo updateInfo;
    private DownloadBean downloadBean = new DownloadBean();
    private boolean isAutoInstall;
    private OnUpdateListener updateListener;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder ntfBuilder;
    private boolean isExit = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_NOTIFICATION_PROGRESS:
                    showDownloadNotificationUI(msg.arg1);
                    break;
                case COMPLETE_DOWNLOAD_APK:
                    if (UpdateManager.this.isAutoInstall) {
                        installApk(Uri.parse("file://" + msg.obj.toString()));
                    } else {
                        if (ntfBuilder == null) {
                            ntfBuilder = new NotificationCompat.Builder(mContext);
                        }
                        ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon)
                                .setContentTitle(updateInfo.getAppName())
                                .setContentText("下载完成，点击安装").setTicker("任务下载完成");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.parse("file://" + msg.obj.toString()),
                                "application/vnd.android.package-archive");
                        PendingIntent pendingIntent = PendingIntent.getActivity(
                                mContext, 0, intent, 0);
                        ntfBuilder.setContentIntent(pendingIntent);
                        if (notificationManager == null) {
                            notificationManager = (NotificationManager) mContext
                                    .getSystemService(Context.NOTIFICATION_SERVICE);
                        }
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
                                ntfBuilder.build());
                    }
                    break;
            }
        }

    };

    public UpdateManager(Context context,  UpdateInfo updateInfo,  OnUpdateListener listener, Boolean isAutoInstall) {
        this.mContext = context;
        this.updateInfo = updateInfo;
        this.updateListener = listener;
        this.isAutoInstall = isAutoInstall;
        downloadBean.fileName = updateInfo.getAppName() + updateInfo.getVersionName() + SUFFIX;
        downloadBean.url = updateInfo.getApkUrl();
        asyncDownLoad = new DownLoadAsyncTask(mContext, this);
    }

    @Override
    public void onDownLoad(int progress) {
        if (progress % 5 == 0) {
            handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS, progress, -1).sendToTarget();
        }
        if (updateListener != null) {
            updateListener.onDownloading(progress);
        }
    }

    @Override
    public void onDownLoadErr() {
        ToastUtils.showShort("下载失败");
    }

    @Override
    public void completeDownLoad(String savePathName, String fileName) {
        handler.obtainMessage(COMPLETE_DOWNLOAD_APK, savePathName+File.separator+fileName).sendToTarget();
        if (updateListener != null) {
            updateListener.onFinshDownload();
        }
    }

    public void setIsExit(boolean isExit){
        this.isExit = isExit;
    }


    public void update(){
        NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
        int type = netWorkUtils.getNetType();
        if (type != 1) {
            showNetDialog(updateInfo);
        }else {

            asyncDownLoad.execute(downloadBean);
        }
    }



    /**
     * 流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
     * @param updateInfo
     */
    private void showNetDialog(final UpdateInfo updateInfo){
        MyAlertDialog.Builder netBuilder = new MyAlertDialog.Builder(mContext);
        netBuilder.setTitle("下载提示");
        netBuilder.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？");
        netBuilder.setNoOnClickListener("取消下载",
                new MyAlertDialog.OnClickListener() {
                    @Override
                    public void onClick() {
                        if(isExit){
                            AppContext.getInstance().finishAllActivitys();
                            System.exit(0);
                        }
                    }});
        netBuilder.setYesOnClickListener("继续下载",
                new MyAlertDialog.OnClickListener() {
                    @Override
                    public void onClick() {
                        asyncDownLoad.execute(downloadBean);
                    }
                });
        MyAlertDialog netDialog = netBuilder.create();
        netDialog.setCanceledOnTouchOutside(false);
        netDialog.show();
    }


    /**
     * 通知栏弹出下载提示进度
     * @param progress
     */
    private void showDownloadNotificationUI(int progress) {
        if (mContext != null) {
            String contentText = new StringBuffer().append(progress)
                    .append("%").toString();
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            if (notificationManager == null) {
                notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
            }
            if (ntfBuilder == null) {
                ntfBuilder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(mContext.getApplicationInfo().icon)
                        .setTicker("开始下载...")
                        .setContentTitle(updateInfo.getAppName())
                        .setContentIntent(contentIntent);
            }
            ntfBuilder.setContentText(contentText);
            ntfBuilder.setProgress(100, progress, false);
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
                    ntfBuilder.build());
        }
    }

//    /**
//     * 异步下载app任务
//     */
//    private class AsyncDownLoad extends AsyncTask<UpdateInfo, Integer, Boolean> {
//        @Override
//        protected Boolean doInBackground(UpdateInfo... params) {
//
//            try {
//                URL url = new URL(params[0].getApkUrl());
//                HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                http.setConnectTimeout(5000);
//
//                long total = http.getContentLength();
//                String apkName = params[0].getAppName()
//                        + params[0].getVersionName() + SUFFIX;
//                cache.put(APP_NAME, params[0].getAppName());
//                cache.put(APK_PATH,
//                        PATH + File.separator + params[0].getAppName()
//                                + File.separator + apkName);
//                File savePath = new File(PATH + File.separator
//                        + params[0].getAppName());
//                if (!savePath.exists())
//                    if(savePath.mkdirs())
//                        return true;
//                File apkFile = new File(savePath, apkName);
//                if (apkFile.exists() && apkFile.length() == http.getContentLength()) {
//                    return true;
//                }
//                FileOutputStream fos = new FileOutputStream(apkFile);
//                BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
//                byte[] buf = new byte[1024];
//                int count = 0;
//                int length = -1;
//                while ((length = inputStream.read(buf)) != -1) {
//                    fos.write(buf, 0, length);
//                    count += length;
//                    int progress = (int) ((count / (float) total) * 100);
//                    if (progress % 5 == 0) {
//                        handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS,
//                                progress, -1, params[0]).sendToTarget();
//                    }
//                    if (updateListener != null) {
//                        updateListener.onDownloading(progress);
//                    }
//                }
//                inputStream.close();
//                fos.close();
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean flag) {
//            if (flag) {
//                handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
//                if (updateListener != null) {
//                    updateListener.onFinshDownload();
//                }
//            } else {
//                Log.e("Error", "下载失败。");
//            }
//        }
//    }



    private void installApk(Uri data) {
        if (mContext != null) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(data, "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(i);
            ((Activity)mContext).startActivityForResult(i, INSTALL_REQUEST_CODE);
            if (notificationManager != null) {
                notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
            }
        } else {
            Log.e("NullPointerException", "The context must not be null.");
        }
    }
}
