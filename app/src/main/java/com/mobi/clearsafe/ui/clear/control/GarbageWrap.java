package com.mobi.clearsafe.ui.clear.control;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.mobi.clearsafe.ui.clear.data.GarbageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:54
 * @Dec 略
 */
public class GarbageWrap {
    private Context context;
    private Handler handler;
    private PackageManager packageManager;

    public GarbageWrap(Context context, Handler handler) {
        this.context = context.getApplicationContext();
        this.handler = handler;
    }

    public void scan() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            packageManager = context.getPackageManager();
            List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
            int applicationInfosSize = applicationInfos.size();
            List<GarbageBean> list = new ArrayList<>();
            for (int i = 0; i < applicationInfosSize; i++) {
                ApplicationInfo applicationInfo = applicationInfos.get(i);
                String packageName = applicationInfo.packageName;
                int icon = applicationInfo.icon;
                Drawable drawable = applicationInfo.loadIcon(packageManager);


                GarbageBean garbageBean = new GarbageBean();
                garbageBean.packageName = packageName;
//                garbageBean.name = applicationInfo.name;
                //获取应用的名字
                garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
                garbageBean.imageDrawable = drawable;
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    garbageBean.isSystemApp = true;
                } else {
                    garbageBean.isSystemApp = false;
                    list.add(garbageBean);
                }
//                        Log.e(TAG, packageName);
//                getAppCacheInfo(packageName);
//                        getPkgSize(MyApplication.getContext(), packageName);
                if (i != applicationInfosSize - 1) {
//                    sleep(50);
                }
            }
            if (handler != null) {
                Message message = Message.obtain();
                message.obj = list;
                message.what = 100;
                handler.sendMessage(message);
            }
        });
    }
}
