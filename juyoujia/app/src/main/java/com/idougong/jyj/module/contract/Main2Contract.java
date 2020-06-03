package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeMenu;
import com.idougong.jyj.model.HomeSDKStatus;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface Main2Contract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList);
        void setHomeBanner1Result(List<HomeBannerBean> homeBannerBeanList);
        void setFunctionDivisionOne(List<ConvenientFunctionsBean> convenientFunctionsBeanList);
        void setVersionResult(VersionShowBean versionShowBean);
        void addPushMessageTokenResult(String str);
        void setHomeSDKStatus(HomeSDKStatus homeSDKStatus);
        void setUpdateUserInfoResult(LoginBean loginBean);
        void setHomeShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void setAdvertiseWindow(List<AdvertiseInfoBean> advertiseInfoBeans);
        void setFlickerScreenResult(List<FlickerScreenBean> flickerScreenBeans);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getHomeBannerResult();
        void getHomeBanner1Result();
        void getFunctionDivisionOne();
        void getVersionResult();
        void addPushMessageToken(String xingeToken, String youmengToken, String jiguangToken, String dNo);
        void getHomeSDKStatus();
        void getUpdateUserInfoResult();
        void getHomeShoppingDataResult();
        void getAdvertiseInfo();
        void getFlickerScreenInfo();
    }
}
