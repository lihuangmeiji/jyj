package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface WXPayEntryContract {
    interface View extends BaseView {
        void setUserOrderCancelResult(BaseResponseInfo baseResponseInfo);

    }

    interface Presenter extends EasyBasePresenter<View> {
        void getUserOrderCancelResult(int orderId);
    }
}
