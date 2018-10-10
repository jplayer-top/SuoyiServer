package com.ilanchuang.xiaoi.suoyiserver.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.github.florent37.viewanimator.ViewAnimator;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;

import java.util.List;

import top.jplayer.baseprolibrary.glide.GlideUtils;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.adapter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayInAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public TodayInAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(InListBean.LEVEL_0, R.layout.adapter_header_today_in);
        addItemType(InListBean.LEVEL_1, R.layout.adapter_item_today_in);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case InListBean.LEVEL_0:
                InListBean.ListBean listBean = (InListBean.ListBean) item;
                ImageView ivExpand = holder.itemView.findViewById(R.id.ivExpand);
                ImageView ivHeaderAvatar = holder.itemView.findViewById(R.id.ivHeaderAvatar);
                ViewAnimator.animate(ivExpand).rotation(listBean.isExpanded() ? 180 : 0).duration(500).start();
                ivExpand.setOnClickListener(v -> {
                    if (listBean.isExpanded()) {
                        collapse(holder.getLayoutPosition());
                    } else {
                        expand(holder.getLayoutPosition());
                    }
                });
                Glide.with(mContext)
                        .load(listBean.favatar)
                        .apply(GlideUtils.init().options(R.drawable.main_home))
                        .into(ivHeaderAvatar);
                boolean hasNotes = listBean.notes != null && listBean.notes.size() > 0;
                holder.setText(R.id.tvHeaderName, listBean.fname)
                        .setText(R.id.tvHeaderNum, listBean.notenum + "")
                        .setText(R.id.tvHeaderNowTime, listBean.date)
                        .setVisible(R.id.tvHeaderTime, hasNotes)
                        .setVisible(R.id.tvHeaderNum, hasNotes)
                        .setVisible(R.id.ivExpand, hasNotes)
                        .addOnClickListener(R.id.ivHeaderEdit)
                        .setText(R.id.tvHeaderTip, "备注");
                if (hasNotes) {
                    InListBean.ListBean.NotesBean notesBean = listBean.notes.get(0);
                    holder.setText(R.id.tvHeaderTime, notesBean.ct)
                            .setText(R.id.tvHeaderTip, notesBean.note);
                }
                break;
            case InListBean.LEVEL_1:
                InListBean.ListBean.NotesBean notesBean = (InListBean.ListBean.NotesBean) item;
                holder.setText(R.id.tvHeaderTip, notesBean.note)
                        .setText(R.id.tvHeaderTime, notesBean.ct);
                break;
        }
    }
}
