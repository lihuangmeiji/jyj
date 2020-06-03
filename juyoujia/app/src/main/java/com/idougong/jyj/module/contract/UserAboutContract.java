package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.VersionShowBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserAboutContract {
    interface View extends BaseView {
        void setUserLogoutResult(String str);
    }

    interface Presenter extends EasyBasePresenter<UserAboutContract.View> {
        void getUserLogoutResult();
    }
}
