package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface RegisterContract {
    interface View extends BaseView {
        void registerUserInfoResult(BaseResponseInfo baseResponseInfo);
        void registerUserInfoSmsResult(BaseResponseInfo baseResponseInfo);
        void registerCheckPhoneResult(BaseResponseInfo baseResponseInfo);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void registerUserInfo(String phone, String password, String code);
        void registerUserInfoSms(String phone, String type);
        void registerCheckPhone(String phone);
    }
}
