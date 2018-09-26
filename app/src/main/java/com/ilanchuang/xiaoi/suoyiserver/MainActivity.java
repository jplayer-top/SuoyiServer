package com.ilanchuang.xiaoi.suoyiserver;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.ArrayMap;
import android.widget.FrameLayout;

import com.ilanchuang.xiaoi.suoyiserver.ui.fragment.TodayInFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.jplayer.baseprolibrary.ui.activity.CommonToolBarActivity;
import top.jplayer.baseprolibrary.ui.adapter.BaseViewPagerFragmentAdapter;
import top.jplayer.baseprolibrary.ui.fragment.TestFragment;
import top.jplayer.baseprolibrary.utils.ToastUtils;

public class MainActivity extends CommonToolBarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private Unbinder mBind;

    @Override
    public int initAddLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initAddView(FrameLayout rootView) {
        super.initAddView(rootView);
        mBind = ButterKnife.bind(this, rootView);
        mToolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ArrayMap<String, Fragment> map = new ArrayMap<>();
        map.put("今日呼入(10)", new TodayInFragment());
        map.put("今日呼出(10)", new TestFragment());
        map.put("用户列表(10)", new TestFragment());
        mViewPager.setAdapter(new BaseViewPagerFragmentAdapter<>(getSupportFragmentManager(), map));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ToastUtils.init().showQuickToast("当前位置：" + position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
