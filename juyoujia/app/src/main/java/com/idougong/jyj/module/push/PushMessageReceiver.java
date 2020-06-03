package com.idougong.jyj.module.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.common.net.Constant;
import com.idougong.jyj.model.PushMessageBean;
import com.idougong.jyj.utils.PushMessagePlayUtil;
import com.idougong.jyj.utils.TargetClick;

import java.lang.reflect.Type;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "PushMessageReceiver";
    private Handler handler;


    @Override
    public void onMessage(final Context context, final CustomMessage customMessage) {
        Log.e(TAG, "[onMessage] " + customMessage);

    }

    @Override
    public void onNotifyMessageOpened(final Context context, final NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageOpened] " + message);
        try {
            Type type = new TypeToken<PushMessageBean>() {
            }.getType();
            PushMessageBean pushMessageBean = new Gson().fromJson(message.notificationExtras, type);
            Log.i(TAG, "target: " + pushMessageBean.getTarget());
            if (EmptyUtils.isEmpty(pushMessageBean.getTarget())) {
                return;
            }
            TargetClick.targetOnClick(context, pushMessageBean.getTarget());
        } catch (Exception e) {
            Log.i(TAG, "pushMessageBean: " + e.toString());
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        Log.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            Log.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        } else if (nActionExtra.equals("my_extra2")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        } else if (nActionExtra.equals("my_extra3")) {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        } else {
            Log.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(final Context context, final NotificationMessage message) {
        Log.e(TAG, "pushMessage:" + message);
        //clearNotificationById(Context context, int notificationId);
        // TODO Auto-generated method stub
        // 对自定义消息的处理方式，点击或者忽略
        try {
            Type type = new TypeToken<PushMessageBean>() {
            }.getType();
            final PushMessageBean pushMessageBean = new Gson().fromJson(message.notificationExtras, type);
            if (EmptyUtils.isEmpty(pushMessageBean)) {
                return;
            }
            PushMessageDbOpenHelper pushMessageDbOpenHelper = new PushMessageDbOpenHelper(context, Constant.pushMessageDbName, null, Constant.pushMessageDbVersion);
            boolean isDate = PushMessageDbOperation.queryPushMessage(pushMessageDbOpenHelper.getReadableDatabase(), pushMessageBean.getUnique_code());
            if (!isDate) {
                handler = new Handler(context.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        PushMessagePlayUtil.getInstance().pushMessagePlay("极光", message.notificationContent, pushMessageBean.getUnique_code(), context, pushMessageBean.getDifferentType());
                    }
                });
            } else {
                JPushInterface.clearNotificationById(context, message.notificationId);
            }
        } catch (Exception e) {
            Log.i(TAG, "pushMessageBean: " + e.toString());
        }

    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        Log.e(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        Log.e(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {

        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {

        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {

        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }
}
