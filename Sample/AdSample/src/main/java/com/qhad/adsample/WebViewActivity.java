package com.qhad.adsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ak.android.base.landingpage.ILandingPageListener;


/**
 * Created by 37X21=777 on 16/5/13.
 */
public class WebViewActivity extends BaseActivity {
    private WebView webView;

    private static ILandingPageListener listener;
    private static String url;

    public static void launch(@NonNull Context mContext, String s, ILandingPageListener iLandingPageListener) {
        url = s;
        listener = iLandingPageListener;
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initViews();
        handleWebViewSettings();
    }

    private void handleWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (listener != null) {
                    listener.onPageStarted(url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (listener != null) {
                    listener.onPageFinished(url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (listener != null) {
                    listener.onReceivedError(errorCode, description, failingUrl);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (listener != null) {
                    listener.onReceivedError(error.getErrorCode(), error.getDescription().toString(), request.getUrl().toString());
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (listener != null && listener.shouldOverrideUrlLoading(url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl(url);
    }

    private void initViews() {
        webView = (WebView)findViewById(R.id.webview);
    }

    @Override
    public void finish() {
        super.finish();
        if (listener != null) {
            listener.onPageExit();
        }
    }
}
