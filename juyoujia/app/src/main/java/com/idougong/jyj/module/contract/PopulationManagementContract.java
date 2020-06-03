package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PopulationManagementBean;
import com.idougong.jyj.model.ProvinceBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;
import java.util.List;

public interface PopulationManagementContract {

    interface View extends BaseView{
        void setFileUploadResult(String str, final int type);
        void setUserLoginResult(LoginBean loginBean);
        void setPopulationManagementInfo(PopulationManagementBean populationManagementBean);
        void addPopulationManagementInfoResult(String str);
        void setProvinceOrCityInfoResult(List<ProvinceBean> provinceBeanList);
    }

    interface Presenter extends EasyBasePresenter<View>{
        void getFileUploadResult(File file, final int type);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getPopulationManagementInfo();
        void addPopulationManagementInfo(int floatingPopulationRegistrationId, String realName,
                                         String gender, String address, String idCode, String cardInfo, String imgFront, String imgBack
                , String validityPeriod, String careerKindId, String teamName, int cpId, String phone);
        void getProvinceOrCityInfoResult();
    }
}
