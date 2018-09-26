package com.ilanchuang.xiaoi.suoyiserver.mvpbe;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayInBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public interface SYServer {

    @GET("serv/call/info2?")
    Observable<TodayInBean> requestTodayIn(@Query("did") String did);
}
