package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserMessage;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserMessageDetailedContract {
    interface View extends BaseView {
        void setMessageDetailedResult(UserMessage userMessage);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getMessageDetailedResult(int mesId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
