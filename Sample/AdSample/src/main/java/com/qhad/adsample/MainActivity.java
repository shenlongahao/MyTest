package com.qhad.adsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qhad.adsample.nativead.AdNativeActivity;
import com.qhad.adsample.splashad.AdSplash;
import com.qhad.adsample.videoad.AdVideoActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTitle(R.string.app_name);
        initContentView();

        inidSplashAd();
    }

    private void initContentView() {
        initItem(findViewById(R.id.item_native), getResources().getColor(R.color.red), R.string.item_native_name);
        initItem(findViewById(R.id.item_video), getResources().getColor(R.color.orange), R.string.item_video_name);
    }

    /**
     * 初始化开屏联动广告
     */
    private void inidSplashAd() {
        new AdSplash(this, findViewById(R.id.rl_ad_splash_parent),
                findViewById(R.id.rl_ad_splash_linked_parent));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_native:
                jumpActivity(AdNativeActivity.class);
                break;
            case R.id.item_video:
                jumpActivity(AdVideoActivity.class);
                break;
            default:
                break;
        }
    }

    private void initItem(View view, int color, int typeName) {
        view.setOnClickListener(this);
        RelativeLayout rlColor = (RelativeLayout) view.findViewById(R.id.rl_color);
        rlColor.setBackgroundColor(color);
        TextView tvTypeName = (TextView) view.findViewById(R.id.tv_ad_type);
        tvTypeName.setText(typeName);
    }

    private void jumpActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
