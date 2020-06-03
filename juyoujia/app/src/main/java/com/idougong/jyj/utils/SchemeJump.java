package com.idougong.jyj.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.module.ui.home.HomePanicBuyingActivity;
import com.idougong.jyj.module.ui.home.UserSearchActivity;
import com.idougong.jyj.module.ui.home.UserSearchResultActivity;
import com.idougong.jyj.module.ui.home.UserSignBoardActivity;
import com.idougong.jyj.module.ui.users.DeliveryAddressInfoActivity;
import com.idougong.jyj.module.ui.users.UserAboutActivity;
import com.idougong.jyj.module.ui.users.UserCouponActivity;
import com.idougong.jyj.module.ui.users.UserCustomerServiceActivity;
import com.idougong.jyj.module.ui.users.UserInvitationActivity;
import com.idougong.jyj.module.ui.users.UserMessageActivity;
import com.idougong.jyj.module.ui.users.UserOrderActivity;
import com.idougong.jyj.module.ui.users.UserPerfectInformationActivity;
import com.idougong.jyj.module.ui.users.UserSettingActivity;
import com.idougong.jyj.module.ui.users.UserWalletActivity;

public class SchemeJump {
    public static void schemeJump(Context context, String path, int keyId, String keyContent, boolean isPeoplemgr) {
        if (!EmptyUtils.isEmpty(path)) {
            Intent intent = null;
            Log.i("path", "initEventAndData: " + path);
            if (path.equals("/home")) {
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("path",path);
            }else if(path.equals("/home/category")){
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("categoryid",keyId);
                intent.putExtra("path",path);
            }else if(path.equals("/home/message")){
                intent = new Intent(context, UserMessageActivity.class);
            }else if(path.equals("/cart")){
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("path",path);
            }else if(path.equals("/mine")){
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("path",path);
            } else if (path.equals("/product/details")) {
                intent = new Intent(context, CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", keyId);
            }else if (path.equals("/home/seckill")) {
                intent = new Intent(context,HomePanicBuyingActivity.class);
                intent.putExtra("point", keyId);
            } else if (path.equals("/home/mine")) {
                intent = new Intent(context, UserSettingActivity.class);
            }else if(path.equals("/home/checkin")){
                intent = new Intent(context, UserSignBoardActivity.class);
            } else if (path.equals("/mine/userinfo")) {
                intent = new Intent(context, UserPerfectInformationActivity.class);
            } else if (path.equals("/mine/address")) {
                intent = new Intent(context, DeliveryAddressInfoActivity.class);
            } else if (path.equals("/mine/about")) {
                intent = new Intent(context, UserAboutActivity.class);
            } else if (path.equals("/mine/order")) {
                intent = new Intent(context, UserOrderActivity.class);
            } else if (path.equals("/mine/coupon")) {
                intent = new Intent(context, UserCouponActivity.class);
            } else if (path.equals("/mine/wallet")) {
                intent = new Intent(context, UserWalletActivity.class);
            }else if (path.equals("/mine/custser")) {
                intent = new Intent(context, UserCustomerServiceActivity.class);
            }else if (path.equals("/mine/invite")) {
                intent = new Intent(context, UserInvitationActivity.class);
            }else if(path.equals("/search/product")){
                if(EmptyUtils.isEmpty(keyContent)){
                    intent = new Intent(context, UserSearchActivity.class);
                }else{
                    intent = new Intent(context, UserSearchResultActivity.class);
                    intent.putExtra("searchContent", keyContent);
                }
            }else {
                ToastUtils.showLongToast("请升级版本后再使用此功能");
                return;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

