package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;

public interface TabCategorizeFourthContract  {

    interface View extends BaseView {
        void setUpdateUserInfoResult(LoginBean loginBean);
        void setUserLoginResult(LoginBean loginBean);
        void setVersionResult(VersionShowBean showBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUpdateUserInfoResult(int id);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getVersionResult();
    }
}
