package com.zjhz.teacher.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Window;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-3
 * Time: 10:30
 * Description:
 */
public class WaitDialog extends ProgressDialog {

	public WaitDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setInverseBackgroundForced(false);
		setProgressStyle(STYLE_SPINNER);
		setOnKeyListener(keylistener);
		setCancelable(false);
		setMessage("正在加载…");
	}

	private OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

}
