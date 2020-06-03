package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserCouponBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserCouponContract {

    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUserCouponList(List<CouponsBean> couponList);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserCouponList();
    }
}
