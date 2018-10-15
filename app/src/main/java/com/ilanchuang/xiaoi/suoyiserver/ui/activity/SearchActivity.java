package com.ilanchuang.xiaoi.suoyiserver.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.event.SaveLogEvent;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.presenter.SearchPresenter;
import com.ilanchuang.xiaoi.suoyiserver.ui.adapter.TodayInAdapter;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogNote;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.rong.callkit.CustomRongCallKit;
import io.rong.callkit.RongCallKit;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.mvp.model.bean.PickerTimeEvent;
import top.jplayer.baseprolibrary.ui.activity.SuperBaseActivity;
import top.jplayer.baseprolibrary.utils.KeyboardUtils;
import top.jplayer.baseprolibrary.utils.PickerUtils;
import top.jplayer.baseprolibrary.utils.ToastUtils;

/**
 * Created by Obl on 2018/10/12.
 * com.ilanchuang.xiaoi.suoyiserver.ui.activity
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class SearchActivity extends SuperBaseActivity {
    @BindView(R.id.editSearch)
    EditText mEditSearch;
    @BindView(R.id.editSearchTime)
    TextView mTvTime;
    @BindView(R.id.llSearchTime)
    LinearLayout llSearchTime;
    @BindView(R.id.llIsOnLine)
    LinearLayout mLlIsOnLine;
    @BindView(R.id.tvToolRight)
    TextView mTvToolRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ivToolLeft)
    ImageView mIvToolLeft;
    @BindView(R.id.ivTimeCancel)
    ImageView ivTimeCancel;
    private Unbinder mBind;
    private String mType;
    private SearchPresenter mPresenter;
    private TodayInAdapter mAdapter;
    private DialogNote mDialogNote;
    List<MultiItemEntity> mEntityList;
    private PickerUtils mPickerUtils;

    @Override
    protected int initRootLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initRootData(View rootView) {
        super.initRootData(rootView);
        mBind = ButterKnife.bind(this, rootView);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mLlIsOnLine.setVisibility(View.GONE);
        mTvToolRight.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
        mTvToolRight.setText("搜索");
        mIvToolLeft.setImageResource(R.drawable.white_left_arrow);
        mIvToolLeft.setOnClickListener(v -> finish());
        mTvToolRight.setTextColor(Color.WHITE);
        mType = mBundle.getString("type");
        mPresenter = new SearchPresenter(this);
        showLoading();
        refreshList(searchName, searchTime);
        initRefreshStatusView(rootView);
        mAdapter = new TodayInAdapter(null);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            InListBean.ListBean listBean = (InListBean.ListBean) mAdapter.getData().get(position);
            if (view.getId() == R.id.ivHeaderEdit) {
                mDialogNote = new DialogNote(this).setFName(listBean.fname).setFAvatar(listBean.favatar);
                mDialogNote.show(R.id.btnSave, view1 -> {
                    EditText editText = (EditText) view1;
                    mDialogNote.dismiss();
                    mPresenter.requestNote(listBean.fid + "", editText.getText().toString());
                });
            } else {
                AndPermission.with(this)
                        .permission(Permission.CAMERA, Permission.RECORD_AUDIO)
                        .onGranted(permissions -> {
                            mPresenter.requestOut(listBean.fid + "");
                        })
                        .onDenied(permissions -> {
                            AndPermission.hasAlwaysDeniedPermission(mActivity, permissions);
                            ToastUtils.init().showQuickToast("请前往应用设置，同意权限");
                        })
                        .start();
            }
            return false;
        });
        mEditSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardUtils.init().hideKeyboard(this, mEditSearch);
                mEditSearch.clearFocus();
                String string = mEditSearch.getText().toString();
                refreshList(string, searchTime);
            }
            return false;
        });
        mTvToolRight.setOnClickListener(v -> {
            searchName = mEditSearch.getText().toString();
            refreshList(searchName, searchTime);
        });
        mPickerUtils = new PickerUtils();
        llSearchTime.setOnClickListener(v -> {
            mPickerUtils.initTimePicker(this);
            mPickerUtils.pvTime.show();
        });
        ivTimeCancel.setOnClickListener(v -> {
            searchTime = null;
            mTvTime.setText("");
        });
    }

    private void refreshList(String searchName, String searchTime) {
        if ("in".equals(mType)) {
            mPresenter.requestInList("1", searchName, searchTime);
        } else {
            mPresenter.requestOutList("1", searchName, searchTime);
        }
    }

    @Override
    public void refreshStart() {
        searchName = null;
        searchTime = null;
        mTvTime.setText("");
        mEditSearch.clearFocus();
        mEditSearch.setText("");
        refreshList(null, null);
    }

    public String searchName = null;
    public String searchTime = null;

    @Subscribe
    public void onEvent(SaveLogEvent event) {
        refreshList(searchName, searchTime);
    }

    @Subscribe
    public void onEvent(PickerTimeEvent event) {
        searchTime = event.time;
        mTvTime.setText(event.time);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(mToolbar).init();
    }


    public void responseInList(InListBean bean) {
        responseSuccess();
        generateData(bean.list);
        mAdapter.setNewData(mEntityList);
    }

    public void responseNote(BaseBean bean) {
        refreshList(searchName, searchTime);
    }

    public void responseOut(CallOutBean bean) {
        SYSApplication.callOutBean = bean;
        CustomRongCallKit.startSingleCall(mActivity, bean.family.duid, RongCallKit.CallMediaType
                .CALL_MEDIA_TYPE_VIDEO, bean);
    }

    public void responseOutList(InListBean bean) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        EventBus.getDefault().unregister(this);
    }

}
