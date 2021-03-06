package com.mobi.clearsafe.ui.clear.data;

import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 11:11
 * @Dec 略
 */
public class GarbageBean implements MultiItemEntity, Comparable<GarbageBean> {

    public int itemType;
    public String packageName;

    public int icon;
    public String name;
    public String dec;
    public Drawable imageDrawable;

    public boolean isSystemApp;

    public long fileSize;

    public boolean isCheck = true;
    public String[] sizeAndUnit;
    public List<File> fileList = new ArrayList<>();

    public String getFileStrSize() {
        if (sizeAndUnit == null || sizeAndUnit.length == 0) {
            return "";
        }
        return sizeAndUnit[0] + sizeAndUnit[1];
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public int compareTo(GarbageBean o) {
        if (o.fileSize > fileSize) {
            return 1;
        } else if (o.fileSize < fileSize) {
            return -1;
        }
        return 0;
    }
}
