package com.idougong.jyj.model.provider;

import com.idougong.jyj.model.HomeShoppingSpreeBean;

import java.util.List;

public class HomePbShoppingItemBean {


    /**
     * seckillStart : 2020-04-22 12:00:00
     * seckillEnd : 2020-04-22 18:00:00
     */

    private String seckillStart;
    private String seckillEnd;
    private List<HomeShoppingSpreeBean> productList;
    private boolean isCloseCdTime;
    /**
     * id : null
     * num : 3
     * status : null
     */

    private int id;
    private int num;
    private Object status;


    public String getSeckillStart() {
        return seckillStart;
    }

    public void setSeckillStart(String seckillStart) {
        this.seckillStart = seckillStart;
    }

    public String getSeckillEnd() {
        return seckillEnd;
    }

    public void setSeckillEnd(String seckillEnd) {
        this.seckillEnd = seckillEnd;
    }

    public List<HomeShoppingSpreeBean> getProductList() {
        return productList;
    }

    public void setProductList(List<HomeShoppingSpreeBean> productList) {
        this.productList = productList;
    }

    public boolean isCloseCdTime() {
        return isCloseCdTime;
    }

    public void setCloseCdTime(boolean closeCdTime) {
        isCloseCdTime = closeCdTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
