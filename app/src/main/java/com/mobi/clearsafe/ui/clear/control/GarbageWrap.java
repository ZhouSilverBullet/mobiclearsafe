package com.mobi.clearsafe.ui.clear.control;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.IFileLengthListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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

//    public void scan() {
//        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
//            packageManager = context.getPackageManager();
//            List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
//            int applicationInfosSize = applicationInfos.size();
//            List<GarbageBean> list = new ArrayList<>();
//            for (int i = 0; i < applicationInfosSize; i++) {
//                ApplicationInfo applicationInfo = applicationInfos.get(i);
//                String packageName = applicationInfo.packageName;
//                int icon = applicationInfo.icon;
//                Drawable drawable = applicationInfo.loadIcon(packageManager);
//
//
//                GarbageBean garbageBean = new GarbageBean();
//                garbageBean.packageName = packageName;
////                garbageBean.name = applicationInfo.name;
//                //获取应用的名字
//                garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
//                garbageBean.imageDrawable = drawable;
//                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
//                    garbageBean.isSystemApp = true;
//                } else {
//                    garbageBean.isSystemApp = false;
//                    list.add(garbageBean);
//                }
////                        Log.e(TAG, packageName);
////                getAppCacheInfo(packageName);
////                        getPkgSize(MyApplication.getContext(), packageName);
//                if (i != applicationInfosSize - 1) {
////                    sleep(50);
//                }
//            }
//            if (handler != null) {
//                Message message = Message.obtain();
//                message.obj = list;
//                message.what = 100;
//                handler.sendMessage(message);
//            }
//        });
//    }

    public void scan2() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            packageManager = MyApplication.PM;
            List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
            int applicationInfosSize = installedApps.size();


            List<GarbageBean> list = new Vector<>();

            for (int i = 0; i < applicationInfosSize; i++) {

                ApplicationInfo applicationInfo = installedApps.get(i);

                String packageName = applicationInfo.packageName;
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
//                    list.add(garbageBean);
                }

                //用线程同步的来进行
                List<File> fileList = new Vector<>();
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    File file = new File(externalStorageDirectory.getPath() + "/Android/data/" + packageName + "/");
                    if (!file.exists()) {
                        continue;
                    }

                    //把garbageBean传进去
                    findCacheFile(garbageBean, file, fileList, (bean, length) -> {
                        bean.fileSize = length;
                        bean.sizeAndUnit = FileUtil.getFileSize0(length);
                        list.add(bean);
                    });

                }
            }

            //按照文件大小进行排序
            Collections.sort(list);

            if (handler != null) {
                Message message = Message.obtain();
                message.obj = list;
                message.what = 100;
                handler.sendMessage(message);
            }
        });
    }

    private static void findCacheFile(GarbageBean garbageBean, File file, List<File> files, IGarbageLengthListener listener) {
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    if ("cache".equals(fileList[i].getName())) {
                        files.add(fileList[i]);
                        garbageBean.fileList.add(fileList[i]);
                        long folderSize = FileUtil.getFolderSize(fileList[i]);
                        if (folderSize != 0) {
                            listener.onFileLen(garbageBean, folderSize);
                        }
                        return;
                    }
                    findCacheFile(garbageBean, fileList[i], files, listener);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public interface IGarbageLengthListener {
        void onFileLen(GarbageBean garbageBean, long length);
    }
}
