
package com.qhad.adsample.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    /**
     * 弹toast并且记录日志
     *
     * @param context 上下文
     * @param msg     信息
     */
    public static void notice(Context context, String msg) {
        Log.e("QhAdSample", msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获得设备宽度,总返回宽与高中较小的一个
     *
     * @return 设备宽度, 如1080
     */
    public static int getDeviceWidth(Context context) {
        DisplayMetrics metrics = context.getResources()
                .getDisplayMetrics();
        if (metrics.widthPixels > metrics.heightPixels) {
            return metrics.heightPixels;
        } else {
            return metrics.widthPixels;
        }
    }

    /**
     * 获得设备高度,总返回宽与高中较大的一个
     *
     * @return 设备宽度, 如1920
     */
    public static int getDeviceHeight(Context context) {
        DisplayMetrics metrics = context.getResources()
                .getDisplayMetrics();
        if (metrics.widthPixels > metrics.heightPixels) {
            return metrics.widthPixels;
        } else {
            return metrics.heightPixels;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 启动开屏动画
     *
     * @param context    上下文
     * @param splashView 开屏所在的view
     * @param listener   动画监听
     */
    public static void startSplashAnimation(Context context, View splashView,
                                            AnimationListener listener) {
        // 初始化 ScaleAnimation动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0, 1, 0);
        // 初始化 Translate动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0,
                getDeviceWidth(context), 0, -getDeviceHeight(context));
        // 初始化 Alpha动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        // 动画集
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimation);
        set.addAnimation(alphaAnimation);
        // 设置动画时间
        set.setDuration(1000);
        splashView.startAnimation(set);
        set.setAnimationListener(listener);
    }

    /**
     * 启动开屏联动动画
     *
     * @param splashLinkView 开屏联动所在的view
     */
    public static void startSplashLinkAnimation(View splashLinkView) {
        // 初始化 Alpha动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        // 设置动画时间
        alphaAnimation.setDuration(1000);
        // 设置重复次数
        alphaAnimation.setRepeatCount(1);
        splashLinkView.startAnimation(alphaAnimation);
    }
}
