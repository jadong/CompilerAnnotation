package com.dong;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dong.lib_annotation.BindView;
import com.dong.library.ViewInjector;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/8/14.
 */
public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ViewInjector.injectView(this);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAllowFileAccess(true);  //设置可以访问文件
        webView.getSettings().setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webView.getSettings().setDatabaseEnabled(true);   //开启 database storage API 功能
        webView.getSettings().setAppCacheEnabled(true);//开启 Application Caches 功能
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        String url = "http://s.h5.jumei.com/s/product_union/global_deal/ht170814p2112116t1.html?t_lang=touch&product_attr_aca=n&site=bj&__from_app=1&__from_app=1";
        webView.loadUrl(url);
    }
}
