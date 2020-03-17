package com.mobi.step.app;

import com.mobi.step.main.PleasantlyBean;
import com.mobi.step.main.bean.ExitDialogBean;
import com.mobi.step.wxapi.bean.UserEntity;

public class Const {

    public static String deviceID = "";
    
    /**
     * DEBUG :是用来控制，第三方插件等是否debug状态
     */
    public static final boolean DEBUG = false;//true 测试服地址 false 正式服地址


    public static final String DEBUGUEL="http://dev.findwxapp.com";//测试服配置表域名
    public static final String RELEASEURL="https://jsonconfig.findwxapp.com";//正式服配置表域名



    public static String getBaseUrl() {
        return Const.DEBUG ? UserEntity.getInstance().getConfigEntity().getDev_domain() : UserEntity.getInstance().getConfigEntity().getPrd_domain();
    }

    public static int SYSTEM_ID = 1;
    public static int SIGNIG_ACTIVITY_ID = 1;//签到活动ID
    public static int INTEGRAL_ACTIVITY_ID = 1;


//    活动列表->活动ID记录
    public static final int ACTIVITY_LOOK_AD_ID = 5;//观看视频广告ID
    public static final int ACTIVITY_ROTARY_AD = 1;//跳转到大转盘ID
    public static final int ACTIVITY_BIND_PHONE_ID = 3;//绑定手机号活动ID
    public static final int ACTIVITY_BING_WX_ID = 4;//绑定微信活动ID
    public static final int ACTIVITY_PLEASANTLY_id = 14;//
    public static final int ACTIVITY_FIRSTWITHDRAWAL_id = 15;//提现活动活动id


    //热云appkey  5dc0df783fc19536ef0005e6
    public static final String RYAPPKEKY_DEBUG="5f312fc6ed72c9b14efa073ba599e627";
    public static final String RYAPPKEKY="cf4263e992a8387b685bb04ebe92fa1c";
    public static String getRYAPPkey() {
        return Const.DEBUG ? RYAPPKEKY_DEBUG : RYAPPKEKY;
    }

    public static PleasantlyBean pBean = null;//惊喜奖励显示在哪个tab
    public static ExitDialogBean exitBean = null;//退出app时弹框信息

}
