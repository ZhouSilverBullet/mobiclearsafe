package com.mobi.clearsafe.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.downloader.PRDownloader;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.SDKManager;
import com.mobi.clearsafe.push.UmPush;
import com.mobi.clearsafe.statistical.errorlog.ErrorLogUtil;
import com.mobi.clearsafe.statistical.umeng.UmDataUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class MyApplication extends MultiDexApplication {

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new MaterialHeader(mContext).setColorSchemeColors(0xff000000);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(mContext).setDrawableSize(20);
            }
        });
    }

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        //多进程适配
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setLog(false)
                .setCustomFragment(true);
        UmDataUtil.UmDataInit();
        UmPush.init(this);
        ErrorLogUtil.init();
        PRDownloader.initialize(getApplicationContext());
        if (!TTAdManagerHolder.getInitStatus()) {
            TTAdManagerHolder.singleInit(getApplicationContext(), "5036888", "全民走路_android");
        }
        SDKManager.InitValPub(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext() {
        return mContext;
    }


}
