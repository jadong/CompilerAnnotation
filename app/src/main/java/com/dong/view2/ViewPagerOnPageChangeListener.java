package com.dong.view2;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public class ViewPagerOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private View view;

    public ViewPagerOnPageChangeListener(View view) {
        this.view = view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (view != null) {
            view.invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
