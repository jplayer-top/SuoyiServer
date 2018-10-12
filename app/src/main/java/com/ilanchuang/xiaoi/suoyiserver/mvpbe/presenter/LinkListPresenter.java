package com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.LinkListFragment;

import top.jplayer.baseprolibrary.mvp.contract.BasePresenter;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.net.tip.LoadingImplTip;
import top.jplayer.baseprolibrary.net.tip.PostImplTip;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class LinkListPresenter extends BasePresenter<LinkListFragment> {

    private final ServerModel mModel;

    public LinkListPresenter(LinkListFragment iView) {
        super(iView);
        mModel = new ServerModel(SYServer.class);
    }

    public void requestLinkList(String pageNum, String fname) {
        mModel.requestLinkList(pageNum, fname).subscribe(new NetCallBackObserver<InListBean>() {
            @Override
            public void responseSuccess(InListBean inListBean) {
                mIView.responseLinkList(inListBean);
            }

            @Override
            public void responseFail(InListBean inListBean) {

            }
        });
    }
    public void requestOut(String fid) {
        mModel.requestOut(fid).subscribe(new NetCallBackObserver<CallOutBean>(new LoadingImplTip(mIView.mActivity)) {
            @Override
            public void responseSuccess(CallOutBean bean) {
                mIView.responseOut(bean);
            }

            @Override
            public void responseFail(CallOutBean bean) {

            }
        });
    }

    public void requestNote(String fid, String note) {
        mModel.requestNote(fid, note).subscribe(new NetCallBackObserver<BaseBean>(new PostImplTip(mIView.mActivity)) {
            @Override
            public void responseSuccess(BaseBean bean) {
                mIView.responseNote(bean);
            }

            @Override
            public void responseFail(BaseBean inListBean) {

            }
        });
    }
}
