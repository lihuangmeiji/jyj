package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.DistrictBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.StaticAddressBean;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface DeliveryAddressContract {
    interface View extends BaseView {
        void addUserDeliveryAddressResult(DeliveryAddressBean deliveryAddressBean);
        void updateUserDeliveryAddressResult(String str);
        void setUserDeliveryAddressResult(DeliveryAddressBean deliveryAddressBean);
        void setUserLoginResult(LoginBean loginBean);
        void setProvinceOrCityInfoResult(List<ProvinceBean> provinceBeanList);
        void setCommunityInfoResult(StaticAddressBean staticAddressBean);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void addUserDeliveryAddress(String name, String phone, Integer areaCode, String address,String province,String city,String district,String street,String community,String livingArea,boolean isDefault);
        void updateUserDeliveryAddress(long id, String name, String phone, Integer areaCode, String address,String province,String city,String district,String street,String community,String livingArea,boolean isDefault);
        void getUserDeliveryAddressResult();
        void getUserLoginResult(String phone, String invCode, String smsCode, String token);
        void getProvinceOrCityInfoResult();
        void getCommunityInfoResult();
    }
}
