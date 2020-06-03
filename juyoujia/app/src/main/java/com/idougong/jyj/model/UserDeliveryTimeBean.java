package com.idougong.jyj.model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class UserDeliveryTimeBean implements IPickerViewData {

    /**
     * date : 2020-03-19
     * time : [{"deliveryDesc":"上午","beginTime":"9:00:00","endTime":"10:30:00"},{"deliveryDesc":"下午","beginTime":"16:00:00","endTime":"19:00:00"}]
     */

    private String date;
    private List<TimeBean> time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    @Override
    public String getPickerViewText() {
        return date;
    }

    public static class TimeBean {
        /**
         * deliveryDesc : 上午
         * beginTime : 9:00:00
         * endTime : 10:30:00
         */

        private String deliveryDesc;
        private String beginTime;
        private String endTime;

        public String getDeliveryDesc() {
            return deliveryDesc;
        }

        public void setDeliveryDesc(String deliveryDesc) {
            this.deliveryDesc = deliveryDesc;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
