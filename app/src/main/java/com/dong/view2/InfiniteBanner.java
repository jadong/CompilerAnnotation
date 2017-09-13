package com.dong.view2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.bobomee.android.scrollloopviewpager.autoscrollviewpager.BannerConfig;
import com.bobomee.android.scrollloopviewpager.autoscrollviewpager.BannerController;
import com.bobomee.android.scrollloopviewpager.loopingviewpager.LoopViewPager;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public class InfiniteBanner extends LoopViewPager {

    private BannerController mBannerController;

    public InfiniteBanner(Context context) {
        super(context);
        init();
    }

    public InfiniteBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        BannerConfig bannerConfig = new BannerConfig(getContext());
        bannerConfig.interval(5000);
        mBannerController = new BannerController(bannerConfig);
        mBannerController.viewPager(this);
        mBannerController.startAutoScroll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mBannerController.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        mBannerController.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }
}
