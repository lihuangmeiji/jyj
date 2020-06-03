package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletRecordTheDetailBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface UserWalletRecordTheDetailContract {
    interface View extends BaseView {
        void setUserLoginResult(LoginBean loginBean);
        void setUserWalletRecordTheDetailList(List<UserWalletRecordTheDetailBean> userWalletRecordTheDetailList);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUserWalletRecordTheDetailList(int pageNum, int pageSize);
    }
}
