package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface CreditsExchangeConfirmContract {
    interface View extends BaseView {
        void setShoppingDetailedResult(HomeShoppingSpreeBean homeShoppingSpreeBean);
        void setUserLoginResult(LoginBean loginBean);
        void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType);
        void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserMarketOrderCancelResult(String str);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getShoppingDetailedResult(int id);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void addUserMarketCreateOrderResult(String phone, String jsonStr, String name, String address, String orderNo, String payType, String description);
        void getUserDeliveryAddressResult();
        void getMethodOfPayment();
        void getUserMarketOrderCancelResult(String orderNo);
        void getUserWalletInfo();
    }
}



