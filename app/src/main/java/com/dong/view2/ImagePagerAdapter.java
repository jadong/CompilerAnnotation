package com.dong.view2;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dong.R;
import com.dong.glide.GlideRoundTransform;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/9/8.
 */
public class ImagePagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;

    public ImagePagerAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    int[] imgRes = {
            R.mipmap.image_5,
            R.mipmap.image_6,
            R.mipmap.image_7,
            R.mipmap.image_8,
            R.mipmap.image_9,
            R.mipmap.image_10,
            R.mipmap.image_11,
            R.mipmap.image_12,
    };

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return getRealCount();
    }

    public int getRealCount() {
        return imgRes.length;
    }

    @Override
    public final Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.layout_image_item, null);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        int index = position % imgRes.length;
        imageViewHolder.initData(index);
        container.addView(view);
        return view;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class ImageViewHolder {

        ImageView imageView;

        public ImageViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.iv_image);
        }

        public void initData(int position) {
            Glide.with(imageView.getContext()).load(imgRes[position])
                    .transform(new GlideRoundTransform(imageView.getContext(), 10))
                    .into(imageView);
//            imageView.setImageResource();
        }
    }
}
