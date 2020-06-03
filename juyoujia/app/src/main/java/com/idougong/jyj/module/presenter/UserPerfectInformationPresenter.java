package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserPerfectInformationContract;
import com.idougong.jyj.utils.MD5Util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserPerfectInformationPresenter extends BasePresenter<UserPerfectInformationContract.View> implements UserPerfectInformationContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserPerfectInformationPresenter() {

    }

    @Override
    public void getUpdateUserInfoResult(String nickName, String gender, String icon, String date, Integer areaCode) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(nickName)) {
            objectMap.put("nickName", nickName);
        }
        if (!EmptyUtils.isEmpty(gender)) {
            objectMap.put("gender", gender);
        }
        if (!EmptyUtils.isEmpty(icon)) {
            objectMap.put("icon", icon);
        }
        if (!EmptyUtils.isEmpty(date)) {
            objectMap.put("bornDate", date);
        }
        if (!EmptyUtils.isEmpty(areaCode)) {
            objectMap.put("areaCode", areaCode);
        }
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUpdateUserInfoResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos" + str);
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().setUpdateUserInfoResult(str);
                    }
                }));
    }

    @Override
    public void getFileUploadResult(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String descriptionString = "fileUpload";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        Map<String, RequestBody> objectMap = new HashMap<>();
        objectMap.put("type", RequestBody.create(MediaType.parse("text/plain"), 1 + ""));
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
                    public void onNext(String pagingInformationBean) {
                        getView().setFileUploadResult
                                (pagingInformationBean);
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

//    @Override
//    public void getProvinceOrCityInfoResult() {
//        addSubscribe(apiService.getProvinceOrCityInfoResult()
//                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<ProvinceBean>>>>rxSchedulerHelper())
//                .compose(RxUtil.<PagingInformationBean<List<ProvinceBean>>>handleResult())
//                .doOnNext(new Consumer<PagingInformationBean<List<ProvinceBean>>>() {
//                    @Override
//                    public void accept(PagingInformationBean<List<ProvinceBean>> productItemInfos) throws Exception {
//                        LogUtils.d("productItemInfos" + productItemInfos.getList().size());
//                    }
//                })
//                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<ProvinceBean>>>(getView()) {
//                    @Override
//                    public void onNext(PagingInformationBean<List<ProvinceBean>> pagingInformationBean) {
//                        getView().setProvinceOrCityInfoResult(pagingInformationBean.getList());
//                    }
//                }));
//    }

    @Override
    public void getUserInfoResult(int id, final int type) {
        addSubscribe(apiService.getUserInfoResult()
                .compose(RxUtil.<BaseResponseInfo<LoginBean>>rxSchedulerHelper())
                .compose(RxUtil.<LoginBean>handleResult())
                .doOnNext(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        LogUtils.d("productItemInfos" + loginBean.getNickName());
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginBean>(getView()) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        getView().setUserInfoResult(loginBean, type);
                    }
                }));
    }

    @Override
    public void getUserLogoutResult() {
        addSubscribe(apiService.getUserLogoutResult()
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos" + str);
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().setUserLogoutResult(str);
                    }
                }));
    }
}

