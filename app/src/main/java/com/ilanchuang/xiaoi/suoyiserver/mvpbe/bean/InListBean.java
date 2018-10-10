package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class InListBean extends BaseBean {

    /**
     * total : 1
     * list : [{"fid":10002,"fname":"小椅太阳系","favatar":"https://imgcdn.xiaoyi99.com/favatar/fd43643add6a4ded83ac8bd78f28d39d.jpg","date":"2018-10-09 15:57:16","noteNum":4,"notes":[{"id":4,"family_id":10002,"note":"fdsfd","cuid":10001,"ct":"2018-09-27 11:00:56"},{"id":5,"family_id":10002,"note":"fdsfd","cuid":10001,"ct":"2018-09-30 16:25:23"},{"id":6,"family_id":10002,"note":"我想你我想去吃","cuid":10001,"ct":"2018-09-30 17:15:11"},{"id":12,"family_id":10002,"note":"121312","cuid":10001,"ct":"2018-10-09 15:55:45"}]}]
     */

    public int total;
    public List<ListBean> list;

    public static final int LEVEL_0 = 0;
    public static final int LEVEL_1 = 1;

    public static class ListBean extends AbstractExpandableItem<ListBean.NotesBean> implements MultiItemEntity {
        /**
         * fid : 10002
         * fname : 小椅太阳系
         * favatar : https://imgcdn.xiaoyi99.com/favatar/fd43643add6a4ded83ac8bd78f28d39d.jpg
         * date : 2018-10-09 15:57:16
         * noteNum : 4
         * notes : [{"id":4,"family_id":10002,"note":"fdsfd","cuid":10001,"ct":"2018-09-27 11:00:56"},{"id":5,"family_id":10002,"note":"fdsfd","cuid":10001,"ct":"2018-09-30 16:25:23"},{"id":6,"family_id":10002,"note":"我想你我想去吃","cuid":10001,"ct":"2018-09-30 17:15:11"},{"id":12,"family_id":10002,"note":"121312","cuid":10001,"ct":"2018-10-09 15:55:45"}]
         */

        public int fid;
        public String fname;
        public String favatar;
        public String date;
        public int notenum;
        public List<NotesBean> notes;

        @Override
        public int getLevel() {
            return LEVEL_0;
        }

        @Override
        public int getItemType() {
            return LEVEL_0;
        }

        public static class NotesBean implements MultiItemEntity {
            /**
             * id : 4
             * family_id : 10002
             * note : fdsfd
             * cuid : 10001
             * ct : 2018-09-27 11:00:56
             */

            public int id;
            public int family_id;
            public String note;
            public int cuid;
            public String ct;

            @Override
            public int getItemType() {
                return LEVEL_1;
            }
        }
    }
}
