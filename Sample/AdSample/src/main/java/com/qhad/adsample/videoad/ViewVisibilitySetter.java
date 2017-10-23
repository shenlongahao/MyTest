package com.qhad.adsample.videoad;

import android.view.View;

import com.qhad.adsample.R;

import static com.qhad.adsample.videoad.AdVideoActivity.coverDisplay_ImageView;
import static com.qhad.adsample.videoad.AdVideoActivity.loading_ProgressBar;
import static com.qhad.adsample.videoad.AdVideoActivity.videoControl_ImageView;
import static com.qhad.adsample.videoad.AdVideoActivity.videoHandler;

/**
 * Created by liangyanmiao on 16/12/21.
 */

public class ViewVisibilitySetter {

    /**
     * 加载视频时View的状态
     */
    public static synchronized void changeViewStatusLoading() {
        videoControl_ImageView.setVisibility(View.INVISIBLE);
        loading_ProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 播放视频时View的状态
     */
    public static synchronized void changeViewStatusPlaying() {
        setPlayViewPlaying();
        coverDisplay_ImageView.setVisibility(View.INVISIBLE);
        videoControl_ImageView.setVisibility(View.INVISIBLE);
        loading_ProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 视频暂停时View的状态
     */
    public static synchronized void changeViewStatusPausing() {
        setPlayViewPausing();
        videoControl_ImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 加载播放完成时View的状态
     */
    public static synchronized void changeViewStatusStoping() {
        setPlayViewPausing();
        coverDisplay_ImageView.setVisibility(View.VISIBLE);
        videoControl_ImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 视频暂停时按钮的图标
     */
    private static void setPlayViewPausing() {
        videoControl_ImageView.setImageResource(R.mipmap.bt_play);
    }

    /**
     * 视频播放时按钮的图标
     */
    private static void setPlayViewPlaying() {
        videoControl_ImageView.setImageResource(R.mipmap.bt_pause);
    }

    /**
     * 使暂停按钮可见
     */
    public static synchronized void setVideoControlVisible() {
        videoControl_ImageView.setVisibility(View.VISIBLE);
    }

    /**
     * 使暂停按钮不可见
     */
    public static synchronized void setVideoControlInvisible() {
        videoControl_ImageView.setVisibility(View.INVISIBLE);
    }

    /**
     * 隐藏View
     */
    public static void hideVideoViewInSencond(long second) {
        videoHandler.sendEmptyMessageDelayed(videoHandler.HIDE_EVENT, second * 1000);
    }

}
