package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;
import java.util.List;

public interface UserPerfectInformationContract {
    interface View extends BaseView {
        void setUpdateUserInfoResult(String str);
        void setFileUploadResult(String str);
        void setUserLoginResult(LoginBean loginBean);
        void setUserInfoResult(LoginBean loginBean, int type);
        void setUserLogoutResult(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUpdateUserInfoResult(String nickName, String gender, String icon, String bornDate, Integer areaCode);
        void getFileUploadResult(File file);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
//        void getProvinceOrCityInfoResult();
        void getUserInfoResult(int id, int type);
        void getUserLogoutResult();
    }
}

