package com.qhad.adsample.util.videocontrol;

import static com.qhad.adsample.util.videocontrol.Video.STATUS.UNPREPARED;

/**
 * Created by liangyanmiao@360.cn on 16/12/8.
 */

public class Video {
    /*Video's path*/
    protected String videoPath;
    /*Video's total duration*/
    protected int runningTime;
    /*Video's width*/
    protected int width;
    /*Video's height*/
    protected int height;
    /*Video's status*/
    protected STATUS status;

    /**
     * Default constructor.
     */
    Video() {
        status = UNPREPARED;
        videoPath = "";
        runningTime = 0;
    }

    enum STATUS {
        /*Video is unprepared*/
        UNPREPARED,
        /*Video is prepared*/
        PREPARED,
        /*Video is playing*/
        PLAYING,
        /*Video is pausing*/
        PAUSING,
        /*Video is stopped*/
        STOPPED,
        /*Video is completed*/
        COMPLETED
    }
}
