package com.qhad.adsample;

import android.app.Application;

import com.ak.android.shell.AKAD;
import com.qhad.adsample.util.Utils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化SDK,传入上下文
        // 打印DebugLog开关,上线前建议改成false
        // 请求测试广告开关,上线前务必改成false
        AKAD.initSdk(getApplicationContext(), true, false);
        Utils.notice(getApplicationContext(), "SDK 初始化完成！");

        AKAD.setLandingPageView(this, AdsLandingPage.newInstance());

        // 预加载开屏联动广告,由于不需要实时返回,
        // 加载模式选择AKAD.LOAD_TYPE_OFFLINE,并且listener设为null
        AKAD.getNativeSplashAd(this, "5PPvPENjI9", AKAD.LOAD_TYPE_OFFLINE, null);
    }

}
