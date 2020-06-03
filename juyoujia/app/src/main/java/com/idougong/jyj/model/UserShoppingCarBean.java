package com.idougong.jyj.model;

public class UserShoppingCarBean {

    /**
     * id : 225
     * archive : 0
     * createAt : 2020-03-01 01:29:46
     * updateAt : 2020-03-01 01:29:46
     * userId : 15
     * productId : 189
     * productNum : 1
     * product
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int userId;
    private int productId;
    private int productNum;
    private boolean isSelect;
    private String skuName;
    private double skuPrice;
    private double oneSkuPrice;
    private String processingWayName;
    private boolean failure;


    public String getProcessingWayName() {
        return processingWayName;
    }

    public void setProcessingWayName(String processingWayName) {
        this.processingWayName = processingWayName;
    }

    public double getOneSkuPrice() {
        return oneSkuPrice;
    }

    public void setOneSkuPrice(double oneSkuPrice) {
        this.oneSkuPrice = oneSkuPrice;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public double getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(double skuPrice) {
        this.skuPrice = skuPrice;
    }


    public boolean isSelect() {

        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public HomeShoppingSpreeBean getProduct() {
        return product;
    }

    public void setProduct(HomeShoppingSpreeBean product) {
        this.product = product;
    }

    private HomeShoppingSpreeBean product;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }


    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }
}
