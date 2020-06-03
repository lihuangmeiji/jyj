package com.idougong.jyj.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserWalletInfoBean implements Serializable{

    /**
     * balance : 0
     * aliRealname : null
     * aliAccount : null
     * disable : false
     */

    private double balance;
    private String aliRealname;
    private String aliAccount;
    private boolean disable;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAliRealname() {
        return aliRealname;
    }

    public void setAliRealname(String aliRealname) {
        this.aliRealname = aliRealname;
    }

    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
