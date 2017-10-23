package com.qhad.adsample.videoad;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.qhad.adsample.util.videocontrol.VideoController;

import static com.qhad.adsample.videoad.AdVideoActivity.videoPlay_TextureView;
import static com.qhad.adsample.videoad.AdVideoActivity.video_ScrollView;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.changeViewStatusLoading;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.setVideoControlInvisible;

/**
 * Created by liangyanmiao on 16/12/21.
 */

public class VideoHandler extends Handler {

    /**
     * 滚动事件消息标示
     */
    public final int SCROLL_EVENT = 666666;

    /**
     * 延迟时间，用于调整监测ScrollView停止滑动的灵敏度
     */
    public final long delayMillis = 100;

    /**
     * 隐藏事件消息标示
     */
    public final int HIDE_EVENT = 999999;



    public VideoHandler(Looper looper) {
        super(looper);
    }

    /**
     * 是否允许视频播放
     */
    public boolean isAllowedPlay = true;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SCROLL_EVENT:
                //获取onScrollChanged中获取的scrollY
                int lastScrollY = (int) msg.obj;
                //当前scrollY与lastScrollY进行比较，如果相同则认为ScrollView停止滑动
                if (video_ScrollView.getScrollY() == lastScrollY) {
                    //获取视频播放区域宽度和高度
                    int width = videoPlay_TextureView.getWidth();
                    int height = videoPlay_TextureView.getHeight();

                    Rect rect = new Rect();
                    //视频播放区域在屏幕中是否可见
                    boolean isInScreen = videoPlay_TextureView.getLocalVisibleRect(rect);
                    //获取视频状态
                    int videoStatus = VideoController.getVideoStatus();
                    if (isInScreen) {
                        //当视频播放区域完全显示在屏幕中时
                        if (rect.left == 0 && rect.top == 0 && rect.right == width && rect.bottom == height) {
                            switch (videoStatus) {
                                case 2:
                                    //播放中无操作
                                    break;
                                case 3:
                                    //暂停状态则继续播放视频
                                    VideoController.continuePlay();
                                    break;
                                default:
                                    //从头播放视频
                                    if (isAllowedPlay) {
                                        changeViewStatusLoading();
                                        VideoController.play();
                                    }
                                    break;
                            }
                        }
                        //当视频播放区域完全不可见
                    } else {
                        switch (videoStatus) {
                            case 2:
                                //播放中则暂停播放
                                VideoController.pause();
                                break;
                            case 4:
                            case 5:
                                //播放被停止或播放完成则重置状态
                                isAllowedPlay = true;
                                break;
                            default:
                                break;
                        }
                    }
                }
                break;
            case HIDE_EVENT:
                setVideoControlInvisible();
            default:
                break;
        }
        super.handleMessage(msg);
    }
};
