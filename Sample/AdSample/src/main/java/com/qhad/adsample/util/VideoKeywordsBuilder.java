package com.qhad.adsample.util;

import android.text.TextUtils;

import com.ak.android.engine.navvideo.NativeVideoAdLoader;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 原生视频关键字构建器
 */

public class VideoKeywordsBuilder {

    private HashMap<String, String> mExtras = new HashMap<>();
    private HashSet<String> mKeyWords = new HashSet<>();
    private NativeVideoAdLoader mNativeVideoAdLoader;

    /**
     * 构造器
     *
     * @param nativeVideoAdLoader 原生视频加载器
     */
    public VideoKeywordsBuilder(NativeVideoAdLoader nativeVideoAdLoader) {
        mNativeVideoAdLoader = nativeVideoAdLoader;
    }

    /**
     * 频道：短视频
     */
    public static final int CHANNEL_SHORT_VIDEO = 0;
    /**
     * 频道：电影
     */
    public static final int CHANNEL_FILM = 1;
    /**
     * 频道：电视剧
     */
    public static final int CHANNEL_TV = 2;
    /**
     * 频道：综艺
     */
    public static final int CHANNEL_VARIETY = 3;
    /**
     * 频道：卡通
     */
    public static final int CHANNEL_CARTOON = 4;
    /**
     * 频道：直播
     */
    public static final int CHANNEL_LIVE = 5;


    /**
     * 频道
     *
     * @param channel
     */
    public VideoKeywordsBuilder setChannel(int channel) {
        mExtras.put("v_channel", String.valueOf(channel));
        return this;
    }

    /**
     * 片名或标题
     *
     * @param name
     */
    public VideoKeywordsBuilder setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            mExtras.put("v_name", name);
        }
        return this;
    }

    /**
     * 集数
     *
     * @param n
     */
    public VideoKeywordsBuilder setEpisode(int n) {
        mExtras.put("v_episode", String.valueOf(n));
        return this;
    }

    /**
     * 视频地址
     *
     * @param url
     */
    public VideoKeywordsBuilder setSource(String url) {
        if (!TextUtils.isEmpty(url)) {
            mExtras.put("v_source", url);
        }
        return this;
    }

    /**
     * 产地
     *
     * @param region
     */
    public VideoKeywordsBuilder setRegion(String region) {
        if (!TextUtils.isEmpty(region)) {
            mKeyWords.add(region);
        }
        return this;
    }

    /**
     * 演员表
     *
     * @param names
     */
    public VideoKeywordsBuilder setCast(HashSet<String> names) {
        if (names != null && names.size() > 0) {
            mKeyWords.addAll(names);
        }
        return this;
    }

    /**
     * 年份
     *
     * @param year
     */
    public VideoKeywordsBuilder setYear(int year) {
        mKeyWords.add(String.valueOf(year));
        return this;
    }

    /**
     * 构建
     */
    public void build() {
        if (mExtras.size() > 0) {
            if (mNativeVideoAdLoader != null) {
                mNativeVideoAdLoader.setExtras(mExtras);
            }
        }
        if (mKeyWords.size() > 0) {
            if (mNativeVideoAdLoader != null) {
                mNativeVideoAdLoader.setKeyWords(mKeyWords);
            }
        }
    }
}
