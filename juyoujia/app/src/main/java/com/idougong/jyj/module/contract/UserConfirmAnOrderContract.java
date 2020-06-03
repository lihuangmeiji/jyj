package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserConfirmAnOrderContract {
    interface View extends BaseView {
        void getDeliveryResult(BaseResponseInfo baseResponseInfo);
        void setUserConfirmAnOrderPayWx(UserRechargeWxBean userRechargeWxBean);
        void setUserConfirmAnOrderPayZfb(String str);
        void setUserConfirmAnOrderPayWallet(String str);
        void setUserLoginResult(LoginBean loginBean);
        void setOnlineSupermarketGoodsOreder(ConfirmOrderBean confirmOrderBean, int payType);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
        void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void addDeliveryResult(String dateTime, int storeId, int deliveryId);
        void getUserConfirmAnOrderPayWx(int deliveryId, int orderId, String dateTime);
        void getUserConfirmAnOrderPayZfb(int deliveryId, int orderId, String dateTime);
        void getUserConfirmAnOrderPayWallet(int deliveryId, int orderId, String dateTime);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getOnlineSupermarketGoodsOreder(String jsonStr, String dateTime, int payType);
        void getMethodOfPayment();
        void getUserWalletInfo();
        void getUserDeliveryAddressResult();
    }
}
