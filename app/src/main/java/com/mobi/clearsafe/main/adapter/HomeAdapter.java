package com.mobi.clearsafe.main.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.ui.clear.CleanQReportActivity;
import com.mobi.clearsafe.ui.clear.CleanReportActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<ClearBean, BaseViewHolder> {

    public HomeAdapter() {
        super(new ArrayList<>());

        addItemType(1, R.layout.item_home_grid_recycler);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ClearBean item) {
        switch (item.getItemType()) {
            case 1:
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvName = helper.getView(R.id.tvName);
                tvName.setText(item.clearName);

                if (helper.getAdapterPosition() == 3) {
                    helper.itemView.setOnClickListener(v -> {
                        CleanQReportActivity.start(v.getContext());
                    });
                } else {
                    helper.itemView.setOnClickListener(v -> {
                        CleanReportActivity.start(v.getContext());
                    });
                }

                break;
        }
    }


}
