package com.dong.view3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dong.R;
import com.dong.glide.GlideCircleTransform;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

/**
 * 重叠图片展示
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/12.
 */
public class OverlayImageView extends FrameLayout {

    private int imageWidth = 50;
    private int imageSpacing = 15;
    private int circleWidth = 2;
    private int[] imgRes = {
            R.mipmap.image_5,
            R.mipmap.image_6,
            R.mipmap.image_7,
            R.mipmap.image_8,
            R.mipmap.image_9,
            R.mipmap.image_10,
            R.mipmap.image_11,
            R.mipmap.image_12,
    };

    public OverlayImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public OverlayImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circleWidth = UIUtil.dip2px(getContext(), 2);
        imageWidth = UIUtil.dip2px(getContext(), 50) + circleWidth;
        imageSpacing = UIUtil.dip2px(getContext(), 15);

    }

    public void initData() {
        int count = imgRes.length;
        ViewGroup.LayoutParams layoutParams1 = getLayoutParams();
        layoutParams1.width = (count - 1) * imageSpacing + imageWidth;
        layoutParams1.height = imageWidth;
        setLayoutParams(layoutParams1);

        int leftMargin = 0;
        for (int i = 0; i < count; i++) {
            final ImageView imageView = new ImageView(getContext());
            FrameLayout.LayoutParams layoutParams = new LayoutParams(imageWidth, imageWidth);
            layoutParams.leftMargin = leftMargin;
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(circleWidth, circleWidth, circleWidth, circleWidth);

            Glide.with(getContext()).load(imgRes[i]).transform(new GlideCircleTransform(getContext()))
                    .listener(new RequestListener<Integer, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Integer integer, Target<GlideDrawable> target, boolean b) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, Integer integer, Target<GlideDrawable> target, boolean b, boolean b1) {
                            imageView.setBackgroundResource(R.drawable.circle_shap);
                            return false;
                        }
                    })
                    .into(imageView);
            addView(imageView);

            leftMargin += imageSpacing;
        }

    }


}
