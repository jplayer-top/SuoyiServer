package com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayInBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserListBean;

import top.jplayer.baseprolibrary.mvp.contract.IContract;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public interface ContructIview {
    interface MainIview extends IContract.IView {
        void todayIn(TodayInBean bean);

        void todayOut(TodayOutBean bean);

        void userList(UserListBean bean);
    }
}
