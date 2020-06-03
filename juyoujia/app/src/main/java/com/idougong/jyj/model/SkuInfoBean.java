package com.idougong.jyj.model;

public class SkuInfoBean {


    /**
     * id : 298
     * archive : 0
     * createAt : 2020-04-28 10:31:28
     * updateAt : 2020-04-28 10:31:28
     * productId : 394
     * name : 鹌鹑蛋
     * attr : 500
     * price : 0.5
     * amount : null
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
    private Object amount;
    private int sales;

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

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}
