//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.common;

public abstract class BaseApiAgent implements IClientConnectCallback {
    public BaseApiAgent() {
    }

    protected void connect() {
        HMSAgentLog.d("connect");
        ApiClientMgr.INST.connect(this, false);
    }
}
