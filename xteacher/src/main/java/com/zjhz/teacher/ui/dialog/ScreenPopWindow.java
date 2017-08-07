package com.zjhz.teacher.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ViewHolder;
import com.zjhz.teacher.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio
 * author: fei.wang
 * Date: 2016-06-25
 * Time: 15:57
 * Description: 筛选级别弹窗
 */
public class ScreenPopWindow implements View.OnClickListener{
    private LinearLayout ll_dialog_holder;
    private DialogPlus dialog;
    private Activity activity;
    private final Holder  holder;
    private ImageView height,center,low,none;
    private List<View> imageViews = new ArrayList<>();

    private OnDialogItemClickListener mOnDialogItemClickListener;

    public ScreenPopWindow(Activity activity) {
        this.activity = activity;
        ll_dialog_holder = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_screen, null);
        holder = new ViewHolder(ll_dialog_holder);

        height = (ImageView) ll_dialog_holder.findViewById(R.id.dialog_screen_height_img);
        center = (ImageView) ll_dialog_holder.findViewById(R.id.dialog_screen_center_img);
        low = (ImageView) ll_dialog_holder.findViewById(R.id.dialog_screen_low_img);
        none = (ImageView) ll_dialog_holder.findViewById(R.id.dialog_screen_none_img);
        imageViews.add(height);
        imageViews.add(center);
        imageViews.add(low);
        imageViews.add(none);
    }

    public void upPopWwindow(){
        showOnlyContentDialog(holder, Gravity.BOTTOM, false,activity);
        ll_dialog_holder.findViewById(R.id.dialog_screen_height).setOnClickListener(this);
        ll_dialog_holder.findViewById(R.id.dialog_screen_center).setOnClickListener(this);
        ll_dialog_holder.findViewById(R.id.dialog_screen_low).setOnClickListener(this);
        ll_dialog_holder.findViewById(R.id.dialog_screen_none).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_screen_height:
                selected(0);
                mOnDialogItemClickListener.onClickListenerHeight();
                break;
            case R.id.dialog_screen_center:
                selected(1);
                mOnDialogItemClickListener.onClickListenerCenter();
                break;
            case R.id.dialog_screen_low:
                selected(2);
                mOnDialogItemClickListener.onClickListenerLow();
                break;
            case R.id.dialog_screen_none:
                selected(3);
                mOnDialogItemClickListener.onClickListenerNone();
                break;
        }
        dialog.dismiss();
    }

    /**
     * 仅显示内容的dialog
     * @param holder
     * @param gravity         显示位置（居中，底部，顶部）
     * @param expanded        是否支持展开（有列表时适用）
     */
    private void showOnlyContentDialog(Holder holder, int gravity, boolean expanded, Activity activity) {
        dialog = DialogPlus.newDialog(activity)
                .setContentHolder(holder)
                .setGravity(gravity)
                .setExpanded(expanded)
                .setCancelable(true)
                .create();
        dialog.show();
    }

    private void selected(int position){
        for (int i = 0; i < imageViews.size(); i++) {
            if (i == position){
                imageViews.get(position).setVisibility(View.VISIBLE);
            }else{
                imageViews.get(i).setVisibility(View.GONE);
            }
        }
    }

    public void setOnDialogItemClickListener(OnDialogItemClickListener onDialogItemClickListener){
        this.mOnDialogItemClickListener = onDialogItemClickListener;
    }

    public interface OnDialogItemClickListener{
        void onClickListenerHeight();
        void onClickListenerCenter();
        void onClickListenerLow();
        void onClickListenerNone();
    }
}