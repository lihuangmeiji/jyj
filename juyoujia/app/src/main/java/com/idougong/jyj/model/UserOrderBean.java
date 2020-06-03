package com.idougong.jyj.model;

import java.sql.Date;
import java.util.List;

public class UserOrderBean {

    /**
     * id : 357
     * archive : 0
     * createAt : 2019-09-03 16:45:41
     * updateAt : 2019-09-03 16:45:41
     * userId : 6
     * orderNo : 15835568257201909031645417843501
     * totalPrice : 0.0
     * discountPrice : null
     * couponPrice : null
     * finalPrice : 0.0
     * point : 300
     * shopId : null
     * status : 2
     * shopName : null
     * payType : null
     * reasonsReturn : null
     * deliveryId : null
     * products : [{"id":585,"archive":0,"createAt":"2019-09-03 16:45:41","updateAt":"2019-09-03 16:45:41","num":1,"skuId":null,"productId":58,"userId":6,"status":1,"deliveryId":null,"orderId":357,"product":{"id":58,"archive":0,"createAt":"2019-05-15 16:25:04","updateAt":"2019-09-03 16:24:05","name":"实体商品1","images":"http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png,http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/0c8d13b7-e001-49d7-83db-e5b95016537c.png,http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/67b8d48d-5055-4f3f-8d81-b9eb55e7e904.png","desc":"商品描述","categoryId":"27","status":0,"dividerPercent":0,"shopId":0,"imageList":["http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png","http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/0c8d13b7-e001-49d7-83db-e5b95016537c.png","http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/67b8d48d-5055-4f3f-8d81-b9eb55e7e904.png"],"image":"http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png","sort":0,"model":2,"type":1,"point":300,"soldTotal":3,"limit":500,"details":"<p>\t\u201c利奇马\u201d已于今晨加强为台风级，5点其中心位于台湾省台北市东南大约890公里的西北太平洋上，北纬19.8度、东经128.1度， 12级（33米/秒）。<\/p><p>\t预计,\u201c利奇马\u201d将以每小时10-15公里的速度向西北方向移动，<strong>强度逐渐增强，最强可达强台风级（14-15级，42-48米/秒），并逐渐向浙江沿海靠近，9日夜间到10日上午将在浙江沿海登陆（台风级或强台风级12-14级，35-42米/秒）<\/strong>；但目前也不完全排除近海北上的可能。<\/p><p>\t受其影响，今天台湾以东洋面、台湾海峡、东海南部海域及钓鱼岛附近海域、南海南部海域、台湾岛北部沿海将有6-7级大风，其中台湾以东洋面部分海域将有8-10级大风，\u201c利奇马\u201d中心经过的附近海域的风力可达11-13级，阵风13-15级；台湾岛北部将有中到大雨。<\/p><p>\t受\u201c利奇马\u201d台风影响，预计9-10日温州将有明显的风雨过程。<\/p><p class=\"ql-align-center\">\t<img src=\"https://inews.gtimg.com/newsapp_bt/0/9949702830/641\"><\/p><p><br><\/p>","homeShow":false,"sourcePrice":0,"currentPrice":0,"skuList":null,"shopIds":null},"sku":null,"totalPrice":null}]
     * phone : 15835568257
     * constructionPlace : {"id":2,"archive":0,"createAt":"2019-05-07 17:01:43","updateAt":"2019-07-31 16:16:49","name":"金茂府","area":"2","province":"浙江省","city":"杭州市","zone":"2","address":"2","period":[2019,7,31],"longitude":"2","latitude":"21"}
     * exchangeOrder : null
     * delivery : {"id":372,"archive":0,"createAt":"2019-09-03 16:45:42","updateAt":null,"time":null,"shopId":null,"orderId":357,"status":0,"qrCodeImg":null,"takeoutTime":null,"model":2,"deliveryInfo":null,"phone":"15888888888","name":"李","address":"浙江省杭州市滨江区"}
     * model : 2
     * type : 1
     * salesReturn : false
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int userId;
    private String orderNo;
    private double totalPrice;
    private Object discountPrice;
    private double couponPrice;
    private double finalPrice;
    private int point;
    private Object shopId;
    private int status;
    private Object shopName;
    private String payType;
    private Object reasonsReturn;
    private Object deliveryId;
    private String phone;
    private ConstructionPlaceBean constructionPlace;
    private ExchangeOrderBean exchangeOrder;
    private DeliveryBean delivery;
    private int model;
    private int type;
    private boolean salesReturn;
    private List<ProductsBean> products;
    private String payAt;
    private String cancelAt;
    private double usedAmount;



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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Object getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Object discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }


    public double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Object getShopId() {
        return shopId;
    }

    public void setShopId(Object shopId) {
        this.shopId = shopId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getShopName() {
        return shopName;
    }

    public void setShopName(Object shopName) {
        this.shopName = shopName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Object getReasonsReturn() {
        return reasonsReturn;
    }

    public void setReasonsReturn(Object reasonsReturn) {
        this.reasonsReturn = reasonsReturn;
    }

    public Object getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Object deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ConstructionPlaceBean getConstructionPlace() {
        return constructionPlace;
    }

    public void setConstructionPlace(ConstructionPlaceBean constructionPlace) {
        this.constructionPlace = constructionPlace;
    }

    public ExchangeOrderBean getExchangeOrder() {
        return exchangeOrder;
    }

    public void setExchangeOrder(ExchangeOrderBean exchangeOrder) {
        this.exchangeOrder = exchangeOrder;
    }

    public String getPayAt() {
        return payAt;
    }

    public void setPayAt(String payAt) {
        this.payAt = payAt;
    }

    public String getCancelAt() {
        return cancelAt;
    }

    public void setCancelAt(String cancelAt) {
        this.cancelAt = cancelAt;
    }

    public DeliveryBean getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryBean delivery) {
        this.delivery = delivery;
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

    public boolean isSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(boolean salesReturn) {
        this.salesReturn = salesReturn;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ConstructionPlaceBean {
        /**
         * id : 2
         * archive : 0
         * createAt : 2019-05-07 17:01:43
         * updateAt : 2019-07-31 16:16:49
         * name : 金茂府
         * area : 2
         * province : 浙江省
         * city : 杭州市
         * zone : 2
         * address : 2
         * period : [2019,7,31]
         * longitude : 2
         * latitude : 21
         */

        private int id;
        private int archive;
        private String createAt;
        private String updateAt;
        private String name;
        private String area;
        private String province;
        private String city;
        private String zone;
        private String address;
        private String longitude;
        private String latitude;
        private List<Integer> period;

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

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public List<Integer> getPeriod() {
            return period;
        }

        public void setPeriod(List<Integer> period) {
            this.period = period;
        }
    }

    public static class DeliveryBean {
        /**
         * id : 372
         * archive : 0
         * createAt : 2019-09-03 16:45:42
         * updateAt : null
         * time : null
         * timeEnd:null
         * shopId : null
         * orderId : 357
         * status : 0  发货
         * qrCodeImg : null
         * takeoutTime : null
         * model : 2
         * deliveryInfo : null
         * phone : 15888888888
         * name : 李
         * address : 浙江省杭州市滨江区
         * description:null
         */

        private int id;
        private int archive;
        private String createAt;
        private Object updateAt;
        private String time;
        private Object shopId;
        private int orderId;
        private int status;
        private String qrCodeImg;
        private Object takeoutTime;
        private int model;
        private String deliveryInfo;
        private String phone;
        private String name;
        private String address;
        private String timeEnd;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(String timeEnd) {
            this.timeEnd = timeEnd;
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

        public Object getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(Object updateAt) {
            this.updateAt = updateAt;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Object getShopId() {
            return shopId;
        }

        public void setShopId(Object shopId) {
            this.shopId = shopId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getQrCodeImg() {
            return qrCodeImg;
        }

        public void setQrCodeImg(String qrCodeImg) {
            this.qrCodeImg = qrCodeImg;
        }

        public Object getTakeoutTime() {
            return takeoutTime;
        }

        public void setTakeoutTime(Object takeoutTime) {
            this.takeoutTime = takeoutTime;
        }

        public int getModel() {
            return model;
        }

        public void setModel(int model) {
            this.model = model;
        }

        public String getDeliveryInfo() {
            return deliveryInfo;
        }

        public void setDeliveryInfo(String deliveryInfo) {
            this.deliveryInfo = deliveryInfo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }

    public static class ProductsBean {
        /**
         * id : 585
         * archive : 0
         * createAt : 2019-09-03 16:45:41
         * updateAt : 2019-09-03 16:45:41
         * num : 1
         * skuId : null
         * productId : 58
         * userId : 6
         * status : 1
         * deliveryId : null
         * orderId : 357
         * product : {"id":58,"archive":0,"createAt":"2019-05-15 16:25:04","updateAt":"2019-09-03 16:24:05","name":"实体商品1","images":"http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png,http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/0c8d13b7-e001-49d7-83db-e5b95016537c.png,http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/67b8d48d-5055-4f3f-8d81-b9eb55e7e904.png","desc":"商品描述","categoryId":"27","status":0,"dividerPercent":0,"shopId":0,"imageList":["http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png","http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/0c8d13b7-e001-49d7-83db-e5b95016537c.png","http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/67b8d48d-5055-4f3f-8d81-b9eb55e7e904.png"],"image":"http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/fa13905a-d7bd-4f86-b7f0-7d4b45245bec.png","sort":0,"model":2,"type":1,"point":300,"soldTotal":3,"limit":500,"details":"<p>\t\u201c利奇马\u201d已于今晨加强为台风级，5点其中心位于台湾省台北市东南大约890公里的西北太平洋上，北纬19.8度、东经128.1度， 12级（33米/秒）。<\/p><p>\t预计,\u201c利奇马\u201d将以每小时10-15公里的速度向西北方向移动，<strong>强度逐渐增强，最强可达强台风级（14-15级，42-48米/秒），并逐渐向浙江沿海靠近，9日夜间到10日上午将在浙江沿海登陆（台风级或强台风级12-14级，35-42米/秒）<\/strong>；但目前也不完全排除近海北上的可能。<\/p><p>\t受其影响，今天台湾以东洋面、台湾海峡、东海南部海域及钓鱼岛附近海域、南海南部海域、台湾岛北部沿海将有6-7级大风，其中台湾以东洋面部分海域将有8-10级大风，\u201c利奇马\u201d中心经过的附近海域的风力可达11-13级，阵风13-15级；台湾岛北部将有中到大雨。<\/p><p>\t受\u201c利奇马\u201d台风影响，预计9-10日温州将有明显的风雨过程。<\/p><p class=\"ql-align-center\">\t<img src=\"https://inews.gtimg.com/newsapp_bt/0/9949702830/641\"><\/p><p><br><\/p>","homeShow":false,"sourcePrice":0,"currentPrice":0,"skuList":null,"shopIds":null}
         * sku : null
         * totalPrice : null
         */

        private int id;
        private int archive;
        private String createAt;
        private String updateAt;
        private int num;
        private Object skuId;
        private int productId;
        private int userId;
        private int status;
        private Object deliveryId;
        private int orderId;
        private HomeShoppingSpreeBean product;
        private SkuInfoBean sku;
        private HandlingBean processingWay;
        private Object totalPrice;

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

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public Object getSkuId() {
            return skuId;
        }

        public void setSkuId(Object skuId) {
            this.skuId = skuId;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getDeliveryId() {
            return deliveryId;
        }

        public void setDeliveryId(Object deliveryId) {
            this.deliveryId = deliveryId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public HomeShoppingSpreeBean getProduct() {
            return product;
        }

        public void setProduct(HomeShoppingSpreeBean product) {
            this.product = product;
        }

        public SkuInfoBean getSku() {
            return sku;
        }

        public void setSku(SkuInfoBean sku) {
            this.sku = sku;
        }

        public Object getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Object totalPrice) {
            this.totalPrice = totalPrice;
        }

        public HandlingBean getProcessingWay() {
            return processingWay;
        }

        public void setProcessingWay(HandlingBean processingWay) {
            this.processingWay = processingWay;
        }
    }

    public static class ExchangeOrderBean{
        private Long orderId;

        /**
         * 1.二维码  2.手机号
         */
        private Integer type;

        /**
         * 二维码地址或手机号
         */
        private String target;

        /**
         * 1.未使用  2.已使用
         */
        private Integer status;

        private Long id;

        private Integer archive;

        private String createAt;

        private String updateAt;

        private String useInfo;

        public String getUseInfo() {
            return useInfo;
        }

        public void setUseInfo(String useInfo) {
            this.useInfo = useInfo;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getArchive() {
            return archive;
        }

        public void setArchive(Integer archive) {
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


    }
}
