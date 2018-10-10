//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.huawei.hms.activity.BridgeActivity;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.api.HuaweiApiClient.Builder;
import com.huawei.hms.api.HuaweiApiClient.ConnectionCallbacks;
import com.huawei.hms.api.HuaweiApiClient.OnConnectionFailedListener;
import com.huawei.hms.support.api.push.HuaweiPush;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ApiClientMgr implements ConnectionCallbacks, OnConnectionFailedListener, IActivityResumeCallback {
    public static final ApiClientMgr INST = new ApiClientMgr();
    private static final String PACKAGE_NAME_HIAPP = "com.huawei.appmarket";
    private static final Object CALLBACK_LOCK = new Object();
    private static final Object STATIC_CALLBACK_LOCK = new Object();
    private static final Object APICLIENT_LOCK = new Object();
    private static final int APICLIENT_CONNECT_TIMEOUT = 30000;
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT = 3000;
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT = 3000;
    private static final int APICLIENT_TIMEOUT_HANDLE_MSG = 3;
    private static final int APICLIENT_STARTACTIVITY_TIMEOUT_HANDLE_MSG = 4;
    private static final int UPDATE_OVER_ACTIVITY_CHECK_TIMEOUT_HANDLE_MSG = 5;
    private static final int MAX_RESOLVE_TIMES = 3;
    private Context context;
    private String curAppPackageName;
    private HuaweiApiClient apiClient;
    private boolean allowResolveConnectError = false;
    private boolean isResolving;
    private BridgeActivity resolveActivity;
    private boolean hasOverActivity = false;
    private int curLeftResolveTimes = 3;
    private List<IClientConnectCallback> connCallbacks = new ArrayList();
    private List<IClientConnectCallback> staticCallbacks = new ArrayList();
    private Handler timeoutHandler = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            boolean hasConnCallbacks;
            synchronized (ApiClientMgr.CALLBACK_LOCK) {
                hasConnCallbacks = !ApiClientMgr.this.connCallbacks.isEmpty();
            }

            if (msg != null && msg.what == 3 && hasConnCallbacks) {
                HMSAgentLog.d("connect time out");
                ApiClientMgr.this.resetApiClient();
                ApiClientMgr.this.onConnectEnd(-1007);
                return true;
            } else if (msg != null && msg.what == 4 && hasConnCallbacks) {
                HMSAgentLog.d("start activity time out");
                ApiClientMgr.this.onConnectEnd(-1007);
                return true;
            } else if (msg != null && msg.what == 5 && hasConnCallbacks) {
                HMSAgentLog.d("Discarded update dispose:hasOverActivity= resolveActivity=" + ApiClientMgr.this.resolveActivity);
                if (ApiClientMgr.this.hasOverActivity && ApiClientMgr.this.resolveActivity != null && !ApiClientMgr.this.resolveActivity.isFinishing()) {
                    ApiClientMgr.this.onResolveErrorRst(13);
                }

                return true;
            } else {
                return false;
            }
        }
    });

    private ApiClientMgr() {
    }

    public void init(Application app) {
        HMSAgentLog.d("init");
        this.context = app.getApplicationContext();
        this.curAppPackageName = app.getPackageName();
        ActivityMgr.INST.unRegisterActivitResumeEvent(this);
        ActivityMgr.INST.registerActivitResumeEvent(this);
    }

    public void release() {
        HMSAgentLog.d("release");
        HuaweiApiClient client = this.getApiClient();
        if (client != null) {
            client.disconnect();
        }

        Object var2 = STATIC_CALLBACK_LOCK;
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.clear();
        }

        var2 = CALLBACK_LOCK;
        synchronized (CALLBACK_LOCK) {
            this.connCallbacks.clear();
        }
    }

    public HuaweiApiClient getApiClient() {
        Object var1 = APICLIENT_LOCK;
        synchronized (APICLIENT_LOCK) {
            return this.apiClient;
        }
    }

    public boolean isConnect(HuaweiApiClient client) {
        return client != null && client.isConnected();
    }

    public void registerClientConnect(IClientConnectCallback staticCallback) {
        Object var2 = STATIC_CALLBACK_LOCK;
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.add(staticCallback);
        }
    }

    public void removeClientConnectCallback(IClientConnectCallback staticCallback) {
        Object var2 = STATIC_CALLBACK_LOCK;
        synchronized (STATIC_CALLBACK_LOCK) {
            this.staticCallbacks.remove(staticCallback);
        }
    }

    private HuaweiApiClient resetApiClient() {
        Object var1 = APICLIENT_LOCK;
        synchronized (APICLIENT_LOCK) {
            if (this.apiClient != null) {
                disConnectClientDelay(this.apiClient, '\uea60');
            }

            HMSAgentLog.d("reset client");
            this.apiClient = (new Builder(this.context)).addApi(HuaweiPush.PUSH_API).addConnectionCallbacks(INST).addOnConnectionFailedListener(INST).build();
            return this.apiClient;
        }
    }

    public void connect(IClientConnectCallback callback, boolean allowResolve) {
        if (this.context == null) {
            callback.onConnect(-1000, (HuaweiApiClient) null);
        } else {
            HuaweiApiClient client = this.getApiClient();
            if (client != null && client.isConnected()) {
                HMSAgentLog.d("client is valid");
                callback.onConnect(0, client);
            } else {
                Object var4 = CALLBACK_LOCK;
                synchronized (CALLBACK_LOCK) {
                    HMSAgentLog.d("client is invalid：size=" + this.connCallbacks.size());
                    this.allowResolveConnectError = this.allowResolveConnectError || allowResolve;
                    if (this.connCallbacks.isEmpty()) {
                        this.connCallbacks.add(callback);
                        this.curLeftResolveTimes = 3;
                        this.startConnect();
                    } else {
                        this.connCallbacks.add(callback);
                    }

                }
            }
        }
    }

    private void startConnect() {
        --this.curLeftResolveTimes;
        HMSAgentLog.d("start thread to connect");
        (new Thread() {
            public void run() {
                super.run();
                HuaweiApiClient client = ApiClientMgr.this.getApiClient();
                if (client == null) {
                    HMSAgentLog.d("create client");
                    client = ApiClientMgr.this.resetApiClient();
                }

                HMSAgentLog.d("connect");
                ApiClientMgr.this.timeoutHandler.sendEmptyMessageDelayed(3, 30000L);
                client.connect();
            }
        }).start();
    }

    private void onConnectEnd(int rstCode) {
        HMSAgentLog.d("connect end:" + rstCode);
        Object var2 = CALLBACK_LOCK;
        Iterator var3;
        IClientConnectCallback callback;
        synchronized (CALLBACK_LOCK) {
            var3 = this.connCallbacks.iterator();

            while (true) {
                if (!var3.hasNext()) {
                    this.connCallbacks.clear();
                    this.allowResolveConnectError = false;
                    break;
                }

                callback = (IClientConnectCallback) var3.next();
                this.aSysnCallback(rstCode, callback);
            }
        }

        var2 = STATIC_CALLBACK_LOCK;
        synchronized (STATIC_CALLBACK_LOCK) {
            var3 = this.staticCallbacks.iterator();

            while (true) {
                if (!var3.hasNext()) {
                    this.staticCallbacks.clear();
                    break;
                }

                callback = (IClientConnectCallback) var3.next();
                this.aSysnCallback(rstCode, callback);
            }
        }

        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.THIRD_PARTY_PUSH_STATE");
        intent.putExtra("pushType", "HW");
        intent.putExtra("action", "connect");
        intent.putExtra("resultCode", (long) rstCode);
        this.context.sendBroadcast(intent);
    }

    private void aSysnCallback(final int rstCode, final IClientConnectCallback callback) {
        (new Thread() {
            public void run() {
                super.run();
                HuaweiApiClient client = ApiClientMgr.this.getApiClient();
                HMSAgentLog.d("callback connect: rst=" + rstCode + " apiClient=" + client);
                callback.onConnect(rstCode, client);
            }
        }).start();
    }

    public void onActivityResume(Activity activity) {
        HMSAgentLog.d("is resolving:" + this.isResolving);
        if (this.isResolving && !"com.huawei.appmarket".equals(this.curAppPackageName)) {
            if (activity instanceof BridgeActivity) {
                this.resolveActivity = (BridgeActivity) activity;
                this.hasOverActivity = false;
                HMSAgentLog.d("received bridgeActivity:" + this.resolveActivity);
            } else if (this.resolveActivity != null && !this.resolveActivity.isFinishing()) {
                this.hasOverActivity = true;
                HMSAgentLog.d("received other Activity:" + this.resolveActivity);
            }

            this.timeoutHandler.removeMessages(5);
            this.timeoutHandler.sendEmptyMessageDelayed(5, 3000L);
        }

    }

    void onResolveErrorRst(int result) {
        HMSAgentLog.d("result=" + result);
        this.isResolving = false;
        this.resolveActivity = null;
        this.hasOverActivity = false;
        if (result == 0) {
            HuaweiApiClient client = this.getApiClient();
            if (!client.isConnecting() && !client.isConnected() && this.curLeftResolveTimes > 0) {
                this.startConnect();
                return;
            }
        }

        this.onConnectEnd(result);
    }

    void onActivityLunched() {
        HMSAgentLog.d("resolve onActivityLunched");
        this.timeoutHandler.removeMessages(4);
        this.isResolving = true;
    }

    public void onConnected() {
        HMSAgentLog.d("connect success");
        this.timeoutHandler.removeMessages(3);
        this.onConnectEnd(0);
    }

    public void onConnectionSuspended(int cause) {
        HMSAgentLog.d("connect suspended");
        this.connect(new ApiClientMgr.EmptyConnectCallback("onConnectionSuspended try end:"), false);
    }

    public void onConnectionFailed(ConnectionResult result) {
        this.timeoutHandler.removeMessages(3);
        if (result == null) {
            HMSAgentLog.e("result is null");
            this.onConnectEnd(-1002);
        } else {
            int errCode = result.getErrorCode();
            HMSAgentLog.d("errCode=" + errCode + " allowResolve=" + this.allowResolveConnectError);
            if (HuaweiApiAvailability.getInstance().isUserResolvableError(errCode) && this.allowResolveConnectError) {
                Activity activity = ActivityMgr.INST.getLastActivity();
                if (activity != null) {
                    try {
                        this.timeoutHandler.sendEmptyMessageDelayed(4, 3000L);
                        Intent intent = new Intent(activity, HMSAgentActivity.class);
                        intent.putExtra("HMSConnectionErrorCode", errCode);
                        activity.startActivity(intent);
                    } catch (Exception var5) {
                        HMSAgentLog.e("start HMSAgentActivity exception:" + var5.getMessage());
                        this.onConnectEnd(-1004);
                    }
                } else {
                    HMSAgentLog.d("no activity");
                    this.onConnectEnd(-1001);
                }
            } else {
                this.onConnectEnd(errCode);
            }
        }
    }

    public void resolveError(int errCode) {
        if (HuaweiApiAvailability.getInstance().isUserResolvableError(errCode)) {
            Activity activity = ActivityMgr.INST.getLastActivity();
            if (activity != null) {
                try {
                    this.timeoutHandler.sendEmptyMessageDelayed(4, 3000L);
                    Intent intent = new Intent(activity, HMSAgentActivity.class);
                    intent.putExtra("HMSConnectionErrorCode", errCode);
                    activity.startActivity(intent);
                } catch (Exception var4) {
                    HMSAgentLog.e("start HMSAgentActivity exception:" + var4.getMessage());
                    this.onConnectEnd(-1004);
                }
            } else {
                HMSAgentLog.d("no activity");
                this.onConnectEnd(-1001);
            }
        }
    }

    private static void disConnectClientDelay(final HuaweiApiClient clientTmp, int delay) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                clientTmp.disconnect();
            }
        }, (long) delay);
    }

    private static class EmptyConnectCallback implements IClientConnectCallback {
        private String msgPre;

        private EmptyConnectCallback(String msgPre) {
            this.msgPre = msgPre;
        }

        public void onConnect(int rst, HuaweiApiClient client) {
            HMSAgentLog.d(this.msgPre + rst);
        }
    }
}
