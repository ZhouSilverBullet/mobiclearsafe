package com.mobi.step.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import android.support.annotation.Nullable;

import com.mobi.step.R;
import com.mobi.step.manager.AppManager;
import com.mobi.step.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.message.PushAgent;

import me.jessyan.autosize.AutoSizeCompat;

public class BaseAppCompatActivity extends RxAppCompatActivity {
    protected AppManager appManager = AppManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
        PushAgent.getInstance(this).onAppStart();
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }

    protected Activity getActivity() {
        return this;
    }

    public void goBack(View view){
        onBackPressed();
    }

    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.getResources();
    }
}
