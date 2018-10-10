//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.android.hms.agent.common;

import android.util.Log;

public final class HMSAgentLog {
    private static final int START_STACK_INDEX = 4;
    private static final int PRINT_STACK_COUTN = 2;
    private static HMSAgentLog.IHMSAgentLogCallback logCallback = null;

    public HMSAgentLog() {
    }

    public static void setHMSAgentLogCallback(HMSAgentLog.IHMSAgentLogCallback callback) {
        logCallback = callback;
    }

    public static void d(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if(logCallback != null) {
            logCallback.logD("HMSAgent", sb.toString());
        } else {
            Log.d("HMSAgent", sb.toString());
        }

    }

    public static void v(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if(logCallback != null) {
            logCallback.logV("HMSAgent", sb.toString());
        } else {
            Log.v("HMSAgent", sb.toString());
        }

    }

    public static void i(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if(logCallback != null) {
            logCallback.logI("HMSAgent", sb.toString());
        } else {
            Log.i("HMSAgent", sb.toString());
        }

    }

    public static void w(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if(logCallback != null) {
            logCallback.logW("HMSAgent", sb.toString());
        } else {
            Log.w("HMSAgent", sb.toString());
        }

    }

    public static void e(String log) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(log);
        if(logCallback != null) {
            logCallback.logE("HMSAgent", sb.toString());
        } else {
            Log.e("HMSAgent", sb.toString());
        }

    }

    private static void appendStack(StringBuilder sb) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if(stacks != null && stacks.length > 4) {
            int lastIndex = Math.min(stacks.length - 1, 6);

            for(int i = lastIndex; i >= 4; --i) {
                if(stacks[i] != null) {
                    String fileName = stacks[i].getFileName();
                    if(fileName != null) {
                        int dotIndx = fileName.indexOf(46);
                        if(dotIndx > 0) {
                            fileName = fileName.substring(0, dotIndx);
                        }
                    }

                    sb.append(fileName);
                    sb.append('(');
                    sb.append(stacks[i].getLineNumber());
                    sb.append(")");
                    sb.append("->");
                }
            }

            sb.append(stacks[4].getMethodName());
        }

        sb.append('\n');
    }

    public interface IHMSAgentLogCallback {
        void logD(String var1, String var2);

        void logV(String var1, String var2);

        void logI(String var1, String var2);

        void logW(String var1, String var2);

        void logE(String var1, String var2);
    }
}
