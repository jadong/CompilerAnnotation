package com.dong.view2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import net.lucode.hackware.magicindicator.NavigatorHelper;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class ScaleLineNavigator extends View implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {
    private int mNormalCircleColor = Color.LTGRAY;
    private int mSelectedCircleColor = Color.GRAY;
    private int mCircleSpacing;
    private int mCircleCount;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<RectF> mRectList = new ArrayList<>();
    private SparseArray<Float> mRectWidthArray = new SparseArray<>();

    // 事件回调
    private boolean mTouchable;
    private ScaleLineNavigator.OnCircleClickListener mCircleClickListener;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    private boolean mFollowTouch = true;    // 是否跟随手指滑动
    private NavigatorHelper mNavigatorHelper = new NavigatorHelper();
    private Interpolator mStartInterpolator = new LinearInterpolator();

    private int minRectWidth = 10;
    private int maxRectWidth = 15;
    private int rectHeight = 10;
    private int mRoundRadius = 3;
    private int currIndex = 0;

    public ScaleLineNavigator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mCircleSpacing = UIUtil.dip2px(context, 2);
        mNavigatorHelper.setNavigatorScrollListener(this);
        mNavigatorHelper.setSkimOver(true);

        minRectWidth = UIUtil.dip2px(context, 10);
        maxRectWidth = UIUtil.dip2px(context, 15);
        rectHeight = UIUtil.dip2px(context, 6);
        mRoundRadius = UIUtil.dip2px(context, 3);
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
                result = (mCircleCount - 1) * minRectWidth * 2 + maxRectWidth * 2 + (mCircleCount - 1) * mCircleSpacing + getPaddingLeft() + getPaddingRight();
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
    protected void onDraw(Canvas canvas) {
        Log.i("Scale", "canvas--");
        for (int i = 0, j = mRectList.size(); i < j; i++) {
            RectF rectF = mRectList.get(i);
            //float rectWidth = mRectWidthArray.get(i, (float) minRectWidth);
            int color = i == currIndex ? mSelectedCircleColor : mNormalCircleColor;
            mPaint.setColor(color);
            //rectF.right = rectF.left + rectWidth;
            canvas.drawRoundRect(rectF, mRoundRadius, mRoundRadius, mPaint);
        }
    }

    private void prepareCirclePoints() {
        if (mCircleCount > 0) {
            int y = Math.round(getHeight() / 2.0f);
            int centerSpacing = minRectWidth * 2 + mCircleSpacing;
            int startX = getPaddingLeft();
            for (int i = 0; i < mCircleCount; i++) {
                RectF rectF = new RectF();
                rectF.left = startX;
                rectF.right = rectF.left + minRectWidth;
                rectF.top = y;
                rectF.bottom = y + rectHeight;
                mRectList.add(rectF);
                startX += centerSpacing;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchable) {
                    mDownX = x;
                    mDownY = y;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mCircleClickListener != null) {
//                    if (Math.abs(x - mDownX) <= mTouchSlop && Math.abs(y - mDownY) <= mTouchSlop) {
//                        float max = Float.MAX_VALUE;
//                        int index = 0;
//                        for (int i = 0; i < mCirclePoints.size(); i++) {
//                            PointF pointF = mCirclePoints.get(i);
//                            float offset = Math.abs(pointF.x - x);
//                            if (offset < max) {
//                                max = offset;
//                                index = i;
//                            }
//                        }
//                        mCircleClickListener.onClick(index);
//                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mNavigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNavigatorHelper.onPageScrollStateChanged(state);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    @Override
    public void onAttachToMagicIndicator() {
    }

    @Override
    public void onDetachFromMagicIndicator() {
    }

    public void setNormalCircleColor(int normalCircleColor) {
        mNormalCircleColor = normalCircleColor;
        invalidate();
    }

    public void setSelectedCircleColor(int selectedCircleColor) {
        mSelectedCircleColor = selectedCircleColor;
        invalidate();
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        prepareCirclePoints();
        invalidate();
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public void setCircleCount(int count) {
        mCircleCount = count;  // 此处不调用invalidate，让外部调用notifyDataSetChanged
        mNavigatorHelper.setTotalCount(mCircleCount);
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    public void setSkimOver(boolean skimOver) {
        mNavigatorHelper.setSkimOver(skimOver);
    }

    public void setCircleClickListener(OnCircleClickListener circleClickListener) {
        if (!mTouchable) {
            mTouchable = true;
        }
        mCircleClickListener = circleClickListener;
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mFollowTouch) {
            currIndex = index;
            //float rectWidth = minRectWidth + (maxRectWidth - minRectWidth) * mStartInterpolator.getInterpolation(enterPercent);
            //mRectWidthArray.put(index, rectWidth);

            //changeRect(index, rectWidth);
            invalidate();
        }
    }

    private void changeRect(int index, float rectWidth) {
        float tempLeft = 0;
        for (int i = 0; i < mRectList.size(); i++) {
            RectF rectF = mRectList.get(i);
            if (i == index) {
                tempLeft = rectF.left + rectWidth;
                rectF.right = tempLeft;
            } else if (i > index) {
                rectF.left = tempLeft + mCircleSpacing;
                rectF.right = rectF.left + minRectWidth;
            }
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mFollowTouch) {
            //float rectWidth = maxRectWidth + (minRectWidth - maxRectWidth) * mStartInterpolator.getInterpolation(leavePercent);
            //mRectWidthArray.put(index, rectWidth);

            //changeRect(index, rectWidth);

            invalidate();
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (!mFollowTouch) {
            currIndex = index;
            mRectWidthArray.put(index, (float) maxRectWidth);
            invalidate();
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (!mFollowTouch) {
            mRectWidthArray.put(index, (float) minRectWidth);
            invalidate();
        }
    }

    public interface OnCircleClickListener {
        void onClick(int index);
    }
}
