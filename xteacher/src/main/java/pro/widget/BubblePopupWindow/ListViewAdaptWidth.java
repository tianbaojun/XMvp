package pro.widget.BubblePopupWindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 自适应宽度的ListView
 */
public class ListViewAdaptWidth extends ListView {
  
    public ListViewAdaptWidth(Context context) {
        super(context);  
    }  
  
    public ListViewAdaptWidth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);  
    }  
  
    public ListViewAdaptWidth(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int width = getMaxWidthOfChildren() + getPaddingLeft() + getPaddingRight();//计算listview的宽度
        int height = getMaxHeight() + getPaddingBottom() + getPaddingTop();
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));//设置listview的宽高
  
    }  
  
    /** 
     * 计算item的最大宽度 
     * 
     * @return 
     */  
    private int getMaxWidthOfChildren() {  
        int maxWidth = 0;  
        View view = null;
        int count = getAdapter().getCount();  
        for (int i = 0; i < count; i++) {  
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int hieght = view.getMeasuredWidth()+view.getPaddingLeft()+view.getPaddingRight();
            if (hieght > maxWidth)
                maxWidth = hieght;
        }  
        return maxWidth ;
    }

    private int getMaxHeight() {
        int maxHeight = 0;
        View view = null;
        int count = getAdapter().getCount();
        if (count > 0) {
            view = getAdapter().getView(0, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if(count>5){
                maxHeight = view.getMeasuredHeight()*5;
                maxHeight += getDividerHeight()*4;
            }else{
                maxHeight = view.getMeasuredHeight()*count;
                maxHeight += getDividerHeight() * (count - 1);
            }
        }else{
            maxHeight = 0;
        }
        return maxHeight;
    }
}  