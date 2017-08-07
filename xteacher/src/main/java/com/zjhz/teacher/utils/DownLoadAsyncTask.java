package com.zjhz.teacher.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.zjhz.teacher.bean.DownloadBean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zzd on 2017/4/21.
 */

public class DownLoadAsyncTask extends AsyncTask<DownloadBean, Integer, Boolean> {

    private String PATH;
    private String appName;
    private String fileName;
    private String savePathName;
    private DownLoadFileListener downLoadFileListener;


    public DownLoadAsyncTask(Context context, DownLoadFileListener loadFileListener) {
        PATH = Environment.getExternalStorageDirectory().getPath();
        appName = BaseUtil.getPackageInfo(context).applicationInfo.loadLabel(context.getPackageManager()).toString();
        savePathName = PATH + File.separator + appName;
        this.downLoadFileListener = loadFileListener;
    }

    @Override
    protected Boolean doInBackground(DownloadBean... downloadBeen) {

        try {
            URL url = new URL(downloadBeen[0].url);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(5000);

            long total = http.getContentLength();
            fileName = downloadBeen[0].fileName;
            File savePath = new File(savePathName);
            if (!savePath.exists())
                savePath.mkdirs();
            File apkFile = new File(savePath, fileName);
            if (apkFile.exists() && apkFile.length() == http.getContentLength()) {
                return true;
            }
            FileOutputStream fos = new FileOutputStream(apkFile);
            BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
            byte[] buf = new byte[1024];
            int count = 0;
            int length = -1;
            while ((length = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, length);
                count += length;
                int progress = (int) ((count / (float) total) * 100);
                downLoadFileListener.onDownLoad(progress);
//                if (progress % 5 == 0) {
//                    handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS,
//                            progress, -1, params[0]).sendToTarget();
//                }
//                if (updateListener != null) {
//                    updateListener.onDownloading(progress);
//                }
            }
            inputStream.close();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        if (flag) {
            downLoadFileListener.completeDownLoad(savePathName, fileName);
//            handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
//            if (updateListener != null) {
//                updateListener.onFinshDownload();
//            }
        } else {
            downLoadFileListener.onDownLoadErr();
        }
    }

    public interface DownLoadFileListener {
        void onDownLoad(int progress);

        void onDownLoadErr();

        void completeDownLoad(String savePathName, String fileName);
    }
}
