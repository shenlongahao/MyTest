package com.qhad.adsample.util.videocontrol;

import android.media.MediaMetadataRetriever;
import android.view.Surface;
import android.view.SurfaceHolder;

/**
 * Created by liangyanmiao@360.cn on 16/12/7.
 */
public class VideoController {

    /*The video controlled*/
    private static Video video;
    /*Video player*/
    private static VideoPlayer videoPlayer;
    /*Initialization sign*/
    private static boolean isInited = false;
    /*Log tag*/
    protected static final String LOG_TAG = "VideoController";

    /**
     * Initialize VideoController,only need executed once.
     */
    public static void init() {
        if (!isInited) {
            synchronized (VideoController.class) {
                if (videoPlayer == null) {
                    videoPlayer = new VideoPlayer();
                }
                if (video == null) {
                    video = new Video();
                }
                isInited = true;
            }
        }
    }

    /**
     * Set displayer where video will be played.
     *
     * @param surface
     */
    public static void setVideoDisplayer(Surface surface) {
        videoPlayer.setVideoDisplayer(surface);
    }

    /**
     * Set displayer where video will be played.
     *
     * @param surfaceHolder
     */
    public static void setVideoDisplayer(SurfaceHolder surfaceHolder) {
        videoPlayer.setVideoDisplayer(surfaceHolder);
    }

    /**
     * Set video path.
     */
    public static void setVideoPath(String videoPath) {
        video.videoPath = videoPath;
        videoPlayer.setVideoPath(video.videoPath);
    }

    /**
     * Get video's path that has been set.
     */
    public static String getVideoPath() {
        return video.videoPath;
    }

    /**
     * Set video duration.
     */
    protected static void setVideoDuration(int videoDuration) {
        video.runningTime = videoDuration;
    }

    /**
     * get video duration.
     */
    public static int getVideoDuration() {
        return video.runningTime;
    }

    /**
     * Set video status.
     */
    protected static void setVideoStatus(Video.STATUS videoStatus) {
        video.status = videoStatus;
    }

    /**
     * Get video status.
     */
    public static int getVideoStatus() {
        return video.status.ordinal();
    }

    /**
     * Play Video.
     */
    public static void play() {
        videoPlayer.startPlay();
    }

    /**
     * Pause Video.
     */
    public static void pause() {
        videoPlayer.pause();
    }

    /**
     * Continue to play Video.
     */
    public static void continuePlay() {
        videoPlayer.continuePlay();
    }

    /**
     * Stop Video.
     */
    public static void stop() {
        videoPlayer.stop();
    }

    /**
     * Seek to progress given.
     */
    public static void seekToProgress(int progress) {
        videoPlayer.seekToProgress(progress);
    }

    /**
     * Recycle Resources
     */
    public static void recycle() {
        isInited = false;

        video = null;
        videoPlayer.recycle();
        videoPlayer = null;
    }

    /*The video's status changed listener*/
    protected static OnVideoStatusChangedListener onVideoStatusChangedListener;

    /**
     * Set video OnVideoStatusChangedListener.
     */
    public static void setOnVideoStatusChangedListener(OnVideoStatusChangedListener onVideoStatusChangedListener) {
        VideoController.onVideoStatusChangedListener = onVideoStatusChangedListener;
    }

    /**
     * When video'status is changed,you can catch it using the listener.
     */
    public interface OnVideoStatusChangedListener {
        /**
         * Called when video can be played,usually means that {@code VideoPlayer} is ready for play.
         *
         * @param totalTuration the video turation.
         */
        void onVideoPrepared(int totalTuration);

        /**
         * Called when video start to play.
         */
        void onVideoPlayed();

        /**
         * Called when video's progress changed.
         */
        void onVideoProgressChanged(int currentProgress);

        /**
         * Called when video is paused.
         *
         * @param currentProgress The progress video has been played.
         */
        void onVideoPaused(int currentProgress);

        /**
         * Called when video continues to play.
         */
        void onVideoContinued();

        /**
         * Called when video is stopped but played completely.
         *
         * @param currentProgress The progress video has been played.
         */
        void onVideoStopped(int currentProgress);

        /**
         * Called when video plays completely.
         */
        void onVideoCompleted();
    }
}

