package com.mobi.clearsafe.ui.clear.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;

import java.io.File;
import java.util.List;

public class ClearItemView extends FrameLayout {
    private TextView tvClear;
    private TextView tvDec;
    private CheckBox cbMemory;
    private ProgressBar pbLoad;
    private WechatBean mBean;

    public ClearItemView(@NonNull Context context) {
        super(context);
        init();
    }

    public ClearItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        View.inflate(getContext(), R.layout.view_clear_item, this);
        tvClear = findViewById(R.id.tvClear);
        tvDec = findViewById(R.id.tvDec);
        cbMemory = findViewById(R.id.cbMemory);
        pbLoad = findViewById(R.id.pbLoad);

    }

    /**
     * 先把title dec icon这三个元素设置好
     *
     * @param bean
     */
    public void initData(WechatBean bean) {
        mBean = bean;
    }

    public synchronized void setData(WechatBean bean) {
        if (mBean == null) {
            throw new IllegalArgumentException("please init mBean call initData method !!");
        }
        //mBean不为空的时候就复制
        if (bean != null) {
            //文件添加一下，可能是多个文件
            mBean.fileList.addAll(bean.fileList);
            mBean.fileSize = mBean.fileSize + bean.fileSize;
            mBean.sizeAndUnit = FileUtil.getFileSize0(mBean.fileSize);
        }
        tvClear.setText(mBean.name);
        tvDec.setText(mBean.dec);

        //处理右边的状态
        pbLoad.setVisibility(GONE);
        cbMemory.setVisibility(VISIBLE);
        if (mBean.fileSize <= 0) {
            cbMemory.setButtonDrawable(null);
            cbMemory.setText("无需清理");
            cbMemory.setChecked(false);
            mBean.isCheck = false;
        } else {
            mBean.isCheck = true;
            cbMemory.setText(mBean.getFileStrSize());
        }
    }

    public boolean isCheck() {
        return cbMemory.isChecked();
    }

    public List<File> getFile() {
        return mBean.fileList;
    }

    public long getSize() {
        return mBean.fileSize;
    }

    public void release() {
        mBean.fileSize = 0;
    }
}
