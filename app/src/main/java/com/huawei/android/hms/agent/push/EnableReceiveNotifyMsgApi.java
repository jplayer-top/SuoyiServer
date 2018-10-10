//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.push;

import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class EnableReceiveNotifyMsgApi extends BaseApiAgent {
    boolean enable;

    public EnableReceiveNotifyMsgApi() {
    }

    public void onConnect(int rst, final HuaweiApiClient client) {
        (new Thread() {
            public void run() {
                if(client != null && ApiClientMgr.INST.isConnect(client)) {
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(client, EnableReceiveNotifyMsgApi.this.enable);
                } else {
                    HMSAgentLog.e("client not connted");
                }
            }
        }).start();
    }

    public void enableReceiveNotifyMsg(boolean enable) {
        this.enable = enable;
        this.connect();
    }
}
