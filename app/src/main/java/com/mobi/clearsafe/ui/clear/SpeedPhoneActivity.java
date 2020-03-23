package com.mobi.clearsafe.ui.clear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mobi.clearsafe.R;

public class SpeedPhoneActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, SpeedPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_phone);
    }
}
