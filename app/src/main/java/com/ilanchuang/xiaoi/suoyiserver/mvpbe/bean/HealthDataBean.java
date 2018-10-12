package com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean;

import java.util.List;

import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;

/**
 * Created by Obl on 2018/10/9.
 * com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class HealthDataBean extends BaseBean {

    /**
     * report : {"rid":10000,"title":"2018.09.10-2018.10.09","rname":"龙哥","pressureTitle":"未测量","pressureSub":"本周未测量过血压","pressureAvgH":"--mmHg","pressureAvgHStatus":"无数据","pressureAvgL":"--mmHg","pressureAvgLStatus":"无数据","bloodStatusDataFormatV":{"hp":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"lp":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-14","09-19","09-24","09-29","10-04","10-09"]},"heartTitle":"未测量","heartSub":"本周未测量过心率","heartAvg":"--bpm","heartAvgStatus":"无数据","heartStatusDataFormatV":{"hr":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-14","09-19","09-24","09-29","10-04","10-09"]},"glucoseTitle":"未测量","glucoseSub":"本周未测量过血糖","glucoseAvgB":"--mmol/L","glucoseAvgBStatus":"无数据","glucoseAvgA":"--mmol/L","glucoseAvgAStatus":"无数据","glucoseStatusDataFormatV":[{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.10","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.16","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.23","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.30","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"10.07","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"10.09","em":1}],"oxygenTitle":"未测量","oxygenSub":"本周未测量过血氧","oxygenAvg":"--%vol/dl","oxygenAvgStatus":"无数据","oxygenDataFormatV":{"bo":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-16","09-23","09-30","10-07","10-09"]},"report":"每天提醒父母检测身体，有助于父母了解健康状况，调整生活状态保持身体健康"}
     */

    public ReportBean report;
    public List<ReportBean> reports;

    public static class ReportBean {
        /**
         * rid : 10000
         * title : 2018.09.10-2018.10.09
         * rname : 龙哥
         * pressureTitle : 未测量
         * pressureSub : 本周未测量过血压
         * pressureAvgH : --mmHg
         * pressureAvgHStatus : 无数据
         * pressureAvgL : --mmHg
         * pressureAvgLStatus : 无数据
         * bloodStatusDataFormatV : {"hp":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"lp":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-14","09-19","09-24","09-29","10-04","10-09"]}
         * heartTitle : 未测量
         * heartSub : 本周未测量过心率
         * heartAvg : --bpm
         * heartAvgStatus : 无数据
         * heartStatusDataFormatV : {"hr":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-14","09-19","09-24","09-29","10-04","10-09"]}
         * glucoseTitle : 未测量
         * glucoseSub : 本周未测量过血糖
         * glucoseAvgB : --mmol/L
         * glucoseAvgBStatus : 无数据
         * glucoseAvgA : --mmol/L
         * glucoseAvgAStatus : 无数据
         * glucoseStatusDataFormatV : [{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.10","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.16","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.23","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"09.30","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"10.07","em":1},{"bg":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"],"ymd":"10.09","em":1}]
         * oxygenTitle : 未测量
         * oxygenSub : 本周未测量过血氧
         * oxygenAvg : --%vol/dl
         * oxygenAvgStatus : 无数据
         * oxygenDataFormatV : {"bo":[{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}],"cat":["09-10","09-16","09-23","09-30","10-07","10-09"]}
         * report : 每天提醒父母检测身体，有助于父母了解健康状况，调整生活状态保持身体健康
         */

        public int rid;
        public String title;
        public String rname;
        public String pressureTitle;
        public String pressureSub;
        public String pressureAvgH;
        public String pressureAvgHStatus;
        public String pressureAvgL;
        public String pressureAvgLStatus;
        public BloodStatusDataFormatVBean bloodStatusDataFormatV;
        public String heartTitle;
        public String heartSub;
        public String heartAvg;
        public String heartAvgStatus;
        public HeartStatusDataFormatVBean heartStatusDataFormatV;
        public String glucoseTitle;
        public String glucoseSub;
        public String glucoseAvgB;
        public String glucoseAvgBStatus;
        public String glucoseAvgA;
        public String glucoseAvgAStatus;
        public String oxygenTitle;
        public String oxygenSub;
        public String oxygenAvg;
        public String oxygenAvgStatus;
        public OxygenDataFormatVBean oxygenDataFormatV;
        public String report;
        public List<GlucoseStatusDataFormatVBean> glucoseStatusDataFormatV;

        public static class BloodStatusDataFormatVBean {
            public List<HpBean> hp;
            public List<LpBean> lp;
            public List<String> cat;

            public static class HpBean {
                /**
                 * data : 0
                 * status : 0
                 */

                public int data;
                public int status;
            }

            public static class LpBean {
                /**
                 * data : 0
                 * status : 0
                 */

                public int data;
                public int status;
            }
        }

        public static class HeartStatusDataFormatVBean {
            public List<HrBean> hr;
            public List<String> cat;

            public static class HrBean {
                /**
                 * data : 0
                 * status : 0
                 */

                public int data;
                public int status;
            }
        }

        public static class OxygenDataFormatVBean {
            public List<BoBean> bo;
            public List<String> cat;

            public static class BoBean {
                /**
                 * data : 0
                 * status : 0
                 */

                public int data;
                public int status;
            }
        }

        public static class GlucoseStatusDataFormatVBean {
            /**
             * bg : [{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0},{"data":0,"status":0}]
             * cat : ["凌晨","早餐前","早餐后","午餐前","午餐后","晚餐前","晚餐后","睡前"]
             * ymd : 09.10
             * em : 1
             */

            public String ymd;
            public int em;
            public List<BgBean> bg;
            public List<String> cat;

            public static class BgBean {
                /**
                 * data : 0.0
                 * status : 0
                 */

                public double data;
                public int status;
            }
        }
    }
}
