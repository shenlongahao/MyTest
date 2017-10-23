package com.qhad.adsample.nativead;


import android.os.Bundle;
import android.widget.ListView;

import com.ak.android.engine.nav.NativeAd;
import com.ak.android.engine.nav.NativeAdLoaderListener;
import com.ak.android.engine.navbase.AdSpace;
import com.ak.android.engine.navbase.NativeAdListener;
import com.ak.android.engine.navbase.NativeAdLoader;
import com.ak.android.shell.AKAD;
import com.qhad.adsample.BaseActivity;
import com.qhad.adsample.R;
import com.qhad.adsample.util.Utils;

import java.util.ArrayList;

/**
 * 原生广告示范页面，模拟了新闻客户端的新闻列表页面
 */
public class AdNativeActivity extends BaseActivity implements NativeAdLoaderListener,
        NativeAdListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_none);
        initTitle(R.string.title_native);
        initView();
        initAd();
    }

    /**
     * 新闻列表ListView
     */
    private ListView lvNews;

    /**
     * 初始化 页面布局
     */
    private void initView() {
        lvNews = (ListView) findViewById(R.id.lv_news);
    }

    /**
     * 原生广告工厂，用来请求广告
     */
    private NativeAdLoader mNativeLoader;

    /**
     * 初始化 广告
     */
    private void initAd() {
        // 创建原生广告工厂，传入上下文、广告位ID、监听
        mNativeLoader = AKAD.getNativeAdLoader(getApplicationContext(), "5kPQ5YXjPx", this);
        // 加载广告，传入广告个数（0<广告个数<=10）
        mNativeLoader.loadAds();
    }

    /**
     * 广告请求成功
     */
    @Override
    public void onAdLoadSuccess(ArrayList<NativeAd> ads) {
        Utils.notice(this, "原生广告请求成功,广告个数:" + ads.size());
        // 容错判断，不为空且个数大于0
        if (null != ads && ads.size() > 0) {
            for (NativeAd nativeAd : ads) {
                nativeAd.setAdListener(this);
            }
            // 为ListView设置适配器,进行页面展示
            lvNews.setAdapter(new AdNativeAdapter(this, ads));
        }
    }

    /**
     * 广告请求失败
     *
     * @param errorCode 错误码
     * @param msg 错误描述
     */
    @Override
    public void onAdLoadFailed(int errorCode, String msg) {
        Utils.notice(this, "原生广告请求失败,错误码:" + errorCode + "\n错误描述:" + msg);
    }

    /**
     * Activity 退出时进行广告资源释放
     */
    @Override
    protected void onDestroy() {
        if (mNativeLoader != null) {
            mNativeLoader.destroy();
        }
        super.onDestroy();
    }

    //-----------广告行为监控 start-----------
    @Override
    public void onAlertChange(int status) {
        switch (status) {
            case 1:
                Utils.notice(this, "下载提示框弹出");
                break;
            case 2:
                Utils.notice(this, "下载提示框消失");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLandingPageChange(int status) {
        switch (status) {
            case 1:
                Utils.notice(this, "内置广告详情页打开");
                break;
            case 2:
                Utils.notice(this, "内置广告详情页退出");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLeftApplication() {
        Utils.notice(this, "离开应用");
    }

    //-----------广告行为监控 end-----------
}
