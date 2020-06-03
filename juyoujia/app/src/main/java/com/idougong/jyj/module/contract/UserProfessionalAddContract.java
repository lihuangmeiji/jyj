package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ProfessionalTypeBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.io.File;
import java.util.List;

public interface UserProfessionalAddContract {
    interface View extends BaseView {
        void setProfessionalTypeListResult(List<ProfessionalTypeBean> professionalTypeListResult);
        void setFileUploadResult(String str);
        void setProfessionalAddResult(String str);
        void setProfessionalUpdateResult(String str);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getProfessionalTypeListResult();
        void getFileUploadResult(File file);
        void getProfessionalAddResult(int type, int ckId, String carrerCertificate);
        void getProfessionalUpdateResult(int id, int type, int ckId, String carrerCertificate);
    }
}
