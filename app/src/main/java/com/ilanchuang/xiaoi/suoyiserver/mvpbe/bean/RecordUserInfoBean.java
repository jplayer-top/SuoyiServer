package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/10/11.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class RecordUserInfoBean extends BaseBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * rid : 10416
         * rname : 这些
         * sex : 男
         * birthday : 1983-01-01
         * idcard : 370783199210140372
         * phone : 18524525855
         * city : 市辖区
         * addr : 北京市市辖区东城区不过
         */

        public int rid;
        public String rname;
        public String sex;
        public String birthday;
        public String idcard;
        public String phone;
        public String city;
        public String addr;
    }
}
