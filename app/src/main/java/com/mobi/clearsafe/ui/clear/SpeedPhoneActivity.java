package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.clearsafe.ui.clear.widget.SpeedLayout;

public class SpeedPhoneActivity extends BaseAppCompatActivity {

    private RecyclerView rv;
    private GoodChangeLayout gclLayout;
    private SpeedLayout slLayout;
    private View rlRoot;
    private Toolbar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, SpeedPhoneActivity.class);
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
        setContentView(R.layout.activity_speed_phone);

        initRv();
        initToolBar();
        initOtherIds();
    }

    private void initOtherIds() {
        rlRoot = findViewById(R.id.rlRoot);
        gclLayout = findViewById(R.id.gclLayout);
        gclLayout.setTvDec("手机已达最佳状态");
        slLayout = findViewById(R.id.slLayout);
        slLayout.setSpeedChangeListener((percent) -> {
            if (percent == 0) {
                slLayout.setVisibility(View.GONE);
                gclLayout.startAnim();
            }
            if (percent <= 0.7f || percent >= 0.69f) {
//                rlRoot.setBackgroundResource(R.drawable.clear_blue_selector);
            }
        });
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.drawable.white_return);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initRv() {
        rv = findViewById(R.id.rv);
    }
}
