package com.qhad.adsample;

import android.content.Context;

import com.ak.android.base.landingpage.ILandingPageListener;
import com.ak.android.base.landingpage.ILandingPageView;

/**
 * Created by 37X21=777 on 16/5/13.
 */
public class AdsLandingPage implements ILandingPageView {

    public static AdsLandingPage newInstance(){
        return new AdsLandingPage();
    }

    @Override
    public void open(Context activity, String s, ILandingPageListener iLandingPageListener) {
        WebViewActivity.launch(activity, s, iLandingPageListener);
    }
}
