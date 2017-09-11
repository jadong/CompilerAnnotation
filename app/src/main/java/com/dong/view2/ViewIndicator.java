package com.dong.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/11.
 */
public class ViewIndicator extends View implements ViewPager.OnPageChangeListener, NavigatorHelper.OnNavigatorScrollListener {

    private int normalColor = Color.LTGRAY;
    private int selectColor = Color.RED;
    private int minRectWidth = 10;
    private int maxRectWidth = 15;
    private int rectHeight = 10;
    private int roundRadius = 3;
    private int indicatorSpacing = 5;
    int count = 1;
    int paddingLeft = 10;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int currIndex = 0;
    private List<RectF> rectList = new ArrayList<>();
    private SparseArray<Float> rectWidthArray = new SparseArray<>();

    private boolean followTouch = true;    // 是否跟随手指滑动
    private NavigatorHelper navigatorHelper = new NavigatorHelper();
    private Interpolator linearInterpolator = new LinearInterpolator();

    public ViewIndicator(Context context) {
        super(context);
        init(context);
    }

    public ViewIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public ViewIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        minRectWidth = UIUtil.dip2px(context, 6);
        maxRectWidth = UIUtil.dip2px(context, 15);
        rectHeight = UIUtil.dip2px(context, 6);
        roundRadius = UIUtil.dip2px(context, 3);
        indicatorSpacing = UIUtil.dip2px(context, 6);
        paddingLeft = UIUtil.dip2px(context, 10);
        navigatorHelper.setNavigatorScrollListener(this);
        navigatorHelper.setSkimOver(true);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() != null) {
            count = viewPager.getAdapter().getCount();
        }
        viewPager.addOnPageChangeListener(this);
        navigatorHelper.setTotalCount(count);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
//                result = (count - 1) * minRectWidth * 2 + maxRectWidth * 2 + (count - 1) * count + getPaddingLeft() + getPaddingRight();
                result = count * (indicatorSpacing + maxRectWidth) + getPaddingLeft() + getPaddingRight() + paddingLeft + maxRectWidth * 2;
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = rectHeight * 2 + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        rectList.clear();
        if (count > 0) {
            int y = Math.round(getHeight() / 2.0f);

            int startX = getPaddingLeft() + paddingLeft;
            for (int i = 0; i < count; i++) {
                RectF rectF = new RectF();
                rectF.left = startX;
                rectF.right = rectF.left + minRectWidth;
                rectF.top = y;
                rectF.bottom = y + rectHeight;
                rectList.add(rectF);
                startX += indicatorSpacing + maxRectWidth;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0, j = rectList.size(); i < j; i++) {
            RectF rectF = rectList.get(i);
            float rectWidth = rectWidthArray.get(i, (float) minRectWidth);
            paint.setColor(ArgbEvaluatorHolder.eval((rectWidth - minRectWidth) / (maxRectWidth - minRectWidth), normalColor, selectColor));
            if (i > currIndex) {
                RectF rectF1 = new RectF();
                rectF1.left = rectF.left + maxRectWidth / 2;
                rectF1.right = rectF1.left + minRectWidth;
                rectF1.top = rectF.top;
                rectF1.bottom = rectF.bottom;

                canvas.drawRoundRect(rectF1, roundRadius, roundRadius, paint);
            } else {
                rectF.right = rectF.left + rectWidth;

                canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
            }

//            rectF.right = rectF.left + rectWidth;
//
//            canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        navigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        navigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        navigatorHelper.onPageScrollStateChanged(state);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
//        if (followTouch) {
//            currIndex = index;
////            float rectWidth = minRectWidth + (maxRectWidth - minRectWidth) * linearInterpolator.getInterpolation(enterPercent);
//            rectWidthArray.put(index, (float) maxRectWidth);
//            invalidate();
//        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
//        if (followTouch) {
////            float rectWidth = maxRectWidth + (minRectWidth - maxRectWidth) * linearInterpolator.getInterpolation(leavePercent);
//            rectWidthArray.put(index, (float) minRectWidth);
//            invalidate();
//        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        currIndex = index;
        rectWidthArray.put(index, (float) maxRectWidth);
        invalidate();
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        rectWidthArray.put(index, (float) minRectWidth);
        invalidate();
    }
}
