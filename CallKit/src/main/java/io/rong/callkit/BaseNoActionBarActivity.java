package io.rong.callkit;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import io.rong.imkit.RongConfigurationManager;

public class BaseNoActionBarActivity extends FragmentActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        Context newContext = RongConfigurationManager.getInstance().getConfigurationContext(newBase);
        super.attachBaseContext(newContext);
    }
}
