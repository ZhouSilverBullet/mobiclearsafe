package com.mobi.clearsafe.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.downloader.PRDownloader;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.SDKManager;
import com.mobi.clearsafe.push.UmPush;
import com.mobi.clearsafe.statistical.errorlog.ErrorLogUtil;
import com.mobi.clearsafe.statistical.umeng.UmDataUtil;
import com.mobi.clearsafe.utils.LogUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

import java.util.List;

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

    public static PackageManager PM;
    public static List<ApplicationInfo> INSTALLED_APPS;
    //手机电池的温度
    public static double PHONE_CELSIUS = 31;

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
        PM = getPackageManager();
        INSTALLED_APPS = PM.getInstalledApplications(0);

        registerNeedReceiver();
    }

    private void registerNeedReceiver() {
        BatteryBroadcastReceiver receiver = new BatteryBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取电池电量的广播
     */
    private static class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);///电池剩余电量
            intent.getIntExtra("scale", 0);  ///获取电池满电量数值
            intent.getStringExtra("technology");  ///获取电池技术支持
            intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN); ///获取电池状态
            intent.getIntExtra("plugged", 0);  ///获取电源信息
            intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);  ///获取电池健康度
            intent.getIntExtra("voltage", 0);  ///获取电池电压
            int temperature = intent.getIntExtra("temperature", 0);///获取电池温度

            double celsius = getCelsius(temperature);
//            Log.e("BatteryBroadcast", "level : " + level + " temperature: " + celsius);
            PHONE_CELSIUS  = celsius;
        }

        /**
         * 华氏度转摄氏度
         *
         * @param fahrenheit
         * @return
         */
        private double getCelsius(double fahrenheit) {
            double value = ((int) ((fahrenheit - 273.15) * 100)) / 100.0;
            return value;
        }
    }


}
