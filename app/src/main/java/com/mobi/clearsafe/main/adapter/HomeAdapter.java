package com.mobi.clearsafe.main.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.ui.clear.CleanQReportActivity;
import com.mobi.clearsafe.ui.clear.CleanReportActivity;
import com.mobi.clearsafe.ui.clear.PowerCoolActivity;
import com.mobi.clearsafe.ui.clear.SpeedPhoneActivity;

import java.util.ArrayList;

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
                TextView tvDec = helper.getView(R.id.tvDec);
                TextView tvReward = helper.getView(R.id.tvReward);
                tvName.setText(item.clearName);

                tvDec.setTextColor(mContext.getResources().getColor(item.color));
                tvDec.setText(item.dec);

                //点击事件处理
                helper.itemView.setOnClickListener(v -> {
                    skipHandler(v, item);
                });
                tvReward.setText("+" + item.points);
//
//                if (helper.getAdapterPosition() == 3) {
//                    helper.itemView.setOnClickListener(v -> {
//                        CleanQReportActivity.start(v.getContext());
//                    });
//                } else {
//                    helper.itemView.setOnClickListener(v -> {
//                        CleanReportActivity.start(v.getContext());
//                    });
//                }

                break;
        }
    }

    private void skipHandler(View v, ClearBean item) {
        switch (item.clearType) {
            //微信
            case 2: {
                CleanReportActivity.start(v.getContext());
            }
            break;
            //手Q
            case 3: {
                CleanQReportActivity.start(v.getContext());
            }
            break;
            //手机降温
            case 4: {
                PowerCoolActivity.start(v.getContext());
            }
            break;
            //手机加速
            default: {
                SpeedPhoneActivity.start(v.getContext());
            }

            break;
        }
    }


}
