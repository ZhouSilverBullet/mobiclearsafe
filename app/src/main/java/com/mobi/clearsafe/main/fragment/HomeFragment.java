package com.mobi.clearsafe.main.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.HomeAdapter;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.main.widget.MyRecyclerView;
import com.mobi.clearsafe.ui.clear.GarbageActivity;
import com.mobi.clearsafe.widget.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends LazyLoadFragment {

    private ImageView ivIcon;
    private MyRecyclerView rvGrid;
    private HomeAdapter homeAdapter;

    public HomeFragment() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void firstIn() {
        initView();
    }

    private void initView() {
        ivIcon = findViewById(R.id.ivIcon);
        rvGrid = findViewById(R.id.ivGrid);
        rvGrid.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        homeAdapter = new HomeAdapter();
        rvGrid.setAdapter(homeAdapter);

        View inflate = getLayoutInflater().inflate(R.layout.home_layout, null);
        homeAdapter.addHeaderView(inflate);

        List<ClearBean> list = getRvGridData();
        homeAdapter.addData(list);

        rvGrid.setOnScrollPercentListener(new MyRecyclerView.OnScrollPercentListener() {
            @Override
            public void onPercent(float percent) {

            }
        });

        inflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarbageActivity.start(v.getContext());
            }
        });
    }

    private List<ClearBean> getRvGridData() {
        List<ClearBean> list = new ArrayList<>();
        list.add(new ClearBean(1, 1, "微信清理"));
        list.add(new ClearBean(1, 2, "加速内存"));
        list.add(new ClearBean(1, 3, "手机降温"));
        list.add(new ClearBean(1, 4, "qq清理"));
        return list;
    }
}
