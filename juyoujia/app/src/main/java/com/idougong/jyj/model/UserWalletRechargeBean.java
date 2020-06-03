package com.idougong.jyj.model;

public class UserWalletRechargeBean {

    /**
     * aliPayInfo : alipay_sdk=alipay-sdk-java-4.8.73.ALL&app_id=2019032663704286&biz_content=%7B%22body%22%3A%22%E5%B7%A5%E5%8F%8B%E5%AE%B6%E5%85%85%E5%80%BC10%E5%85%83%22%2C%22out_trade_no%22%3A%2215110344121201912061844560208344%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%B7%A5%E5%8F%8B%E5%AE%B6%E4%BD%99%E9%A2%9D%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%225m%22%2C%22total_amount%22%3A%2210.00%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fgyj-dev-api.idougong.com%2Fv2%2FwalletTopUp%2Fali%2FnotifyUrl&sign=b2H%2F6vv8W9YotGu763xX0QMBN1Yt6SBOFWxR58tSm6tsWgGO9uugQeRuwF1aObPJvFid6oTkUNve9V%2Beulk5izW6lJVjjUv5M646ovgO7pMPyG2LLJOkwP56puigSfYL8AVO39Tr%2FPUW6KB89SsbWBZL4FyHRQEGW0FwIoPHOnKQLntBDl7QFURrM%2FL0OiWtw2J6fxM1binmQL0VYUNatOwyYIGs8vpsqHwltUv%2FvlcSskzhFYkEy6eOxZdbzU2fcy%2FXK4SzT%2FF8okX0eUMsa9N%2BJQVLoDEa0Fl4zz3t6eiFguSb4Sry3OZ4qe5QKGf%2FnWx9sMlvwQwgSEujHCi8tw%3D%3D&sign_type=RSA2&timestamp=2019-12-06+18%3A44%3A56&version=1.0
     * info : {"id":16,"archive":0,"createAt":"2019-12-06 18:44:56","updateAt":"2019-12-06 18:44:56","userId":50,"bizNo":"15110344121201912061844560208344","tradeNo":null,"amount":10,"payType":1,"status":1}
     */

    private UserRechargeWxBean wxPayInfo;
    private String aliPayInfo;
    private InfoBean info;

    public UserRechargeWxBean getWxPayInfo() {
        return wxPayInfo;
    }

    public void setWxPayInfo(UserRechargeWxBean wxPayInfo) {
        this.wxPayInfo = wxPayInfo;
    }
    public String getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(String aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 16
         * archive : 0
         * createAt : 2019-12-06 18:44:56
         * updateAt : 2019-12-06 18:44:56
         * userId : 50
         * bizNo : 15110344121201912061844560208344
         * tradeNo : null
         * amount : 10
         * payType : 1
         * status : 1
         */

        private int id;
        private int archive;
        private String createAt;
        private String updateAt;
        private int userId;
        private String bizNo;
        private Object tradeNo;
        private double amount;
        private int payType;
        private int status;

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

        public String getBizNo() {
            return bizNo;
        }

        public void setBizNo(String bizNo) {
            this.bizNo = bizNo;
        }

        public Object getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(Object tradeNo) {
            this.tradeNo = tradeNo;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
