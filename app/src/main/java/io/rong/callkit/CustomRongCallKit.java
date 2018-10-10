package io.rong.callkit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.Toast;

import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static io.rong.callkit.RongCallKit.isInVoipCall;

/**
 * Created by Obl on 2018/10/10.
 * io.rong.callkit
 * call me : jplayer_top@163.com
 * github : https://github.com/oblivion0001
 */

public class CustomRongCallKit {
    /**
     * 发起单人通话。
     *
     * @param context   上下文
     * @param targetId  会话 id
     * @param mediaType 会话媒体类型
     */
    public static void startSingleCall(Context context, String targetId, RongCallKit.CallMediaType mediaType,
                                       Parcelable parcelable) {
        if (checkEnvironment(context, mediaType)) {
            String action;
            if (mediaType.equals(RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO)) {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO;
            } else {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO;
            }
            Intent intent = new Intent(action);
            intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase());
            intent.putExtra("targetId", targetId);
            intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
            intent.putExtra("bean", parcelable);
            intent.setPackage(context.getPackageName());
            context.startActivity(intent);
        }
    }

    /**
     * 检查应用音视频授权信息
     * 检查网络连接状态
     * 检查是否在通话中
     *
     * @param context   启动的 activity
     * @param mediaType 启动音视频的媒体类型
     * @return 是否允许启动通话界面
     */
    private static boolean checkEnvironment(Context context, RongCallKit.CallMediaType mediaType) {
        if (context instanceof Activity) {
            String[] permissions;
            if (mediaType.equals(RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO)) {
                permissions = new String[]{Manifest.permission.RECORD_AUDIO};
            } else {
                permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            }
            if (!PermissionCheckUtil.requestPermissions((Activity) context, permissions)) {
                return false;
            }
        }

        if (isInVoipCall(context)) {
            return false;
        }
        if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            Toast.makeText(context, context.getResources().getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
