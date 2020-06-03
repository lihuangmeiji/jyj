package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserConsumptionBean;
import com.idougong.jyj.model.UserIntegralBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserIntegralContract {
    interface View extends BaseView {
        void setUserIntegralListResult(List<UserIntegralBean> userIntegralListResult);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserIntegralListResult(int pageNum, int pageSize, String type);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
