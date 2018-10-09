package com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter;

import com.ilanchuang.xiaoi.suoyiserver.MainActivity;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TypeNumBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;

import top.jplayer.baseprolibrary.mvp.contract.BasePresenter;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class MainPresenter extends BasePresenter<MainActivity> {

    private final ServerModel mModel;

    public MainPresenter(MainActivity iView) {
        super(iView);
        mModel = new ServerModel(SYServer.class);
    }

    public void requestTypeNum() {
        mModel.requestTypeNum().subscribe(new NetCallBackObserver<TypeNumBean>() {
            @Override
            public void responseSuccess(TypeNumBean typeNumBean) {
                mIView.responseTypeNum(typeNumBean);
            }

            @Override
            public void responseFail(TypeNumBean typeNumBean) {

            }
        });
    }



}
