package com.ilanchuang.xiaoi.suoyiserver.ui.activity;

import android.os.SystemClock;
import android.view.View;

import com.github.florent37.viewanimator.ViewAnimator;
import com.ilanchuang.xiaoi.suoyiserver.MainActivity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserInfoBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview.ContractIView;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.SplashPresenter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import top.jplayer.baseprolibrary.net.retrofit.IoMainSchedule;
import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.utils.ActivityUtils;
import top.jplayer.baseprolibrary.utils.SharePreUtil;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SplashActivity extends SuperBaseActivity implements ContractIView.UserIView {
    private long preTime;

    @Override
    protected int initRootLayout() {
        return R.layout.activity_splash;
    }


    @Override
    public void initRootData(View view) {
        super.initRootData(view);
        String login_account = (String) SharePreUtil.getData(this, "login_account", "");
        String login_password = (String) SharePreUtil.getData(this, "login_password", "");

        ViewAnimator.animate(view.findViewById(R.id.image))
                .scale(1f, 1.1f)
                .duration(1000)
                .onStart(() -> {
                    preTime = SystemClock.currentThreadTimeMillis();
                    if (!"".equals(login_account) && !"".equals(login_password)) {
                        new SplashPresenter(this).login(login_account, login_password);
                    }
                })
                .onStop(() -> {
                    if ("".equals(login_account) || "".equals(login_password)) {
                        ActivityUtils.init().start(this, LoginActivity.class);
                        finish();
                    }
                })
                .start();
    }

    public void loginGetUserInfo(UserInfoBean userInfoBean) {
        SYSApplication.name = userInfoBean.name;
        SYSApplication.type = String.format(Locale.CHINA,"账号类型：%s",userInfoBean.type);
        SYSApplication.avatar = userInfoBean.avator;
        Observable.timer(1000 - (SystemClock.currentThreadTimeMillis() - preTime), TimeUnit.MILLISECONDS).compose(new IoMainSchedule<>())
                .subscribe(aLong -> {
                    ActivityUtils.init().start(this, MainActivity.class);
                    finish();
                });
    }
}
