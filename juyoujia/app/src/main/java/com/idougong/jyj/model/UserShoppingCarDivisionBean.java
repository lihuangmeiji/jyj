package com.idougong.jyj.model;

import java.util.List;

public class UserShoppingCarDivisionBean {
    private List<UserShoppingCarBean> failureTrue;
    private List<UserShoppingCarBean> failureFalse;

    public List<UserShoppingCarBean> getFailureTrue() {
        return failureTrue;
    }

    public void setFailureTrue(List<UserShoppingCarBean> failureTrue) {
        this.failureTrue = failureTrue;
    }

    public List<UserShoppingCarBean> getFailureFalse() {
        return failureFalse;
    }

    public void setFailureFalse(List<UserShoppingCarBean> failureFalse) {
        this.failureFalse = failureFalse;
    }
}
