package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.IdentificationInfosBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserNameInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;

public interface UserNameCertificationAddContract {
    interface View extends BaseView {
        void setFileUploadResult(String str, final int type);

        void setNameCertificationAddResult(String str);

        //作废
        void setNameCertificationUpdateResult(String str);


        void setNameCertificationInfoResult(UserNameInfoBean userNameInfoBean);

        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getFileUploadResult(File file, final int type);

        void getNameCertificationAddResult(int type, String realname, String idCode, String imgFront, String imgBack, String cardInfo, String validityPeriod);

        //作废
        void getNameCertificationUpdateResult(int id, int type, String realname, String idCode, String imgFront, String imgBack);

        void getNameCertificationInfoResult();

        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }

}
