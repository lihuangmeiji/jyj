package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.GroupUserInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface GroupDetailsContract {
    interface View extends BaseView {
        void refreshUserTimeResult(BaseResponseInfo result);
        void loadGroupUserInforResult(List<GroupUserInfoBean> object);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void refreshUserTime();
        void loadGroupUserInfo(String groupId);
    }
}
