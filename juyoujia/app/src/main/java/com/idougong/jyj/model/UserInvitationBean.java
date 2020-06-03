package com.idougong.jyj.model;

import java.util.List;

public class UserInvitationBean {

    private UserInvitationCountBean invCount;
    private List<UserInvitationPeopleBean> invUsers;

    public UserInvitationCountBean getInvCount() {
        return invCount;
    }

    public void setInvCount(UserInvitationCountBean invCount) {
        this.invCount = invCount;
    }

    public List<UserInvitationPeopleBean> getInvUsers() {
        return invUsers;
    }

    public void setInvUsers(List<UserInvitationPeopleBean> invUsers) {
        this.invUsers = invUsers;
    }
}
