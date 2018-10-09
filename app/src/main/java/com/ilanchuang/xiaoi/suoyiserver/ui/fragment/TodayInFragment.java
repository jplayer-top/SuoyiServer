package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.CommonPresenter;
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

public class TodayInFragment extends SuperBaseFragment {

    private TodayInAdapter mAdapter;
    List<MultiItemEntity> mEntityList;
    private CommonPresenter mPresenter;

    @Override
    public int initLayout() {
        return R.layout.layout_refresh_white_nofoot;
    }

    @Override
    protected void initData(View rootView) {
        initRefreshStatusView(rootView);
        mAdapter = new TodayInAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new CommonPresenter(this);
        showLoading();
        mPresenter.requestInList("1", null, null);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ivHeaderEdit) {

            }
            return false;
        });
    }

    @Override
    public void refreshStart() {
        mPresenter.requestInList("1", null, null);
    }

    public void responseInList(InListBean bean) {
        responseSuccess();
        generateData(bean.list);
        mAdapter.setNewData(mEntityList);
    }

    private void generateData(List<InListBean.ListBean> listBeans) {
        if (mEntityList != null) {
            mEntityList.clear();
        } else {
            mEntityList = new ArrayList<>();
        }
        Observable.fromIterable(listBeans).subscribe(listBean -> {
            mEntityList.add(listBean);
            Observable.fromIterable(listBean.notes).subscribe(listBean::addSubItem);
            listBean.removeSubItem(0);
        });
    }
}
