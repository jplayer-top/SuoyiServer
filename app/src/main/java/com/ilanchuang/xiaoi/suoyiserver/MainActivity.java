package com.ilanchuang.xiaoi.suoyiserver;

import android.os.Bundle;
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
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.event.EditSearchEvent;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.event.SaveLogEvent;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.MainPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.activity.LoginActivity;
import com.ilanchuang.xiaoi.suoyiserver.ui.activity.SearchActivity;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.LinkListFragment;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayInFragment;
import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayOutFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.rong.imkit.RongIM;
import top.jplayer.baseprolibrary.glide.GlideUtils;
import top.jplayer.baseprolibrary.net.retrofit.IoMainSchedule;
import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.ui.adapter.BaseViewPagerFragmentAdapter;
import top.jplayer.baseprolibrary.utils.ActivityUtils;
import top.jplayer.baseprolibrary.utils.KeyboardUtils;
import top.jplayer.baseprolibrary.utils.LogUtil;
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
    @BindView(R.id.tvIsOnLine)
    TextView mTvIsOnLine;
    @BindView(R.id.tvIsOtherLine)
    TextView tvIsOtherLine;
    @BindView(R.id.ivIsOnLine)
    ImageView mIvIsOnLine;
    @BindView(R.id.llIsOnLine)
    LinearLayout mLlIsOnLine;
    @BindView(R.id.llOnline)
    LinearLayout mLlOnline;
    @BindView(R.id.llOutline)
    LinearLayout mLlOutline;
    @BindView(R.id.llShowOnline)
    LinearLayout mLlShowOnline;
    private Unbinder mBind;
    private MainPresenter mPresenter;

    @Override
    protected int initRootLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initRootData(View view) {
        super.initRootData(view);
        mBind = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        RongIM.setConnectionStatusListener(connectionStatus -> {
            Observable.just(connectionStatus).compose(new IoMainSchedule<>()).subscribe(connectionStatus1 -> {
                if (connectionStatus.getValue() == 3) {
                    tvIsOtherLine.setVisibility(View.VISIBLE);
                    mTvIsOnLine.setText("离线");
                    RongIM.getInstance().disconnect();
                } else {
                    tvIsOtherLine.setVisibility(View.GONE);
                }
            });
            LogUtil.str("当前状态：" + connectionStatus);
        });
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mPresenter = new MainPresenter(this);
        mPresenter.requestTypeNum(false);
        mIvToolLeft.setImageResource(R.drawable.main_user);
        mIvToolLeft.setOnClickListener(v -> mDrawer.openDrawer(Gravity.START));
        mEditSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.init().hideKeyboard(this, mEditSearch);
                mEditSearch.clearFocus();
                EventBus.getDefault().post(new EditSearchEvent(mTabLayout.getSelectedTabPosition(), mEditSearch
                        .getText().toString()));
            }
            return false;
        });
        mLlInFind.setOnClickListener(v -> startToSearch("in"));
        mLlOutFind.setOnClickListener(v -> startToSearch("out"));
        mTvUserName.setText(SYSApplication.name);
        mTvUserType.setText(SYSApplication.type);
        Glide.with(this)
                .load(SYSApplication.avatar)
                .apply(GlideUtils.init().options(R.mipmap.ic_launcher))
                .into(mIvUserAvatar);

        mLlLogout.setOnClickListener(v -> {
            mPresenter.requestLogout();
        });
        mLlIsOnLine.setOnClickListener(v -> {
            mLlShowOnline.setVisibility(mLlShowOnline.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        mLlOnline.setOnClickListener(v -> {
            SYSApplication.connectRongIm(SYSApplication.imToken);
            mLlShowOnline.setVisibility(View.GONE);
            mTvIsOnLine.setText("在线");
        });
        mLlOutline.setOnClickListener(v -> {
            RongIM.getInstance().disconnect();
            mLlShowOnline.setVisibility(View.GONE);
            mTvIsOnLine.setText("离线");
        });
        String version = "版本号：" + BuildConfig.VERSION_NAME;
        mTvVersion.setText(version);
        mTvIsOnLine.setText("离线");
    }

    private void startToSearch(String in) {
        Bundle bundle = new Bundle();
        bundle.putString("type", in);
        ActivityUtils.init().start(this, SearchActivity.class, "", bundle);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mToolbar).init();
    }


    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Subscribe
    public void onEvent(SaveLogEvent event) {
        mPresenter.requestTypeNum(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        RongIM.getInstance().disconnect();
        EventBus.getDefault().unregister(this);
    }

    public void responseTypeNum(TypeNumBean bean, boolean isFlush) {
        if (isFlush) {
            TabLayout.Tab tab = mTabLayout.getTabAt(0);
            if (tab != null) {
                tab.setText("今日呼入(" + bean.in + ")");
            }
            TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
            if (tab1 != null) {
                tab1.setText("今日呼出(" + bean.out + ")");
            }
            TabLayout.Tab tab2 = mTabLayout.getTabAt(2);
            if (tab2 != null) {
                tab2.setText("用户列表(" + bean.linkman + ")");
            }
        } else {
            ArrayMap<String, Fragment> map = new ArrayMap<>();
            map.put("今日呼入(" + bean.in + ")", new TodayInFragment());
            map.put("今日呼出(" + bean.out + ")", new TodayOutFragment());
            map.put("用户列表(" + bean.linkman + ")", new LinkListFragment());
            mViewPager.setAdapter(new BaseViewPagerFragmentAdapter<>(getSupportFragmentManager(), map));
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    public void logout() {
        ActivityUtils.init().start(this, LoginActivity.class);
        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> finish());
    }
}
