package com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayInFragment;

import top.jplayer.baseprolibrary.mvp.contract.BasePresenter;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class CommonPresenter extends BasePresenter<TodayInFragment> {

    private final ServerModel mModel;

    public CommonPresenter(TodayInFragment iView) {
        super(iView);
        mModel = new ServerModel(SYServer.class);
    }

    public void requestInList(String flag, String fname, String date) {
        mModel.requestInList(flag, fname, date).subscribe(new NetCallBackObserver<InListBean>() {
            @Override
            public void responseSuccess(InListBean inListBean) {
                mIView.responseInList(inListBean);
            }

            @Override
            public void responseFail(InListBean inListBean) {

            }
        });
    }
}
