package com.mobi.step.moneyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.step.R;
import com.mobi.step.app.Const;
import com.mobi.step.base.BaseAppCompatActivity;
import com.mobi.step.moneyactivity.bean.ShareContentBean;
import com.mobi.step.net.BaseObserver;
import com.mobi.step.net.BaseResponse;
import com.mobi.step.net.CommonSchedulers;
import com.mobi.step.net.OkHttpClientManager;
import com.mobi.step.statistical.umeng.ButtonStatistical;
import com.mobi.step.utils.AppUtil;
import com.mobi.step.utils.DialogUtils;
import com.mobi.step.utils.ToastUtils;
import com.mobi.step.widget.ShareDialog;
import com.mobi.step.wxapi.bean.UserEntity;

public class ShareActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView share_btn;
    private ImageView ewm_btn;
    private TextView tv_gz;
    private LinearLayout copy;
    private TextView tv_yqm;
    private ShareContentBean mShareBean;
    private ImageView iv_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
    }

    private void initView() {
        share_btn=findViewById(R.id.btn_share);
        ewm_btn=findViewById(R.id.ewm_btn);
        tv_gz=findViewById(R.id.tv_btn);
        copy=findViewById(R.id.layout_copy);
        tv_yqm=findViewById(R.id.tv_yqm);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);


        share_btn.setOnClickListener(this);
        ewm_btn.setOnClickListener(this);
        tv_gz.setOnClickListener(this);
        copy.setOnClickListener(this);
        getShareContent();



    }

    //得到分享内容
    private void getShareContent() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getShareContent(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ShareContentBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ShareContentBean>() {
                    @Override
                    public void onSuccess(ShareContentBean demo) {
                        if(demo!=null){
                            mShareBean=demo;
                            if(!TextUtils.isEmpty(demo.getCode())){
                                tv_yqm.setText(demo.getCode());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_share:
                if(mShareBean!=null){
                    ButtonStatistical.inviteToTimeline();
                    ShareDialog shareFragment = ShareDialog.getInstance(mShareBean, new ShareDialog.ClickListener() {
                        @Override
                        public void onClick() {

                        }
                    });
                    DialogUtils.showDialog(getSupportFragmentManager(), shareFragment, ShareDialog.TAG);
                }
                break;
            case R.id.ewm_btn:
                if(mShareBean!=null){
                    ButtonStatistical.inviteToSaoma();
                    Intent intent = new Intent(this, EwmActivity.class);
                    intent.putExtra("url",mShareBean.getJump_address()+ "?code=" + mShareBean.getCode());
                    startActivity(intent);
                }
                break;
            case R.id.tv_btn:

                break;
            case R.id.layout_copy:
                if(!TextUtils.isEmpty(tv_yqm.getText())){
                    if (AppUtil.copy(this,tv_yqm.getText().toString().trim())) {
                        ToastUtils.showShort(getResources().getString(R.string.yqm_tip));
                    }
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
