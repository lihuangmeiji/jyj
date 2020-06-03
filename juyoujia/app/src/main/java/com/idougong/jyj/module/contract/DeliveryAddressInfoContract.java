package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface DeliveryAddressInfoContract {
    interface View extends BaseView {
        void setDeliveryAddressInfoResult(List<DeliveryAddressBean> homeShoppingDataResult);
        void delDeliveryAddressInfoResult(String str);
        void setUserLoginResult(LoginBean loginBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getDeliveryAddressInfoResult(int pageNum, int pageSize);
        void delDeliveryAddressInfo(int deliverAddressId);
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
    }

}
