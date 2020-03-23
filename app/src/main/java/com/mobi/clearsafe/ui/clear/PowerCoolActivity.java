package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;

/**
 * 降温
 */
public class PowerCoolActivity extends BaseAppCompatActivity {
    public static final String TAG = "PowerCoolActivity";

    public static void start(Context context) {
        Intent intent = new Intent(context, PowerCoolActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_cool);

    }

}
