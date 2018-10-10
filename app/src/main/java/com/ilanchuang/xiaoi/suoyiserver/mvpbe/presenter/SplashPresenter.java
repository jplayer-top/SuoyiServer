package com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter;

import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.LoginBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.activity.SplashActivity;

import top.jplayer.baseprolibrary.mvp.contract.BasePresenter;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.utils.SharePreUtil;

import static com.ilanchuang.xiaoi.suoyiserver.SYSApplication.connectRongIm;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SplashPresenter extends BasePresenter<SplashActivity> {

    private final ServerModel mModel;

    public SplashPresenter(SplashActivity iView) {
        super(iView);
        mModel = new ServerModel(SYServer.class);
    }

    public void login(String account, String password) {
        mModel.requestLogin(account, password).subscribe(new NetCallBackObserver<LoginBean>() {
            @Override
            public void responseSuccess(LoginBean loginBean) {
                SharePreUtil.saveData(mIView, "login_account", account);
                SharePreUtil.saveData(mIView, "login_password", password);
                SYSApplication.uid = loginBean.uid + "";
                SYSApplication.imToken = loginBean.imtoken;
                connectRongIm(loginBean.imtoken);
                mModel.requestUserInfo().subscribe(userInfoBean -> mIView.loginGetUserInfo(userInfoBean), throwable -> {
                });
            }

            @Override
            public void responseFail(LoginBean loginBean) {

            }
        });
    }
}
