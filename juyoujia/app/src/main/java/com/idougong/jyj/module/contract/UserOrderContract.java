package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserOrderContract {
    interface View extends BaseView {
        void setUserOrderListResult(List<UserOrderBean> userOrderListResult);
        void setOnlineSupermarketGoodsOreder(List<DeliveryBean> deliveryBeanList);
        void setUserConfirmAnOrderPayWx(UserRechargeWxBean userRechargeWxBean);
        void setUserConfirmAnOrderPayZfb(String str);
        void setUserConfirmAnOrderPayWallet(String str);
        void setUserLoginResult(LoginBean loginBean);
        void addUserOrderRefundsResult(String str);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
        void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType,int position);
        void setUserCancelOrderResult(String str);

        void addUserOrderPayWalletResult(String str);
        void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean);
        void addUserOrderPayAliResult(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserOrderListResult(int pageNum, int pageSize, Integer status, Integer model);
        void getOnlineSupermarketGoodsOreder(String jsonStr);
        void getUserConfirmAnOrderPayWx(int deliveryId, int orderId, String dateTime);
        void getUserConfirmAnOrderPayZfb(int deliveryId, int orderId, String dateTime);
        void getUserConfirmAnOrderPayWallet(int deliveryId, int orderId, String dateTime);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void addUserOrderRefunds(String reasons, long orderId);
        void getMethodOfPayment();
        void getUserWalletInfo();
        void addUserMarketCreateOrderResult(String orderNo, String paytype,int position);
        void getUserCancelOrderResult(int orderId);

        void addUserOrderPayWallet(String orderNo);
        void addUserOrderPayWx(String orderNo);
        void addUserOrderPayAli(String orderNo);
    }
}
