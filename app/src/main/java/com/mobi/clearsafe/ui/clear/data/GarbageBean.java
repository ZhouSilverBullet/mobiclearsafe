package com.mobi.clearsafe.ui.clear.data;

import android.graphics.drawable.Drawable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 11:11
 * @Dec 略
 */
public class GarbageBean implements MultiItemEntity {

    public int itemType;
    public String packageName;

    public int icon;
    public String name;
    public String dec;
    public Drawable imageDrawable;

    public boolean isSystemApp;

    public long fileSize;

    @Override
    public int getItemType() {
        return itemType;
    }
}
