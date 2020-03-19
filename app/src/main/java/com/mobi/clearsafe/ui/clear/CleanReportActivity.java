package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.ui.clear.data.Consts;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileManager;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;
import com.mobi.clearsafe.utils.StatusBarUtil;
import com.mobi.clearsafe.utils.ToastUtils;

import java.io.File;

public class CleanReportActivity extends BaseAppCompatActivity implements Consts, Handler.Callback {

    private Toolbar mToolBar;
    private Handler handler;
    private TextView tvText;
    private TextView tvNum;
    private TextView tvPoint;
    private ClearItemView civAd;
    private ClearItemView civFriend;
    private Button btnClear;

    public static void start(Context context) {
        Intent intent = new Intent(context, CleanReportActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_report);

        if (handler == null) {
            handler = new Handler(this);
        }
        tvText = findViewById(R.id.tvText);
        initToolBar();
        initContent();
        initEvent();

        //获取微信广告缓存
        findWechatClear();

        //启动扫描
//        ClearBiz clearBiz = new ClearBiz(this, handler);
//        clearBiz.scan();
//        clearBiz.updataTotalDataSiz();

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(intent);

    }

    private void findWechatClear() {

        WechatClearUtil.findWxAdCache(bean -> {
            handler.post(() -> {
                civAd.setData(bean);
            });
        });

        //获取微信朋友圈文件缓存
        WechatClearUtil.findWxFriendCache(bean -> {
            handler.post(() -> {
                civFriend.setData(bean);
            });
        });
    }

    private void initEvent() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        boolean check = civAd.isCheck();
                        if (check) {
                            for (File file : civAd.getFile()) {
                                FileUtil.deleteFolderFile(file.getPath(), false);
                            }
                        }
                        civAd.release();
                        findWechatClear();
                    }
                });

            }
        });
    }

    private void initContent() {
        tvNum = findViewById(R.id.tvNum);
        tvPoint = findViewById(R.id.tvPoint);
        civAd = findViewById(R.id.civAd);
        civFriend = findViewById(R.id.civFriend);
        btnClear = findViewById(R.id.btnClear);

        civAd.setData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_wechat_ad_title),
                R.mipmap.ic_launcher,
                getString(R.string.clear_wechat_ad_dec)));

        civFriend.setData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_wechat_friend_title),
                R.mipmap.ic_launcher,
                getString(R.string.clear_wechat_friend_dec)));

//        mRv = findViewById(R.id.rv);
//        mRv.setLayoutManager(new LinearLayoutManager(this));
//        //todo 有好几种adapter
//        WechatAdapter adapter = new WechatAdapter();
//        mRv.setAdapter(adapter);

//        WechatBean wechatBean = new WechatBean();
//        wechatBean.itemType = 1;
//        adapter.addData(wechatBean);
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("微信清理");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.mipmap.back_icon);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShortSafe("aaaaaa");
                StatusBarUtil.setStatusBarMode(CleanReportActivity.this, true, R.color.white);
                FileManager.scaFile(handler);
            }
        });


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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Consts.MSG_SCANNIG:
                Bundle data = msg.getData();
//                long totalCacheSize = data.getLong(Consts.KEY_TOTAL_CACHESIZE);
                String name = data.getString(Consts.KEY_NAME);
                tvText.setText("正在扫描: " + name);
                break;
            case MSG_PROGRESSBAR_SET_MAX:
                int max = (int) msg.obj;
                ToastUtils.showShortSafe("" + max);
//                progressBar.setMax(max);
                break;
//            case MSG_SCANNIG:
//                Bundle data = msg.getData();
//                totalCacheSize = data.getLong(KEY_TOTAL_CACHESIZE);
//                if (totalCacheSize > 0 && background != YELLOW) {
//                    Tools.loadColorAnimator(ClearActivity.this, rlScanInfo, R.animator.animator_green2yellow);
//                    background = YELLOW;
//                }
//                String name = data.getString(KEY_NAME);
//                tvName.setText("正在扫描: " + name);
//                progress++;
//                progressBar.setProgress(progress);
//                groups = data.getParcelableArrayList(KEY_GROUPS);
//                adpater.notifyDataSetChanged(groups);
//                break;
            case MSG_UPDATE_TOTAL_CACHESIZE:
//                htvTotalCacheSize.animateText(Formatter.formatFileSize(ClearActivity.this, totalCacheSize));
                break;
            case MSG_SCAN_FINISH:
//                groups = (List<Group>) msg.obj;
//                if (totalCacheSize > 0) {
//                    Tools.loadColorAnimator(ClearActivity.this, rlScanInfo, R.animator.animator_yellow2red);
//                    background = RED;
//                }
//                htvTotalCacheSize.setAnimateType(HTextViewType.ANVIL);
//                htvTotalCacheSize.animateText("一共发现: " + Formatter.formatFileSize(ClearActivity.this, totalCacheSize));
//                tvName.setText("扫描结束");
//                adpater.notifyDataSetChanged(groups);
//                rlClear.startAnimation(btnEnterAnim);
//                rlClear.setVisibility(View.VISIBLE);
                break;
            case 1000:
                String[] obj = (String[]) msg.obj;
                tvNum.setText(obj[0]);
                tvPoint.setText(obj[1]);
                break;
        }
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return super.onSupportNavigateUp();
//    }
}
