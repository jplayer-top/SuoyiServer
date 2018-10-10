package io.rong.message;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by Obl on 2018/5/14.
 * io.rong.message
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class NotificationReceive extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }
}
