package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeMenu;
import com.idougong.jyj.model.HomeSDKStatus;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface Main1ActivityContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList);
        void setFunctionDivisionOne(List<ConvenientFunctionsBean> convenientFunctionsBeanList);
        void setVersionResult(VersionShowBean versionShowBean);
        void addPushMessageTokenResult(String str);
        void setHomeMenu(HomeMenu homeMenu);
        void setHomeSDKStatus(HomeSDKStatus homeSDKStatus);
        void setUpdateUserInfoResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getHomeBannerResult();
        void getFunctionDivisionOne();
        void getVersionResult();
        void addPushMessageToken(String xingeToken, String youmengToken, String jiguangToken, String dNo);
        void getHomeMenu();
        void getHomeSDKStatus();
        void getUpdateUserInfoResult();
    }
}
