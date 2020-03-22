package com.mobi.clearsafe.ui.clear.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:51
 * @Dec 略
 */
public class GarbageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public long dataAllSize;
    /**
     * 加载结束
     */
    public boolean isLoadFinish;

    public GarbageAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(0, R.layout.garbage_app_item);
        addItemType(1, R.layout.item_garbage_header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case 0: {
                GarbageBean garbageBean = (GarbageBean) item;
                int icon = garbageBean.icon;
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvClear = helper.getView(R.id.tvClear);
                tvClear.setText(garbageBean.name);
                ivIcon.setImageDrawable(garbageBean.imageDrawable);

                CheckBox cbMemory = helper.getView(R.id.cbMemory);
                cbMemory.setText(garbageBean.getFileStrSize());

                cbMemory.setOnCheckedChangeListener(null);
                cbMemory.setChecked(garbageBean.isCheck);
                cbMemory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    garbageBean.isCheck = isChecked;

                    notifyDataSetChanged();

                    if (isChecked) {
                        dataAllSize += garbageBean.fileSize;
                    } else {
                        dataAllSize -= garbageBean.fileSize;
                    }

                    if (checkedListener != null) {
                        String[] fileSize01 = FileUtil.getFileSize0(dataAllSize);
                        checkedListener.onAllChecked(dataAllSize, fileSize01);
                    }
                });

                TextView tvDec = helper.getView(R.id.tvDec);
                tvDec.setText(garbageBean.dec);
            }

            break;
            case 1: {
//                ImageView ivIcon = helper.getView(R.id.ivIcon);
//                TextView tvClear = helper.getView(R.id.tvClear);
                TextView tvText = helper.getView(R.id.tvText);
                ImageView ivImage = helper.getView(R.id.ivImage);
                ProgressBar pbLoad = helper.getView(R.id.pbLoad);
                CheckBox cbText = helper.getView(R.id.cbText);


                final GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) item;

                //设置状态
                pbLoad.setVisibility(garbageHeaderBean.isLoading ? View.VISIBLE : View.GONE);
                cbText.setVisibility(garbageHeaderBean.isLoading ? View.INVISIBLE : View.VISIBLE);

                ivImage.setRotation(garbageHeaderBean.isExpanded() ? 0 : 180);

                tvText.setText(garbageHeaderBean.name);

                String[] fileSize0 = FileUtil.getFileSize0(garbageHeaderBean.allSize);
                cbText.setText(fileSize0[0] + fileSize0[1]);
                cbText.setOnCheckedChangeListener(null);
                cbText.setChecked(garbageHeaderBean.isCheck);

                cbText.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isLoadFinish) {
                        return;
                    }

                    if (isChecked) {
                        for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
                            subItem.isCheck = true;
                            dataAllSize += subItem.fileSize;
                        }
                    } else {
                        for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
                            subItem.isCheck = false;
                            dataAllSize -= subItem.fileSize;
                        }
                    }
                    garbageHeaderBean.isCheck = isChecked;
                    notifyDataSetChanged();
                    if (checkedListener != null) {
                        String[] fileSize01 = FileUtil.getFileSize0(dataAllSize);
                        checkedListener.onAllChecked(dataAllSize, fileSize01);
                    }
                });

                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isLoadFinish) {
                           return;
                        }
                        int pos = helper.getAdapterPosition();
                        if (garbageHeaderBean.isExpanded()) {
                            collapse(pos + getHeaderLayoutCount(), false);
                        } else {
                            expand(pos + getHeaderLayoutCount(), true);
                        }
                    }
                });
            }

            break;
        }

    }

    private IGAdapterCheckedListener checkedListener;

    public void setCheckedListener(IGAdapterCheckedListener checkedListener) {
        this.checkedListener = checkedListener;
    }

    public interface IGAdapterCheckedListener {

        void onAllChecked(long allSize, String[] data);

//        void onSingleChecked(long allSize, String[] data);

    }

}
