package com.qhad.adsample.videoad;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;

import com.qhad.adsample.util.videocontrol.VideoController;

/**
 * Created by liangyanmiao on 16/12/21.
 */

public class VideoSurfaceTextureListener implements TextureView.SurfaceTextureListener {
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //设置视频的播放区域
        VideoController.setVideoDisplayer(new Surface(surface));
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
