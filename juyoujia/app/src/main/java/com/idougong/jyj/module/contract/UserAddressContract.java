package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConstructionPlaceBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProvinceBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserAddressContract {
    interface View extends BaseView {
        void setUserAddressResult(String str);
        void setProvinceOrCityInfoResult(List<ProvinceBean> provinceBeanList);
        void setConstructionPlaceInfoResult(List<ConstructionPlaceBean> constructionPlaceBeanList);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserAddressResult(int cpId);
        void getProvinceOrCityInfoResult();
        void getConstructionPlaceInfoResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
