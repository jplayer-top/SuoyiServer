//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.push;

import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

public class GetTokenApi extends BaseApiAgent {
    private static final int MAX_RETRY_TIMES = 1;
    private GetTokenHandler handler;
    private int retryTimes = 1;

    public GetTokenApi() {
    }

    public void onConnect(int rst, HuaweiApiClient client) {
        if(client != null && ApiClientMgr.INST.isConnect(client)) {
            PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
            tokenResult.setResultCallback(new ResultCallback<TokenResult>() {
                public void onResult(TokenResult result) {
                    if(result == null) {
                        HMSAgentLog.e("result is null");
                        GetTokenApi.this.onPushTokenResult(-1002, (TokenResult)null);
                    } else {
                        Status status = result.getStatus();
                        if(status == null) {
                            HMSAgentLog.e("status is null");
                            GetTokenApi.this.onPushTokenResult(-1003, (TokenResult)null);
                        } else {
                            int rstCode = status.getStatusCode();
                            HMSAgentLog.d("rstCode=" + rstCode);
                            if((rstCode == 907135006 || rstCode == 907135003) && GetTokenApi.this.retryTimes > 0) {
                                GetTokenApi.this.retryTimes--;
                                GetTokenApi.this.connect();
                            } else {
                                GetTokenApi.this.onPushTokenResult(rstCode, result);
                            }

                        }
                    }
                }
            });
        } else {
            HMSAgentLog.e("client not connted");
            this.onPushTokenResult(rst, (TokenResult)null);
        }
    }

    void onPushTokenResult(int rstCode, TokenResult result) {
        HMSAgentLog.d("callback=" + this.handler + " retCode=" + rstCode);
        if(this.handler != null) {
            this.handler.onResult(rstCode, result);
            this.handler = null;
        }

        this.retryTimes = 1;
    }

    public void getToken(GetTokenHandler handler) {
        this.handler = handler;
        this.retryTimes = 1;
        this.connect();
    }
}
