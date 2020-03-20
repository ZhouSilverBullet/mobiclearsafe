package com.mobi.clearsafe.ui.clear.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.data.GarbageHeaderBean;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:51
 * @Dec 略
 */
public class GarbageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

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
            }

            break;
            case 1: {
//                ImageView ivIcon = helper.getView(R.id.ivIcon);
//                TextView tvClear = helper.getView(R.id.tvClear);
                TextView tvText = helper.getView(R.id.tvText);
                ImageView ivImage = helper.getView(R.id.ivImage);

                GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) item;

                ivImage.setRotation(garbageHeaderBean.isExpanded() ? 0 : 180);

                tvText.setText(garbageHeaderBean.name);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (garbageHeaderBean.isExpanded()) {
                            collapse(pos + getHeaderLayoutCount(), false);
                        } else {
                            expand(pos + getHeaderLayoutCount(), true);
                        }
//                        garbageHeaderBean.setExpanded(!garbageHeaderBean.isExpanded());
//                        notifyDataSetChanged();
                    }
                });
            }

            break;
        }

    }

}
