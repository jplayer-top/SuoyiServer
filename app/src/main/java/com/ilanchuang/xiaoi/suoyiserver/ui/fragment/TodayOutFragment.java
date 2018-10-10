package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.TodayOutPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.TodayInAdapter;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogNote;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.ui.fragment.SuperBaseFragment;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.fragment
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayOutFragment extends SuperBaseFragment {

    private TodayInAdapter mAdapter;
    List<MultiItemEntity> mEntityList;
    private TodayOutPresenter mPresenter;
    private DialogNote mDialogNote;

    @Override
    public int initLayout() {
        return R.layout.layout_refresh_white_nofoot;
    }

    @Override
    protected void initData(View rootView) {
        initRefreshStatusView(rootView);
        mAdapter = new TodayInAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new TodayOutPresenter(this);
        showLoading();
        mPresenter.requestOutList("1", null, null);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ivHeaderEdit) {
                InListBean.ListBean listBean = (InListBean.ListBean) mAdapter.getData().get(position);
                mDialogNote = new DialogNote(this.getContext()).setFName(listBean.fname).setFAvatar(listBean.favatar);
                mDialogNote.show(R.id.btnSave, view1 -> {
                    EditText editText = (EditText) view1;
                    mDialogNote.dismiss();
                    mPresenter.requestNote(listBean.fid + "", editText.getText().toString());
                });
            }
            return false;
        });
    }

    @Override
    public void refreshStart() {
        mPresenter.requestOutList("1", null, null);
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

    public void responseNote(BaseBean bean) {
        mPresenter.requestOutList("1", null, null);
    }

    public void responseOutList(InListBean bean) {
        responseSuccess();
        generateData(bean.list);
        mAdapter.setNewData(mEntityList);
    }
}
