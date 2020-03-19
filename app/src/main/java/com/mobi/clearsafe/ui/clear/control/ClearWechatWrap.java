package com.mobi.clearsafe.ui.clear.control;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class ClearWechatWrap {

    private AtomicInteger currentTime = new AtomicInteger(0);

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
    public void findWechatClear(Handler handler, TextView tvNum, TextView tvUnit, ClearItemView... civ) {
        WechatClearUtil.findWxAdCache(new WechatClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {
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
        WechatClearUtil.findWxFriendCache(new WechatClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {
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
        //获取微信垃圾
        WechatClearUtil.findWxCache(new WechatClearUtil.IAdCacheListener() {
            @Override
            public void onFindLoad(WechatBean bean) {
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
        }
    }

    public interface IClearCallback {
        void onFinish();
    }
}
