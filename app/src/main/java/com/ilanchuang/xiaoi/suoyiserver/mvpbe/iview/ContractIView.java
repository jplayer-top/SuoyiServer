package com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserInfoBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserListBean;

import top.jplayer.baseprolibrary.mvp.contract.IContract;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.iview
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public interface ContractIView {
    interface MainIView extends IContract.IView {
        void todayIn(CallOutBean bean);

        void todayOut(TodayOutBean bean);

        void userList(UserListBean bean);

    }

    interface UserIView extends IContract.IView {
        void loginGetUserInfo(UserInfoBean userInfoBean);
    }
}
