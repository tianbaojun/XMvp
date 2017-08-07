package com.zjhz.teacher.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.OptionsGradeView;
import com.zjhz.teacher.ui.view.OptionsPickerView;
import com.zjhz.teacher.ui.view.TimePickerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro.widget.BubblePopupWindow.ListViewAdaptWidth;


/**
 * @author fei.wang
 * Describe：基本工具
 */
public class BaseUtil {
    //一天的秒数
    public static final long ONE_DAY_TIME = 24 * 60 * 60;

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int getWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeigt = dm.heightPixels;
        return screenHeigt;
    }

    /**
     * 判断链表是否为空
     *
     * @param list
     * @return true:为Null或者size为0
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }


    /**
     * 判断链表第某个位置是否为空
     *
     * @param list
     * @param index
     * @return true:为Null或者size为0
     */
    public static boolean isEmpty(List<?> list, int index) {
        return list == null || list.size() == 0 || list.size() <= index;
    }

    public static int dp2px(Context mContext, int dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }


    /**
     * 获取当前app版本
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context mContext) {
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

    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yymmddString = sdf.format(new Date());
        return yymmddString;
    }


    /**
     * 获取年月日时间
     */
    public static String getNYYMMDD(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        long now = date.getTime();
        long next = now + day * ONE_DAY_TIME * 1000;
        String yymmddString = sdf.format(next);
        return yymmddString;
    }

    //安卓自带分享
    public static void share(Context context, String questionTitle, String questionUrl) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        //noinspection deprecation
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, questionTitle + " " + questionUrl + " 分享自知乎网");
        context.startActivity(Intent.createChooser(share, "分享至…"));
    }


    //替换html标签
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    public static void upKeyboard(EditText editText) {
        InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void downKeyboard(EditText editText) {
        InputMethodManager imm1 = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean isStringEmpty(String str){
        return !TextUtils.isEmpty(str);
    }

    public static int index_leave = -1;
    public static  void selectSubject(final List<String> lists, Context context, final TextView view) {
        if (!lists.isEmpty()){
            OptionsPickerView optionsPickerView = new OptionsPickerView(context);
            optionsPickerView.setPicker((ArrayList) lists, 0, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3,int options4) {
                    view.setText(lists.get(options1));
                    index_leave = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.toast("没有数据");
        }
    }

    /**
     * 选择日期
     */
    public static void selectDate(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY,null,0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,String type) {
                textView.setText(TimeUtil.getYMD(date));
            }
        });
    }

    /**
     * 选择日期
     */
    public static void selectDateYMDHM(Context context, final TextView textView) {
        BaseUtil.hideSoftKeyBoard(context, textView);
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN,null,0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR)-1, calendar.get(Calendar.YEAR) + 1);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,String type) {
                textView.setText(TimeUtil.getNowYMDHMTime(date));
            }
        });
    }

    /**
     * 选择日期
     */
    public static void selectTime(Context context, final TextView textView) {
        TimePickerView pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY,null,0);
        // 控制时间范围在2016年-20之间,去掉将显示全部
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 10);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);// 是否滚动
        pvTime.setCancelable(true);
        // 弹出时间选择器
        pvTime.show();
        // 时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date,String type) {
                textView.setText("检查时间：" + TimeUtil.getYMD(date));
            }
        });
    }

    public static int index_grade = 0;
    public static  void selectGrade(final List<String> lists,final ArrayList<ArrayList<String>> lists1, Context context, final TextView view) {
        if (!lists.isEmpty()){
            OptionsGradeView optionsPickerView = new OptionsGradeView(context);
            optionsPickerView.setPicker((ArrayList) lists,lists1, index_grade, 0, 0);
            optionsPickerView.setCyclic(false);
            optionsPickerView.setCancelable(true);
            optionsPickerView.setOnoptionsSelectListener(new OptionsGradeView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int option2,int options3) {
                    view.setText(lists.get(options1)+ lists1.get(options1).get(option2) );
                    index_grade = options1;
                }
            });
            optionsPickerView.show();
        }else {
            ToastUtils.toast("没有数据");
        }
    }

    /**保存本地对象信息*/
    public static void saveLocalFile(Context mContext,Object mObject,String filename){
        FileOutputStream stream = null;
        ObjectOutputStream oos = null;
        try {
            stream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(stream);
            oos.writeObject(mObject);
            stream.close();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (IOException e){ e.printStackTrace();}
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e){ e.printStackTrace();}
            }
        }
    }

    /**提取本地信息*/
    public static Object getLocalFile(Context mContext,String filename){
        FileInputStream stream = null;
        ObjectInputStream ois= null;
        try {
            stream = mContext.openFileInput(filename);
            ois = new ObjectInputStream(stream);
            return ois.readObject();

        } catch (FileNotFoundException e) {
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (IOException e) { e.printStackTrace();}
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) { e.printStackTrace();}
            }
        }
    }

    //(如果输入法在窗口上已经显示，则隐藏，反之则显示)
    public static void  showOrHideSoftKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //（view为接受软键盘输入的视图，SHOW_FORCED表示强制显示）
    public static void  showSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    //强制隐藏键盘
    public static void  hideSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    //获取输入法打开的状态
    public static boolean isKeyBoardOpen(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//isOpen若返回true，则表示输入法打开
    }

    public static PopupWindow showListPopupWindow(View view, List<String> spans, final OnItemClickListener listener) {
        final PopupWindow popupWindow = new PopupWindow(view.getContext());
        ListViewAdaptWidth popListView = new ListViewAdaptWidth(view.getContext());
        popListView.setCacheColorHint(Color.WHITE);
        popListView.setDivider(new ColorDrawable(Color.WHITE));
        popListView.setDividerHeight(1);
        ArrayAdapter<String> popAdapter = new ArrayAdapter<>(view.getContext(), R.layout.textview);
        popListView.setAdapter(popAdapter);
        popupWindow.setContentView(popListView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(view.getResources().getColor(R.color.title_background_red)));
        popupWindow.setOutsideTouchable(true);
        popAdapter.clear();
        popAdapter.addAll(spans);
        popListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(position);
                popupWindow.dismiss();
            }
        });
        popAdapter.notifyDataSetChanged();
        popupWindow.showAsDropDown(view, 0, 0);
        return popupWindow;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public static int getWidthPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public static int getHeightPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.widthPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
