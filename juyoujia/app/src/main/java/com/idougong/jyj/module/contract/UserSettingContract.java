package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserSettingContract {

    interface View extends BaseView {
        void setVersionResult(VersionShowBean showBean);
        void setUpdateUserInfoResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<UserSettingContract.View> {
        void getVersionResult();
        void getUpdateUserInfoResult(int id);
    }
}
