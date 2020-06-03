package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.AfterpayAdvertiseBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserPaySuccessContract {

    interface View extends BaseView{
        void setUserLoginResult(LoginBean loginBean);
        void setAfterpayAdvertiseResult(List<AfterpayAdvertiseBean> advertiseResult);
        void addUserOrderPayWalletResult(String str);
        void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean);
        void addUserOrderPayAliResult(String str);
        void setUserOrderInfoResult(List<UserOrderBean> userOrderListResult);
        void setMethodOfPayment(PayMethodBean payMethodBean);
        void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean);
    }

    interface Presenter extends EasyBasePresenter<View>{

        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getAfterpayAdvertise();
        void addUserOrderPayWallet(String orderNo);
        void addUserOrderPayWx(String orderNo);
        void addUserOrderPayAli(String orderNo);
        void getUserOrderInfoResult(String orderNo);
        void getMethodOfPayment();
        void getUserWalletInfo();
    }
}
