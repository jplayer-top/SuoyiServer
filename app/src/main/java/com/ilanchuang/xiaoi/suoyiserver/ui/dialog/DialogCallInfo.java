package com.ilanchuang.xiaoi.suoyiserver.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.RecordUserInfoBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.widgets.dialog.BaseCustomDialog;

/**
 * Created by Obl on 2018/10/11.
 * com.ilanchuang.xiaoi.suoyiserver.ui.dialog
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class DialogCallInfo extends BaseCustomDialog {
    public String fid;
    public String fname;
    @BindView(R.id.ivCancel)
    ImageView mIvCancel;
    @BindView(R.id.tvFName)
    TextView mTvFName;
    @BindView(R.id.tvOne)
    TextView mTvOne;
    @BindView(R.id.ivOne)
    ImageView mIvOne;
    @BindView(R.id.tvFPhoneOne)
    TextView mTvFPhoneOne;
    @BindView(R.id.tvFBirthdayOne)
    TextView mTvFBirthdayOne;
    @BindView(R.id.tvFIDOne)
    TextView mTvFIDOne;
    @BindView(R.id.tvFLocalOne)
    TextView mTvFLocalOne;
    @BindView(R.id.tvFAddrOne)
    TextView mTvFAddrOne;
    @BindView(R.id.llOne)
    LinearLayout mLlOne;
    @BindView(R.id.tvTwo)
    TextView mTvTwo;
    @BindView(R.id.ivTwo)
    ImageView mIvTwo;
    @BindView(R.id.tvFPhoneTwo)
    TextView mTvFPhoneTwo;
    @BindView(R.id.tvFBirthdayTwo)
    TextView mTvFBirthdayTwo;
    @BindView(R.id.tvFIDTwo)
    TextView mTvFIDTwo;
    @BindView(R.id.tvFLocalTwo)
    TextView mTvFLocalTwo;
    @BindView(R.id.tvFAddrTwo)
    TextView mTvFAddrTwo;
    @BindView(R.id.llTwo)
    LinearLayout mLlTwo;
    private Unbinder mBind;

    public DialogCallInfo(Context context) {
        super(context);
    }

    @Override
    protected void initView(View view) {
        mBind = ButterKnife.bind(this, view);
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_call_info;
    }

    public DialogCallInfo setFid(String fid, String fname) {
        this.fid = fid;
        this.fname = fname;
        new ServerModel(SYServer.class).requestRecordUserInfo(fid).subscribe(new NetCallBackObserver<RecordUserInfoBean>() {
            @Override
            public void responseSuccess(RecordUserInfoBean recordUserInfoBean) {
                bindDate(recordUserInfoBean);
            }

            @Override
            public void responseFail(RecordUserInfoBean recordUserInfoBean) {

            }
        });
        return this;
    }

    private void bindDate(RecordUserInfoBean bean) {
        mIvCancel.setOnClickListener(v -> cancel());
        mTvFName.setText(fname);
        if (bean.list != null && bean.list.size() > 0) {
            RecordUserInfoBean.ListBean bean0 = bean.list.get(0);
            mTvOne.setText(String.format(Locale.CHINA, "姓名：%s", bean0.rname));
            mIvOne.setImageResource("男".equals(bean0.sex) ? R.drawable.call_man : R.drawable.call_woman);
            mTvFPhoneOne.setText(String.format(Locale.CHINA, "电话：%s", bean0.phone));
            mTvFBirthdayOne.setText(String.format(Locale.CHINA, "生日：%s", bean0.birthday));
            mTvFIDOne.setText(String.format(Locale.CHINA, "身份证号：%s", bean0.idcard));
            mTvFLocalOne.setText(String.format(Locale.CHINA, "所在城市：%s", bean0.city));
            mTvFAddrOne.setText(String.format(Locale.CHINA, "详细地址：%s", bean0.addr));
            if (bean.list.size() > 1) {
                RecordUserInfoBean.ListBean bean1 = bean.list.get(1);
                mTvTwo.setText(String.format(Locale.CHINA, "姓名：%s", bean1.rname));
                mIvTwo.setImageResource("男".equals(bean1.sex) ? R.drawable.call_man : R.drawable.call_woman);
                mTvFPhoneTwo.setText(String.format(Locale.CHINA, "电话：%s", bean1.phone));
                mTvFBirthdayTwo.setText(String.format(Locale.CHINA, "生日：%s", bean1.birthday));
                mTvFIDTwo.setText(String.format(Locale.CHINA, "身份证号：%s", bean1.idcard));
                mTvFLocalTwo.setText(String.format(Locale.CHINA, "所在城市：%s", bean1.city));
                mTvFAddrTwo.setText(String.format(Locale.CHINA, "详细地址：%s", bean1.addr));
            }
        }
    }

    @Override
    public int setWidth(int i) {
        return super.setWidth(10);
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
