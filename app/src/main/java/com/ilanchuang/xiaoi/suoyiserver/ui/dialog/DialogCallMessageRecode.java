package com.ilanchuang.xiaoi.suoyiserver.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.HealthReportFragment;

import top.jplayer.baseprolibrary.utils.ScreenUtils;
import top.jplayer.baseprolibrary.widgets.dialog.BaseCustomDialogFragment;

/**
 * Created by Obl on 2018/10/11.
 * com.ilanchuang.xiaoi.suoyiserver.ui.dialog
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class DialogCallMessageRecode extends BaseCustomDialogFragment {
    public String fid;
    public String fname;
    ImageView mIvCancel;
    TextView mTvFName;
    HealthReportFragment mReportFragment;


    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            fid = bundle.getString("fid");
            fname = bundle.getString("fname");
        }
        bindDate();

    }


    @Override
    public int initLayout() {
        return R.layout.dialog_call_message_recode;
    }

    private void bindDate() {
        mIvCancel = mContentView.findViewById(R.id.ivCancel);
        mTvFName = mContentView.findViewById(R.id.tvFName);
        mIvCancel.setOnClickListener(v -> dismiss());
        mTvFName.setText(fname);
        Bundle args = new Bundle();
        args.putString("fid", fid);
        mReportFragment = new HealthReportFragment();
        mReportFragment.setArguments(args);
        getChildFragmentManager().beginTransaction().replace(R.id.flAddRecode, mReportFragment).commit();
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

}
