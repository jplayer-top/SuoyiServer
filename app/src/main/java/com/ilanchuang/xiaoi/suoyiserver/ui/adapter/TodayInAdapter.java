package com.ilanchuang.xiaoi.suoyiserver.ui.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayInBean;

import java.util.List;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.ui.adapter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayInAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public TodayInAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TodayInBean.LEVEL_0, R.layout.adapter_header_today_in);
        addItemType(TodayInBean.LEVEL_1, R.layout.adapter_item_today_in);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {

    }
}
