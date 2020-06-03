package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface NativeWebDealWithContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
