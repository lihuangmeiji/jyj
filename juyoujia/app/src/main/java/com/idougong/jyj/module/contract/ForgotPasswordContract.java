package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface ForgotPasswordContract {
    interface View extends BaseView {
        void forgotPasswordInfoResult(BaseResponseInfo baseResponseInfo);
        void forgotPasswordSmsResult(BaseResponseInfo baseResponseInfo);
        void forgotPasswordCheckPhoneResult(BaseResponseInfo baseResponseInfo);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void forgotPasswordInfo(String phone, String password, String code);
        void forgotPasswordSms(String phone, String type);
        void forgotPasswordCheckPhone(String phone);
    }
}
