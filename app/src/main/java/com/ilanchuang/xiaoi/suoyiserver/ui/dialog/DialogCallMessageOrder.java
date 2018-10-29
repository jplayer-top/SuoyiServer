package com.ilanchuang.xiaoi.suoyiserver.ui.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallMessageBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.CallMessageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.utils.ScreenUtils;
import top.jplayer.baseprolibrary.widgets.dialog.BaseCustomDialog;

/**
 * Created by Obl on 2018/10/11.
 * com.ilanchuang.xiaoi.suoyiserver.ui.dialog
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class DialogCallMessageOrder extends BaseCustomDialog {
    public String fid;
    public String fname;
    @BindView(R.id.ivCancel)
    ImageView mIvCancel;
    @BindView(R.id.tvFName)
    TextView mTvFName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Unbinder mBind;
    private CallMessageAdapter mAdapter;

    public DialogCallMessageOrder(Context context) {
        super(context);
    }

    @Override
    protected void initView(View view) {
        mBind = ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CallMessageAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        View inflate = View.inflate(getContext(), R.layout.layout_empty_view_dialog, null);
        mAdapter.setEmptyView(inflate);
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_call_message_order;
    }

    public DialogCallMessageOrder setFid(String fid, String fname) {
        this.fid = fid;
        this.fname = fname;
        new ServerModel(SYServer.class).requestCallMessage(fid).subscribe(new NetCallBackObserver<CallMessageBean>() {
            @Override
            public void responseSuccess(CallMessageBean bean) {
                bindDate(bean);
            }

            @Override
            public void responseFail(CallMessageBean bean) {

            }
        });
        return this;
    }

    private void bindDate(CallMessageBean bean) {
        mIvCancel.setOnClickListener(v -> cancel());
        mTvFName.setText(fname);
        mAdapter.setNewData(bean.list);
    }

    @Override
    public int setWidth(int i) {
        return super.setWidth(10);
    }

    @Override
    public int setHeight() {
        return ScreenUtils.getScreenHeight() - ScreenUtils.dp2px(140);
    }

    @Override
    public int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int setAnim() {
        return top.jplayer.baseprolibrary.R.style.AnimBottom;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBind.unbind();
    }
}
