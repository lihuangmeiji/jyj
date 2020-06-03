package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.PopulationManagementBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.PopulationManagementContract;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PopulationManagementPresenter extends BasePresenter<PopulationManagementContract.View> implements PopulationManagementContract.Presenter {


    @Inject
    ApiService apiService;

    @Inject
    public PopulationManagementPresenter() {

    }


    @Override
    public void getFileUploadResult(File file, final int type) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String descriptionString = "fileUpload";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        Map<String, RequestBody> objectMap = new HashMap<>();
        objectMap.put("type", RequestBody.create(MediaType.parse("text/plain"), 3 + ""));
        addSubscribe(apiService.getFileUploadResult(description, body, objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos);
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().setFileUploadResult
                                (str, type);
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
                .doOnNext(new Consumer<LoginBean>() {
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
    public void getPopulationManagementInfo() {
        addSubscribe(apiService.getPopulationManagementInfo()
                .compose(RxUtil.<BaseResponseInfo<PopulationManagementBean>>rxSchedulerHelper())
                .compose(RxUtil.<PopulationManagementBean>handleResult())
                .doOnNext(new Consumer<PopulationManagementBean>() {
                    @Override
                    public void accept(PopulationManagementBean populationManagementBean) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<PopulationManagementBean>(getView()) {
                    @Override
                    public void onNext(PopulationManagementBean populationManagementBean) {
                        getView().setPopulationManagementInfo(populationManagementBean);
                    }
                }));

    }

    @Override
    public void addPopulationManagementInfo(int floatingPopulationRegistrationId, String realName,String gender,String address, String idCode, String cardInfo, String imgFront, String imgBack, String validityPeriod, String careerKindId, String teamName, int cpId, String phone) {
        Map<String, Object> objectMap = new HashMap<>();

        if (floatingPopulationRegistrationId!=0) {
            objectMap.put("floatingPopulationRegistrationId", floatingPopulationRegistrationId);
        }
        if (!EmptyUtils.isEmpty(realName)) {
            objectMap.put("realName", realName);
        }
        if (!EmptyUtils.isEmpty(idCode)) {
            objectMap.put("idCode", idCode);
        }
        if (!EmptyUtils.isEmpty(gender)) {
            objectMap.put("gender", gender);
        }
        if (!EmptyUtils.isEmpty(address)) {
            objectMap.put("address", address);
        }
        if (!EmptyUtils.isEmpty(cardInfo)) {
            objectMap.put("cardInfo", cardInfo);
        }
        if (!EmptyUtils.isEmpty(imgFront)) {
            objectMap.put("imgFront", imgFront);
        }
        if (!EmptyUtils.isEmpty(imgBack)) {
            objectMap.put("imgBack", imgBack);
        }
        if (!EmptyUtils.isEmpty(validityPeriod)) {
            objectMap.put("validityPeriod", validityPeriod);
        }
        if (!EmptyUtils.isEmpty(careerKindId)) {
            objectMap.put("careerKindId", careerKindId);
        }
        if (!EmptyUtils.isEmpty(teamName)) {
            objectMap.put("teamName", teamName);
        }
        if (cpId!=0) {
            objectMap.put("cpId", cpId);
        }
        if (!EmptyUtils.isEmpty(phone)) {
            objectMap.put("phone", phone);
        }
        addSubscribe(apiService.addPopulationManagementInfo(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().addPopulationManagementInfoResult(str);
                    }
                }));
    }

    @Override
    public void getProvinceOrCityInfoResult() {
        addSubscribe(apiService.getProvinceOrCityInfoResult()
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<ProvinceBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<ProvinceBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<ProvinceBean>>>() {
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
}
