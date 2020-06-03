package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface PanicBuyingContract {
    interface View extends BaseView {
        void setHomeShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult);
        void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getHomeShoppingDataResult(int pageNum, int pageSize);
        void getHomeBannerResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
