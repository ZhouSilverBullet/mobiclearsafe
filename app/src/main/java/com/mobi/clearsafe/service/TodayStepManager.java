package com.mobi.clearsafe.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

/**
 * author:zhaijinlu
 * date: 2019/10/29
 * desc:
 */
public class TodayStepManager {

    private static final String TAG = "TodayStepManager";


    /**
     * 在程序的最开始调用，最好在自定义的application oncreate中调用
     *
     * @param application
     */
    public static void init(Context application) {

        StepAlertManagerUtils.set0SeparateAlertManager(application);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startTodayStepService(application);
        }
    }

    public static void startTodayStepService(Context application) {
        Intent intent = new Intent(application, StepService.class);
        application.startService(intent);
      //  application.bindService(intent, MainActivity.serviceConnection, Service.BIND_AUTO_CREATE);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


}
