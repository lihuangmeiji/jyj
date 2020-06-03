package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PopulationManagementBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface PopulationManagementInformationContract {
    interface View extends BaseView {

        void setUserLoginResult(LoginBean loginBean);

        void setPopulationManagementInfo(PopulationManagementBean populationManagementBean);

    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);

        void getPopulationManagementInfo();
    }
}
