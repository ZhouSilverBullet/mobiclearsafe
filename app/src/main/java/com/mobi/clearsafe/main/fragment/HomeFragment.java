package com.mobi.clearsafe.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.main.adapter.HomeAdapter;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.main.bean.InviteDetail;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.ui.clear.GarbageActivity;
import com.mobi.clearsafe.ui.clear.control.ClearQQWrap;
import com.mobi.clearsafe.ui.clear.control.ClearWechatWrap;
import com.mobi.clearsafe.ui.clear.entity.CleanListBean;
import com.mobi.clearsafe.ui.clear.util.ItemDivider;
import com.mobi.clearsafe.ui.clear.widget.wave.MultiWaveHeader;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.widget.LazyLoadFragment;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends LazyLoadFragment implements Handler.Callback {

    public static final int H_WX_CACHE = 10;
    public static final int H_QQ_CACHE = 11;

    private RecyclerView rv;
    private HomeAdapter homeAdapter;
    private Button btnSkipClear;
    private Handler handler;
    private View vLine;
    private TextView tvPoint;
    private TextView tvStatus;
    private MultiWaveHeader waveHeader;


    public HomeFragment() {
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {
        getCleanList();
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
        vLine = findViewById(R.id.vLine);
        tvPoint = findViewById(R.id.tvPoint);
        tvStatus = findViewById(R.id.tvStatus);
        waveHeader = findViewById(R.id.waveHeader);

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

//        StatusBarUtil.setStatusBarColor(getActivity(), R.color.c_008dff);
        getCleanList();
    }

    private void getCleanList() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getCleanList()
                .compose(CommonSchedulers.<BaseResponse<CleanListBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<CleanListBean>() {
                    @Override
                    public void onSuccess(CleanListBean data) {
                        if (data != null) {
                            handleData(data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }

    private void handleData(CleanListBean data) {
        List<CleanListBean.ListBean> list = data.getList();
        if (list.size() != 5) {
            return;
        }

        List<ClearBean> dataList = homeAdapter.getData();

        for (int i = 0; i < dataList.size(); i++) {
            CleanListBean.ListBean listBean = list.get(i + 1);
            ClearBean clearBean = dataList.get(i);
            clearBean.id = listBean.getId();
            clearBean.points = listBean.getPoints();
        }

        homeAdapter.notifyDataSetChanged();
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
        int randomValue = new Random().nextInt(10);
        if (size < 30000000) {
            vLine.setBackgroundResource(R.drawable.clear_blue_selector);
            int point = 80 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_2B86FF));
            tvStatus.setText("亚健康");
            waveHeader.setStartColor(getResources().getColor(R.color.c_0043ff));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_008dff));

        } else if (size < 50000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
            int point = 60 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF6A00));
            tvStatus.setText("不健康");

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else if (size < 100000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
            int point = 40 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF6A00));
            tvStatus.setText("超负荷");

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
            int point = 20 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF6A00));
            tvStatus.setText("超负荷");

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
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
