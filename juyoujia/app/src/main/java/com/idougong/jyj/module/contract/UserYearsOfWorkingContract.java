package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserYearsOfWorkingContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUpdateUserInfoResult(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUpdateUserInfoResult(String cpYear);
    }
}
