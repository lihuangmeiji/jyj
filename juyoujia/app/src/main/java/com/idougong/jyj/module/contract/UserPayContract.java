package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayOrderBean;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.StoreOrderBean;
import com.idougong.jyj.model.UserRechargeAliBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.umeng.socialize.sina.message.BaseResponse;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserPayContract {
    interface View extends BaseView {
        void setStoreInfoResult(StoreInfoBean storeInfoResult);

        void setStoreOrderResult(StoreOrderBean storeOrderResult);

        void setUserRechargeWx(UserWxPayBean userWxPayBean);

        void setUserRechargeZfb(UserRechargeAliBean userRechargeAliBean);

        void setUserRechargeWallet(PayOrderBean payOrderBean);

        void setUserRechargeXj(String str);

        void setUserOrderCancelResult(String str);

        void setUserOrderConfirmResult(String str);

        void setUserLoginResult(LoginBean loginBean);

        void setUserFreeOfChargeResult(String str);

        void setShopInfoResult(StoreInfoBean storeInfoBean);

        void setUpdateUserInfoResult(LoginBean loginBean);

        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getStoreInfoResult(int id);

        void getStoreOrderResult(int storeId, String price, String point);

        void getUserRechargeWx(int shopId, boolean usePoint, double price, double finalPrice);

        void getUserRechargeZfb(int shopId, boolean usePoint, double price, double finalPrice);

        void getUserRechargeWallet(int shopId, boolean usePoint, double price, double finalPrice);

        void getUserRechargeXj(int orderId);

        void getUserOrderCancelResult(int orderId);

        void getUserOrderConfirmResult(int orderId);

        void getUserLoginResult(String phone, String invCode, String smsCode, String token);

        void getUserFreeOfChargeResult(long orderId, Integer payType);

        void getShopInfo(String content);

        void getUpdateUserInfoResult(int id);

        void getUserWalletInfo();
    }
}
