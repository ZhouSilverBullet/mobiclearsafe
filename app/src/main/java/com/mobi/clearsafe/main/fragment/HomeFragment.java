package com.mobi.clearsafe.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.HomeAdapter;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.ui.clear.GarbageActivity;
import com.mobi.clearsafe.ui.clear.util.ItemDivider;
import com.mobi.clearsafe.widget.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends LazyLoadFragment {

//    private ImageView ivIcon;
    private RecyclerView rv;
    private HomeAdapter homeAdapter;
    private Button btnSkipClear;

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
//        ivIcon = findViewById(R.id.ivIcon);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new ItemDivider().setLastLineNotDraw(true));
        homeAdapter = new HomeAdapter();
        rv.setAdapter(homeAdapter);

//        View inflate = getLayoutInflater().inflate(R.layout.home_layout, null);
//        homeAdapter.addHeaderView(inflate);

        List<ClearBean> list = getRvGridData();
        homeAdapter.addData(list);

        btnSkipClear = findViewById(R.id.btnSkipClear);
        btnSkipClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarbageActivity.start(v.getContext());
            }
        });
    }

    private List<ClearBean> getRvGridData() {
        List<ClearBean> list = new ArrayList<>();
        list.add(new ClearBean(1, 1, "微信清理"));
        list.add(new ClearBean(1, 2, "手机加速"));
        list.add(new ClearBean(1, 3, "手机降温"));
        list.add(new ClearBean(1, 4, "qq清理"));
        return list;
    }
}
