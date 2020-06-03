package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.IdentificationInfosBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProfessionalTypeBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserProfessionalContract {
    interface View extends BaseView {
        void setProfessionalListResult(List<IdentificationInfosBean> professionalTypeListResult);
        void getProfessionalResult(String str);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getProfessionalListResult();
        void addProfessionalResult(String ckIds);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
