package com.ilanchuang.xiaoi.suoyiserver.mvpbe.event;

/**
 * Created by Obl on 2018/10/12.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.event
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class EditSearchEvent {
    public String search;
    public int pos;

    public EditSearchEvent(int pos, String string) {
        this.search = string;
        this.pos = pos;
    }
}
