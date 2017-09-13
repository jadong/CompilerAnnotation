package com.dong;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dong.view2.ImagePagerAdapter;
import com.dong.view2.InfiniteBanner;
import com.dong.view2.ScaleLineNavigator;
import com.dong.view2.ViewIndicator;
import com.dong.view2.ZoomOutPageTransformer;
import com.dong.view3.OverlayImageView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;


public class MainActivity extends AppCompatActivity {

    private InfiniteBanner viewPager;
    private LinearLayout ll_root_view;
    private RelativeLayout ll_content;
    private ImagePagerAdapter imagePagerAdapter;
    private OverlayImageView overlayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_root_view = (LinearLayout) findViewById(R.id.ll_root_view);
        ll_content = (RelativeLayout) findViewById(R.id.ll_content);
        viewPager = (InfiniteBanner) findViewById(R.id.viewPager);
        overlayImageView = (OverlayImageView) findViewById(R.id.overlayImageView);
        initViewPager();
        ViewIndicator viewIndicator = (ViewIndicator) findViewById(R.id.magic_indicator);
        viewIndicator.setViewPager(viewPager);
        //initMagicIndicator();

        overlayImageView.initData();
    }

    private void initViewPager() {

        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        viewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        ll_content.setClipChildren(false);

        int margin = dip2px(this, 50);
        int viewPagerWidth = getWindowWidth(this) - margin * 2;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(viewPagerWidth, dip2px(this, 130));
        viewPager.setLayoutParams(layoutParams);

        imagePagerAdapter = new ImagePagerAdapter(this);
        viewPager.setAdapter(imagePagerAdapter);

        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        viewPager.setOffscreenPageLimit(imagePagerAdapter.getCount()+2);

        //viewPager.setOnPageChangeListener(new ViewPagerOnPageChangeListener(ll_content));
        viewPager.setTag(R.id.viewPager, imagePagerAdapter.getRealCount());
        //设置页与页之间的间距
        viewPager.setPageMargin(dip2px(this, 20));

        //将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
//        ll_root_view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return viewPager.dispatchTouchEvent(event);
//            }
//        });
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        ScaleLineNavigator scaleLineNavigator = new ScaleLineNavigator(this);
        scaleLineNavigator.setCircleCount(imagePagerAdapter.getRealCount());
        scaleLineNavigator.setNormalCircleColor(Color.LTGRAY);
        scaleLineNavigator.setSelectedCircleColor(Color.RED);
        scaleLineNavigator.setCircleClickListener(new ScaleLineNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index, false);
            }
        });
        magicIndicator.setNavigator(scaleLineNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initMagicIndicator7() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.TRANSPARENT);
        CommonNavigator commonNavigator7 = new CommonNavigator(this);
        commonNavigator7.setScrollPivotX(0.65f);
        commonNavigator7.setAdjustMode(true);
        commonNavigator7.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return imagePagerAdapter.getCount();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                return new DummyPagerTitleView(context);
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 6));
                indicator.setLineWidth(UIUtil.dip2px(context, 10));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.BLUE);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator7);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    public int dip2px(Context context, float dipValue) {
        float scale = context.getResources()
                .getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getWindowWidth(Context ctx) {
        Display display = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.widthPixels;
    }
}
