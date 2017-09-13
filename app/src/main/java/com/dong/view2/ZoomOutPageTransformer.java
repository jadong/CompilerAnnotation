package com.dong.view2;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    private static final float MAX_SCALE = 1.2f;
    private static final float MIN_SCALE = 0.9f;//0.85f

    @Override
    public void transformPage(View view, float position) {
        if (position < -1) {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        } else if (position <= 1) {
            //缩放因素
            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE);
            view.setScaleX(scaleFactor);
            //微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
            if (position > 0) {
                view.setTranslationX(-scaleFactor * 2);
            } else if (position < 0) {
                view.setTranslationX(scaleFactor * 2);
            }
            view.setScaleY(scaleFactor);

        } else {
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
        }
    }

}