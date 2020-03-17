package com.mobi.step.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.step.R;
import com.mobi.step.app.Const;
import com.mobi.step.base.BaseAppCompatActivity;
import com.mobi.step.eventbean.UserInfoEvent;
import com.mobi.step.me.bean.UploadNikeName;
import com.mobi.step.net.BaseObserver;
import com.mobi.step.net.BaseResponse;
import com.mobi.step.net.CommonSchedulers;
import com.mobi.step.net.OkHttpClientManager;
import com.mobi.step.utils.ToastUtils;
import com.mobi.step.wxapi.bean.UserEntity;
import com.mobi.step.wxapi.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;

public class NickNameAcitivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_nickName;
    private String nick_name;

    public static Intent IntoSettings(Activity activity) {
        Intent intent = new Intent(activity, NickNameAcitivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name_acitivity);
        nick_name = getIntent().getStringExtra("nick_name");
        initView();
    }

    private void initView() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_save=findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        et_nickName=findViewById(R.id.et_nickname);
        if(!TextUtils.isEmpty(nick_name)){
            et_nickName.setText(nick_name);
            et_nickName.setSelection(nick_name.length());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:
                if(et_nickName.getText().toString().trim().length()>10){
                    ToastUtils.showShort(getString(R.string.nick_name_toast));
                    return;
                }
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .upLoadNickName(UserEntity.getInstance().getUserId(),et_nickName.getText().toString().trim())
                        .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(this))
                        .subscribe(new BaseObserver<UploadNikeName>() {
                            @Override
                            public void onSuccess(UploadNikeName demo) {
                                UserInfo userInfo = UserEntity.getInstance().getUserInfo();
                                userInfo.setNickname(et_nickName.getText().toString().trim());
                                UserEntity.getInstance().setUserInfo(userInfo);
                                EventBus.getDefault().post(new UserInfoEvent(userInfo));
                                finish();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {

                            }

                        });
                break;
        }

    }
}
