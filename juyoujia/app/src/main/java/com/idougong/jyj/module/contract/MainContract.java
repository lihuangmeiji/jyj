package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface MainContract {
    interface View extends BaseView {
        void setVersionResult(VersionShowBean versionShowBean);
        void setUpdateUserInfoResult(LoginBean loginBean);
        void addPushMessageTokenResult(String str);
        void setAdvertiseWindow(List<AdvertiseInfoBean> advertiseInfoBeans);
        void setShoppingNumber(String str);
        void setFlickerScreenResult(List<FlickerScreenBean> flickerScreenBeans);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getVersionResult();
        void getUpdateUserInfoResult(int id);
        void addPushMessageToken(String xingeToken, String youmengToken, String jiguangToken, String dNo);
        void getAdvertiseInfo();
        void getShoppingNumber();
        void getFlickerScreenInfo();
    }
}
