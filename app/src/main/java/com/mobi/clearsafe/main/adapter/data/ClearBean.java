package com.mobi.clearsafe.main.adapter.data;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;

public class ClearBean implements MultiItemEntity {
    public int itemType;
    //清理的类型
    public int clearType;
    //清理的名字
    public String clearName;

    public String dec;

    @ColorRes
    public int color = R.color.c_999999;

    public ClearBean(int itemType, int clearType, String clearName) {
        this.itemType = itemType;
        this.clearType = clearType;
        this.clearName = clearName;
    }

    public ClearBean(int itemType, int clearType, String clearName, String dec, int color) {
        this.itemType = itemType;
        this.clearType = clearType;
        this.clearName = clearName;
        this.dec = dec;
        this.color = color;
    }

    public ClearBean() {
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
