package io.rong.callkit;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ilanchuang.xiaoi.suoyiserver.SYSApplication;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.SYServer;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.bean.CallOutBean;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.event.SaveLogEvent;
import com.ilanchuang.xiaoi.suoyiserver.mvpbe.model.ServerModel;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogCallInfo;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogCallMessageOrder;
import com.ilanchuang.xiaoi.suoyiserver.ui.dialog.DialogCallMessageRecode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.rong.calllib.CallUserProfile;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import top.jplayer.baseprolibrary.glide.GlideUtils;
import top.jplayer.baseprolibrary.mvp.model.bean.BaseBean;
import top.jplayer.baseprolibrary.net.retrofit.IoMainSchedule;
import top.jplayer.baseprolibrary.net.retrofit.NetCallBackObserver;
import top.jplayer.baseprolibrary.utils.LogUtil;

public class CustomSingleCallActivity extends BaseCallActivity implements Handler.Callback {
    private static final String TAG = "VoIPSingleActivity";
    private LayoutInflater inflater;
    private RongCallSession callSession;
    private FrameLayout mLPreviewContainer;
    private FrameLayout mSPreviewContainer;
    private FrameLayout mButtonContainer;
    private LinearLayout mUserInfoContainer;
    private Boolean isInformationShow = false;
    private SurfaceView mLocalVideo = null;
    private boolean muted = false;
    private boolean handFree = false;
    private boolean startForCheckPermissions = false;

    private int EVENT_FULL_SCREEN = 1;

    private String targetId = null;
    private RongCallCommon.CallMediaType mediaType;
    private RelativeLayout mCustomBtnBelow;
    private ConstraintLayout mCustomCall;
    private ImageView mIvCallInfo;
    private ImageView mIvCallMessage;
    private Map<String, String> mStringMap;
    private Disposable mSubscribe;

    @Override
    final public boolean handleMessage(Message msg) {
        if (msg.what == EVENT_FULL_SCREEN) {
            hideVideoCallInformation();
            return true;
        }
        return false;
    }

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ilanchuang.xiaoi.suoyiserver.R.layout.rc_voip_activity_single_call);
        Intent intent = getIntent();
        mStringMap = new ArrayMap<>();
        mLPreviewContainer = findViewById(R.id.rc_voip_call_large_preview);
        mSPreviewContainer = findViewById(R.id.rc_voip_call_small_preview);
        mButtonContainer = findViewById(R.id.rc_voip_btn);
        mUserInfoContainer = findViewById(R.id.rc_voip_user_info);
        mCustomBtnBelow = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.customBtnBelow);
        mIvCallInfo = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.ivCallInfo);
        mIvCallMessage = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.ivCallMessage);
        mCustomCall = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.customCall);

        startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));

        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
//            mCustomBtnBelow.setVisibility(View.GONE);
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
        } else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
//            mCustomBtnBelow.setVisibility(View.VISIBLE);
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
        } else {
//            mCustomBtnBelow.setVisibility(View.GONE);
            callSession = RongCallClient.getInstance().getCallSession();
            if (callSession != null) {
                mediaType = callSession.getMediaType();
            }
        }
        if (mediaType != null) {
            inflater = LayoutInflater.from(this);
            initView(mediaType, callAction);

            if (!requestCallPermissions(mediaType, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)) {
                return;
            }
            setupIntent();
        } else {
            RLog.w(TAG, "恢复的瞬间，对方已挂断");
            setShouldShowFloat(false);
            CallFloatBoxView.hideFloatBox();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
        if (callAction == null) {
            return;
        }
        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
//            mCustomBtnBelow.setVisibility(View.GONE);
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
        } else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
//            mCustomBtnBelow.setVisibility(View.VISIBLE);
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
        } else {
//            mCustomBtnBelow.setVisibility(View.GONE);
            callSession = RongCallClient.getInstance().getCallSession();
            mediaType = callSession.getMediaType();
        }

        if (!requestCallPermissions(mediaType, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)) {
            return;
        }
        if (callSession != null) {
            setupIntent();
        }

        super.onNewIntent(intent);
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                boolean permissionGranted;
                if (mediaType == RongCallCommon.CallMediaType.AUDIO) {
                    permissionGranted = PermissionCheckUtil.checkPermissions(this, AUDIO_CALL_PERMISSIONS);
                } else {
                    permissionGranted = PermissionCheckUtil.checkPermissions(this, VIDEO_CALL_PERMISSIONS);

                }
                if (permissionGranted) {
                    if (startForCheckPermissions) {
                        startForCheckPermissions = false;
                        RongCallClient.getInstance().onPermissionGranted();
                    } else {
                        setupIntent();
                    }
                } else {
                    if (startForCheckPermissions) {
                        startForCheckPermissions = false;
                        RongCallClient.getInstance().onPermissionDenied();
                    } else {
                        finish();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {

            String[] permissions;
            if (mediaType == RongCallCommon.CallMediaType.AUDIO) {
                permissions = AUDIO_CALL_PERMISSIONS;
            } else {
                permissions = VIDEO_CALL_PERMISSIONS;
            }
            if (PermissionCheckUtil.checkPermissions(this, permissions)) {
                if (startForCheckPermissions) {
                    RongCallClient.getInstance().onPermissionGranted();
                } else {
                    setupIntent();
                }
            } else {
                if (startForCheckPermissions) {
                    RongCallClient.getInstance().onPermissionDenied();
                } else {
                    finish();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupIntent() {
        RongCallCommon.CallMediaType mediaType;
        Intent intent = getIntent();
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
//        if (callAction.equals(RongCallAction.ACTION_RESUME_CALL)) {
//            return;
//        }
        if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
            targetId = callSession.getInviterUserId();
        } else if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
            Conversation.ConversationType conversationType = Conversation.ConversationType.valueOf(intent.getStringExtra("conversationType").toUpperCase(Locale.US));
            targetId = intent.getStringExtra("targetId");

            List<String> userIds = new ArrayList<>();
            userIds.add(targetId);
            RongCallClient.getInstance().startCall(conversationType, targetId, userIds, mediaType, null);
        } else { // resume call
            callSession = RongCallClient.getInstance().getCallSession();
            mediaType = callSession.getMediaType();
        }

        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            handFree = false;
        } else if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            handFree = true;
        }

        UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
        if (userInfo != null) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_name);
            userName.setText(userInfo.getName());
            if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_portrait);
                if (userPortrait != null && userInfo.getPortraitUri() != null) {
                    userPortrait.setResource(userInfo.getPortraitUri().toString(), io.rong.callkit.R.drawable.rc_default_portrait);
                }
            }
        }

        createPowerManager();
        createPickupDetector();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pickupDetector != null && mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            pickupDetector.register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pickupDetector != null) {
            pickupDetector.unRegister();
        }
    }

    private void initView(RongCallCommon.CallMediaType mediaType, RongCallAction callAction) {
        FrameLayout buttonLayout = (FrameLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_connected_button_layout, null);
        RelativeLayout userInfoLayout = (RelativeLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_audio_call_user_info, null);
        userInfoLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_minimize).setVisibility(View.GONE);

        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            buttonLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_mute).setVisibility(View.GONE);
            buttonLayout.findViewById(io.rong.callkit.R.id.rc_voip_handfree).setVisibility(View.GONE);
            customOutCall(getIntent().getParcelableExtra("bean"));
        }

        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            findViewById(io.rong.callkit.R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(io.rong.callkit.R.color.rc_voip_background_color));
            mLPreviewContainer.setVisibility(View.GONE);
            mSPreviewContainer.setVisibility(View.GONE);

            if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
//                customButtomBtn();
                customInCall();
                buttonLayout = (FrameLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_incoming_button_layout, null);
                TextView callInfo = (TextView) userInfoLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_remind_info);
                callInfo.setText(io.rong.callkit.R.string.rc_voip_audio_call_inviting);
                onIncomingCallRinging();
            }
        } else {
            userInfoLayout = (RelativeLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_video_call_user_info, null);
            if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
//                customButtomBtn();
                customInCall();
                findViewById(io.rong.callkit.R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(io.rong.callkit.R.color.rc_voip_background_color));
                buttonLayout = (FrameLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_incoming_button_layout, null);
                TextView callInfo = (TextView) userInfoLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_remind_info);
                callInfo.setText(io.rong.callkit.R.string.rc_voip_video_call_inviting);
                onIncomingCallRinging();
                ImageView answerV = (ImageView) buttonLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_answer_btn);
                answerV.setImageResource(io.rong.callkit.R.drawable.rc_voip_vedio_answer_selector);
            }
        }
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(buttonLayout);
        mUserInfoContainer.removeAllViews();
        mUserInfoContainer.addView(userInfoLayout);
    }

    private void customInCall() {
        RongCallSession callSession = RongCallClient.getInstance().getCallSession();
        String targetId = callSession.getTargetId();
        new ServerModel(SYServer.class)
                .requestIn(targetId.replace("u", "d"))
                .compose(new IoMainSchedule<>())
                .subscribe(new NetCallBackObserver<CallOutBean>() {
                    @Override
                    public void responseSuccess(CallOutBean callOutBean) {
                        initCallInfo(callOutBean);
                        saveLog(callOutBean, "2");
                    }

                    @Override
                    public void responseFail(CallOutBean callOutBean) {

                    }
                });
    }

    public String fid = "";
    public String fname = "家";

    private void customOutCall(CallOutBean bean) {
        initCallInfo(bean);
        findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.llCallOk).setVisibility(View.GONE);
        TextView tvCallError = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvCallError);
        tvCallError.setText("取消");
        saveLog(bean, "1");

    }

    private void initCallConnect() {
        mCustomCall.setVisibility(View.GONE);
        mIvCallInfo.setVisibility(View.VISIBLE);
        mIvCallMessage.setVisibility(View.VISIBLE);
        mIvCallMessage.setOnClickListener(v -> {
            if (SYSApplication.type.contains("客服")) {
                new DialogCallMessageOrder(this).setFid(fid, fname).show();
            } else {
                DialogCallMessageRecode dialogCallMessageRecode = new DialogCallMessageRecode();
                Bundle args = new Bundle();
                args.putString("fid", fid);
                args.putString("fname", fname);
                dialogCallMessageRecode.setArguments(args);
                dialogCallMessageRecode.show(getSupportFragmentManager(), "");
            }
        });
        mIvCallInfo.setOnClickListener(v -> new DialogCallInfo(this).setFid(fid, fname).show());

    }

    private void initCallInfo(CallOutBean bean) {


        mCustomCall.setVisibility(View.VISIBLE);
        mIvCallMessage.setVisibility(View.GONE);
        mIvCallInfo.setVisibility(View.GONE);
        fid = bean.family.fid + "";
        fname = bean.family.fname;
        ImageView ivFAvatar = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.ivFAvatar);
        CallOutBean.FamilyBean family = bean.family;
        Glide.with(this).load(family.favatar).apply(GlideUtils.init().options(com.ilanchuang.xiaoi.suoyiserver.R.drawable.main_home)).into(ivFAvatar);
        TextView tvFName = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvFName);
        TextView tvFLocal = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvFLocal);
        tvFName.setText(family.fname);
        String addr = family.city + family.addr;
        if ("nullnull".equals(addr) || "".equals(addr)) {
            tvFLocal.setText("暂无家庭组信息");
        } else {
            tvFLocal.setText(addr);
        }
        List<CallOutBean.RecordsBean> records = bean.records;
        findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.llTwo).setVisibility(records.size() < 2 ? View.GONE : View.VISIBLE);
        if (records.size() > 0) {
            TextView tvOne = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvOne);
            TextView tvOneNode = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvOneNode);
            ImageView ivOne = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.ivOne);
            if (records.size() > 1) {
                ImageView ivTwo = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.ivTwo);
                TextView tvTwo = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvTwo);
                TextView tvTwoNode = findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.tvTwoNode);
                CallOutBean.RecordsBean bean1 = records.get(0);
                tvTwo.setText(bean1.rname);
                ivTwo.setImageResource("男".equals(bean1.sex) ? com.ilanchuang.xiaoi.suoyiserver.R.drawable.call_man : com.ilanchuang.xiaoi.suoyiserver.R.drawable.call_woman);
            }
            CallOutBean.RecordsBean bean0 = records.get(0);
            tvOne.setText(bean0.rname);
            ivOne.setImageResource("男".equals(bean0.sex) ? com.ilanchuang.xiaoi.suoyiserver.R.drawable.call_man : com.ilanchuang.xiaoi.suoyiserver.R.drawable.call_woman);
        }
        findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.llCallError).setOnClickListener(this::onHangupBtnClick);
        findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.llCallOk).setOnClickListener(v -> {
            onReceiveBtnClick(v);
            voiceCall = false;
        });
        mSubscribe = Observable.interval(1, 30, TimeUnit.SECONDS).compose(new IoMainSchedule<>())
                .subscribe(aLong -> {
                    new ServerModel(SYServer.class).freebusy("0").subscribe(new NetCallBackObserver<BaseBean>() {
                        @Override
                        public void responseSuccess(BaseBean bean) {
                            LogUtil.str(bean);
                        }

                        @Override
                        public void responseFail(BaseBean bean) {

                        }
                    });
                });
    }

    private void saveLog(CallOutBean bean, String direction) {
        if (mStringMap != null) {
            mStringMap.clear();
        } else {
            mStringMap = new ArrayMap<>();
        }
        mStringMap.put("callType", "2");//1语音通话 2视频通话
        mStringMap.put("direction", direction);//1发出 2接收
        mStringMap.put("logId", bean.logId + "");
        mStringMap.put("duid", "d_" + bean.family.duid);
    }

    private void hangupListener(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
        String flag;
        if (reason == RongCallCommon.CallDisconnectedReason.CANCEL ||
                reason == RongCallCommon.CallDisconnectedReason.REJECT) {
            flag = "reject";
        } else if (reason == RongCallCommon.CallDisconnectedReason.HANGUP) {
            flag = "hungup";
        } else if (reason == RongCallCommon.CallDisconnectedReason.REMOTE_CANCEL ||
                reason == RongCallCommon.CallDisconnectedReason.REMOTE_REJECT) {
            flag = "reject2";
        } else if (reason == RongCallCommon.CallDisconnectedReason.REMOTE_HANGUP) {
            flag = "hungup2";
        } else {
            flag = "busy";
        }
        mStringMap.put("flag", flag);
        new ServerModel(SYServer.class).requestSaveLog(mStringMap).subscribe(new NetCallBackObserver<BaseBean>() {
            @Override
            public void responseSuccess(BaseBean bean) {
                EventBus.getDefault().post(new SaveLogEvent(mStringMap.get("direction")));
            }

            @Override
            public void responseFail(BaseBean bean) {

            }
        });
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
        new ServerModel(SYServer.class).freebusy("1").subscribe(new NetCallBackObserver<BaseBean>() {
            @Override
            public void responseSuccess(BaseBean bean) {
                LogUtil.str(bean);
            }

            @Override
            public void responseFail(BaseBean bean) {

            }
        });
    }

    boolean voiceCall = false;

    private void customButtomBtn() {
        mCustomBtnBelow.setVisibility(View.VISIBLE);
        mCustomBtnBelow.findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.answerVoiceBtn).setOnClickListener(v -> {
            onReceiveBtnClick(v);
            voiceCall = true;
        });
        mCustomBtnBelow.findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.answerResumeBtn).setOnClickListener(this::onHangupBtnClick);
        mCustomBtnBelow.findViewById(com.ilanchuang.xiaoi.suoyiserver.R.id.answerVideoBtn).setOnClickListener(v -> {
//                RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
//                callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
//                initAudioCallView();
            onReceiveBtnClick(v);
            voiceCall = false;
        });
    }

    @Override
    public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
        super.onCallOutgoing(callSession, localVideo);
        this.callSession = callSession;
        if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {
            mLPreviewContainer.setVisibility(View.VISIBLE);
            localVideo.setTag(callSession.getSelfUserId());
            mLPreviewContainer.addView(localVideo);
        }
        onOutgoingCallRinging();
    }

    @Override
    public void onCallConnected(final RongCallSession callSession, SurfaceView localVideo) {
        super.onCallConnected(callSession, localVideo);
        this.callSession = callSession;
        TextView remindInfo = (TextView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_call_remind_info);
        setupTime(remindInfo);
//        mCustomBtnBelow.setVisibility(View.GONE);
        initCallConnect();
        if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
            findViewById(io.rong.callkit.R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);
            FrameLayout btnLayout = (FrameLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_connected_button_layout, null);
            mButtonContainer.removeAllViews();
            mButtonContainer.addView(btnLayout);
        } else {
            mLocalVideo = localVideo;
            mLocalVideo.setTag(callSession.getSelfUserId());
        }

        RongCallClient.getInstance().setEnableLocalAudio(!muted);
        View muteV = mButtonContainer.findViewById(io.rong.callkit.R.id.rc_voip_call_mute);
        if (muteV != null) {
            muteV.setSelected(muted);
        }

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn()) {
            RongCallClient.getInstance().setEnableSpeakerphone(false);
        } else {
            RongCallClient.getInstance().setEnableSpeakerphone(handFree);
        }
        View handFreeV = mButtonContainer.findViewById(io.rong.callkit.R.id.rc_voip_handfree);
        if (handFreeV != null) {
            handFreeV.setSelected(handFree);
        }
        stopRing();
        if (voiceCall) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
                    callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
                    initAudioCallView();
                }
            }, 100);
        }
    }

    @Override
    protected void onDestroy() {
        stopRing();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.setReferenceCounted(false);
            wakeLock.release();
        }
        RLog.d(TAG, "SingleCallActivity onDestroy");
        super.onDestroy();
    }

    @Override
    public void onRemoteUserJoined(final String userId, RongCallCommon.CallMediaType mediaType, SurfaceView remoteVideo) {
        super.onRemoteUserJoined(userId, mediaType, remoteVideo);
        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            findViewById(io.rong.callkit.R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(android.R.color.transparent));
            mLPreviewContainer.setVisibility(View.VISIBLE);
            mLPreviewContainer.removeAllViews();
            remoteVideo.setTag(userId);

            mLPreviewContainer.addView(remoteVideo);
            mLPreviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInformationShow) {
                        hideVideoCallInformation();
                    } else {
                        showVideoCallInformation();
                        handler.sendEmptyMessageDelayed(EVENT_FULL_SCREEN, 5 * 1000);
                    }
                }
            });
            mSPreviewContainer.setVisibility(View.VISIBLE);
            mSPreviewContainer.removeAllViews();
            if (mLocalVideo != null) {
                mLocalVideo.setZOrderMediaOverlay(true);
                mLocalVideo.setZOrderOnTop(true);
                mSPreviewContainer.addView(mLocalVideo);
            }
            mSPreviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SurfaceView fromView = (SurfaceView) mSPreviewContainer.getChildAt(0);
                    SurfaceView toView = (SurfaceView) mLPreviewContainer.getChildAt(0);

                    mLPreviewContainer.removeAllViews();
                    mSPreviewContainer.removeAllViews();
                    fromView.setZOrderOnTop(false);
                    fromView.setZOrderMediaOverlay(false);
                    mLPreviewContainer.addView(fromView);
                    toView.setZOrderOnTop(true);
                    toView.setZOrderMediaOverlay(true);
                    mSPreviewContainer.addView(toView);
                }
            });
            mButtonContainer.setVisibility(View.GONE);
            mUserInfoContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {
        if (callSession.getSelfUserId().equals(userId)) {
            showShortToast(getString(io.rong.callkit.R.string.rc_voip_switched_to_audio));
        } else {
            if (callSession.getMediaType() != RongCallCommon.CallMediaType.AUDIO) {
                RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
                callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
                showShortToast(getString(io.rong.callkit.R.string.rc_voip_remote_switched_to_audio));
            }
        }
        initAudioCallView();
        handler.removeMessages(EVENT_FULL_SCREEN);
        mButtonContainer.findViewById(io.rong.callkit.R.id.rc_voip_call_mute).setSelected(muted);
    }

    private void initAudioCallView() {
        mLPreviewContainer.removeAllViews();
        mLPreviewContainer.setVisibility(View.GONE);
        mSPreviewContainer.removeAllViews();
        mSPreviewContainer.setVisibility(View.GONE);

        findViewById(io.rong.callkit.R.id.rc_voip_call_information).setBackgroundColor(getResources().getColor(io.rong.callkit.R.color.rc_voip_background_color));
        findViewById(io.rong.callkit.R.id.rc_voip_audio_chat).setVisibility(View.GONE);

        View userInfoView = inflater.inflate(io.rong.callkit.R.layout.rc_voip_audio_call_user_info, null);
        TextView timeView = (TextView) userInfoView.findViewById(io.rong.callkit.R.id.rc_voip_call_remind_info);
        setupTime(timeView);

        mUserInfoContainer.removeAllViews();
        mUserInfoContainer.addView(userInfoView);
        UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
        if (userInfo != null) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_name);
            userName.setText(userInfo.getName());
            if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_portrait);
                if (userPortrait != null) {
                    userPortrait.setAvatar(userInfo.getPortraitUri().toString(), io.rong.callkit.R.drawable.rc_default_portrait);
                }
            }
        }
        mUserInfoContainer.setVisibility(View.VISIBLE);
        mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);

        View button = inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_connected_button_layout, null);
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(button);
        mButtonContainer.setVisibility(View.VISIBLE);
        View handFreeV = mButtonContainer.findViewById(io.rong.callkit.R.id.rc_voip_handfree);
        handFreeV.setSelected(handFree);

        if (pickupDetector != null) {
            pickupDetector.register(this);
        }
    }

    public void onHangupBtnClick(View view) {
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session == null || isFinishing) {
            finish();
            return;
        }
        RongCallClient.getInstance().hangUpCall(session.getCallId());
        stopRing();
    }

    public void onReceiveBtnClick(View view) {
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session == null || isFinishing) {
            finish();
            return;
        }
        RongCallClient.getInstance().acceptCall(session.getCallId());
    }

    public void hideVideoCallInformation() {
        isInformationShow = false;
        mUserInfoContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.GONE);

        findViewById(io.rong.callkit.R.id.rc_voip_audio_chat).setVisibility(View.GONE);
    }

    public void showVideoCallInformation() {
        isInformationShow = true;
        mUserInfoContainer.setVisibility(View.VISIBLE);
        mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_call_minimize).setVisibility(View.VISIBLE);
        mButtonContainer.setVisibility(View.VISIBLE);
        FrameLayout btnLayout = (FrameLayout) inflater.inflate(io.rong.callkit.R.layout.rc_voip_call_bottom_connected_button_layout, null);
        btnLayout.findViewById(io.rong.callkit.R.id.rc_voip_call_mute).setSelected(muted);
        btnLayout.findViewById(io.rong.callkit.R.id.rc_voip_handfree).setVisibility(View.GONE);
        btnLayout.findViewById(io.rong.callkit.R.id.rc_voip_camera).setVisibility(View.VISIBLE);
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(btnLayout);
        View view = findViewById(io.rong.callkit.R.id.rc_voip_audio_chat);
        view.setVisibility(View.GONE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
                callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
                initAudioCallView();
            }
        });
    }

    public void onHandFreeButtonClick(View view) {
        RongCallClient.getInstance().setEnableSpeakerphone(!view.isSelected());
        view.setSelected(!view.isSelected());
        handFree = view.isSelected();
    }

    public void onMuteButtonClick(View view) {
        RongCallClient.getInstance().setEnableLocalAudio(view.isSelected());
        view.setSelected(!view.isSelected());
        muted = view.isSelected();
    }

    @Override
    public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
        super.onCallDisconnected(callSession, reason);

        String senderId;
        String extra = "";
        isFinishing = true;

        hangupListener(callSession, reason);

        if (callSession == null) {
            RLog.e(TAG, "onCallDisconnected. callSession is null!");
            postRunnableDelay(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            return;
        }
        senderId = callSession.getInviterUserId();
        switch (reason) {
            case HANGUP:
            case REMOTE_HANGUP:
                long time = getTime();
                if (time >= 3600) {
                    extra = String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
                } else {
                    extra = String.format("%02d:%02d", (time % 3600) / 60, (time % 60));
                }
                break;
            case OTHER_DEVICE_HAD_ACCEPTED:
                showShortToast(getString(io.rong.callkit.R.string.rc_voip_call_other));
                break;
        }
        if (!TextUtils.isEmpty(senderId)) {
            CallSTerminateMessage message = new CallSTerminateMessage();
            message.setReason(reason);
            message.setMediaType(callSession.getMediaType());
            message.setExtra(extra);
            if (senderId.equals(callSession.getSelfUserId())) {
                message.setDirection("MO");
                RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), io.rong.imlib.model.Message.SentStatus.SENT, message, null);
            } else {
                message.setDirection("MT");
                io.rong.imlib.model.Message.ReceivedStatus receivedStatus = new io.rong.imlib.model.Message.ReceivedStatus(0);
                receivedStatus.setRead();
                RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), senderId, receivedStatus, message, null);
            }
        }


        postRunnableDelay(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }


    @Override
    public void onRestoreFloatBox(Bundle bundle) {
        super.onRestoreFloatBox(bundle);
        if (bundle == null)
            return;
        muted = bundle.getBoolean("muted");
        handFree = bundle.getBoolean("handFree");
        setShouldShowFloat(true);

        callSession = RongCallClient.getInstance().getCallSession();
        RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
        RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
        inflater = LayoutInflater.from(this);
        initView(mediaType, callAction);
        targetId = callSession.getTargetId();
        UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(targetId);
        if (userInfo != null) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_name);
            userName.setText(userInfo.getName());
            if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_portrait);
                if (userPortrait != null) {
                    userPortrait.setAvatar(userInfo.getPortraitUri().toString(), io.rong.callkit.R.drawable.rc_default_portrait);
                }
            }
        }
        SurfaceView localVideo = null;
        SurfaceView remoteVideo = null;
        String remoteUserId = null;
        for (CallUserProfile profile : callSession.getParticipantProfileList()) {
            if (profile.getUserId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                localVideo = profile.getVideoView();
            } else {
                remoteVideo = profile.getVideoView();
                remoteUserId = profile.getUserId();
            }
        }
        if (localVideo != null && localVideo.getParent() != null) {
            ((ViewGroup) localVideo.getParent()).removeView(localVideo);
        }
        onCallOutgoing(callSession, localVideo);
        onCallConnected(callSession, localVideo);
        if (remoteVideo != null && remoteVideo.getParent() != null) {
            ((ViewGroup) remoteVideo.getParent()).removeView(remoteVideo);
            onRemoteUserJoined(remoteUserId, mediaType, remoteVideo);
        }
    }

    @Override
    public String onSaveFloatBoxState(Bundle bundle) {
        super.onSaveFloatBoxState(bundle);
        callSession = RongCallClient.getInstance().getCallSession();
        bundle.putBoolean("muted", muted);
        bundle.putBoolean("handFree", handFree);
        bundle.putInt("mediaType", callSession.getMediaType().getValue());

        return getIntent().getAction();
    }

    public void onMinimizeClick(View view) {
        super.onMinimizeClick(view);
    }

    public void onSwitchCameraClick(View view) {
        RongCallClient.getInstance().switchCamera();
    }

    @Override
    public void onBackPressed() {
        return;
//        List<CallUserProfile> participantProfiles = callSession.getParticipantProfileList();
//        RongCallCommon.CallStatus callStatus = null;
//        for (CallUserProfile item : participantProfiles) {
//            if (item.getUserId().equals(callSession.getSelfUserId())) {
//                callStatus = item.getCallStatus();
//                break;
//            }
//        }
//        if (callStatus != null && callStatus.equals(RongCallCommon.CallStatus.CONNECTED)) {
//            super.onBackPressed();
//        } else {
//            RongCallClient.getInstance().hangUpCall(callSession.getCallId());
//        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        if (targetId != null && targetId.equals(userInfo.getUserId())) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_name);
            if (userInfo.getName() != null)
                userName.setText(userInfo.getName());
            AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(io.rong.callkit.R.id.rc_voip_user_portrait);
            if (userPortrait != null && userInfo.getPortraitUri() != null) {
                userPortrait.setResource(userInfo.getPortraitUri().toString(), io.rong.callkit.R.drawable.rc_default_portrait);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }
//    @Override
//    public void showOnGoingNotification() {
//        Intent intent = new Intent(getIntent().getAction());
//        Bundle bundle = new Bundle();
//        onSaveFloatBoxState(bundle);
//        intent.putExtra("floatbox", bundle);
//        intent.putExtra("callAction", RongCallAction.ACTION_RESUME_CALL.getName());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationUtil.showNotification(this, "todo", "coontent", pendingIntent, CALL_NOTIFICATION_ID);
//    }
}
