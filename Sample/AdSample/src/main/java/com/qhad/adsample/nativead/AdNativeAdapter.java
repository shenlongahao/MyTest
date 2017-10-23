
package com.qhad.adsample.nativead;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ak.android.engine.nav.NativeAd;
import com.qhad.adsample.R;
import com.qhad.adsample.util.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

/**
 * 新闻列表ListView适配器，开发者主要参考getView方法内被注释标记的部分
 */
public class AdNativeAdapter extends BaseAdapter {
    /**
     * 准备展示的广告
     */
    private List<NativeAd> mAds;
    /**
     * 展示广告的activity
     */
    private Activity mAct;
    /**
     * 全局上下文
     */
    private Context mContext;
    /**
     * 布局构造器
     */
    private LayoutInflater mInflater;

    /**
     * 构造方法
     * 
     * @param act 展示广告的activity
     * @param ads 准备展示的广告
     */
    public AdNativeAdapter(Activity act, List<NativeAd> ads) {
        mAct = act;
        mContext = mAct.getApplicationContext();
        mAds = ads;
        mInflater = LayoutInflater.from(mContext);
        initAdSize();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdViewHolder adViewHolder = null;
        int type = getItemViewType(position);
        // 进行重复实例convertView的优化
        if (convertView == null) {
            switch (type) {
                case TYPE_NEWS:
                    convertView = mInflater.inflate(R.layout.layout_item_news, null);
                    break;
                case TYPE_AD:
                    adViewHolder = new AdViewHolder();
                    convertView = mInflater.inflate(R.layout.layout_item_native, null);
                    // 获取标题TextView
                    adViewHolder.tv_ad_title = (TextView) convertView
                            .findViewById(R.id.tv_ad_title);
                    // 获取描述TextView
                    adViewHolder.tv_ad_desc = (TextView) convertView.findViewById(R.id.tv_ad_desc);
                    // 获取下载按钮描述TextView
                    adViewHolder.tv_ad_btntext = (TextView) convertView
                            .findViewById(R.id.tv_ad_btntext);
                    // 获取大图ImageView
                    adViewHolder.iv_ad_pic = (ImageView) convertView.findViewById(R.id.iv_ad_pic);
                    convertView.setTag(adViewHolder);
                    break;
                default:
                    break;
            }
        } else {
            if (type == TYPE_AD) {
                adViewHolder = (AdViewHolder) convertView.getTag();
            }
        }
        /** -----------此处进行广告数据的设置 START----------- **/
        if (type == TYPE_AD) {
            final NativeAd ad = mAds.get(((position + 1) / AD_FREQUENCY) - 1);
            // 获取广告元素Json
            final JSONObject adContent = ad.getContent();
            // 设置标题文字
            adViewHolder.tv_ad_title.setText(adContent.optString("title"));
            // 设置描述文字
            adViewHolder.tv_ad_desc.setText(adContent.optString("desc"));
            // 设置按钮文字
            adViewHolder.tv_ad_btntext.setText(adContent.optString("btntext"));
            // 容错处理 判断大图URL是否为空
            if (!"".equals(adContent.optString("contentimg", ""))) {
                // 通过三方库进行图片下载与加载
                Picasso.with(mContext)
                        .load(adContent.optString("contentimg"))
                        .resize(mAdWidth, mAdHeight)
                        .into(adViewHolder.iv_ad_pic);
            }
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** -----------此处进行广告点击上报----------- **/
                    ad.onAdClick(mAct, v);
                    Utils.notice(mContext, "广告:" + adContent.optString("title") + ",调用广告点击");
                }
            });
            /** -----------此处进行广告曝光上报----------- **/
            ad.onAdShowed(convertView);
            Utils.notice(mContext, "广告:" + adContent.optString("title") + ",调用广告曝光");
        }
        /** -----------此处进行广告数据的设置 END----------- **/
        return convertView;
    }

    /**
     * 广告出现频率
     */
    private final int AD_FREQUENCY = 3;

    @Override
    public int getCount() {
        // 新闻条数为广告个数的AD_FREQUENCY倍
        return mAds.size() * AD_FREQUENCY;
    }

    /**
     * 布局类型数量
     */
    private final int TYPE_COUNT = 2;

    @Override
    public int getViewTypeCount() {
        // 返回布局类型数量
        return TYPE_COUNT;
    }

    /**
     * 布局类型为新闻
     */
    private final int TYPE_NEWS = 0;
    /**
     * 布局类型为广告
     */
    private final int TYPE_AD = 1;

    @Override
    public int getItemViewType(int position) {
        // 不超过广告总数情况下，3条内容中有一条广告
        if ((position + 1) % AD_FREQUENCY == 0 && ((position + 1) / AD_FREQUENCY) <= mAds.size()) {
            return TYPE_AD;
        } else {
            return TYPE_NEWS;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 广告布局Holder,优化重复实例
     */
    static class AdViewHolder
    {
        ImageView iv_ad_pic;
        TextView tv_ad_title, tv_ad_desc, tv_ad_btntext;
    }

    int mAdWidth = 0, mAdHeight = 0;

    /**
     * 初始化预期的广告大图宽高<br>
     * 宽高比=1.8：1<br>
     * 宽度=屏幕宽-20dp<br>
     * 高度=宽度/1.8
     */
    private void initAdSize() {
        // 广告大图宽，这里取手机屏幕宽-20dp
        mAdWidth = Utils.getDeviceWidth(mContext) - Utils.dip2px(mContext, 20);
        // 广告大图高
        mAdHeight = (int) (mAdWidth / 1.8f);
    }
}
