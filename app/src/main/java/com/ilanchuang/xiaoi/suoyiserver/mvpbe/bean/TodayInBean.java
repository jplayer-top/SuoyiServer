package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class TodayInBean extends BaseBean {

    /**
     * family : {"fid":10002,"favatar":"https://imgcdn.xiaoyi99.com/favatar/fd43643add6a4ded83ac8bd78f28d39d.jpg","fname":"小椅太阳系","city":"武汉市","addr":"湖北省武汉市新洲区null规划局那个活得精彩啊的啊"}
     * records : [{"rid":10000,"rname":"龙哥","sex":"男"},{"rid":10001,"rname":"斯柯达","sex":"男"}]
     */

    public FamilyBean family;
    public List<RecordsBean> records;
    public static int LEVEL_0 = 0;
    public static int LEVEL_1 = 1;

    public static class FamilyBean extends AbstractExpandableItem<RecordsBean> implements MultiItemEntity {
        /**
         * fid : 10002
         * favatar : https://imgcdn.xiaoyi99.com/favatar/fd43643add6a4ded83ac8bd78f28d39d.jpg
         * fname : 小椅太阳系
         * city : 武汉市
         * addr : 湖北省武汉市新洲区null规划局那个活得精彩啊的啊
         */

        public int fid;
        public String favatar;
        public String fname;
        public String city;
        public String addr;

        @Override
        public int getItemType() {
            return TodayInBean.LEVEL_0;
        }

        @Override
        public int getLevel() {
            return 0;
        }
    }

    public static class RecordsBean implements MultiItemEntity {
        /**
         * rid : 10000
         * rname : 龙哥
         * sex : 男
         */

        public int rid;
        public String rname;
        public String sex;

        @Override
        public int getItemType() {
            return LEVEL_1;
        }
    }
}
