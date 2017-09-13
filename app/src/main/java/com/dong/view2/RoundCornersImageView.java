package com.dong.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/12.
 */
public class RoundCornersImageView extends ImageView {
    private float radiusX;
    private float radiusY;

    public RoundCornersImageView(Context context) {
        super(context);
        init();
    }

    public RoundCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundCornersImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * @param rx x方向弧度
     * @param ry y方向弧度
     */
    public void setRadius(float rx, float ry) {
        this.radiusX = rx;
        this.radiusY = ry;
    }

    private void init() {
        radiusX = 58;
        radiusY = 58;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        Path path = new Path();
        Rect rect = new Rect(0, 0, getWidth(), getHeight());
        RectF rectF = new RectF(rect);
        path.addRoundRect(rectF, radiusX, radiusY, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);//Op.REPLACE这个范围内的都将显示，超出的部分覆盖
        super.onDraw(canvas);
    }
}