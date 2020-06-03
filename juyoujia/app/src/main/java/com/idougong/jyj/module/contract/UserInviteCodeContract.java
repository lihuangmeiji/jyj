package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserInviteCodeContract {
    interface View extends BaseView {
        void setUserShareContentResult(String str);
        void setUserLoginResult(LoginBean loginBean);
        void getUserShareResult(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserShareContentResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void addUserShareResult();
    }
}
