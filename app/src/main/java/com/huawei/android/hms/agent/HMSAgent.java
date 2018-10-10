//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.huawei.android.hms.agent.common.ActivityMgr;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.IClientConnectCallback;
import com.huawei.android.hms.agent.common.INoProguard;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.DeleteTokenApi;
import com.huawei.android.hms.agent.push.EnableReceiveNormalMsgApi;
import com.huawei.android.hms.agent.push.EnableReceiveNotifyMsgApi;
import com.huawei.android.hms.agent.push.GetPushStateApi;
import com.huawei.android.hms.agent.push.GetTokenApi;
import com.huawei.android.hms.agent.push.QueryAgreementApi;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.TokenResult;

public final class HMSAgent implements INoProguard {
    private static Context context;
    private static final String VER_020503001 = "020503001";
    private static final String VER_020503002 = "020503002";
    private static final String VER_020503003 = "020503003";
    private static final String VER_020503305 = "020503305";
    public static final String CURVER = "020503305";

    private HMSAgent() {
    }

    public static boolean init(Activity activity) {
        context = activity;
        return init((Application)null, activity);
    }

    public static boolean init(Application app) {
        context = app;
        return init(app, (Activity)null);
    }

    public static boolean init(Application app, Activity activity) {
        Application appTmp = app;
        Activity activityTmp = activity;
        if(app == null && activity == null) {
            HMSAgentLog.e("the param of method HMSAgent.init can not be null !!!");
            return false;
        } else {
            if(app == null) {
                appTmp = activity.getApplication();
            }

            if(appTmp == null) {
                HMSAgentLog.e("the param of method HMSAgent.init app can not be null !!!");
                return false;
            } else {
                if(activity != null && activity.isFinishing()) {
                    activityTmp = null;
                }

                HMSAgentLog.i("init HMSAgent 020503305 with hmssdkver 20503305");
                ActivityMgr.INST.init(appTmp, activityTmp);
                ApiClientMgr.INST.init(appTmp);
                context = appTmp;
                return true;
            }
        }
    }

    public static void destroy() {
        ActivityMgr.INST.release();
        ApiClientMgr.INST.release();
    }

    public static void connect(Activity activity, final ConnectHandler callback) {
        ApiClientMgr.INST.connect(new IClientConnectCallback() {
            public void onConnect(int rst, HuaweiApiClient client) {
                if(callback != null) {
                    callback.onConnect(rst);
                }

            }
        }, false);
    }

    public static void checkUpdate(final Activity activity) {
        HMSAgentLog.d("start checkUpdate");
        ApiClientMgr.INST.connect(new IClientConnectCallback() {
            public void onConnect(int rst, HuaweiApiClient client) {
                Activity activityCur = ActivityMgr.INST.getLastActivity();
                if(activityCur != null && client != null) {
                    client.checkUpdate(activityCur);
                } else if(activity != null && client != null) {
                    client.checkUpdate(activity);
                } else {
                    HMSAgentLog.e("no activity to checkUpdate");
                }

            }
        }, true);
    }

    public static void resolveError(long errCode) {
        ApiClientMgr.INST.registerClientConnect(new IClientConnectCallback() {
            public void onConnect(int rst, HuaweiApiClient client) {
                if(rst == 0) {
                    (new GetTokenApi()).getToken(new GetTokenHandler() {
                        public void onResult(int rtnCode, TokenResult tokenResult) {
                            Intent intent = new Intent();
                            intent.setAction("io.rong.push.intent.THIRD_PARTY_PUSH_STATE");
                            intent.putExtra("pushType", "HW");
                            intent.putExtra("action", "Get token");
                            intent.putExtra("resultCode", (long)rtnCode);
                            intent.setPackage(HMSAgent.context.getPackageName());
                            HMSAgent.context.sendBroadcast(intent);
                        }
                    });
                }

            }
        });
        ApiClientMgr.INST.resolveError((int)errCode);
    }

    public static final class Push {
        public Push() {
        }

        public static void getToken(GetTokenHandler handler) {
            (new GetTokenApi()).getToken(handler);
        }

        public static void deleteToken(String token) {
            (new DeleteTokenApi()).deleteToken(token);
        }

        public static void getPushState() {
            (new GetPushStateApi()).getPushState();
        }

        public static void enableReceiveNotifyMsg(boolean enable) {
            (new EnableReceiveNotifyMsgApi()).enableReceiveNotifyMsg(enable);
        }

        public static void enableReceiveNormalMsg(boolean enable) {
            (new EnableReceiveNormalMsgApi()).enableReceiveNormalMsg(enable);
        }

        public static void queryAgreement() {
            (new QueryAgreementApi()).queryAgreement();
        }
    }

    public static final class AgentResultCode {
        public static final int HMSAGENT_SUCCESS = 0;
        public static final int HMSAGENT_NO_INIT = -1000;
        public static final int NO_ACTIVITY_FOR_USE = -1001;
        public static final int RESULT_IS_NULL = -1002;
        public static final int STATUS_IS_NULL = -1003;
        public static final int START_ACTIVITY_ERROR = -1004;
        public static final int ON_ACTIVITY_RESULT_ERROR = -1005;
        public static final int REQUEST_REPEATED = -1006;
        public static final int APICLIENT_TIMEOUT = -1007;

        public AgentResultCode() {
        }
    }
}
