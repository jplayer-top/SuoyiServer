package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class LinkListBean  extends BaseBean{

    /**
     * more : false
     * total : 14
     * list : [{"fid":10406,"fname":"fnnnc","favatar":"https://imgcdn.xiaoyi99.com/favatar/ed6132551684477f8265057b61bfb033.jpg","notenum":3,"notes":[{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:51:16"},{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:51:11"},{"id":0,"family_id":0,"note":"dsfdsf","cuid":0,"ct":"2018-09-10 17:45:14"}]},{"fid":10433,"fname":"不过","favatar":"","notenum":0,"notes":[]},{"fid":10390,"fname":"你说了","favatar":"","notenum":1,"notes":[{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:50:20"}]},{"fid":10423,"fname":"后","favatar":"","notenum":0,"notes":[]},{"fid":10434,"fname":"后天","favatar":"","notenum":1,"notes":[{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:49:46"}]},{"fid":10391,"fname":"在一起时你","favatar":"","notenum":0,"notes":[]},{"fid":10389,"fname":"在家里面","favatar":"","notenum":0,"notes":[]},{"fid":10422,"fname":"我们不","favatar":"https://imgcdn.xiaoyi99.com/favatar/abd6345aa4354bf8a789c0fce7731636.jpg","notenum":0,"notes":[]},{"fid":10429,"fname":"泛银河系研发","favatar":"https://imgcdn.xiaoyi99.com/favatar/d53e075863b9457b8f331c5ad26da764.jpg","notenum":1,"notes":[{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:49:40"}]},{"fid":10386,"fname":"的，的","favatar":"","notenum":0,"notes":[]},{"fid":10430,"fname":"老王家","favatar":"https://imgcdn.xiaoyi99.com/favatar/76f46d0ab8a04c70987b6c36e102305e.jpg","notenum":0,"notes":[]},{"fid":10393,"fname":"苑肖敏1026测试","favatar":"","notenum":0,"notes":[]},{"fid":10435,"fname":"顿顿吃肉","favatar":"","notenum":0,"notes":[]},{"fid":10424,"fname":"龙5","favatar":"https://imgcdn.xiaoyi99.com/favatar/dcfe90f9420e445baa4715502fe8d532.jpg","notenum":0,"notes":[]}]
     */

    public boolean more;
    public int total;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * fid : 10406
         * fname : fnnnc
         * favatar : https://imgcdn.xiaoyi99.com/favatar/ed6132551684477f8265057b61bfb033.jpg
         * notenum : 3
         * notes : [{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:51:16"},{"id":0,"family_id":0,"note":"fff","cuid":0,"ct":"2018-10-09 15:51:11"},{"id":0,"family_id":0,"note":"dsfdsf","cuid":0,"ct":"2018-09-10 17:45:14"}]
         */

        public int fid;
        public String fname;
        public String favatar;
        public int notenum;
        public List<NotesBean> notes;

        public static class NotesBean {
            /**
             * id : 0
             * family_id : 0
             * note : fff
             * cuid : 0
             * ct : 2018-10-09 15:51:16
             */

            public int id;
            public int family_id;
            public String note;
            public int cuid;
            public String ct;
        }
    }
}
