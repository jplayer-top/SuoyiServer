package com.ilanchuang.xiaoi.suoyiserver.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ilanchuang.xiaoi.suoyiserver.MainActivity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserInfoBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.LoginPresenter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.utils.ActivityUtils;
import top.jplayer.baseprolibrary.utils.StringUtils;
import top.jplayer.baseprolibrary.utils.ToastUtils;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class LoginActivity extends SuperBaseActivity implements TextWatcher {

    @BindView(R.id.edName)
    EditText mEdName;
    @BindView(R.id.edPassword)
    EditText mEdPassword;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    private Unbinder mBind;
    private LoginPresenter mPresenter;

    @Override
    protected int initRootLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initRootData(View view) {
        super.initRootData(view);
        mBind = ButterKnife.bind(this);
        mEdName.addTextChangedListener(this);
        mPresenter = new LoginPresenter(this);
        mEdPassword.addTextChangedListener(this);
        mBtnLogin.setOnClickListener(v -> {
            if (StringUtils.init().isEmpty(mEdName)) {
                ToastUtils.init().showQuickToast(this, "请输入账号");
                return;
            }
            if (StringUtils.init().isEmpty(mEdPassword)) {
                ToastUtils.init().showQuickToast(this, "请输入密码");
                return;
            }
            mPresenter.login(mEdName.getText().toString(), mEdPassword.getText().toString());
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!StringUtils.init().isEmpty(mEdPassword) && !StringUtils.init().isEmpty(mEdName)) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }


    public void loginGetUserInfo(UserInfoBean userInfoBean) {
        SYSApplication.name = userInfoBean.name;
        SYSApplication.type = String.format(Locale.CHINA, "账号类型：%s", userInfoBean.type);
        SYSApplication.avatar = userInfoBean.avator;
        ActivityUtils.init().start(this, MainActivity.class);
        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> finish());
    }
}
