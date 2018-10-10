//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.push;

import android.text.TextUtils;

import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.PushException;

public class DeleteTokenApi extends BaseApiAgent {
    private String token;

    public DeleteTokenApi() {
    }

    public void onConnect(int rst, final HuaweiApiClient client) {
        (new Thread() {
            public void run() {
                if(!TextUtils.isEmpty(DeleteTokenApi.this.token)) {
                    try {
                        if(client == null || !ApiClientMgr.INST.isConnect(client)) {
                            HMSAgentLog.e("client not connted");
                            return;
                        }

                        HuaweiPush.HuaweiPushApi.deleteToken(client, DeleteTokenApi.this.token);
                    } catch (PushException var2) {
                        HMSAgentLog.e("删除TOKEN失败:" + var2.getMessage());
                    }
                }

            }
        }).start();
    }

    public void deleteToken(String token) {
        this.token = token;
        this.connect();
    }
}
