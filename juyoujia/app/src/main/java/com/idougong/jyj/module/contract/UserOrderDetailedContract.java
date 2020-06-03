package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserOrderDetailedContract {
    interface View extends BaseView {
        void setUserCancelOrderResult(String str);
        void setUserLoginResult(LoginBean loginBean);
        void setUserOrderInfoResult(List<UserOrderBean> userOrderListResult);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserCancelOrderResult(int orderId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserOrderInfoResult(String orderNo);
        void getMethodOfPayment();
        void getUserWalletInfo();
    }
}
