package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.EnrollmentBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface EnrollmentContract {
    interface View extends BaseView {
        void setEnrollmentInfoResult(EnrollmentBean enrollmentBean);
        void setUpdateUserInfoResult(LoginBean loginBean);
        void setWhetherTheRegistrationIsSuccessful(String str);
        void setWhetherTheRegistration(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getEnrollmentInfoResult();
        void getUpdateUserInfoResult(int id);
        void getWhetherTheRegistrationIsSuccessful(int raceId, String project);
        void getWhetherTheRegistration(int raceId);
    }
}
