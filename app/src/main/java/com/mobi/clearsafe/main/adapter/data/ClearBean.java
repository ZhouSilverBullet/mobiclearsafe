package com.mobi.clearsafe.main.adapter.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ClearBean implements MultiItemEntity {
    public int itemType;
    //清理的类型
    public int clearType;
    //清理的名字
    public String clearName;

    public ClearBean(int itemType, int clearType, String clearName) {
        this.itemType = itemType;
        this.clearType = clearType;
        this.clearName = clearName;
    }

    public ClearBean() {
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
