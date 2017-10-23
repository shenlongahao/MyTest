package com.qhad.adsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liangyanmiao on 16/11/14.
 */
public class BaseActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected ImageView ivBack;
    protected TextView tvTitle;

    protected void initTitle(int title) {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        if(this instanceof MainActivity){
            ivBack.setVisibility(View.INVISIBLE);
        }
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
