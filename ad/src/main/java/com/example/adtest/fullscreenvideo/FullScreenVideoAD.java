package com.example.adtest.fullscreenvideo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.example.adtest.config.TTAdManagerHolder;

/**
 * author : liangning
 * date : 2019-12-24  15:19
 */
public class FullScreenVideoAD {

    private static final String TAG = "FullScreenVideoAD";

    private Context mContext;
    private TTAdNative mTTAdNative;
    private TTFullScreenVideoAd mttFullVideoAd;

    public FullScreenVideoAD(Builder builder) {
        mContext = builder.mContext;
        loadTTAD("942652331");
    }


    /**
     * 加载穿山甲全屏视频
     */
    private void loadTTAD(String CodeId) {
        if (!TTAdManagerHolder.getInitStatus()) {
            TTAdManagerHolder.singleInit(mContext.getApplicationContext(), "5036888", "全民走路_android");
        }
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext.getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CodeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.HORIZONTAL)
                .build();
        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "加载失败" + s + i);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
                mttFullVideoAd = ad;
                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        Log.e(TAG, "广告展示");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.e(TAG, "广告点击");
                    }

                    @Override
                    public void onAdClose() {
                        Log.e(TAG, "广告关闭");
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.e(TAG, "广告播放完毕");
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.e(TAG, "广告跳过");
                    }
                });
                showTTAD();
            }

            @Override
            public void onFullScreenVideoCached() {
                Log.e(TAG, "广告已缓存");
            }
        });
    }

    /**
     * 显示穿山甲广告
     */
    private void showTTAD() {
        if (mttFullVideoAd != null) {
            mttFullVideoAd.showFullScreenVideoAd((Activity) mContext);
            mttFullVideoAd = null;
        }
    }

    public static class Builder {

        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public FullScreenVideoAD builde() {
            return new FullScreenVideoAD(this);
        }

    }


}
