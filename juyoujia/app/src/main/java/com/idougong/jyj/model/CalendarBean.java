package com.idougong.jyj.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarBean {


    /**
     * msg : 今日已签到
     * checkInAmount : 1
     * checkNum : 7
     * bonus : 1
     * checkInListImg : [{"amount":0.11,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/20f5309e-fa6d-4cd4-bd97-a5d1c097fad4.png","state":true,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"}]
     * state : true
     * checkInSuccessImg : https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/e86eb98b-c5a1-4f2f-bec8-069b2da5a0c2.png
     *userAccount
     */

    private String msg;
    private int checkInAmount;
    private int checkNum;
    private double bonus;
    private boolean state;
    private String checkInSuccessImg;
    private List<CheckInListImgBean> checkInListImg;
    private double userAccount;
    /**
     * amount : 0.11
     * checkInListImg : [{"amount":0.11,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/20f5309e-fa6d-4cd4-bd97-a5d1c097fad4.png","state":true,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"},{"amount":null,"img":"https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/c2ac3e16-26f0-4f52-b95a-087e078898f2.png","state":false,"textColor":"#E73632"}]
     * checkInSuccessText : <font  color="#FF1F01" size="10">签到成功</font ><br><font  color="#999999" size="6">获得</font><font  color="#FF6753" size="6">0.11</font><font  color="#999999" size="6">元菜金</font> <br><font  color="#999999" size="2"> 注：获得的菜金可在我的页面查看</font>
     */

    private double amount;
    private String checkInSuccessText;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCheckInAmount() {
        return checkInAmount;
    }

    public void setCheckInAmount(int checkInAmount) {
        this.checkInAmount = checkInAmount;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getCheckInSuccessImg() {
        return checkInSuccessImg;
    }

    public void setCheckInSuccessImg(String checkInSuccessImg) {
        this.checkInSuccessImg = checkInSuccessImg;
    }

    public List<CheckInListImgBean> getCheckInListImg() {
        return checkInListImg;
    }

    public void setCheckInListImg(List<CheckInListImgBean> checkInListImg) {
        this.checkInListImg = checkInListImg;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCheckInSuccessText() {
        return checkInSuccessText;
    }

    public void setCheckInSuccessText(String checkInSuccessText) {
        this.checkInSuccessText = checkInSuccessText;
    }

    public double getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(double userAccount) {
        this.userAccount = userAccount;
    }

    public static class CheckInListImgBean {
        /**
         * amount : 0.11
         * img : https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/20f5309e-fa6d-4cd4-bd97-a5d1c097fad4.png
         * state : true
         * textColor : #E73632
         */

        private double amount;
        private String img;
        private boolean state;
        private String textColor;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }


}
