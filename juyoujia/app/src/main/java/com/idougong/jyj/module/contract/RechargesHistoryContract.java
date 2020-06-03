package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.RechargesHistoryBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface RechargesHistoryContract {

    interface View extends BaseView{

        void setUserLoginResult(LoginBean loginBean);
        void setRechargesHistoryResult(List<RechargesHistoryBean> rechargesHistory);

    }

    interface Presenter extends EasyBasePresenter<View>{

        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getRechargesHistoryResult(int pageNum, int pageSize);

    }
}
