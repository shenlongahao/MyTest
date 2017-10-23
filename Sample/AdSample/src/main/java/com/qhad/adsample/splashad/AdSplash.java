
package com.qhad.adsample.splashad;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.ak.android.engine.navbase.NativeAdListener;
import com.ak.android.engine.navsplash.NativeSplashAd;
import com.ak.android.engine.navsplash.NativeSplashAdLoaderListener;
import com.ak.android.shell.AKAD;
import com.qhad.adsample.util.Utils;
import com.squareup.picasso.Picasso;
import com.qhad.adsample.R;


/**
 * 开屏联动广告,模拟了开屏及存在联动情况下的简单示例,动画及时机仅供参考
 */
public class AdSplash implements NativeSplashAdLoaderListener, NativeAdListener, OnClickListener,
        AnimationListener {
    private Activity mActivity;
    private Context mContext;
    private static final int SPLASH_FINISH = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SPLASH_FINISH:
                    splashFinish();
                    break;
            }
        }
    };


    public AdSplash(Activity activity, View splashParent, View splashLinkParent) {
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        initViews(splashParent, splashLinkParent);
        requestAd();
        //启动简单延迟,3秒后隐藏开屏
        mHandler.sendEmptyMessageDelayed(SPLASH_FINISH, 3000);
    }

    /**
     * 开屏布局
     */
    private View mSplashParent;
    /**
     * 开屏图片
     */
    private ImageView mIvSplash;
    /**
     * 联动布局
     */
    private View mSplashLinkParent;
    /**
     * 联动图片
     */
    private ImageView mIvSplashLink;

    private void initViews(View splashParent, View splashLinkParent) {
        mSplashParent = splashParent;
        mSplashLinkParent = splashLinkParent;
        mIvSplash = (ImageView) mSplashParent.findViewById(R.id.iv_ad_splash);
        mIvSplashLink = (ImageView) mSplashLinkParent.findViewById(R.id.iv_ad_splash_linked);
    }

    /**
     * 请求广告
     */
    private void requestAd() {
        // 上下文,
        // 广告位ID,
        // 请求模式(为了保证展示成功率这里设置AKAD.LOAD_TYPE_ALL,将优先展现离线广告,离线广告没有时返回实时广告),
        // 广告请求监听
        AKAD.getNativeSplashAd(mContext, "5PPvPENjI9", AKAD.LOAD_TYPE_ALL, this);
    }

    /**
     * 原生开屏广告实例
     */
    private NativeSplashAd mNativeSplashAd;
    /**
     * 是否联动广告
     */
    private boolean mIsLinked = false;

    /**
     * 广告请求成功
     *
     * @param nativeSplashAd 原生开屏广告实例
     */
    @Override
    public void onAdLoadSuccess(NativeSplashAd nativeSplashAd) {
        if (null != nativeSplashAd) {
            mNativeSplashAd = nativeSplashAd;
            mIsLinked = mNativeSplashAd.isLinked();
            // 设置广告监听
            mNativeSplashAd.setAdListener(this);
            Utils.notice(mContext, "原生开屏广告请求成功,是否联动:" + mNativeSplashAd.isLinked());
            showSplash();
            if (mIsLinked) {
                showLinked();
            }
        }
    }

    /**
     * 广告请求失败
     *
     * @param errorCode 错误码
     * @param msg       错误信息
     */
    @Override
    public void onAdLoadFailed(int errorCode, String msg) {
        Utils.notice(mContext, "原生开屏广告请求失败,错误码:" + errorCode + "\n错误描述:" + msg);
    }

    /**
     * 显示开屏广告
     */
    private void showSplash() {
        //为开屏布局添加点击监听
        mSplashParent.setOnClickListener(this);
        //由于采用了AKAD.LOAD_TYPE_ALL,
        //返回的图片地址格式可能为 http://xxxx.jpg(网络地址)或 file://xxx.jpg(本地地址)
        //开发者需先进行判断,然后采用与之对应的加载方式
        Picasso.with(mContext).
                load(Uri.decode(mNativeSplashAd.getContent().optString("contentimg", "")))
                .into(mIvSplash);
        mNativeSplashAd.onAdShowed(mSplashParent);
    }

    /**
     * 显示联动广告
     */
    private void showLinked() {
        //为联动布局添加点击监听
        mSplashLinkParent.setOnClickListener(this);
        //由于采用了AKAD.LOAD_TYPE_ALL,
        //返回的图片地址格式可能为 http://xxxx.jpg(网络地址)或 file://xxx.jpg(本地地址)
        //开发者需先进行判断,然后采用与之对应的加载方式
        Picasso.with(mContext).
                load(Uri.decode(mNativeSplashAd.getContent().optString("linkedimg", "")))
                .into(mIvSplashLink);
        mSplashLinkParent.setVisibility(View.VISIBLE);
    }

    //-----------广告行为监控 start-----------
    @Override
    public void onAlertChange(int status) {
        switch (status) {
            case 1:
                Utils.notice(mContext, "下载提示框弹出");
                break;
            case 2:
                Utils.notice(mContext, "下载提示框消失");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLandingPageChange(int status) {
        switch (status) {
            case 1:
                Utils.notice(mContext, "内置广告详情页打开");
                break;
            case 2:
                Utils.notice(mContext, "内置广告详情页退出");
                break;
            default:
                break;
        }
    }

    @Override
    public void onLeftApplication() {
        Utils.notice(mContext, "离开应用");
    }

    //-----------广告行为监控 end-----------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_ad_splash_parent:
            case R.id.rl_ad_splash_linked_parent:
                if (mNativeSplashAd != null) {
                    // 调用广告点击
                    mNativeSplashAd.onAdClick(mActivity, v);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开屏展示结束
     */
    private void splashFinish() {
        if (mNativeSplashAd != null) {
            Utils.startSplashAnimation(mContext, mSplashParent, this);
        } else {
            mSplashParent.setVisibility(View.GONE);
        }
        if (mIsLinked) {
            //开屏动画结束后启动联动动画
            Utils.startSplashLinkAnimation(mSplashLinkParent);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mSplashParent.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

}
