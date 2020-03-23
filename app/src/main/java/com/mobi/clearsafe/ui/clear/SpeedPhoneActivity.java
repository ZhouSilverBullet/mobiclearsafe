package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.clearsafe.ui.clear.widget.SpeedLayout;

public class SpeedPhoneActivity extends AppCompatActivity {

    private RecyclerView rv;
    private GoodChangeLayout gclLayout;
    private SpeedLayout slLayout;
    private View rlRoot;

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
        initOtherIds();
    }

    private void initOtherIds() {
        rlRoot = findViewById(R.id.rlRoot);
        gclLayout = findViewById(R.id.gclLayout);
        slLayout = findViewById(R.id.slLayout);
        slLayout.setSpeedChangeListener((percent) -> {
            if (percent == 0) {
                slLayout.setVisibility(View.GONE);
            }
            if (percent <= 0.7f || percent >= 0.69f) {
//                rlRoot.setBackgroundResource(R.drawable.clear_blue_selector);
            }
            gclLayout.startAnim();
        });
    }

    private void initRv() {
        rv = findViewById(R.id.rv);
    }
}
