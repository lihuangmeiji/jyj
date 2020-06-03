package com.idougong.jyj.model;

import java.util.List;

public class StaticAddressBean {

    /**
     * province : 浙江省
     * city : 杭州市
     */

    private List<DistrictBean> districtList;
    private String province;
    private String city;

    public List<DistrictBean> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictBean> districtList) {
        this.districtList = districtList;
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
}
