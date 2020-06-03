package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CalendarBean;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserSignBoardContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUseSignBoardInfo(CalendarBean calendarBean);
        void getUseSignBoardResult(CalendarBean calendarBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUseSignBoardInfo();
        void addUseSignBoardResult();
    }
}
