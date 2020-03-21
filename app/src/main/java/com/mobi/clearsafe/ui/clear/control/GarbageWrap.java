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
import com.mobi.clearsafe.ui.clear.util.GarbageClearUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:54
 * @Dec 略
 * <p>
 * 按钮要数据
 * top要数据
 * 然后列表要数据
 */
public class GarbageWrap {

    public static final String TAG = "GarbageWrap";

    //全部完成了
    public static final int H_ALL_FINISH = 98;

    public static final int H_PROGRESS = 99;
    public static final int H_GARBAGE_DATA_FINISH = 100;
    //无效安装包
    public static final int H_INVALID_INSTALLATION_PACKAGE_DATA = 101;
    //无效安装包扫描完成
    public static final int H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH = 102;

    //系统垃圾
    public static final int H_SYSTEM_GARBAGE_DATA = 103;
    //系统垃圾扫描完成
    public static final int H_SYSTEM_GARBAGE_DATA_FINISH = 104;

    //广告垃圾
    public static final int H_AD_GARBAGE_DATA = 105;
    //广告垃圾扫描完成
    public static final int H_AD_GARBAGE_DATA_FINISH = 106;

    //卸载垃圾
    public static final int H_UNINSTALL_GARBAGE_DATA = 107;
    //卸载残留完成
    public static final int H_UNINSTALL_GARBAGE_DATA_FINISH = 108;


    private Context context;
    private Handler handler;
    private PackageManager packageManager;

    private AtomicLong allSizeAic = new AtomicLong(0);

    /**
     * 完成的个数
     */
    private AtomicLong finishCount = new AtomicLong(0);
    //任务完成的数量，是5个
    public static final int TASK_FINISH_MAX_COUNT = 5;

//    private AtomicLong garbageSizeAic = new AtomicLong();
//    private AtomicLong invalidSizeAic = new AtomicLong();


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

    public void scan() {

        findCache();
        findSystemGarbage();
        findAdGarbage();
        findUninstallGarbage();
        findApk();

    }

    private void findUninstallGarbage() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            sendMessage(null, H_UNINSTALL_GARBAGE_DATA_FINISH);
        });
    }

    private void findAdGarbage() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            sendMessage(null, H_AD_GARBAGE_DATA_FINISH);
        });
    }

    private void findSystemGarbage() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            sendMessage(null, H_SYSTEM_GARBAGE_DATA_FINISH);
        });
    }


    private void findCache() {
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

                    if (!garbageBean.isSystemApp) {
                        list.add(garbageBean);

                        //把garbageBean传进去
                        findCacheFile(garbageBean, file, fileList, (bean, length) -> {
                            bean.fileSize = length;
                            bean.sizeAndUnit = FileUtil.getFileSize0(length);

//                            long allSize = GarbageClearUtil.getAllSize(list);

                            sendLengthToHandler(length);
                        });
                    }
                }
            }

            //按照文件大小进行排序
            Collections.sort(list);

            if (handler != null) {

//                Log.e(TAG, "--> <--" + allSizeAic.get());
//                Message message = Message.obtain();
//                message.obj = list;
//                message.what = H_GARBAGE_DATA;
//                handler.sendMessage(message);
                sendMessage(list, H_GARBAGE_DATA_FINISH);
            }
        });
    }


    private void findApk() {
        GarbageClearUtil.findApk(new GarbageClearUtil.IGarbageListener() {
            @Override
            public void onFindLoad(GarbageBean bean) {

                //发送长度数据给外面
                sendLengthToHandler(bean.fileSize);

                //发送bean给到外面处理
                Message message = Message.obtain();
                message.obj = bean;
                message.what = H_INVALID_INSTALLATION_PACKAGE_DATA;
                handler.sendMessage(message);

            }

            @Override
            public void onFinish() {
                //无效安装包完成
//                handler.sendEmptyMessage(H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH);
                sendMessage(null, H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH);
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

    private void sendMessage(Object obj, int what) {
        Message message = Message.obtain();
        message.obj = obj;
        message.what = what;
        handler.sendMessage(message);

        finishCount.incrementAndGet();

        if (finishCount.get() == TASK_FINISH_MAX_COUNT) {
            //发送任务全部完成消息
            Message msg = Message.obtain();
            msg.obj = obj;
            msg.what = H_ALL_FINISH;
            handler.sendMessage(msg);
        }
    }

    private void sendLengthToHandler(long length) {
        //重新设置这个的长度
        allSizeAic.set(allSizeAic.get() + length);

        Message message = Message.obtain();
        message.obj = allSizeAic;
        message.what = H_PROGRESS;
        handler.sendMessage(message);
    }

    public interface IGarbageLengthListener {
        void onFileLen(GarbageBean garbageBean, long length);
    }
}
