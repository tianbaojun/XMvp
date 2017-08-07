package pro.kit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.zjhz.teacher.R;

/**
 * Created by Tabjin on 2017/4/11.
 * Description:
 * What Changed:
 */

public class ViewTools {

    /**
     * 防止在3秒内重复点击
     * @param view 点击的view
     * @return true 是重复点击  false 不是重复点击
     */
    public static boolean avoidRepeatClick(View view){
        boolean flag = false;
//
        if(view instanceof TextView){
            if (view.getResources().getString(R.string.submit).equals(((TextView) view).getText().toString())||"完成".equals(((TextView) view).getText().toString())) {
                long lastTime = view.getTag(-1) == null ? 0 : (long) view.getTag(-1);
                if (System.currentTimeMillis() - lastTime <= 3000) {
                    flag = true;
                }else{
                    view.setTag(-1, System.currentTimeMillis());
                }
            }
        }
        return flag;
    }

    /**
     * 判断是否重复点击
     * @param view 点击的view
     * @param delay 多少时间内算是重复点击（毫秒数）
     * @return true 不是重复点击  false 是重复点击
     */
    public static boolean avoidRepeatClick(View view,long delay){
        boolean flag = true;
        long lastTime = view.getTag(-1)==null?0:(long)view.getTag(-1);
        if (System.currentTimeMillis()-lastTime>delay){
            flag = false;
        }
        view.setTag(-1,System.currentTimeMillis());
        return flag;
    }

    public static void hideSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    /**
     * 弹出软键盘
     */
    public static void popSoftInput(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 给tv设置勾选状态
     * @param tv  tv
     * @param str 显示的文字
     */
    public static void tvShowCheck(TextView tv,String str){
        SpannableString sp = new SpannableString(str+" ");
        Drawable d = tv.getResources().getDrawable(R.mipmap.check);
        d.setBounds(0, 0, (int)tv.getResources().getDimension(R.dimen.dp_15), (int)tv.getResources().getDimension(R.dimen.dp_15));
        sp.setSpan(new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE), sp.length()-1, sp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setTextColor(tv.getResources().getColor(R.color.text_color_8CA404));
        tv.setText(sp);
    }

    public static void tvAppendImage(TextView tv,String str,int drawable){
        SpannableString sp = new SpannableString(str+" ");
        Drawable d = tv.getResources().getDrawable(drawable);
        d.setBounds(0, 0, (int)tv.getResources().getDimension(R.dimen.dp_15), (int)tv.getResources().getDimension(R.dimen.dp_15));
        sp.setSpan(new ImageSpan(d), sp.length()-1, sp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(sp);
    }

    /**
     *     显示基本的AlertDialog
     */
    public static void showDialog(Context context,String msg,
                                  DialogInterface.OnClickListener posListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(R.drawable.icon);
//        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", posListener);
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
