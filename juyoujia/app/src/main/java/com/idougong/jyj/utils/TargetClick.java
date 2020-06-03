package com.idougong.jyj.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.blankj.utilcode.util.ToastUtils;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.module.ui.entertainment.TableReservationActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.module.ui.home.InformationConsultingActivity;

import java.util.List;

public class TargetClick {
    public static void targetOnClick(Context context, String target) {
        if (target.contains("jyj://")) {
            Intent intent = null;
            if (target.contains("main")) {
                PackageManager manager = context.getPackageManager();
                String scheme;
                if (target.contains("?")) {
                    scheme = target + "&lytype=bd";
                } else {
                    scheme = target + "?lytype=bd";
                }
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(scheme));
                List list = manager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
                if (list != null && list.size() > 0) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    ToastUtils.showLongToast("请升级版本后再使用此功能");
                }
            } else {
                if (target.contains("food")) {
                    intent = new Intent(context, TableReservationActivity.class);
                } else if (target.contains("home/exchange")) {
                    if (target.contains("home/exchange?id")) {
                        intent = new Intent(context, CreditsExchangeDetailedActivity.class);
                        intent.putExtra("shoppingId", Integer.valueOf(target.replace("gyj://home/exchange?id=", "")).intValue());
                    } else {
                        intent = new Intent(context, CreditsExchangeActivity.class);
                    }
                } else if (target.contains("news")) {
                    intent = new Intent(context, InformationConsultingActivity.class);
                } else {
                    ToastUtils.showLongToast("请升级版本后再使用此功能");
                    return;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            Intent intentWeb = new Intent(context, QuestionDetailesActivity.class);
            intentWeb.putExtra("url", target);
            intentWeb.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentWeb);
        }
    }
}
