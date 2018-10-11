package com.ilanchuang.xiaoi.suoyiserver.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ilanchuang.xiaoi.suoyiserver.R;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallMessageBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by Obl on 2018/10/11.
 * com.ilanchuang.xiaoi.suoyiserver.ui.adapter
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class CallMessageAdapter extends BaseQuickAdapter<CallMessageBean.ListBean, BaseViewHolder> {
    public CallMessageAdapter(List<CallMessageBean.ListBean> data) {
        super(R.layout.adapter_call_message_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CallMessageBean.ListBean item) {
        helper.setText(R.id.tvOrderID, String.format(Locale.CHINA, "序号：%s", item.orderid))
                .setText(R.id.tvOrderStatus, String.format(Locale.CHINA, "订单状态：%s", item.status))
                .setText(R.id.tvOrderPrice, String.format(Locale.CHINA, "支付价格：%s元", item.price))
                .setText(R.id.tvOrderBuyTime, String.format(Locale.CHINA, "购买时间：%s", item.ct))
                .setText(R.id.tvOrderEndTime, String.format(Locale.CHINA, "订单最后修改状态的时间：%s", item.lt))
                .setText(R.id.tvOrderName, String.format(Locale.CHINA, "商品名称：%s", item.title));
    }
}
