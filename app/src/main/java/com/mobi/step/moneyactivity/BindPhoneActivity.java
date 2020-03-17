package com.mobi.step.moneyactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.step.R;
import com.mobi.step.app.Const;
import com.mobi.step.base.BaseAppCompatActivity;
import com.mobi.step.eventbean.UserInfoEvent;
import com.mobi.step.moneyactivity.bean.BindPhoneBean;
import com.mobi.step.moneyactivity.bean.SendSMSVerificationCode;
import com.mobi.step.net.BaseObserver;
import com.mobi.step.net.BaseResponse;
import com.mobi.step.net.CommonSchedulers;
import com.mobi.step.net.OkHttpClientManager;
import com.mobi.step.statistical.umeng.ButtonStatistical;
import com.mobi.step.utils.ToastUtils;
import com.mobi.step.utils.UiUtils;
import com.mobi.step.wxapi.bean.UserEntity;
import com.mobi.step.wxapi.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class BindPhoneActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private EditText phone_num;
    private TextView bt_code;
    private EditText verification_code;
    private TextView confirm_btn;

    private LinearLayout ll_back;
    private CodeCountDown codeCountDown;
    private boolean isSend=false;//是否发送验证码

    public static void IntoBindPhone(Activity activity){
        Intent intent = new Intent(activity,BindPhoneActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_binding_phonenum);
        ButtonStatistical.bindPhonePage();
        UiUtils.setTitleBar(this,null,null,getResources().getString(R.string.bind_phonenum_title),null);
        initView();


    }

    private void initView() {
        phone_num=findViewById(R.id.input_num);
        bt_code=findViewById(R.id.bt_code);
        verification_code=findViewById(R.id.input_yzm);
        confirm_btn=findViewById(R.id.confirm_btn);
        bt_code.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);
        ll_back=findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);

        verification_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4&&isSend){
                    confirm_btn.setBackgroundResource(R.mipmap.btn_click);
                }else {
                    confirm_btn.setBackgroundResource(R.mipmap.btn_click);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code:
                ButtonStatistical.sendSMSBtn();
                if(TextUtils.isEmpty(phone_num.getText().toString().trim())){
                    ToastUtils.showShort(R.string.empty_phone_tip);
                    return;
                }
                if(!checkPhone(phone_num.getText().toString().trim())){
                    ToastUtils.showShort(R.string.correct_phone_tip);
                    return;
                }
                if(codeCountDown!=null&&codeCountDown.isStart){
                    ToastUtils.showShort(R.string.yzm_tip2);
                    return;
                }
                JSONObject jsonObject=new JSONObject();
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .sendSMSCode(phone_num.getText().toString().trim(),"SMS_176295081","全民走路",jsonObject.toString())
                        .compose(CommonSchedulers.<BaseResponse<SendSMSVerificationCode>>observableIO2Main(this))
                        .subscribe(new BaseObserver<SendSMSVerificationCode>() {
                            @Override
                            public void onSuccess(SendSMSVerificationCode demo) {
                                isSend=true;
                                ToastUtils.showShort(R.string.send_sms_success);
                                startCountDown();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {

                            }
                        });
                break;
            case R.id.confirm_btn:
                ButtonStatistical.clickBindPhoneBtn();
                if(TextUtils.isEmpty(phone_num.getText().toString().trim())){
                    ToastUtils.showShort(R.string.empty_phone_tip);
                    return;
                }
                if(!checkPhone(phone_num.getText().toString().trim())){
                    ToastUtils.showShort(R.string.correct_phone_tip);
                    return;
                }
                if(TextUtils.isEmpty(verification_code.getText().toString().trim())){
                    ToastUtils.showShort(R.string.yzm_tip);
                    return;
                }
                UserEntity entity = UserEntity.getInstance();
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .bindPhoneNum(entity.getUserId(),phone_num.getText().toString().trim(),verification_code.getText().toString().trim())
                        .compose(CommonSchedulers.<BaseResponse<BindPhoneBean>>observableIO2Main(this))
                        .subscribe(new BaseObserver<BindPhoneBean>() {
                            @Override
                            public void onSuccess(BindPhoneBean demo) {
                                isSend=false;
                                ToastUtils.showShort(R.string.bind_success);
                                getUserInfo();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {
                                ToastUtils.showShort(errorMsg);
                            }

                        });
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    private void getUserInfo() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getUserInfo(UserEntity.getInstance().getToken(),UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<UserInfo>>observableIO2Main(this))
                .subscribe(new BaseObserver<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo demo) {
                        if(demo!=null){
                            UserEntity.getInstance().setUserInfo(demo);
                            EventBus.getDefault().post(new UserInfoEvent(demo));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }

                });
    }

    // 验证手机号
    public  boolean checkPhone(String s) {
        return s.matches("^((13[0-9])|(16[0-9])|(19[0-9])|(17[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
    }

    public void startCountDown(){
        if(codeCountDown==null){
            codeCountDown=new CodeCountDown(30*1000,1000);
        }
        codeCountDown.start();
        bt_code.setTextColor(getResources().getColor(R.color.gray_a7cb));

    }

    class CodeCountDown extends CountDownTimer {

        private boolean isStart=false;

        public CodeCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            isStart=true;
            bt_code.setText((millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            bt_code.setTextColor(getResources().getColor(R.color.green_5d31));
            bt_code.setText(getResources().getString(R.string.regain_get));
            isStart=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(codeCountDown!=null){
            codeCountDown.cancel();
            codeCountDown=null;
        }
    }
}
