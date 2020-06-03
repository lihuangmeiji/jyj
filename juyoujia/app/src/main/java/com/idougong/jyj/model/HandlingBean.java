package com.idougong.jyj.model;

public class HandlingBean {

    /**
     * id : 161
     * archive : 0
     * createAt : 2020-04-16 16:14:04
     * updateAt : 2020-04-16 16:14:04
     * productId : 465
     * name : 小黄鱼
     * attr : 活鱼
     * price : 0
     * amount : 9
     * sales : 0
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int productId;
    private String name;
    private String attr;
    private double price;
    private double amount;
    private double sales;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArchive() {
        return archive;
    }

    public void setArchive(int archive) {
        this.archive = archive;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }
}
