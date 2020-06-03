package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.DistrictBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.RechargersTelephoneChargesBean;
import com.idougong.jyj.model.StaticAddressBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.DeliveryAddressContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

public class DeliveryAddressPresenter extends BasePresenter<DeliveryAddressContract.View> implements DeliveryAddressContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public DeliveryAddressPresenter() {

    }

    @Override
    public void addUserDeliveryAddress(String name, String phone, Integer areaCode, String address, String province, String city, String district, String street, String community, String livingArea,boolean isDefault) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(name)) {
            objectMap.put("name", name);
        }
        if (!EmptyUtils.isEmpty(phone)) {
            objectMap.put("phone", phone);
        }
        if (!EmptyUtils.isEmpty(areaCode)) {
            objectMap.put("areaCode", areaCode);
        }
        if (!EmptyUtils.isEmpty(address)) {
            objectMap.put("address", address);
        }
        if (!EmptyUtils.isEmpty(province)) {
            objectMap.put("province", province);
        }
        if (!EmptyUtils.isEmpty(city)) {
            objectMap.put("city", city);
        }
        if (!EmptyUtils.isEmpty(district)) {
            objectMap.put("district", district);
        }
        if (!EmptyUtils.isEmpty(street)) {
            objectMap.put("street", street);
        }
        if (!EmptyUtils.isEmpty(community)) {
            objectMap.put("community", community);
        }
        if (!EmptyUtils.isEmpty(livingArea)) {
            objectMap.put("livingArea", livingArea);
        }
        objectMap.put("isDefault", isDefault);
        addSubscribe(apiService.addUserDeliveryAddress(objectMap)
                .compose(RxUtil.<BaseResponseInfo<DeliveryAddressBean>>rxSchedulerHelper())
                .compose(RxUtil.<DeliveryAddressBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<DeliveryAddressBean>() {
                    @Override
                    public void accept(DeliveryAddressBean str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<DeliveryAddressBean>(getView()) {
                    @Override
                    public void onNext(DeliveryAddressBean deliveryAddressBean) {
                        getView().addUserDeliveryAddressResult(deliveryAddressBean);
                    }
                }));
    }

    @Override
    public void updateUserDeliveryAddress(long id, String name, String phone, Integer areaCode, String address, String province, String city, String district, String street, String community, String livingArea,boolean isDefault) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("id", id);
        if (!EmptyUtils.isEmpty(name)) {
            objectMap.put("name", name);
        }
        if (!EmptyUtils.isEmpty(phone)) {
            objectMap.put("phone", phone);
        }
        if (!EmptyUtils.isEmpty(areaCode)) {
            objectMap.put("areaCode", areaCode);
        }
        if (!EmptyUtils.isEmpty(address)) {
            objectMap.put("address", address);
        }
        if (!EmptyUtils.isEmpty(province)) {
            objectMap.put("province", province);
        }
        if (!EmptyUtils.isEmpty(city)) {
            objectMap.put("city", city);
        }
        if (!EmptyUtils.isEmpty(district)) {
            objectMap.put("district", district);
        }
        if (!EmptyUtils.isEmpty(street)) {
            objectMap.put("street", street);
        }
        if (!EmptyUtils.isEmpty(community)) {
            objectMap.put("community", community);
        }
        if (!EmptyUtils.isEmpty(livingArea)) {
            objectMap.put("livingArea", livingArea);
        }
        objectMap.put("isDefault", isDefault);
        addSubscribe(apiService.updateUserDeliveryAddress(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos" + str);
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().updateUserDeliveryAddressResult(str);
                    }
                }));
    }

    @Override
    public void getUserDeliveryAddressResult() {
        addSubscribe(apiService.getUserDeliveryAddressResult()
                .compose(RxUtil.<BaseResponseInfo<DeliveryAddressBean>>rxSchedulerHelper())
                .compose(RxUtil.<DeliveryAddressBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<DeliveryAddressBean>() {
                    @Override
                    public void accept(DeliveryAddressBean deliveryAddressBean) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<DeliveryAddressBean>(getView()) {
                    @Override
                    public void onNext(DeliveryAddressBean deliveryAddressBean) {
                        getView().setUserDeliveryAddressResult(deliveryAddressBean);
                    }
                }));
    }

    @Override
    public void getUserLoginResult(String phone, String invCode, String smsCode, String token) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        if (!EmptyUtils.isEmpty(invCode)) {
            objectMap.put("invCode", invCode);
        }
        if (!EmptyUtils.isEmpty(smsCode)) {
            objectMap.put("smsCode", smsCode);
        }
        if (!EmptyUtils.isEmpty(token)) {
            objectMap.put("token", token);
        }
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserLoginResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<LoginBean>>rxSchedulerHelper())
                .compose(RxUtil.<LoginBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean productItemInfos) throws Exception {
                        LogUtils.d("loginInfo--->" + productItemInfos.getNickName());
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginBean>(getView()) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        getView().setUserLoginResult(loginBean);
                    }
                }));
    }

    @Override
    public void getProvinceOrCityInfoResult() {
        addSubscribe(apiService.getProvinceOrCityInfoResult()
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<ProvinceBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<ProvinceBean>>>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<PagingInformationBean<List<ProvinceBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<ProvinceBean>> productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos.getList().size());
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<ProvinceBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<ProvinceBean>> pagingInformationBean) {
                        getView().setProvinceOrCityInfoResult(pagingInformationBean.getList());
                    }
                }));
    }

    @Override
    public void getCommunityInfoResult() {
        addSubscribe(apiService.getCommunityInfoResult()
                .compose(RxUtil.<BaseResponseInfo<StaticAddressBean>>rxSchedulerHelper())
                .compose(RxUtil.<StaticAddressBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<StaticAddressBean>() {
                    @Override
                    public void accept(StaticAddressBean productItemInfos) throws Exception {
                        LogUtils.d("SUCCESS");
                    }
                })
                .subscribeWith(new CommonSubscriber<StaticAddressBean>(getView()) {
                    @Override
                    public void onNext(StaticAddressBean staticAddressBean) {
                        getView().setCommunityInfoResult(staticAddressBean);
                    }
                }));
    }
}
