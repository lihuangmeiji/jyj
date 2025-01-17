package com.idougong.jyj.module.push;

import android.content.Context;

import com.idougong.jyj.R;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class JpushNotifictionUtil {
    /**
     * 自定义通知栏
     *
     * @param context
     * @param number     自定义样式编号
     * @param layoutId   布局Id
     * @param iconTipId  指定最顶层状态栏小图标
     * @param iconShowId 指定下拉状态栏时显示的通知图标
     */
    public static void customPushNotification(Context context, int number,
                                              int layoutId, int iconTipId, int iconShowId) {
        // 指定定制的 Notification Layout
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(
                context, layoutId, R.id.notification_large_icon,
                R.id.notification_title,
                R.id.notification_text);


        // 指定最顶层状态栏小图标
        builder.statusBarDrawable = iconTipId;

        // 指定下拉状态栏时显示的通知图标
        builder.layoutIconDrawable = iconShowId;

        JPushInterface.setDefaultPushNotificationBuilder(builder);
    }

}
