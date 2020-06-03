package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;

import com.idougong.jyj.model.UserWalletRechargeBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserWalletRechargeContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUserWalletRechargeResult(UserWalletRechargeBean userWalletRechargeBean, int payType);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserWalletRechargeInfo(String amount, int payType);
    }
}
