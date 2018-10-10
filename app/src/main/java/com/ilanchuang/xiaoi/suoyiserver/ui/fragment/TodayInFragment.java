package com.ilanchuang.xiaoi.suoyiserver.ui.fragment;

import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.TodayInPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.TodayInAdapter;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogNote;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.rong.callkit.RongCallKit;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.ui.fragment.SuperBaseFragment;
import top.jplayer.baseprolibrary.utils.ToastUtils;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.fragment
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayInFragment extends SuperBaseFragment {

    private TodayInAdapter mAdapter;
    List<MultiItemEntity> mEntityList;
    private TodayInPresenter mPresenter;
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
        mPresenter = new TodayInPresenter(this);
        showLoading();
        mPresenter.requestInList("1", null, null);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            InListBean.ListBean listBean = (InListBean.ListBean) mAdapter.getData().get(position);
            if (view.getId() == R.id.ivHeaderEdit) {
                mDialogNote = new DialogNote(this.getContext()).setFName(listBean.fname).setFAvatar(listBean.favatar);
                mDialogNote.show(R.id.btnSave, view1 -> {
                    EditText editText = (EditText) view1;
                    mDialogNote.dismiss();
                    mPresenter.requestNote(listBean.fid + "", editText.getText().toString());
                });
            } else {
                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.RECORD_AUDIO)
                        .onGranted(permissions -> {
                            RongCallKit.startSingleCall(mActivity, "d_10017", RongCallKit.CallMediaType
                                    .CALL_MEDIA_TYPE_VIDEO);
//                            mPresenter.requestLevel();
                        })
                        .onDenied(permissions -> {
                            AndPermission.hasAlwaysDeniedPermission(mActivity, permissions);
                            ToastUtils.init().showQuickToast("请前往应用设置，同意权限");
                        })
                        .start();
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

    public void responseNote(BaseBean bean) {
        mPresenter.requestInList("1", null, null);
    }
}
