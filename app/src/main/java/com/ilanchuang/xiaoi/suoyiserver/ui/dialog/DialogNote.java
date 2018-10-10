package com.ilanchuang.xiaoi.suoyiserver.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ilanchuang.xiaoi.suoyiserver.R;

import top.jplayer.baseprolibrary.glide.GlideUtils;
import top.jplayer.baseprolibrary.widgets.dialog.BaseCustomDialog;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.ui.dialog
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class DialogNote extends BaseCustomDialog {

    private EditText mEdNote;
    private ImageView mIvFAvatar;
    private TextView mTvFName;

    public DialogNote(Context context) {
        super(context);
    }

    @Override
    protected void initView(View view) {
        mEdNote = view.findViewById(R.id.edNote);
        mIvFAvatar = view.findViewById(R.id.ivFAvatar);
        mTvFName = view.findViewById(R.id.tvFName);
    }

    public DialogNote setFName(String name) {
        mTvFName.setText(name);
        return this;
    }

    public DialogNote setFAvatar(String avatar) {
        Glide.with(getContext()).load(avatar).apply(GlideUtils.init().options(R.drawable.main_home)).into(mIvFAvatar);
        return this;
    }

    @Override
    public void setSureListener(SureListener listener) {
        listener.onSure(mEdNote);
    }

    @Override
    public int initLayout() {
        return R.layout.dialog_note;
    }

}
