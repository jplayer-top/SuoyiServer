package com.ilanchuang.xiaoi.suoyiserver;

import android.support.multidex.MultiDexApplication;

import top.jplayer.baseprolibrary.BaseInitApplication;

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
    }
}
