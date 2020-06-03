package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.AliyunPlayerSkinBean;
import com.idougong.jyj.model.OnDemandBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

public interface AliyunPlayerSkinContract {
    interface View extends BaseView {
        void setAliyunPlayerSkinInfoResult(AliyunPlayerSkinBean aliyunPlayerSkinBean);
        void setOnDemandListInfoResult(OnDemandBean onDemandBean);
        void setOnDemandDetailesInfoResult(BaseResponseInfo baseResponseInfo);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getAliyunPlayerSkinInfoResult();
        void getOnDemandListInfoResult(int vodPageNum, int vodPageSize);
        void getOnDemandDetailesInfoResult(String vodId);
    }
}
