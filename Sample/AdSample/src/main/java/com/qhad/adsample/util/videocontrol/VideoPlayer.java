package com.qhad.adsample.util.videocontrol;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by liangyanmiao@360.cn on 16/12/8.
 */

public class VideoPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    /*Video's player*/
    private MediaPlayer mediaPlayer = new MediaPlayer();
    /*the beginning of video*/
    private final int BEGINNING_OF_VIDEO = 0;

    /**
     * Default constructor.
     */
    VideoPlayer() {
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /*Video start to prepare sign*/
    private boolean isStartedPrepare = false;

    /**
     * Start to play video from {@link #BEGINNING_OF_VIDEO}
     */
    protected synchronized void startPlay() {
        if (isPrepared) {
            playFromProgress(BEGINNING_OF_VIDEO);
            VideoController.setVideoStatus(Video.STATUS.PLAYING);
            startProgressTask();
            if (VideoController.onVideoStatusChangedListener != null) {
                VideoController.onVideoStatusChangedListener.onVideoPlayed();
            }
        } else {
            if (!isStartedPrepare) {
                isStartedPrepare = true;
                /*Prepare the player for playing, asynchronously.Download video,if necessary.*/
                mediaPlayer.prepareAsync();
            }
        }
    }

    /**
     * Continue to play video
     */
    protected synchronized void continuePlay() {
        mediaPlayer.start();
        VideoController.setVideoStatus(Video.STATUS.PLAYING);
        if (VideoController.onVideoStatusChangedListener != null) {
            VideoController.onVideoStatusChangedListener.onVideoContinued();
        }
    }

    /**
     * Pause video
     */
    protected synchronized void pause() {
        mediaPlayer.pause();
        VideoController.setVideoStatus(Video.STATUS.PAUSING);
        int currentProgress = mediaPlayer.getCurrentPosition();
        if (VideoController.onVideoStatusChangedListener != null) {
            VideoController.onVideoStatusChangedListener.onVideoPaused(currentProgress);
        }
    }

    /**
     * Stop video
     */
    protected synchronized void stop() {
        mediaPlayer.stop();
        isPrepared = false;
        VideoController.setVideoStatus(Video.STATUS.STOPPED);
        stopProgressTask();
        int currentProgress = mediaPlayer.getCurrentPosition();
        if (VideoController.onVideoStatusChangedListener != null) {
            VideoController.onVideoStatusChangedListener.onVideoStopped(currentProgress);
        }
    }

    /**
     * Seeks to specified progress.
     */
    protected void seekToProgress(int progress) {
        mediaPlayer.seekTo(progress);
    }

    /**
     * Recycle resources
     */
    protected void recycle() {
        progressTimer.cancel();
        progressTimer.purge();
        progressTimer = null;

        mediaPlayer.release();
        mediaPlayer = null;
    }

    /**
     * Set video that will be played
     *
     * @param videoPath Video's path
     */
    protected void setVideoPath(String videoPath) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(videoPath);
        } catch (Throwable e) {
            Log.e(VideoController.LOG_TAG, Log.getStackTraceString(e));
        }
    }

    /**
     * Set displayer where video will be played
     *
     * @param surface Video's displayer
     */
    protected void setVideoDisplayer(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    /**
     * Set displayer where video will be played
     *
     * @param surfaceHolder Video's displayer
     */
    protected void setVideoDisplayer(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    /*Video's progress timer*/
    private Timer progressTimer = new Timer();
    /*Video's progress task*/
    private TimerTask progressTask;
    /*progress task period*/
    private int PROGRESSTASK_PERIOD = 1000;

    /**
     * Start progress task
     */
    private void startProgressTask() {
        progressTask = new TimerTask() {
            public void run() {
                final int currentProgress = mediaPlayer.getCurrentPosition();
                if (VideoController.onVideoStatusChangedListener != null) {
                    runOnMainThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    VideoController.onVideoStatusChangedListener.onVideoProgressChanged(currentProgress);
                                }
                            }
                    );
                }
            }
        };
        progressTimer.schedule(progressTask, 0, PROGRESSTASK_PERIOD);
    }

    /**
     * Stop progress task
     */
    private void stopProgressTask() {
        if (progressTask != null) {
            progressTask.cancel();
        }
    }

    /*Video preparation sign*/
    private boolean isPrepared = false;

    @Override
    public synchronized void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        VideoController.setVideoStatus(Video.STATUS.PREPARED);
        int duration = mp.getDuration();
        VideoController.setVideoDuration(duration);
        if (VideoController.onVideoStatusChangedListener != null) {
            VideoController.onVideoStatusChangedListener.onVideoPrepared(duration);
        }

        playFromProgress(BEGINNING_OF_VIDEO);
        VideoController.setVideoStatus(Video.STATUS.PLAYING);
        startProgressTask();
        if (VideoController.onVideoStatusChangedListener != null) {
            VideoController.onVideoStatusChangedListener.onVideoPlayed();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isPrepared) {
            Log.e(VideoController.LOG_TAG, "error.");
        } else {
            VideoController.setVideoStatus(Video.STATUS.COMPLETED);
            stopProgressTask();
            if (VideoController.onVideoStatusChangedListener != null) {
                VideoController.onVideoStatusChangedListener.onVideoCompleted();
            }
        }
    }

    /**
     * Play video from progress given
     *
     * @param progress
     */
    private void playFromProgress(int progress) {
        mediaPlayer.seekTo(progress);
        mediaPlayer.start();
    }

    /*The handler with main thread's looper*/
    private Handler mainHandel = new Handler(Looper.getMainLooper());

    /**
     * Run in main thread
     *
     * @param run Runnable that will be run in main thread
     */
    private void runOnMainThread(Runnable run) {
        mainHandel.post(run);
    }
}