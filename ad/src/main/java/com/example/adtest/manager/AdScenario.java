package com.example.adtest.manager;

/**
 * author : liangning
 * date : 2019-11-16  13:41
 */
public class AdScenario {
//
//    gold_bubble_native,//金币泡泡信息流
//    gold_bubble_double_video,//金币泡泡翻倍激励视频
//    step_togold_native,//步数换金币信息流
//    sign_native,//签到信息流
//    sign_double_video,//签到翻倍激励视频
//    task_video,//观看激励视频任务 激励视频
//    task_video_native,//观看激励视频 信息流
//    turn_table_video,//大转盘激励视频
//    turn_table_gold_native,//大转盘金币奖励信息流
//    turn_table_double_gold_native,//大转盘翻倍后信息流
//    turn_table_gold_double_video,//大转盘金币奖励翻倍 激励视频
//    turn_table_pkg_gold_natice,//大转盘底部宝箱 信息流
//    bind_wx_native,//绑定微信信息流
//    bind_phone_native,//绑定手机信息流
//    me_page_native,//我的页面信息流
//    setting_page_native,//设置页面信息流
//    default_native,//默认信息流
//    withdrawal_page_video,//提现页激励视频
//    default_video,//默认激励视频

    public static String getSelfPosId(ScenarioEnum scenario) {
        if (scenario == null) {
            return "";
        }
        String posId = "";
        switch (scenario) {
            case gold_bubble_native:
                posId = "1024001";
                break;
            case gold_bubble_double_video:
                posId = "1024002";
                break;
            case step_togold_native:
                posId = "1024003";
                break;
            case sign_native:
                posId = "1024004";
                break;
            case sign_double_video:
                posId = "1024005";
                break;
            case task_video:
                posId = "1024006";
                break;
            case task_video_native:
                posId = "1024007";
                break;
            case turn_table_video:
                posId = "1024008";
                break;
            case turn_table_gold_native:
                posId = "1024009";
                break;
            case turn_table_double_gold_native:
                posId = "1024010";
                break;
            case turn_table_gold_double_video:
                posId = "1024011";
                break;
            case turn_table_pkg_gold_natice:
                posId = "1024012";
                break;
            case bind_wx_native:
                posId = "1024013";
                break;
            case bind_phone_native:
                posId = "1024014";
                break;
            case turn_table_first_task_native:
                posId = "1024015";
                break;
            case me_page_native:
                posId = "1024016";
                break;
            case setting_page_native:
                posId = "1024017";
                break;
            case default_native:
                posId = "1024018";
                break;
            case withdrawal_page_video:
                posId = "1024019";
                break;
            case default_video:
                posId = "1024020";
                break;
            case gold_bubble_double_native:
                posId = "1024021";
                break;
            case sign_double_native:
                posId = "1024022";
                break;
            case step_ceiling_native:
                posId = "1024023";
                break;
            case userinfo_native:
                posId = "1024024";
                break;
            case splash_ad:
                posId = "1024025";
                break;
            case turn_table_jibei_video:
                posId = "1024026";
                break;
            case fly_knife_video:
                posId = "1024027";
                break;
            case fly_knife_native:
                posId = "1024028";
                break;
            case step_history_banner:
                posId = "1024029";
                break;
            case step_shangxian_video:
                posId = "1024030";
                break;
            case pleasantly_double_video:
                posId = "1024031";
                break;
            case pleasantly_native:
                posId = "1024032";
                break;
            case bubble_video_native:
                posId = "1024033";
                break;
            case stage_of_reward_video:
                posId = "1024034";
                break;
            case stage_of_reward_native:
                posId = "1024035";
                break;
            case task_reward_native:
                posId = "1024036";
                break;
            case step_challenge_video:
                posId = "1024037";
                break;
            case dongdong_bubble_native:
                posId = "1024038";
                break;
            case dongdong_bubble_video:
                posId = "1024039";
                break;
            case drink_water_video:
                posId = "1024040";
                break;
            case drink_water_native:
                posId = "1024041";
                break;
            case game_plaque:
                posId = "1024042";
                break;
            case default_plaque:
                posId = "1024043";
                break;
            case reward_video:
                posId = "1024044";
                break;
            case low_plaque:
                posId = "1024045";
                break;
            case move_water_plaque:
                posId = "1024046";
                break;
            case scratch_cards_plaque:
                posId = "1024048";
                break;
            case scratch_cards_native:
                posId = "1024049";
                break;
            case scratch_cards_video:
                posId = "1024050";
                break;
            case click_egg_native:
                posId = "1024052";
                break;
            case click_egg_video:
                posId = "1024053";
                break;
            case gambling_video:
                posId = "1024056";
                break;
            case gambling_native:
                posId = "1024057";
                break;
            case steprace_native:
                posId = "1024028";
                break;
        }
        return posId;
    }

}
