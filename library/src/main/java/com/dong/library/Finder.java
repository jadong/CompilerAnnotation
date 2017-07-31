package com.dong.library;

import android.app.Activity;
import android.view.View;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/7/31.
 */
public enum Finder {
    VIEW {
        @Override
        public View findView(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },
    ACTIVITY {
        @Override
        public View findView(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };

    public abstract View findView(Object source, int id);
}
