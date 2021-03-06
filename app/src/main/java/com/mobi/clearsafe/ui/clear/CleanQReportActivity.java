package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.ui.clear.control.ClearQQWrap;
import com.mobi.clearsafe.ui.clear.control.ScanAnimatorContainer;
import com.mobi.clearsafe.ui.clear.data.Consts;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;
import com.mobi.clearsafe.ui.clear.widget.wave.MultiWaveHeader;
import com.mobi.clearsafe.utils.ToastUtils;

import static com.mobi.clearsafe.main.fragment.HomeFragment.H_QQ_CACHE;

public class CleanQReportActivity extends BaseAppCompatActivity implements Consts, Handler.Callback {

    private Toolbar mToolBar;
    private Handler handler;
    private TextView tvText;
    private TextView tvNum;
    private TextView tvUnit;
    private ClearItemView civAd;
    private ClearItemView civFriend;
    private ClearItemView civWechatGarbage;
    private Button btnClear;
    private ClearQQWrap clearWechatWrap;
//    private View vScan;

    public static final String TAG = "CleanReportActivity";
    private ScanAnimatorContainer scanAnimatorContainer;

    private View vLine;
    private MultiWaveHeader waveHeader;

    public static void start(Context context) {
        Intent intent = new Intent(context, CleanQReportActivity.class);
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

        setContentView(R.layout.activity_clean_q_report);

        if (handler == null) {
            handler = new Handler(this);
        }
        clearWechatWrap = new ClearQQWrap();

//        tvText = findViewById(R.id.tvText);
        initToolBar();
        initContent();
        initEvent();

        findWechatClear();

        //启动扫描
//        ClearBiz clearBiz = new ClearBiz(this, handler);
//        clearBiz.scan();
//        clearBiz.updataTotalDataSiz();

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(intent);
        startScanAnimal();
    }

    private void startScanAnimal() {
//        vScan = findViewById(R.id.vScan);
//        scanAnimatorContainer = new ScanAnimatorContainer(vScan);
//        scanAnimatorContainer.startAnimator();
    }

    private void findWechatClear() {
        clearWechatWrap.findQQClear(handler, tvNum, tvUnit, civAd, civFriend, civWechatGarbage);
    }

    private void initEvent() {
        btnClear.setOnClickListener(v -> {

            execClearForWrap();

        });

        clearWechatWrap.setOnCheckedListener(size -> {
            String[] fileSize0 = FileUtil.getFileSize0(size);
            tvNum.setText(fileSize0[0]);
            tvUnit.setText(fileSize0[1]);
            btnClear.setText("放心清理（" + fileSize0[0] + fileSize0[1] + "）");
        });
    }

    private void execClearForWrap() {
        clearWechatWrap.btnClear(() -> {
            if (clearWechatWrap != null) {
                clearWechatWrap.findQQClear(handler, tvNum, tvUnit, civAd, civFriend, civWechatGarbage);
            }
        }, civAd, civFriend, civWechatGarbage);
    }

    private void initContent() {
        tvNum = findViewById(R.id.tvNum);
        tvUnit = findViewById(R.id.tvUnit);
        civAd = findViewById(R.id.civAd);
        civFriend = findViewById(R.id.civFriend);
        civWechatGarbage = findViewById(R.id.civWechatGarbage);
        btnClear = findViewById(R.id.btnClear);

        civAd.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_qq_friend_icon_title),
                R.mipmap.ic_launcher,
                getString(R.string.clear_qq_friend_icon_dec)));

        civFriend.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_qq_video_title),
                R.mipmap.ic_launcher,
                getString(R.string.clear_qq_video_dec)));

        civWechatGarbage.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_qq_title),
                R.mipmap.ic_launcher,
                getString(R.string.clear_qq_dec)));

        vLine = findViewById(R.id.vLine);
        waveHeader = findViewById(R.id.waveHeader);
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("QQ清理");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.drawable.white_return);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShortSafe("aaaaaa");
//                StatusBarUtil.setStatusBarMode(CleanQReportActivity.this, true, R.color.white);
//                FileManager.scaFile(handler);
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
//                tvText.setText("正在扫描: " + name);
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
                tvUnit.setText(obj[1]);
                break;
            case 1001:
                if (scanAnimatorContainer != null) {
                    scanAnimatorContainer.stop();
                }
                break;
            case H_QQ_CACHE: {
//                String[] fileSizeStr = (String[]) msg.obj;
                int size = msg.arg1;
//                handleFindData(fileSizeStr[0] + fileSizeStr[1], size);
                handleBgStatus(size);
            }

            break;
        }
        return true;
    }

    private void handleBgStatus(int size) {

        if (size < 30000000) {
            vLine.setBackgroundResource(R.drawable.clear_blue_selector);

            waveHeader.setStartColor(getResources().getColor(R.color.c_0043ff));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_008dff));

        } else if (size < 50000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else if (size < 100000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWechatWrap = null;
//        scanAnimatorContainer.stop();
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return super.onSupportNavigateUp();
//    }
}
