package com.mobi.clearsafe.ui.clear.util;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.data.WechatBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WechatClearUtil {
    public static final String TAG = "WechatClearUtil";

    public static void findWxAdCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + "/tencent/MicroMsg/sns_ad_landingpages/");
            if (!file.exists()) {
                return;
            }
            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
                long folderSize = FileUtil.getFolderSize(file);
                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
                if (listener != null) {
                    WechatBean bean = new WechatBean();
                    bean.itemType = 1;
                    bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
                    bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
                    bean.sizeAndUnit = fileSize0;
                    bean.isCheck = true;
                    bean.icon = R.mipmap.ic_launcher;
                    bean.fileList.add(file);
                    bean.fileSize = folderSize;

                    listener.onFindLoad(bean);
                    listener.onFinish();
                }
                Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
            });
        }
    }

    /**
     * cache的时候监听
     */
    public interface IAdCacheListener {
        void onFindLoad(WechatBean bean);

        default void onFinish() {

        }
    }

    public static void findWxFriendCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + "/tencent/MicroMsg/");
            if (!file.exists()) {
                return;
            }
            AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
                List<File> list = new ArrayList<>();
                FileUtil.findPositionFile(file, list, "voice2", (findFile, length) -> {

                    String[] fileSize0 = FileUtil.getFileSize0(length);
                    if (listener != null) {
                        WechatBean bean = new WechatBean();
                        bean.itemType = 1;
                        bean.name = MyApplication.getContext().getString(R.string.clear_wechat_friend_title);
                        bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_friend_dec);
                        bean.sizeAndUnit = fileSize0;
                        bean.isCheck = true;
                        bean.icon = R.mipmap.ic_launcher;
                        bean.fileList.add(findFile);
                        bean.fileSize = length;

                        listener.onFindLoad(bean);
                    }
                    Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
                });
                Log.e(TAG, " fileSize0 ad " + list);
                if (listener != null) {
                    listener.onFinish();
                }
            });
        }
    }

    public static void findWxCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + "/Zuizui/");
            if (!file.exists()) {
                return;
            }
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                int currentTime = 0;

                @Override
                public void run() {
                    List<File> list = new ArrayList<>();
                    //时间次数

                    FileUtil.findPositionFile(file, list, "cache", (findFile, length) -> {

                        String[] fileSize0 = FileUtil.getFileSize0(length);
                        if (listener != null) {
                            WechatBean bean = new WechatBean();
                            bean.itemType = 1;
                            bean.name = MyApplication.getContext().getString(R.string.clear_wechat_title);
                            bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_dec);
                            bean.sizeAndUnit = fileSize0;
                            bean.isCheck = true;
                            bean.icon = R.mipmap.ic_launcher;
                            bean.fileList.add(findFile);
                            bean.fileSize = length;

                            listener.onFindLoad(bean);
                        }
                        Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
                    });
                    Log.e(TAG, " fileSize0 ad " + list);
                    if (listener != null) {
                        listener.onFinish();
                    }
                }
            });
        }
    }

    public static WechatBean createWechatBean(String name, int icon, String dec) {
        WechatBean wechatBean = new WechatBean();
        wechatBean.fileSize = 0;
        wechatBean.name = name;
        wechatBean.icon = icon;
        wechatBean.dec = dec;
        return wechatBean;
    }
}
