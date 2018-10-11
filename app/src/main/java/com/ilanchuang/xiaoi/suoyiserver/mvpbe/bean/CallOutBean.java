package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/9/21.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class CallOutBean extends BaseBean implements Parcelable {

    /**
     * family : {"fid":10017,"favatar":"","fname":"在家里面还有人会觉得","duid":"d_0","date":"","city":"青岛市","addr":"山东省青岛市市北区null青岛站"}
     * records : [{"rid":10416,"rname":"这些","sex":"男"},{"rid":10417,"rname":"你说了","sex":"男"}]
     * logId : 942
     */

    public FamilyBean family;
    public int logId;
    public List<RecordsBean> records;

    protected CallOutBean(Parcel in) {
        family = in.readParcelable(FamilyBean.class.getClassLoader());
        logId = in.readInt();
        records = in.createTypedArrayList(RecordsBean.CREATOR);
    }

    public static final Creator<CallOutBean> CREATOR = new Creator<CallOutBean>() {
        @Override
        public CallOutBean createFromParcel(Parcel in) {
            return new CallOutBean(in);
        }

        @Override
        public CallOutBean[] newArray(int size) {
            return new CallOutBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(family, flags);
        dest.writeInt(logId);
        dest.writeTypedList(records);
    }

    public static class FamilyBean implements Parcelable {
        /**
         * fid : 10017
         * favatar :
         * fname : 在家里面还有人会觉得
         * duid : d_0
         * date :
         * city : 青岛市
         * addr : 山东省青岛市市北区null青岛站
         */

        public int fid;
        public String favatar;
        public String fname;
        public String duid;
        public String date;
        public String city;
        public String addr;

        protected FamilyBean(Parcel in) {
            fid = in.readInt();
            favatar = in.readString();
            fname = in.readString();
            duid = in.readString();
            date = in.readString();
            city = in.readString();
            addr = in.readString();
        }

        public static final Creator<FamilyBean> CREATOR = new Creator<FamilyBean>() {
            @Override
            public FamilyBean createFromParcel(Parcel in) {
                return new FamilyBean(in);
            }

            @Override
            public FamilyBean[] newArray(int size) {
                return new FamilyBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(fid);
            dest.writeString(favatar);
            dest.writeString(fname);
            dest.writeString(duid);
            dest.writeString(date);
            dest.writeString(city);
            dest.writeString(addr);
        }
    }

    public static class RecordsBean implements Parcelable {
        /**
         * rid : 10416
         * rname : 这些
         * sex : 男
         */

        public int rid;
        public String rname;
        public String sex;

        protected RecordsBean(Parcel in) {
            rid = in.readInt();
            rname = in.readString();
            sex = in.readString();
        }

        public static final Creator<RecordsBean> CREATOR = new Creator<RecordsBean>() {
            @Override
            public RecordsBean createFromParcel(Parcel in) {
                return new RecordsBean(in);
            }

            @Override
            public RecordsBean[] newArray(int size) {
                return new RecordsBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(rid);
            dest.writeString(rname);
            dest.writeString(sex);
        }
    }
}
