package com.mobi.clearsafe.ui.clear.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 17:52
 * @Dec 略
 */
public class GarbageHeaderBean extends AbstractExpandableItem<GarbageBean> implements MultiItemEntity {
    public String name;
    public int level;
    public boolean isCheck = true;
    public long allSize;
    public int headerType;

    //正在加载中一个状态
    public boolean isLoading = true;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
