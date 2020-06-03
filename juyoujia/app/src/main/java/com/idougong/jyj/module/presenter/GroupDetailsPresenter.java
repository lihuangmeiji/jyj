package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.GroupUserInfoBean;
import com.idougong.jyj.model.OnDemandBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.GroupDetailsContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class GroupDetailsPresenter extends BasePresenter<GroupDetailsContract.View> implements GroupDetailsContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public GroupDetailsPresenter() {

    }

    @Override
    public void refreshUserTime() {
        addSubscribe(apiService.refreshUserTime()
                .compose(RxUtil.<BaseResponseInfo>rxSchedulerHelper())
                .doOnNext(new Consumer<BaseResponseInfo>() {
                    @Override
                    public void accept(BaseResponseInfo baseResponseInfo) throws Exception {
                        LogUtils.d(baseResponseInfo.getMsg());
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseResponseInfo>(getView()) {
                    @Override
                    public void onNext(BaseResponseInfo result) {
                        getView().refreshUserTimeResult(result);
                    }
                }));
    }

    @Override
    public void loadGroupUserInfo(String groupId) {
        addSubscribe(apiService.loadGroupUserInfo(groupId)
                .compose(RxUtil.<BaseResponseInfo<List<GroupUserInfoBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<GroupUserInfoBean>>handleResult())
                .doOnNext(new Consumer<List<GroupUserInfoBean>>() {
                    @Override
                    public void accept(List<GroupUserInfoBean> productItemInfos) throws Exception {

                        LogUtils.d(productItemInfos.toString());

                    }
                })
                .subscribeWith(new CommonSubscriber<List<GroupUserInfoBean>>(getView()) {
                    @Override
                    public void onNext(List<GroupUserInfoBean> o) {
                        getView().loadGroupUserInforResult(o);
                    }
                }));
    }
}
