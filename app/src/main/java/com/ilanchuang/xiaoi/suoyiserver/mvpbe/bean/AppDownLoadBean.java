package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;


import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/4/24.
 * com.ilanchuang.xiaoi.mvp.model.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class AppDownLoadBean extends BaseBean {


    /**
     * ver : {"id":35,"ver_num":"3.0.1","build_num":"33","pkg_name":"","channel":"1","terminal":5,"url":"https://imgcdn.xiaoyi99.com/app/xiaoi-app-3.0.1.apk","description":"","ct":"2018-03-31 10:34:53"}
     */

    public VerBean ver;

    public static class VerBean {
        /**
         * id : 35
         * ver_num : 3.0.1
         * build_num : 33
         * pkg_name :
         * channel : 1
         * terminal : 5
         * url : https://imgcdn.xiaoyi99.com/app/xiaoi-app-3.0.1.apk
         * description :
         * ct : 2018-03-31 10:34:53
         */

        public int id;
        public String ver_num;
        public String build_num;
        public String pkg_name;
        public String channel;
        public int terminal;
        public String url;
        public String description;
        public String ct;
    }
}
