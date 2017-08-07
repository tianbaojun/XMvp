package pro.widget.NineGridLayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by Tabjin on 2017/5/4.
 * Description:
 * What Changed:
 */

public class MyImageView extends CustomImageView {
    private Drawable mDrawable;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private boolean holdWidth, holdHeight;

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = getDrawable();
        initImage();
    }

    public MyImageView(Context context) {
        super(context);

        mDrawable = getDrawable();
        initImage();
    }

    private void initImage() {
        if (mDrawable != null) {
            mDrawableWidth = mDrawable.getIntrinsicWidth();
            mDrawableHeight = mDrawable.getIntrinsicHeight();
            if(mDrawableHeight>mDrawableWidth) {
                holdWidth = true;
            }else {
                holdHeight = true;
            }
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mDrawable = drawable;
        initImage();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (holdHeight != holdWidth && mDrawable != null) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthSize);
            int heightMode = MeasureSpec.getMode(heightSize);
            if (holdWidth) {
                int calcHeightSize = getHoldHeight(widthSize);
                int finalHeightSize = getFinalSize(calcHeightSize, heightSize);
                setMeasuredDimension(widthSize + getPaddingLeft() + getPaddingRight()
                        , finalHeightSize + getPaddingTop() + getPaddingBottom());
            } else {
                int calcWidthSize = getHoldWidth(heightSize);
                int finalWidthSize = getFinalSize(calcWidthSize, widthSize);
                setMeasuredDimension(finalWidthSize + getPaddingLeft() + getPaddingRight()
                        , heightSize + getPaddingTop() + getPaddingBottom());
            }
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getFinalSize(int measureSize, int calcSize) {
        if (measureSize <= 0) {
            return calcSize;
        }
        if (calcSize <= 0) {
            return measureSize;
        }
        return Math.min(measureSize, calcSize);
    }

    private int getHoldHeight(int widthSize) {
        return widthSize * mDrawableHeight / mDrawableWidth;
    }

    private int getHoldWidth(int heightSize) {
        return heightSize * mDrawableWidth / mDrawableHeight;
    }
}

