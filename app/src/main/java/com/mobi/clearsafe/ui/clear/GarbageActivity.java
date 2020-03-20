package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.adapter.GarbageAdapter;
import com.mobi.clearsafe.ui.clear.control.GarbageWrap;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.clearsafe.ui.clear.util.FileManager;
import com.mobi.clearsafe.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class GarbageActivity extends AppCompatActivity implements Handler.Callback {

    public static final String TAG = "GarbageActivity";

    private Handler handler;
    private RecyclerView rv;
    private GarbageAdapter garbageAdapter;
    private Toolbar mToolBar;

    private NestedScrollView nsvScroll;

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
        list.add(createGarbageHeader("无用安装包", 0));

        return list;
    }

    @NonNull
    private GarbageHeaderBean createGarbageHeader(String name, int level) {
        GarbageHeaderBean bean = new GarbageHeaderBean();
        bean.name = name;
        bean.allSize = 10L;
        bean.headerType = level;
        bean.setSubItems(new ArrayList<GarbageBean>());
        return bean;
    }

    @Override
    public boolean handleMessage(Message msg) {
        List<MultiItemEntity> garbageHeaders = createGarbageHeaders();
        List<GarbageBean> list = (List<GarbageBean>) msg.obj;
        for (MultiItemEntity datum : garbageHeaders) {
            if (datum instanceof GarbageHeaderBean) {
                GarbageHeaderBean bean = (GarbageHeaderBean) datum;
                if (bean.headerType == 0) {
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
        garbageAdapter.replaceData(garbageHeaders);
//        garbageAdapter.expandAll();

        return false;
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
