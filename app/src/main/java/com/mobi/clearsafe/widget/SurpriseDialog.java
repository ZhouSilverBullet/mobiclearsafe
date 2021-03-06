package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.nativeexpress.NativeLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author:zhaijinlu
 * date: 2019/12/26
 * desc: 惊喜弹框
 */
public class SurpriseDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private int mGold;
    private int mAllGold;
    private String btn_text;//按钮文案
    private SurpriseDialogClick mClick;
    private TextView tv_gold_num, tv_gold_money, tv_double, tv_time, tv_bottom_btn;
    private ImageView bg_light,lable;
    private float mMoney;//所有金币数约等于多少钱
    private boolean isVisible;//控制翻倍按钮显示隐藏
    private boolean lableIsVisible=false;//lable显隐
    private boolean fanbei_title=false;//判断title是不是翻倍成功
    private CircleAroundView fl_ad;
    private NativeExpressAd ad;
    private ScenarioEnum scenario;
    private String postID;
    private long countDownTime = 3000;
    private LinearLayout ll_close;
    private boolean isShowAd=true;//是否展示信息流广告


    public SurpriseDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mGold = builder.mGold;
        this.mAllGold = builder.mAllGold;
        this.mClick = builder.mDialogClick;
        this.mMoney = builder.mMoney;
        this.isVisible = builder.isVisible;
        this.scenario = builder.scenario;
        this.btn_text=builder.btn_text;
        this.postID=builder.postID;
        this.lableIsVisible=builder.lableIsVisible;
        this.fanbei_title=builder.fanbei_title;
        this.isShowAd=builder.isShowAd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diglog_surprise_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
        initWindow();
        if (UserEntity.getInstance().getConfigEntity() != null && UserEntity.getInstance().getConfigEntity().getCount_down_time() > 0) {
            countDownTime = UserEntity.getInstance().getConfigEntity().getCount_down_time();
        }
    }

    @Override
    public void show() {
        super.show();

    }

    private void countdown() {
        Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 4 - aLong - 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_time.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tv_time.setVisibility(View.GONE);
                        ll_close.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView() {
        tv_double = findViewById(R.id.tv_double);
        tv_gold_num = findViewById(R.id.tv_gold_num);
        tv_gold_money = findViewById(R.id.tv_gold_money);
        ll_close = findViewById(R.id.ll_close);
        tv_time = findViewById(R.id.tv_time);
        tv_bottom_btn = findViewById(R.id.tv_bottom_btn);
        tv_bottom_btn.setOnClickListener(this);
        ll_close.setOnClickListener(this);
        tv_double.setOnClickListener(this);
        bg_light=findViewById(R.id.bg_light);
        lable=findViewById(R.id.lable);
        lable.setVisibility(lableIsVisible?View.VISIBLE:View.GONE);

        if(!TextUtils.isEmpty(btn_text)){
            tv_double.setText(btn_text);
        }

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_light);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        bg_light.startAnimation(operatingAnim);

        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_double.startAnimation(scaleAnim);

        String gold_num;
        if(fanbei_title){
            gold_num = mContext.getResources().getString(R.string.get_gold_num2);
        }else {
            gold_num = mContext.getResources().getString(R.string.surprise_get_gold);
        }
        String realShow = String.format(gold_num, mGold);
        tv_gold_num.setText(Html.fromHtml(realShow));

        String allMoney = mContext.getResources().getString(R.string.sign_dialog_allmoney);
        String realAllMoney = String.format(allMoney, mAllGold, String.valueOf(mMoney));
        tv_gold_money.setText(Html.fromHtml(realAllMoney));
        tv_double.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        fl_ad = findViewById(R.id.fl_ad);

        if(isShowAd){
            fl_ad.setVisibility(View.VISIBLE);
            ad = new NativeExpressAd.Builder(getContext())
                    .setAdCount(1)
                    .setADViewSize(335, 0)
                    .setHeightAuto(true)
                    .setSupportDeepLink(true)
                    .setScenario(scenario)
                    .setPosID(postID)
                    .setBearingView(fl_ad)
                    .setNativeLoadListener(new NativeLoadListener() {
                        @Override
                        public void onAdClick(String channel) {

                        }

                        @Override
                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                        }

                        @Override
                        public void onAdDismissed(String channel) {

                        }

                        @Override
                        public void onAdRenderSuccess(String channel) {

                        }

                        @Override
                        public void onAdShow(String channel) {

                        }
                    })
                    .build();
            countdown();
        }else {
            fl_ad.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
            ll_close.setVisibility(View.VISIBLE);
        }

    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_CC000000);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
        lps.height = h;
//        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                if (mClick != null) {
                    mClick.closeBtnClick(SurpriseDialog.this);
                }
                break;
            case R.id.tv_double:
                if (mClick != null) {
                    mClick.doubleBtnClick(SurpriseDialog.this);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (ad != null) {
            ad.destory();
        }

    }

    public static class Builder {
        private Context mContext;
        private int mGold = 0;//本次获得金币数量
        private int mAllGold = 0;//当前拥有的所有金币数量
        private float mMoney = 0.0f;//所有金币数约等于多少钱
        private SurpriseDialogClick mDialogClick;
        private boolean isVisible;
        private ScenarioEnum scenario;
        private String btn_text;
        private String postID;
        private boolean lableIsVisible;
        private boolean fanbei_title;
        private boolean isShowAd;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setGold(int gold) {
            this.mGold = gold;
            return this;
        }

        public Builder setIsShowAd(boolean isShowAd){
            this.isShowAd=isShowAd;
            return this;
        }

        public Builder setFanbeiTitle(boolean fanbei_title){
            this.fanbei_title=fanbei_title;
            return this;
        }
        public Builder setPostID(String postID) {
            this.postID = postID;
            return this;
        }

        public Builder setlableIsVisible(boolean lableIsVisible) {
            this.lableIsVisible = lableIsVisible;
            return this;
        }

        public Builder setBtnText(String btnText) {
            this.btn_text = btnText;
            return this;
        }

        public Builder isVisible(boolean isVisible) {
            this.isVisible = isVisible;
            return this;
        }

        public Builder setAllGold(int allGold) {
            this.mAllGold = allGold;
            return this;
        }

        public Builder setMoney(float money) {
            this.mMoney = money;
            return this;
        }

        public Builder setDialogClick(SurpriseDialogClick click) {
            this.mDialogClick = click;
            return this;
        }



        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public SurpriseDialog build() {
            return new SurpriseDialog(this);
        }
    }

    public interface SurpriseDialogClick {
        //金币翻倍按钮 点击事件
        void doubleBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);



    }

}
