package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.CustomerServiceBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface UserCustomerServiceContract {
    interface View extends BaseView {
        void setUserCustomerServiceInfoResult(CustomerServiceBean customerServiceInfoResult);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserCustomerServiceInfoResult();
    }
}
