//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseAgentActivity extends Activity {
    public BaseAgentActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestActivityTransparent();
    }

    private void requestActivityTransparent() {
        this.requestWindowFeature(1);
        Window window = this.getWindow();
        if(window != null) {
            window.addFlags(67108864);
        }

    }
}
