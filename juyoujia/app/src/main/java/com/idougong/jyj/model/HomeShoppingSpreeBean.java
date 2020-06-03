package com.idougong.jyj.model;

import java.util.List;

public class HomeShoppingSpreeBean {


    /**
     * id : 148
     * archive : 0
     * createAt : 2019-12-12 10:14:47
     * updateAt : 2019-12-15 01:00:00
     * name : zh测试商品
     * images : http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/b2114918-1662-4bae-8993-1b42bd85a38c.png
     * desc : 测试数据
     * categoryId : null
     * status : 0
     * dividerPercent : null
     * shopId : 0
     * imageList : ["http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/b2114918-1662-4bae-8993-1b42bd85a38c.png"]
     * image : http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/b2114918-1662-4bae-8993-1b42bd85a38c.png?x-oss-process=image/resize,w_256
     * sort : null
     * model : 2
     * type : 3
     * point : 300
     * sourcePoint : 500
     * soldTotal : 49
     * limit : 10
     * details : <p>测试数据</p>
     * inventory : 56
     * homeShow : null
     * todayBuy : null
     * descDisplay : false
     * sourcePrice : 0
     * currentPrice : 0.02
     * userCount : null
     * skuList : null
     * shopIds : null
     * seckill:false
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private String name;
    private String nameAppend;
    private String images;
    private String desc;
    private String categoryId;
    private int status;
    private int dividerPercent;
    private int storeId;
    private String image;
    private int sort;
    private int model;
    private int type;
    private int point;
    private int sourcePoint;
    private int soldTotal;
    private int limit;
    private String details;
    private int inventory;
    private Object homeShow;
    private Object todayBuy;
    private boolean descDisplay;
    private double sourcePrice;
    private double currentPrice;
    private Object userCount;
    private List<SkuInfoBean> skuList;
    private List<HandlingBean> processingWayList;
    private Object shopIds;
    private List<String> imageList;
    private int productNum;
    private String specifications;
    private String refundHint;
    private String qrCode;

    /**
     * secKillStart : 2020-04-22 18:00:00
     * secKillEnd : 2020-04-22 19:00:00
     */

    private String secKillStart;
    private String secKillEnd;

    public String getRefundHint() {
        return refundHint;
    }

    public void setRefundHint(String refundHint) {
        this.refundHint = refundHint;
    }

    public String getNameAppend() {
        return nameAppend;
    }

    public void setNameAppend(String nameAppend) {
        this.nameAppend = nameAppend;
    }

    public boolean isSeckill() {
        return seckill;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public boolean getSeckill() {
        return seckill;
    }

    public void setSeckill(boolean seckill) {
        this.seckill = seckill;
    }

    private boolean seckill;


    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDividerPercent() {
        return dividerPercent;
    }

    public void setDividerPercent(int dividerPercent) {
        this.dividerPercent = dividerPercent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(int sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public int getSoldTotal() {
        return soldTotal;
    }

    public void setSoldTotal(int soldTotal) {
        this.soldTotal = soldTotal;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Object getHomeShow() {
        return homeShow;
    }

    public void setHomeShow(Object homeShow) {
        this.homeShow = homeShow;
    }

    public Object getTodayBuy() {
        return todayBuy;
    }

    public void setTodayBuy(Object todayBuy) {
        this.todayBuy = todayBuy;
    }

    public boolean isDescDisplay() {
        return descDisplay;
    }

    public void setDescDisplay(boolean descDisplay) {
        this.descDisplay = descDisplay;
    }

    public double getSourcePrice() {
        return sourcePrice;
    }

    public void setSourcePrice(double sourcePrice) {
        this.sourcePrice = sourcePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Object getUserCount() {
        return userCount;
    }

    public void setUserCount(Object userCount) {
        this.userCount = userCount;
    }

    public List<SkuInfoBean> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuInfoBean>  skuList) {
        this.skuList = skuList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Object getShopIds() {
        return shopIds;
    }

    public void setShopIds(Object shopIds) {
        this.shopIds = shopIds;
    }

    public String getSecKillStart() {
        return secKillStart;
    }

    public void setSecKillStart(String secKillStart) {
        this.secKillStart = secKillStart;
    }

    public String getSecKillEnd() {
        return secKillEnd;
    }

    public void setSecKillEnd(String secKillEnd) {
        this.secKillEnd = secKillEnd;
    }

    public List<HandlingBean> getProcessingWayList() {
        return processingWayList;
    }

    public void setProcessingWayList(List<HandlingBean> processingWayList) {
        this.processingWayList = processingWayList;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
