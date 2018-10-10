//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ActivityMgr implements ActivityLifecycleCallbacks {
    public static final ActivityMgr INST = new ActivityMgr();
    private Application application;
    private Activity lastActivity;
    private List<IActivityResumeCallback> resumeCallbacks = new ArrayList();
    private List<IActivityPauseCallback> pauseCallbacks = new ArrayList();

    private ActivityMgr() {
    }

    public void init(Application app, Activity initActivity) {
        HMSAgentLog.d("init");
        if(this.application != null) {
            this.application.unregisterActivityLifecycleCallbacks(this);
        }

        this.application = app;
        this.lastActivity = initActivity;
        app.registerActivityLifecycleCallbacks(this);
    }

    public void release() {
        HMSAgentLog.d("release");
        if(this.application != null) {
            this.application.unregisterActivityLifecycleCallbacks(this);
        }

        this.lastActivity = null;
        this.clearActivitResumeCallbacks();
        this.application = null;
    }

    public void registerActivitResumeEvent(IActivityResumeCallback callback) {
        HMSAgentLog.d("registerOnResume:" + callback);
        this.resumeCallbacks.add(callback);
    }

    public void unRegisterActivitResumeEvent(IActivityResumeCallback callback) {
        HMSAgentLog.d("unRegisterOnResume:" + callback);
        this.resumeCallbacks.remove(callback);
    }

    public void registerActivitPauseEvent(IActivityPauseCallback callback) {
        HMSAgentLog.d("registerOnPause:" + callback);
        this.pauseCallbacks.add(callback);
    }

    public void unRegisterActivitPauseEvent(IActivityPauseCallback callback) {
        HMSAgentLog.d("unRegisterOnPause:" + callback);
        this.pauseCallbacks.remove(callback);
    }

    public void clearActivitResumeCallbacks() {
        HMSAgentLog.d("clearOnResumeCallback");
        this.resumeCallbacks.clear();
    }

    public void clearActivitPauseCallbacks() {
        HMSAgentLog.d("clearOnPauseCallback");
        this.pauseCallbacks.clear();
    }

    public Activity getLastActivity() {
        return this.lastActivity;
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        HMSAgentLog.d("onCreated:" + activity.getClass().toString());
        this.lastActivity = activity;
    }

    public void onActivityStarted(Activity activity) {
        HMSAgentLog.d("onStarted:" + activity.getClass().toString());
        this.lastActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        HMSAgentLog.d("onResumed:" + activity.getClass().toString());
        this.lastActivity = activity;
        List<IActivityResumeCallback> tmdCallbacks = new ArrayList(this.resumeCallbacks);
        Iterator var3 = tmdCallbacks.iterator();

        while(var3.hasNext()) {
            IActivityResumeCallback callback = (IActivityResumeCallback)var3.next();
            callback.onActivityResume(activity);
        }

    }

    public void onActivityPaused(Activity activity) {
        HMSAgentLog.d("onPaused:" + activity.getClass().toString());
        List<IActivityPauseCallback> tmdCallbacks = new ArrayList(this.pauseCallbacks);
        Iterator var3 = tmdCallbacks.iterator();

        while(var3.hasNext()) {
            IActivityPauseCallback callback = (IActivityPauseCallback)var3.next();
            callback.onActivityPause(activity);
        }

    }

    public void onActivityStopped(Activity activity) {
        HMSAgentLog.d("onStopped:" + activity.getClass().toString());
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
        HMSAgentLog.d("onDestroyed:" + activity.getClass().toString());
        if(activity == this.lastActivity) {
            this.lastActivity = null;
        }

    }
}
