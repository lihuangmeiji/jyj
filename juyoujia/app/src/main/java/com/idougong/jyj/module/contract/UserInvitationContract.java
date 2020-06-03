package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserInvitationBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserInvitationContract {
    interface View extends BaseView {
        void setUserInvitationInfoResult(UserInvitationBean userInvitationBean);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserInvitationInfoResult(int pageNum, int pageSize);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
