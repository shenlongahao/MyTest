package com.qhad.adsample.videoad;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ak.android.engine.navvideo.NativeVideoAd;
import com.ak.android.engine.navvideo.NativeVideoAdLoader;
import com.ak.android.engine.navvideo.NativeVideoAdLoaderListener;
import com.ak.android.shell.AKAD;
import com.qhad.adsample.BaseActivity;
import com.qhad.adsample.R;
import com.qhad.adsample.util.Utils;
import com.qhad.adsample.util.VideoKeywordsBuilder;
import com.qhad.adsample.util.videocontrol.VideoController;
import com.qhad.adsample.util.videocontrol.VideoController.OnVideoStatusChangedListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

import static com.qhad.adsample.videoad.ViewVisibilitySetter.changeViewStatusLoading;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.changeViewStatusPausing;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.changeViewStatusPlaying;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.changeViewStatusStoping;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.hideVideoViewInSencond;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.setVideoControlInvisible;
import static com.qhad.adsample.videoad.ViewVisibilitySetter.setVideoControlVisible;

/**
 * Created by liangyanmiao@360.cn on 16/11/17.
 */
public class AdVideoActivity extends BaseActivity implements OnVideoStatusChangedListener, ViewTreeObserver.OnScrollChangedListener, NativeVideoAdLoaderListener {

    /**
     * 是否自动播放
     */
    private boolean isAutoPlay = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initTitle(R.string.title_video);

        //初始化视频控制器
        VideoController.init();
        //设置视频状态变化监听
        VideoController.setOnVideoStatusChangedListener(this);

        //初始化View
        initView();

        //初始化广告
        initAd();
    }

    /**
     * 初始化广告请求参数并请求广告
     */
    private void initAd() {
        //初始化原生视频广告加载器
        NativeVideoAdLoader adLoader = AKAD.getNativeVideoAdLoader(getApplicationContext(), "u5F613GL1M", this);

        //-----可选的额外参数 start------
        //拼装请求参数
        HashSet<String> names = new HashSet<>();
        names.add("演员1");
        names.add("演员2");
        //拼装参数可以借助sample封装好的VideoKeywordsBuilder类
        new VideoKeywordsBuilder(adLoader)
                .setName("奔跑吧熊大")
                .setEpisode(10)
                .setCast(names)
                .setChannel(VideoKeywordsBuilder.CHANNEL_CARTOON)
                .setRegion("中国")
                .setSource("http://www.360.cn")
                .setYear(2016)
                .build();
        //-----可选的额外参数 end------

        //请求一条广告
        adLoader.loadAds();
    }

    /**
     * 广告实例
     */
    private NativeVideoAd ad;

    /**
     * 广告获取成功
     */
    @Override
    public void onAdLoadSuccess(ArrayList<NativeVideoAd> arrayList) {
        //获取第一条原生视频广告
        ad = arrayList.remove(0);
        //如果含有视频素材
        if (ad.hasVideo()) {
            Utils.notice(AdVideoActivity.this, "原生视频广告请求成功");
            //添加广告
            attachAdtoView(ad);
        } else {
            Utils.notice(AdVideoActivity.this, "无视频素材");
        }
    }

    /**
     * 广告获取失败
     */
    @Override
    public void onAdLoadFailed(int i, String s) {
        Utils.notice(AdVideoActivity.this, "原生视频广告请求失败,错误码:" + i + "\n错误描述:" + s);
    }

    /**
     * 将广告素材添加到View
     */
    private void attachAdtoView(NativeVideoAd ad) {
        //获取原生视频广告内容
        final JSONObject adContent = ad.getContent();

        // 容错处理 判断视频地址是否为空
        if (!"".equals(adContent.optString("video", ""))) {
            //设置视频路径
            VideoController.setVideoPath(adContent.optString("video", ""));
        }

        // 容错处理 判断标题是否为空
        if (!"".equals(adContent.optString("title", ""))) {
            video_title_TextView.setText(adContent.optString("title", ""));
        }

        // 容错处理 判断描述是否为空
        if (!"".equals(adContent.optString("desc", ""))) {
            video_desc_TextView.setText(adContent.optString("desc", ""));
        }

        // 容错处理 判断按钮文字是否为空
        if (!"".equals(adContent.optString("btntext", ""))) {
            ldButton_TextView.setText(adContent.optString("btntext", ""));
        }

        // 容错处理 判断大图URL是否为空
        if (!"".equals(adContent.optString("contentimg", ""))) {
            // 通过三方库进行图片下载与加载
            Picasso.with(AdVideoActivity.this)
                    .load(adContent.optString("contentimg"))
                    .into(coverDisplay_ImageView);
        }

        //使广告可见
        video_area_RelativeLayout.setVisibility(View.VISIBLE);
    }


    /* start 监听视频状态的改变 start */
    @Override
    public void onVideoPrepared(int totalTuration) {
    }

    @Override
    public void onVideoPlayed() {
        changeViewStatusPlaying();

        /** -----------此处进行广告 开始播放 上报----------- **/
        if (ad != null) {
            Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",开始播放");
            ad.onVideoChanged(NativeVideoAd.VIDEO_START, 0);
        }
    }

    /**
     * mProgress 视频播放进度
     */
    private int mProgress;

    @Override
    public void onVideoProgressChanged(int currentProgress) {
        this.mProgress = currentProgress;
    }

    @Override
    public void onVideoPaused(int currentProgress) {
        this.mProgress = currentProgress;
        changeViewStatusPausing();

        /** -----------此处进行广告 暂停播放 上报----------- **/
        if (ad != null) {
            Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",暂停播放，当前进度:" + currentProgress);
            ad.onVideoChanged(NativeVideoAd.VIDEO_PAUSE, currentProgress);
        }
    }

    @Override
    public void onVideoContinued() {
        changeViewStatusPlaying();

        /** -----------此处进行广告 继续播放 上报----------- **/
        if (ad != null) {
            Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",继续播放，继续进度:" + mProgress);
            ad.onVideoChanged(NativeVideoAd.VIDEO_CONTINUE, mProgress);
        }
    }

    @Override
    public void onVideoStopped(int currentProgress) {
        changeViewStatusStoping();

        /** -----------此处进行广告 停止播放 上报----------- **/
        if (ad != null) {
            Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",播放被停止，停止进度:" + currentProgress);
            ad.onVideoChanged(NativeVideoAd.VIDEO_EXIT, currentProgress);
        }
    }

    @Override
    public void onVideoCompleted() {
        changeViewStatusStoping();
        videoHandler.isAllowedPlay = false;
        removeMessages(videoHandler.HIDE_EVENT);

        /** -----------此处进行广告 完成播放 上报----------- **/
        if (ad != null) {
            Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",播放完毕");
            ad.onVideoChanged(NativeVideoAd.VIDEO_EXIT, VideoController.getVideoDuration());
        }
    }
    /* end 监听视频状态的改变 end */

    /* start 监听ScrollView的onScrollChanged事件 start */
    @Override
    public void onScrollChanged() {
        //曝光发送监控
        adShowedMonitering();
        //视频控制监控
        videoMonitering();
    }
    /* end 监听onScrollChanged的Touch事件 end */

    /**
     * 上次onScrollChanged事件时视频播放区域是否可见
     */
    private boolean isInScreenLastTime = false;

    /**
     * 曝光监控
     */
    private void adShowedMonitering() {
        //视频播放区域在屏幕中是否可见
        boolean isInScreen = videoPlay_TextureView.getLocalVisibleRect(new Rect());
        //视频播放区域由不可见变为可见，则发送曝光
        if (!isInScreenLastTime && isInScreen) {

            /** -----------此处进行广告 曝光 上报----------- **/
            if (ad != null) {
                Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",被曝光");
                ad.onAdShowed(null);
            }
        }
        isInScreenLastTime = isInScreen;
    }

    /**
     * 此Handler用于处理ScrollView滑动完成后对视频进行操作,以及控制按钮的隐藏
     */
    public static VideoHandler videoHandler = new VideoHandler(Looper.getMainLooper());

    /**
     * 视频控制监控
     */
    private void videoMonitering() {
        //获取ScrollView中可见区域顶部的位置（像素）
        int scrollY = video_ScrollView.getScrollY();
        //delayMillis后监测ScrollView是否停止滑动
        videoHandler.sendMessageDelayed(videoHandler.obtainMessage(videoHandler.SCROLL_EVENT, scrollY), videoHandler.delayMillis);
    }

    /**
     * 视频控制及显示组件
     * <p>
     * video_ScrollView 视频容器所在ScrollView
     * video_area_RelativeLayout 视频广告区域
     * video_title_TextView 视频标题
     * videoPlay_TextureView 视频显示容器
     * coverDisplay_ImageView 视频封面容器
     * ldButton_TextView 视频按钮
     * videoControl_ImageView 视频控制按钮
     * loading_ProgressBar 加载视频时的loading
     */
    public static ScrollView video_ScrollView;
    private RelativeLayout video_area_RelativeLayout;
    private TextView video_title_TextView;
    public static TextureView videoPlay_TextureView;
    public static ImageView coverDisplay_ImageView;
    private TextView video_desc_TextView;
    private TextView ldButton_TextView;
    public static ImageView videoControl_ImageView;
    public static ProgressBar loading_ProgressBar;

    /**
     * 初始化View
     */
    private void initView() {
        video_ScrollView = (ScrollView) findViewById(R.id.sv_video);
        if (isAutoPlay) {
            video_ScrollView.getViewTreeObserver().addOnScrollChangedListener(this);
        }

        video_area_RelativeLayout = (RelativeLayout) findViewById(R.id.video);

        video_title_TextView = (TextView) findViewById(R.id.tv_video_title);

        videoPlay_TextureView = (TextureView) findViewById(R.id.texture_view);
        videoPlay_TextureView.setSurfaceTextureListener(new VideoSurfaceTextureListener());
        videoPlay_TextureView.setOnClickListener(this);

        coverDisplay_ImageView = (ImageView) findViewById(R.id.iv_cover);
        coverDisplay_ImageView.setOnClickListener(this);

        video_desc_TextView = (TextView) findViewById(R.id.tv_ad_desc);

        ldButton_TextView = (TextView) findViewById(R.id.tv_ad_btntext);
        ldButton_TextView.setOnClickListener(this);

        videoControl_ImageView = (ImageView) findViewById(R.id.iv_controller);
        videoControl_ImageView.setOnClickListener(this);

        loading_ProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
    }

    /**
     * 处理View点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_controller: {
                int videoStatus = VideoController.getVideoStatus();
                switch (videoStatus) {
                    case 2:
                        //播放中则暂停播放
                        VideoController.pause();
                        removeMessages(videoHandler.HIDE_EVENT);
                        break;
                    case 3:
                        //暂停中则继续播放
                        VideoController.continuePlay();
                        break;
                    default:
                        //从头播放视频
                        changeViewStatusLoading();
                        VideoController.play();
                        break;
                }
            }
            break;
            case R.id.texture_view:
                int videoStatus = VideoController.getVideoStatus();
                switch (videoStatus) {
                    case 2:
                        //播放中点击视频显示或隐藏按钮
                        int viewStatuse = videoControl_ImageView.getVisibility();
                        switch (viewStatuse) {
                            case View.INVISIBLE:
                                setVideoControlVisible();
                                //3s后隐藏
                                hideVideoViewInSencond(3);
                                break;
                            case View.VISIBLE:
                                setVideoControlInvisible();
                                removeMessages(videoHandler.HIDE_EVENT);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.tv_ad_btntext:
                if (ad != null) {
                    ad.onAdClick(AdVideoActivity.this, v);
                }
                break;
            default:
                break;
        }
    }

    private void removeMessages(int what) {
        videoHandler.removeMessages(what);
    }

    /**
     * Activity Resume时继续播放视频
     */
    @Override
    protected void onResume() {
        int videoStatus = VideoController.getVideoStatus();
        if (videoStatus == 3) {
            VideoController.continuePlay();
        }
        super.onResume();
    }

    /**
     * Activity Pause时暂停播放视频
     */
    @Override
    protected void onPause() {
        int videoStatus = VideoController.getVideoStatus();
        if (videoStatus == 2) {
            VideoController.pause();
        }
        super.onPause();
    }

    /**
     * 当Activity被销毁时需要进行相关上报以及回收资源
     */
    @Override
    protected void onDestroy() {
        int videoStatus = VideoController.getVideoStatus();
        //videoStatus 状态为 2:播放中 3:暂停中 则上报打点
        if (videoStatus == 2 || videoStatus == 3) {
            if (ad != null) {
                /** -----------此处进行广告 停止播放 上报----------- **/
                Utils.notice(AdVideoActivity.this, "广告:" + ad.getContent().optString("title") + ",播放被停止，停止进度:" + mProgress);
                ad.onVideoChanged(NativeVideoAd.VIDEO_EXIT, mProgress);
            }
        }

        //回收资源
        VideoController.recycle();
        videoHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}