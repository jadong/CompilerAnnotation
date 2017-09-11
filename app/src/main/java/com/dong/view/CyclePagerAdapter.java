package com.dong.view;

import android.support.annotation.IntRange;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public abstract class CyclePagerAdapter extends PagerAdapter {

    /**
     * 系数，可以自行设置，但又以下原则需要遵循：
     * <ul>
     * <li>必须大于1</li>
     * <li>尽量小</li>
     * </ul>
     */
    private static final int COEFFICIENT = 10;
    private ViewPager mViewPager;

    public CyclePagerAdapter(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    /**
     * @return 实际数据数量
     */
    @IntRange(from = 0)
    public abstract int getRealDataCount();

    @Override
    public final int getCount() {
        long realDataCount = getRealDataCount();
        if (realDataCount > 1) {
            realDataCount = getRealDataCount() * COEFFICIENT;
            realDataCount = realDataCount > Integer.MAX_VALUE ? Integer.MAX_VALUE : realDataCount;
        }
        return (int) realDataCount;
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        position = position % getRealDataCount();
        return this.instantiateRealItem(container, position);
    }

    public abstract Object instantiateRealItem(ViewGroup container, int position);

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public final void finishUpdate(ViewGroup container) {
        // 数量为1，不做position替换
        if (getCount() <= 1) {
            return;
        }

        int position = mViewPager.getCurrentItem();
        // ViewPager的更新即将完成，替换position，以达到无限循环的效果
        if (position == 0) {
            position = getRealDataCount();
            mViewPager.setCurrentItem(position, false);
        } else if (position == getCount() - 1) {
            position = getRealDataCount() - 1;
            mViewPager.setCurrentItem(position, false);
        }
    }
}
