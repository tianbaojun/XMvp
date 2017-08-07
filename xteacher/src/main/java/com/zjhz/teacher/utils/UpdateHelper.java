package com.zjhz.teacher.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.zjhz.teacher.NetworkRequests.NetworkRequest;
import com.zjhz.teacher.NetworkRequests.request.CheckRequest;
import com.zjhz.teacher.app.AppContext;
import com.zjhz.teacher.base.EventCenter;
import com.zjhz.teacher.bean.UpdateInfo;
import com.zjhz.teacher.ui.view.dialog.MyAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-08-09
 * Time: 15:57
 * Description:
 */
public class UpdateHelper {

	private Context mContext;
	private String checkUrl;
	private boolean isAutoInstall = true;
	private OnUpdateListener updateListener;


	private HashMap<String, String> cache = new HashMap<String, String>();

//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//				case UPDATE_NOTIFICATION_PROGRESS:
//					showDownloadNotificationUI((UpdateInfo) msg.obj, msg.arg1);
//					break;
//				case COMPLETE_DOWNLOAD_APK:
//					if (UpdateHelper.this.isAutoInstall) {
//						installApk(Uri.parse("file://" + cache.get(APK_PATH)));
//					} else {
//						if (ntfBuilder == null) {
//							ntfBuilder = new NotificationCompat.Builder(mContext);
//						}
//						ntfBuilder.setSmallIcon(mContext.getApplicationInfo().icon)
//								.setContentTitle(cache.get(APP_NAME))
//								.setContentText("下载完成，点击安装").setTicker("任务下载完成");
//						Intent intent = new Intent(Intent.ACTION_VIEW);
//						intent.setDataAndType(
//								Uri.parse("file://" + cache.get(APK_PATH)),
//								"application/vnd.android.package-archive");
//						PendingIntent pendingIntent = PendingIntent.getActivity(
//								mContext, 0, intent, 0);
//						ntfBuilder.setContentIntent(pendingIntent);
//						if (notificationManager == null) {
//							notificationManager = (NotificationManager) mContext
//									.getSystemService(Context.NOTIFICATION_SERVICE);
//						}
//						notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
//								ntfBuilder.build());
//					}
//					break;
//			}
//		}
//
//	};

	private UpdateHelper(UpdateHelper.Builder builder) {
		this.mContext = builder.context;
		this.checkUrl = builder.checkUrl;
		this.isAutoInstall = builder.isAutoInstall;
	}

	/**
	 * 检查app是否有新版本，check之前先Builer所需参数
	 */
	public void check() {
		check(null);
	}

	public void check(OnUpdateListener listener) {
		if (listener != null) {
			this.updateListener = listener;
		}
		if (mContext == null) {
			Log.e("NullPointerException", "The context must not be null.");
			return;
		}

		/**服务端获取的版本信息*/
		EventBus.getDefault().register(this);
		NetworkRequest.request(new CheckRequest("1", "TEACHER_APP"), checkUrl, "version");
		
	}

	@Subscribe
	public void onEventMainThread(EventCenter ev) {
		switch (ev.getEventCode()) {
			case "version":
				JSONObject jsonObject = (JSONObject) ev.getData();
				UpdateInfo updateInfo = new UpdateInfo();
				JSONObject data = jsonObject.optJSONObject("data");
				LogUtil.e("版本返回的data = ", data + "");
				if (data != null) {
					updateInfo.setApkUrl(data.optString("urlDownload"));
					updateInfo.setVersionName(data.optString("appVersion"));
					updateInfo.setChangeLog(data.optString("description"));
					updateInfo.setUpdateTips("更新提示");

					String appName = BaseUtil.getPackageInfo(mContext).applicationInfo.
							loadLabel(mContext.getPackageManager()).toString();
					updateInfo.setAppName(appName);
				}
				boolean isUpdateLowestVersion = false;
				if (updateInfo.getVersionName() != null) {
					if (notLessThanLowerVersion(getPackageInfo().versionName, updateInfo.getVersionName())) {  // TODO 这里比对版本号
						showUpdateUI(updateInfo);
						isUpdateLowestVersion = true;
					}
				}
				if (updateListener != null) {
					updateListener.onFinishCheck(updateInfo, isUpdateLowestVersion);

				}
				EventBus.getDefault().unregister(this);
				break;

		}
	}

	//不低于当前最小强制更新版本
	private boolean notLessThanLowerVersion(String currentVersion, String lowerVersion) {
		int length = Math.min(currentVersion.split("\\.").length, lowerVersion.split("\\.").length);
		if (TextUtils.isEmpty(currentVersion) || TextUtils.isEmpty(lowerVersion) || length == 0)
			return false;
		for (int i = 0; i < length; i++) {
			if (Integer.parseInt(currentVersion.split("\\.")[i]) < Integer.parseInt(lowerVersion.split("\\.")[i])) {
				return true;
			}
		}
		return false;
	}

//	/**
//	 * 流量提示框，当网络为数据流量方式时，下载就会弹出此对话框提示
//	 * @param updateInfo
//	 */
//	private void showNetDialog(final UpdateInfo updateInfo){
//		MyAlertDialog.Builder netBuilder = new MyAlertDialog.Builder(mContext);
//		netBuilder.setTitle("下载提示");
//		netBuilder.setMessage("您在目前的网络环境下继续下载将可能会消耗手机流量，请确认是否继续下载？");
//		netBuilder.setNoOnClickListener("退出",
//				new MyAlertDialog.OnClickListener() {
//					@Override
//					public void onClick() {
////						AppContext.getInstance().finishAllActivitys();
////						System.exit(0);
//					}
//				});
//		netBuilder.setYesOnClickListener("继续下载",
//				new MyAlertDialog.OnClickListener() {
//					@Override
//					public void onClick() {
//						UpdateManager updateManager = new UpdateManager(mContext, updateInfo, updateListener, isAutoInstall);
//						updateManager.downLoad();
//					}
//				});
//		MyAlertDialog netDialog = netBuilder.create();
//		netDialog.setCanceledOnTouchOutside(false);
//		netDialog.show();
//	}

	/**
	 * 弹出提示更新窗口
	 *
	 * @param updateInfo
	 */
	private void showUpdateUI(final UpdateInfo updateInfo) {
		MyAlertDialog.Builder upDialogBuilder = new MyAlertDialog.Builder(mContext);
		upDialogBuilder.setTitle(updateInfo.getUpdateTips());
		upDialogBuilder.setMessage(updateInfo.getChangeLog());
		upDialogBuilder.setNoOnClickListener("退出",
				new MyAlertDialog.OnClickListener() {
					@Override
					public void onClick() {
						AppContext.getInstance().finishAllActivitys();
						System.exit(0);
					}
				});
		upDialogBuilder.setYesOnClickListener("下载",
				new MyAlertDialog.OnClickListener() {
					@Override
					public void onClick() {
						UpdateManager updateManager = new UpdateManager(mContext, updateInfo, updateListener, isAutoInstall);
						updateManager.setIsExit(true);
						updateManager.update();
//						NetWorkUtils netWorkUtils = new NetWorkUtils(mContext);
//						int type = netWorkUtils.getNetType();
//						if (type != 1) {
//							showNetDialog(updateInfo);
//						}else {
//							UpdateManager updateManager = new UpdateManager(mContext, updateInfo, updateListener, isAutoInstall);
//							updateManager.downLoad();
//						}
					}
				});
		MyAlertDialog updateDialog = upDialogBuilder.create();
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.show();
	}

//	/**
//	 * 通知栏弹出下载提示进度
//	 *
//	 * @param updateInfo
//	 * @param progress
//	 */
//	private void showDownloadNotificationUI(UpdateInfo updateInfo,
//											final int progress) {
//		if (mContext != null) {
//			String contentText = new StringBuffer().append(progress)
//					.append("%").toString();
//			PendingIntent contentIntent = PendingIntent.getActivity(mContext,
//					0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
//			if (notificationManager == null) {
//				notificationManager = (NotificationManager) mContext
//						.getSystemService(Context.NOTIFICATION_SERVICE);
//			}
//			if (ntfBuilder == null) {
//				ntfBuilder = new NotificationCompat.Builder(mContext)
//						.setSmallIcon(mContext.getApplicationInfo().icon)
//						.setTicker("开始下载...")
//						.setContentTitle(updateInfo.getAppName())
//						.setContentIntent(contentIntent);
//			}
//			ntfBuilder.setContentText(contentText);
//			ntfBuilder.setProgress(100, progress, false);
//			notificationManager.notify(DOWNLOAD_NOTIFICATION_ID,
//					ntfBuilder.build());
//		}
//	}

	/**
	 * 获取当前app版本
	 *
	 * @return
	 * @throws PackageManager.NameNotFoundException
	 */
	private PackageInfo getPackageInfo() {
		PackageInfo pinfo = null;
		if (mContext != null) {
			try {
				pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return pinfo;
	}

//	/**
//	 * 异步下载app任务
//	 */
//	private class AsyncDownLoad extends AsyncTask<UpdateInfo, Integer, Boolean> {
//		@Override
//		protected Boolean doInBackground(UpdateInfo... params) {
//
//			try {
//				URL url = new URL(params[0].getApkUrl());
//				HttpURLConnection http = (HttpURLConnection) url.openConnection();
//				http.setConnectTimeout(5000);
//
//				long total = http.getContentLength();
//				String apkName = params[0].getAppName()
//						+ params[0].getVersionName() + SUFFIX;
//				cache.put(APP_NAME, params[0].getAppName());
//				cache.put(APK_PATH,
//						PATH + File.separator + params[0].getAppName()
//								+ File.separator + apkName);
//				File savePath = new File(PATH + File.separator
//						+ params[0].getAppName());
//				if (!savePath.exists())
//					if(savePath.mkdirs())
//						return true;
//				File apkFile = new File(savePath, apkName);
//				if (apkFile.exists() && apkFile.length() == http.getContentLength()) {
//					return true;
//				}
//				FileOutputStream fos = new FileOutputStream(apkFile);
//				BufferedInputStream inputStream = new BufferedInputStream(http.getInputStream());
//				byte[] buf = new byte[1024];
//				int count = 0;
//				int length = -1;
//				while ((length = inputStream.read(buf)) != -1) {
//					fos.write(buf, 0, length);
//					count += length;
//					int progress = (int) ((count / (float) total) * 100);
//					if (progress % 5 == 0) {
//						handler.obtainMessage(UPDATE_NOTIFICATION_PROGRESS,
//								progress, -1, params[0]).sendToTarget();
//					}
//					if (updateListener != null) {
//						updateListener
//								.onDownloading(progress);
//					}
//				}
//				inputStream.close();
//				fos.close();
//
//
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//			return true;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean flag) {
//			if (flag) {
//				handler.obtainMessage(COMPLETE_DOWNLOAD_APK).sendToTarget();
//				if (updateListener != null) {
//					updateListener.onFinshDownload();
//				}
//			} else {
//				Log.e("Error", "下载失败。");
//			}
//		}
//	}

	public static class Builder {
		private Context context;
		private String checkUrl;
		private boolean isAutoInstall = true;

		public Builder(Context ctx) {
			this.context = ctx;
		}

		/**
		 * 检查是否有新版本App的URL接口路径
		 *
		 * @param checkUrl
		 * @return
		 */
		public UpdateHelper.Builder checkUrl(String checkUrl) {
			this.checkUrl = checkUrl;
			return this;
		}

		/**
		 * 是否需要自动安装, 不设置默认自动安装
		 *
		 * @param isAuto
		 *            true下载完成后自动安装，false下载完成后需在通知栏手动点击安装
		 * @return
		 */
		public UpdateHelper.Builder isAutoInstall(boolean isAuto) {
			this.isAutoInstall = isAuto;
			return this;
		}
		

		/**
		 * 构造UpdateHelper对象
		 *
		 * @return
		 */
		public UpdateHelper build() {
			return new UpdateHelper(this);
		}
	}

//	private void installApk(Uri data) {
//		if (mContext != null) {
//			Intent i = new Intent(Intent.ACTION_VIEW);
//			i.setDataAndType(data, "application/vnd.android.package-archive");
//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			mContext.startActivity(i);
//			if (notificationManager != null) {
//				notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
//			}
//		} else {
//			Log.e("NullPointerException", "The context must not be null.");
//		}
//	}
}
