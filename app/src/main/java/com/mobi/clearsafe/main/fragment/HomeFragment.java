package com.mobi.clearsafe.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.HomeAdapter;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.ui.clear.GarbageActivity;
import com.mobi.clearsafe.ui.clear.control.ClearQQWrap;
import com.mobi.clearsafe.ui.clear.control.ClearWechatWrap;
import com.mobi.clearsafe.ui.clear.util.ItemDivider;
import com.mobi.clearsafe.widget.LazyLoadFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends LazyLoadFragment implements Handler.Callback {

    public static final int H_WX_CACHE = 10;
    public static final int H_QQ_CACHE = 11;

    private RecyclerView rv;
    private HomeAdapter homeAdapter;
    private Button btnSkipClear;
    private Handler handler;


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
        if (handler == null) {
            handler = new Handler(this);
        }

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

        ClearWechatWrap clearWechatWrap = new ClearWechatWrap();
        clearWechatWrap.findWechatClear(handler, null, null);

        ClearQQWrap clearQQWrap = new ClearQQWrap();
        clearQQWrap.findQQClear(handler, null, null);
        
    }

    private List<ClearBean> getRvGridData() {
        List<ClearBean> list = new ArrayList<>();
        list.add(new ClearBean(1, 1, "手机加速", "一键清理，释放空间", R.color.c_999999));
        list.add(new ClearBean(1, 2, "微信清理", "正在获取...", R.color.c_999999));
        list.add(new ClearBean(1, 3, "QQ清理", "正在获取...", R.color.c_999999));
        list.add(new ClearBean(1, 4, "手机降温", "快来给手机降温", R.color.c_999999));
        return list;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case H_WX_CACHE: {
                String[] fileSizeStr = (String[]) msg.obj;
                handleFindData(fileSizeStr[0] + fileSizeStr[1], 1);
                int size = msg.arg1;
                handleBgStatus(size);
            }

            break;
            case H_QQ_CACHE: {
                String[] fileSizeStr = (String[]) msg.obj;
                handleFindData(fileSizeStr[0] + fileSizeStr[1], 2);
                int size = msg.arg1;
                handleBgStatus(size);
            }

            break;
            default:
                break;

        }
        return false;
    }

    private void handleBgStatus(int size) {
        if (size > 50000000) {

        }
    }

    private void handleFindData(String msg, int index) {
        if (homeAdapter != null) {
            homeAdapter.getData().get(index).dec = msg;
            homeAdapter.getData().get(index).color = R.color.c_F49F1F;
            homeAdapter.notifyDataSetChanged();
        }
    }
}
