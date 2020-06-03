package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.FamilyAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface TableReservationContract {
    interface View extends BaseView {
        void setTableReservationList(List<OnlineSupermarketBean> onlineSupermarketGoodsList);
        void setOnlineSupermarketGoodsOreder(ConfirmOrderBean confirmOrderBean);
        void refreshUserTimeResult(BaseResponseInfo result);
        void setUserLoginResult(LoginBean loginBean);
        void setUpdateUserInfoResult(LoginBean loginBean);
        void setDeliveryAddress(FamilyAddressBean familyAddressBean);
        void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getTableReservationList(String name, int pageSize, int pageNum);
        void getOnlineSupermarketGoodsOreder(String jsonStr);
        void refreshUserTime();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getUpdateUserInfoResult(int id);
        void getDeliveryAddress();
        void getUserDeliveryAddressResult();
    }
}
