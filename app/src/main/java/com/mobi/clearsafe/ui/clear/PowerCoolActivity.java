package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.clearsafe.ui.clear.widget.PowerCoolLayout;

/**
 * 降温
 */
public class PowerCoolActivity extends BaseAppCompatActivity {
    public static final String TAG = "PowerCoolActivity";
    private PowerCoolLayout pclLayout;
    private GoodChangeLayout gclLayout;
    private Toolbar mToolBar;

    public static void start(Context context) {
        Intent intent = new Intent(context, PowerCoolActivity.class);
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
        setContentView(R.layout.activity_power_cool);
        initToolBar();
        initOtherIds();
    }

    private void initOtherIds() {
        gclLayout = findViewById(R.id.gclLayout);
        pclLayout = findViewById(R.id.pclLayout);
        pclLayout.setCoolChangeListener(percent -> {
            if (percent == 1) {
                pclLayout.setVisibility(View.GONE);
                gclLayout.startAnim();
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

}
