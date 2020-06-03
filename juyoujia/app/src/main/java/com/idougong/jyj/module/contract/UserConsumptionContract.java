package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserConsumptionBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserConsumptionContract {
    interface View extends BaseView {
        void setUserConsumptionListResult(List<UserConsumptionBean> userConsumptionListResult);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserConsumptionListResult(int pageNum, int pageSize);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }
}
