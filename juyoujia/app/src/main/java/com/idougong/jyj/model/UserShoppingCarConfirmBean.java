package com.idougong.jyj.model;

import java.util.List;

public class UserShoppingCarConfirmBean {

    /**
     * price : 72.4
     * couponAmount : -10
     * freight : 0
     * totalAmount : 82.4
     * totalNum : 6
     * couponId
     */

    private double price;
    private double couponAmount;
    private double freight;
    private double totalAmount;
    private int totalNum;
    private int couponId;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    private List<UserShoppingCarBean> shopCartList;

    public List<UserShoppingCarBean> getShopCartList() {
        return shopCartList;
    }

    public void setShopCartList(List<UserShoppingCarBean> shopCartList) {
        this.shopCartList = shopCartList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
