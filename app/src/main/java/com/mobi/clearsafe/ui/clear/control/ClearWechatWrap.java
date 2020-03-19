package com.mobi.clearsafe.ui.clear.control;

import android.os.AsyncTask;
import android.os.Handler;

import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;

import java.io.File;

public class ClearWechatWrap {
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
    public void findWechatClear(Handler handler, ClearItemView... civ) {
        WechatClearUtil.findWxAdCache(bean -> {
            handler.post(() -> {
                civ[0].setData(bean);
            });
        });

        //获取微信朋友圈文件缓存
        WechatClearUtil.findWxFriendCache(bean -> {
            handler.post(() -> {
                if (civ.length > 1) {
                    civ[1].setData(bean);
                }
            });
        });
        //获取微信垃圾
        WechatClearUtil.findWxCache(bean -> {
            handler.post(() -> {
                if (civ.length > 2) {
                    civ[2].setData(bean);
                }
            });
        });

    }

    public interface IClearCallback {
        void onFinish();
    }
}
