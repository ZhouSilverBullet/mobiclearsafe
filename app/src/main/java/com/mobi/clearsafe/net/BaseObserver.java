package com.mobi.clearsafe.net;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.statistical.errorlog.ErrorLogUtil;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.wxapi.bean.LoginBean;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {


    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResponse<T> response) {
        //在这边对 基础数据 进行统一处理
        if(response.getCode().equals("200")){
            onSuccess(response.getData());
        }else{
            if(response.getCode().equals("401")){
                login();
            }else {
                onFailure(null,response.getMessage(),response.getCode());
            }
            ErrorLogUtil.upDateService(response.getCode());
        }
    }

    @Override
    public void onError(Throwable e) { //这里用来隐藏进度框，还可以提示错误消息
        onFailure(e, RxExceptionUtil.exceptionHandler(e),"");
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T demo);

    public abstract void onFailure(Throwable e, String errorMsg,String code);


    private void login() {
        UserEntity.getInstance().clearInfo();
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .login()
                .compose(CommonSchedulers.<BaseResponse<LoginBean>>observableIO2Main(MyApplication.getContext()))
                .subscribe(new BaseObserver<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean demo) {
                        if (demo != null) {
                            //保存token user_id
                            UserEntity.getInstance().setToken(demo.getToken());
                            UserEntity.getInstance().setUserId(demo.getUser_id());
                            UserEntity.getInstance().setNickname(demo.getNickname());
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getCash());
                            ButtonStatistical.loginSuccess();
//                            EventBus.getDefault().post(new RequestStep());
//                            EventBus.getDefault().post(new UserInfoEvent(null));
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });

    }
}
