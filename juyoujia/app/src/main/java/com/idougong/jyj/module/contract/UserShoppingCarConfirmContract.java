package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserShoppingCarConfirmContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType);
        void setCouponsListResult(List<CouponsBean> couponsListResult);
        void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
        void setUserDeliveryTime(List<UserDeliveryTimeBean> userDeliveryTimeBeanList);
        void addUserOrderPayWalletResult(String str);
        void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean);
        void addUserOrderPayAliResult(String str);
    }


    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserDeliveryAddressResult();
        void getMethodOfPayment();
        void addUserMarketCreateOrderResult(String phone, List<Integer> productIds, String name, String address, String orderNo, String payType, String description,int couponId,String time,String timeEnd);
        void getCouponsListResult(double price,String productIds);
        void getUserShoppingCarConfirm(String ids,Integer couponId,boolean isUsed);
        void getUserWalletInfo();
        void getUserDeliveryTime();
        void addUserOrderPayWallet(String orderNo);
        void addUserOrderPayWx(String orderNo);
        void addUserOrderPayAli(String orderNo);
    }
}
