package pro.widget.NineGridLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.zjhz.teacher.R;

public class HoldScaleImageView extends CustomImageView {

    private Drawable mDrawable;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private boolean holdWidth, holdHeight;

    public HoldScaleImageView(Context context) {
        this(context, null);
    }

    public HoldScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoldScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.HoldScaleImageView, defStyleAttr,0);
        holdWidth = a.getBoolean(R.styleable.HoldScaleImageView_holdWidth, false);
        holdHeight = a.getBoolean(R.styleable.HoldScaleImageView_holdHeight, false);
        a.recycle();
        mDrawable = getDrawable();
        initImage();
    }

    private void initImage() {
        if (mDrawable != null) {
            mDrawableWidth = mDrawable.getIntrinsicWidth();
            mDrawableHeight = mDrawable.getIntrinsicHeight();
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