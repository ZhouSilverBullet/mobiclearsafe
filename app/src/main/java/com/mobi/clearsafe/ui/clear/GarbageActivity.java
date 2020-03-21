package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.adapter.GarbageAdapter;
import com.mobi.clearsafe.ui.clear.control.GarbageWrap;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.GarbageClearUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.annotations.NonNull;

import static com.mobi.clearsafe.ui.clear.control.GarbageWrap.H_ALL_FINISH;
import static com.mobi.clearsafe.ui.clear.control.GarbageWrap.H_GARBAGE_DATA_FINISH;
import static com.mobi.clearsafe.ui.clear.control.GarbageWrap.H_INVALID_INSTALLATION_PACKAGE_DATA;
import static com.mobi.clearsafe.ui.clear.control.GarbageWrap.H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH;
import static com.mobi.clearsafe.ui.clear.control.GarbageWrap.H_PROGRESS;

public class GarbageActivity extends AppCompatActivity implements Handler.Callback {

    public static final String TAG = "GarbageActivity";

    private Handler handler;
    private RecyclerView rv;
    private GarbageAdapter garbageAdapter;
    private Toolbar mToolBar;

    private NestedScrollView nsvScroll;

    private TextView tvNum;
    private TextView tvUnit;

    public static void start(Context context) {
        Intent intent = new Intent(context, GarbageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_garbage);
        handler = new Handler(this);
        GarbageWrap garbageWrap = new GarbageWrap(this, handler);
        garbageWrap.scan();

        initToolBar();
        initRv();
        initOtherIds();

//        GarbageClearUtil.findApk(new GarbageClearUtil.IGarbageListener() {
//            @Override
//            public void onFindLoad(GarbageBean bean) {
//                handler.post(() -> {
//                    GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) garbageAdapter.getData().get(4);
//                    garbageHeaderBean.getSubItems().add(bean);
//                    garbageAdapter.notifyDataSetChanged();
//                });
//            }
//
//            @Override
//            public void onFinish() {
//                handler.post(() -> {
//                    GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) garbageAdapter.getData().get(4);
//                    long allSize = 0L;
//                    for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
//                        //选中了才进行计算
//                        if (subItem.isCheck) {
//                            allSize += subItem.fileSize;
//                        }
//                    }
//                    garbageHeaderBean.isLoading = false;
//                    garbageHeaderBean.allSize = allSize;
//                    garbageAdapter.notifyDataSetChanged();
//                });
//            }
//        });

    }

    private void initOtherIds() {
        tvNum = findViewById(R.id.tvNum);
        tvUnit = findViewById(R.id.tvUnit);
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("垃圾清理");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.drawable.white_return);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShortSafe("aaaaaa");
//                StatusBarUtil.setStatusBarMode(CleanReportActivity.this, true, R.color.white);
//                FileManager.scaFile(handler);
            }
        });

    }

    private void initRv() {
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        garbageAdapter = new GarbageAdapter(new ArrayList<>());
        rv.setNestedScrollingEnabled(true);
        rv.setAdapter(garbageAdapter);

        List<MultiItemEntity> headerBeans = createGarbageHeaders();

        garbageAdapter.addData(headerBeans);

        nsvScroll = findViewById(R.id.nsvScroll);
        nsvScroll.setOnScrollChangeListener((NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
            Log.e(TAG, "scrollX: " + scrollX + " scrollY: " + scrollY + " oldScrollX: " + oldScrollX + " oldScrollY: " + oldScrollY);

            if (scrollY < 300) {
                mToolBar.setBackgroundColor(getResources().getColor(R.color.transparent));
                mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
                mToolBar.setNavigationIcon(R.drawable.white_return);
                setDarkStatusIcon(getWindow(), false);
            } else {
                mToolBar.setBackgroundColor(getResources().getColor(R.color.white));
                mToolBar.setTitleTextColor(getResources().getColor(R.color.c_3D3F5C));
                mToolBar.setNavigationIcon(R.mipmap.back_icon);
                setDarkStatusIcon(getWindow(), true);
            }
        });

    }

    private List<MultiItemEntity> createGarbageHeaders() {
        List<MultiItemEntity> list = new ArrayList<>();

        list.add(createGarbageHeader("缓存垃圾", 0));
        list.add(createGarbageHeader("系统垃圾", 1));
        list.add(createGarbageHeader("广告垃圾", 2));
        list.add(createGarbageHeader("卸载残留", 3));
        list.add(createGarbageHeader("无用安装包", 4));

        return list;
    }

    @NonNull
    private GarbageHeaderBean createGarbageHeader(String name, int headerType) {
        GarbageHeaderBean bean = new GarbageHeaderBean();
        bean.name = name;
        bean.allSize = 10L;
        bean.headerType = headerType;
        bean.setSubItems(new ArrayList<GarbageBean>());
        return bean;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case H_PROGRESS: {
                //1152905858
                //1063357649
                AtomicLong allSizeAic = (AtomicLong) msg.obj;

                computeAllSize(allSizeAic.get());

            }
            break;
            case H_GARBAGE_DATA_FINISH: {
                List<MultiItemEntity> data = garbageAdapter.getData();

                List<GarbageBean> list = (List<GarbageBean>) msg.obj;
                for (MultiItemEntity datum : data) {
                    if (datum instanceof GarbageHeaderBean) {
                        GarbageHeaderBean bean = (GarbageHeaderBean) datum;
                        if (bean.headerType == 0) {

                            long allSize = GarbageClearUtil.getAllSize(list);
                            //数据回来了，重置一下状态
                            bean.isLoading = false;
                            bean.allSize = allSize;
                            bean.setSubItems(list);

                        } else if (bean.headerType == 1) {
                            List<GarbageBean> list2 = new ArrayList<>();
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            list2.add(new GarbageBean());
                            bean.setSubItems(list2);
                        }
                    }
                }

                garbageAdapter.setNewData(data);
            }
            break;

            case H_INVALID_INSTALLATION_PACKAGE_DATA: {
                GarbageBean bean = (GarbageBean) msg.obj;
                GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) garbageAdapter.getData().get(4);
                garbageHeaderBean.getSubItems().add(bean);
                garbageAdapter.notifyDataSetChanged();
            }
            break;
            case H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH: {
                GarbageHeaderBean garbageHeaderBean = (GarbageHeaderBean) garbageAdapter.getData().get(4);
                long allSize = 0L;
                for (GarbageBean subItem : garbageHeaderBean.getSubItems()) {
                    //选中了才进行计算
                    if (subItem.isCheck) {
                        allSize += subItem.fileSize;
                    }
                }
                garbageHeaderBean.isLoading = false;
                garbageHeaderBean.allSize = allSize;
                garbageAdapter.notifyDataSetChanged();
            }
            break;

            case H_ALL_FINISH: {
                long allSize = 0L;
                for (MultiItemEntity datum : garbageAdapter.getData()) {
                    if (datum instanceof GarbageHeaderBean) {
                        allSize += ((GarbageHeaderBean) datum).allSize;
                    }
                }
                computeAllSize(allSize);
            }
            break;
        }

//        garbageAdapter.expandAll();

        return false;
    }

    private void computeAllSize(long allSize) {
        String[] fileSize0 = FileUtil.getFileSize0(allSize);
        tvNum.setText(fileSize0[0]);
        tvUnit.setText(fileSize0[1]);
        Log.e(TAG, "allSizeAic : " + allSize);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //要抽取
    public static void setDarkStatusIcon(Window window, boolean bDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }
}
