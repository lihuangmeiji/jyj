package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.AliyunPlayerSkinBean;
import com.idougong.jyj.model.CustomerServiceBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserCustomerServiceContract;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserCustomerServicePresenter extends BasePresenter<UserCustomerServiceContract.View> implements UserCustomerServiceContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserCustomerServicePresenter() {
    }

    @Override
    public void getUserCustomerServiceInfoResult() {
        addSubscribe(apiService.getUserCustomerServiceInfoResult()
                .compose(RxUtil.<BaseResponseInfo<CustomerServiceBean>>rxSchedulerHelper())
                .compose(RxUtil.<CustomerServiceBean>handleResult())
                .doOnNext(new Consumer<CustomerServiceBean>() {
                    @Override
                    public void accept(CustomerServiceBean customerServiceBean) throws Exception {
                        LogUtils.d("info----cusscess");
                    }
                })
                .subscribeWith(new CommonSubscriber<CustomerServiceBean>(getView()) {
                    @Override
                    public void onNext(CustomerServiceBean customerServiceBean) {
                        getView().setUserCustomerServiceInfoResult(customerServiceBean);
                    }
                }));
    }
}
