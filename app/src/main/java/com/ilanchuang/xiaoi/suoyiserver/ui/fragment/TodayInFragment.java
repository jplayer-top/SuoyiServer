package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayInBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview.ContructIview;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.TodayInAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import top.jplayer.baseprolibrary.ui.fragment.SuperBaseFragment;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.fragment
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayInFragment extends SuperBaseFragment implements ContructIview.MainIview {

    private TodayInAdapter mAdapter;

    @Override
    public int initLayout() {
        return R.layout.layout_refresh_white_nofoot;
    }

    @Override
    protected void initData(View rootView) {
        initRefreshStatusView(rootView);
        mAdapter = new TodayInAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void todayIn(TodayInBean bean) {
        mAdapter.setNewData(null);
    }

    @Override
    public void todayOut(TodayOutBean bean) {

    }

    @Override
    public void userList(UserListBean bean) {

    }

    private ArrayList<MultiItemEntity> generateData(List<TodayInBean.RecordsBean> beans) {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        Observable.fromIterable(beans).subscribe(resultBeans -> {
            res.add(resultBeans);
        });
        return res;
    }
}
