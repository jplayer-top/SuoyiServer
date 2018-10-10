package com.ilanchuang.xiaoi.suoyiserver;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TypeNumBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.MainPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.activity.LoginActivity;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.LinkListFragment;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayInFragment;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayOutFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import top.jplayer.baseprolibrary.glide.GlideUtils;
import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.ui.adapter.BaseViewPagerFragmentAdapter;
import top.jplayer.baseprolibrary.utils.ActivityUtils;
import top.jplayer.baseprolibrary.utils.KeyboardUtils;
import top.jplayer.baseprolibrary.utils.ToastUtils;
import top.jplayer.baseprolibrary.widgets.polygon.PolygonImageView;

public class MainActivity extends SuperBaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tvToolTitle)
    TextView mTvToolTitle;
    @BindView(R.id.editSearch)
    EditText mEditSearch;
    @BindView(R.id.ivToolLeft)
    ImageView mIvToolLeft;
    @BindView(R.id.ivToolRightLeft)
    ImageView mIvToolRightLeft;
    @BindView(R.id.ivToolRight)
    ImageView mIvToolRight;
    @BindView(R.id.tvToolRight)
    TextView mTvToolRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvVersion)
    TextView mTvVersion;
    @BindView(R.id.drawer)
    DrawerLayout mDrawer;
    @BindView(R.id.ivUserAvatar)
    PolygonImageView mIvUserAvatar;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvUserType)
    TextView mTvUserType;
    @BindView(R.id.llInFind)
    LinearLayout mLlInFind;
    @BindView(R.id.llOutFind)
    LinearLayout mLlOutFind;
    @BindView(R.id.llLogout)
    LinearLayout mLlLogout;
    private Unbinder mBind;
    private MainPresenter mPresenter;

    @Override
    public void initRootData(View view) {
        super.initRootData(view);
        mBind = ButterKnife.bind(this, view);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mPresenter = new MainPresenter(this);
        mPresenter.requestTypeNum();
        mIvToolLeft.setImageResource(R.drawable.main_user);
        mIvToolLeft.setOnClickListener(v -> mDrawer.openDrawer(Gravity.START));
        mEditSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ToastUtils.init().showQuickToast("快快");
                KeyboardUtils.init().hideKeyboard(this, mEditSearch);
                mEditSearch.clearFocus();
            }
            return false;
        });

        mTvUserName.setText(SYSApplication.name);
        mTvUserType.setText(SYSApplication.type);
        Glide.with(this)
                .load(SYSApplication.avatar)
                .apply(GlideUtils.init().options(R.mipmap.ic_launcher))
                .into(mIvUserAvatar);

        mLlLogout.setOnClickListener(v -> {
            mPresenter.requestLogout();
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mToolbar).init();
    }


    @Override
    protected int initRootLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    public void responseTypeNum(TypeNumBean bean) {
        ArrayMap<String, Fragment> map = new ArrayMap<>();
        map.put("今日呼入(" + bean.in + ")", new TodayInFragment());
        map.put("今日呼出(" + bean.out + ")", new TodayOutFragment());
        map.put("用户列表(" + bean.linkman + ")", new LinkListFragment());
        mViewPager.setAdapter(new BaseViewPagerFragmentAdapter<>(getSupportFragmentManager(), map));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void logout() {
        ActivityUtils.init().start(this, LoginActivity.class);
        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> finish());
    }
}
