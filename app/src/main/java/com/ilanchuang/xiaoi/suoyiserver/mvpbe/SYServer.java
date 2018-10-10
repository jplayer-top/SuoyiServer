package com.ilanchuang.xiaoi.suoyiserver.mvpbe;

import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.HealthDataBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.InListBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.LoginBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TodayInBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.TypeNumBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.UserInfoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public interface SYServer {

    @GET("serv/call/info2?")
    Observable<TodayInBean> requestTodayIn(@Query("duid") String did);

    @GET("serv/call/info?")
    Observable<TodayInBean> requestTodayOut(@Query("fid") String did);

    @POST("serv/call/inlist?")
    Observable<InListBean> requestInList(@Query("flag") String flag, @Query("fname") String fname, @Query("date") String date);

    @POST("serv/call/outlist?")
    Observable<InListBean> requestOutList(@Query("flag") String flag, @Query("fname") String fname, @Query("date") String date);

    @POST("serv/call/linkman?")
    Observable<InListBean> requestLinkList(@Query("pageNumber") String pageNumber, @Query("fname") String fname);

    @POST("serv/call/savenote?")
    Observable<BaseBean> requestSaveNode(@Query("fid") String fid, @Query("note") String note);

    @GET("serv/call/index?")
    Observable<TypeNumBean> requestTypeNum();

    @GET("serv/health/reportlst30?")
    Observable<HealthDataBean> requestData(@Query("rid") String rid);

    @GET("serv/user/onoff?")
    Observable<BaseBean> requestOnOff(@Query("online") String online);

    @GET("serv/user/logout?")
    Observable<BaseBean> logout();

    @POST("serv/user/login?")
    Observable<LoginBean> requestLogin(@Query("account") String account, @Query("password") String password);

    @POST("serv/user/info?")
    Observable<UserInfoBean> requestUserInfo();
}
