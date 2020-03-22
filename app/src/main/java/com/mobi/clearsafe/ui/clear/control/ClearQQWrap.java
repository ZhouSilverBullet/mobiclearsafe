package com.mobi.clearsafe.ui.clear.control;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.mobi.clearsafe.main.fragment.HomeFragment;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.QQClearUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClearQQWrap {

    private AtomicInteger currentTime = new AtomicInteger(0);

    private AtomicLong allSize = new AtomicLong(0);

    /**
     * 按钮触发清理
     *
     * @param callback
     * @param civ
     */
    public void btnClear(IClearCallback callback, ClearItemView... civ) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            for (ClearItemView clearItemView : civ) {
                boolean check = clearItemView.isCheck();
                if (check) {
                    for (File file : clearItemView.getFile()) {
                        FileUtil.deleteFolderFile(file.getPath(), false);
                    }
                }
                clearItemView.release();
            }

            if (callback != null) {
                callback.onFinish();
            }
        });
    }

    /**
     * 找微信对应的清理的位置
     *
     * @param handler
     * @param civ
     */
    public void findQQClear(Handler handler, TextView tvNum, TextView tvUnit, ClearItemView... civ) {
        QQClearUtil.findFriendIconCache(new QQClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {

                allSize.set(allSize.get() + bean.fileSize);
                if (tvNum == null || civ == null) {
                    return;
                }

                handler.post(() -> {
                    civ[0].setData(bean);

                    handleTextNum(tvNum, tvUnit, civ);

                });
            }

            @Override
            public void onFinish() {
                addCurrent(handler);
            }
        });

        //获取微信朋友圈文件缓存
        QQClearUtil.findFriendIconCache(new QQClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {

                allSize.set(allSize.get() + bean.fileSize);
                if (tvNum == null || civ == null) {
                    return;
                }

                handler.post(() -> {
                    if (civ.length > 1) {
                        civ[1].setData(bean);

                        handleTextNum(tvNum, tvUnit, civ);
                    }
                });
            }

            @Override
            public void onFinish() {
                addCurrent(handler);
            }
        });

        //获取qq垃圾
        QQClearUtil.findQQCache(new QQClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {

                allSize.set(allSize.get() + bean.fileSize);
                if (tvNum == null || civ == null) {
                    return;
                }

                handler.post(() -> {
                    if (civ.length > 2) {
                        civ[2].setData(bean);

                        handleTextNum(tvNum, tvUnit, civ);
                    }
                });
            }

            @Override
            public void onFinish() {
                addCurrent(handler);
            }
        });

    }

    private void handleTextNum(TextView tvNum, TextView tvUnit, ClearItemView... civ) {
        long size = 0L;
        for (ClearItemView clearItemView : civ) {
            size += clearItemView.getSize();
        }
        String[] fileSize0 = FileUtil.getFileSize0(size);
        tvNum.setText(fileSize0[0]);
        tvUnit.setText(fileSize0[1]);
    }

    public void addCurrent(Handler handler) {
        currentTime.incrementAndGet();
        if (currentTime.get() == 3) {
            handler.sendEmptyMessageDelayed(1001, 2000);

            Message message = Message.obtain();
            message.what = HomeFragment.H_QQ_CACHE;
            message.arg1 = getIntMermory(allSize.get());
            message.obj = FileUtil.getFileSize0(allSize.get());
            handler.sendMessage(message);
        }
    }

    private int getIntMermory(long l) {
        if (l >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return (int) l;
    }

    public interface IClearCallback {
        void onFinish();
    }
}
