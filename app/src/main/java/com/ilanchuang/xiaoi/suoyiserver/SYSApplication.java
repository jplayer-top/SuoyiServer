package com.ilanchuang.xiaoi.suoyiserver;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.push.RongPushClient;
import top.jplayer.baseprolibrary.BaseInitApplication;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;
import static top.jplayer.baseprolibrary.BaseInitApplication.getContext;

/**
 * Created by Obl on 2018/9/20.
 * com.ilanchuang.xiaoi.suoyiserver
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SYSApplication extends MultiDexApplication {
    public static String imToken = "";
    public static String uid = "";
    public static String avatar = "";
    public static String name = "";
    public static String type = "";

    @Override
    public void onCreate() {
        super.onCreate();
        BaseInitApplication.with(this)
                // .addUrl(KEY,VALUE)//外域api
                .retrofit()//网络请求
                .swipeBack()//侧滑返回
                .zxing()//二维码
                .skin()//主题切换
                .fixFileProvide();//代码修复7.0文件 provide 问题
        rong();
    }

    public void rong() {

        Application application = this;
        if (application.getApplicationInfo().packageName.equals(getCurProcessName(application.getApplicationContext()))) {
            try {
                RongPushClient.registerHWPush(application);
                RongPushClient.registerMiPush(application, "2882303761517571827", "5841757175827");
            } catch (Exception e) {
                e.printStackTrace();
            }
            RongIM.init(application);
            RongIM.setOnReceiveMessageListener((message, i) -> false);
        }
    }

    /**
     * 连接融云
     *
     * @param imtoken
     */
    public static void connectRongIm(final String imtoken) {
        if (getContext().getApplicationInfo().packageName.equals(getCurProcessName(getContext()
                .getApplicationContext()))) {
            RongIM.connect(imtoken, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    //Token 过期
//                    reGetToken();
                }

                @Override
                public void onSuccess(String userid) {
                    RongCallClient.getInstance().setVideoProfile(RongCallCommon.CallVideoProfile.VIDEO_PROFILE_240P);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                }
            });
        }
    }
}
