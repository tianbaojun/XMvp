package com.zjhz.teacher.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.zjhz.teacher.R;
import com.zjhz.teacher.utils.SharePreCache;

/**
 * 虚线
 * Created by Administrator on 2016/7/22.
 */
public class DashedLineView extends View{
    private Paint paint = null;
    private Path path = null;
    private PathEffect pe = null;

    public DashedLineView(Context context) {
        super(context);
    }
    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dashedline);
        int lineColor = a.getColor(R.styleable. dashedline_lineColor, 0XFF000000);
        a.recycle();
        this.paint = new Paint();
        this.path = new Path();
        this.paint.setStyle(Paint.Style. STROKE);
        this.paint.setTextSize(16);
        this.paint.setColor(lineColor);
        this.paint.setAntiAlias( true);
        this.paint.setStrokeWidth(SharePreCache.dp2px(getContext(), 6f));
        float[] arrayOfFloat = new float[4];
        arrayOfFloat[0] = SharePreCache.dp2px(getContext(), 4f);
        arrayOfFloat[1] =SharePreCache.dp2px(getContext(), 4f);
        arrayOfFloat[2] = SharePreCache.dp2px(getContext(), 4f);
        arrayOfFloat[3] = SharePreCache.dp2px(getContext(), 4f);
        this.pe = new DashPathEffect(arrayOfFloat, SharePreCache.dp2px(getContext(), 1f));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.path.moveTo(0.0F, 0.0F);
        this.path.lineTo(getMeasuredWidth(), 200.0F);
        this.paint.setPathEffect( this.pe);
        canvas.drawPath( this.path, this.paint);
    }
}
