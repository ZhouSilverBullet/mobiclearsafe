package com.mobi.clearsafe.main.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.HomeAdapter;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.widget.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends LazyLoadFragment {

    private ImageView ivIcon;
    private RecyclerView rvGrid;
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

        List<ClearBean> list = getRvGridData();
        homeAdapter.addData(list);
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
