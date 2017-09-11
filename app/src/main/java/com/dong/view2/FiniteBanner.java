package com.dong.view2;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bobomee.android.scrollloopviewpager.autoscrollviewpager.BannerController;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public class FiniteBanner extends ViewPager {

    private BannerController mBannerController;

    public FiniteBanner(Context context) {
        super(context);
        init();
    }

    public FiniteBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBannerController = new BannerController(getContext());
        mBannerController.viewPager(this);
        mBannerController.startAutoScroll();

        mBannerController.pageChangeListener(new SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mBannerController.isFirst() || mBannerController.isLast()) {
                    mBannerController.toggleDirection();
                }
            }
        });
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        mBannerController.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override protected void onDetachedFromWindow() {
        mBannerController.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }
}
