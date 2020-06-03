package com.idougong.jyj.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryAddressBean {

    /**
     * id : 41
     * archive : 0
     * createAt : 2020-03-03 11:58:06
     * updateAt : 2020-03-08 01:23:52
     * userId : 15
     * name : 李白
     * phone : 15111111111
     * areaCode : 330100
     * province : 浙江省
     * city : 杭州市
     * district : 萧山区
     * street : null
     * community : null
     * livingArea : 林之语
     * address : 2号6栋11单元1801
     * isDefault : false
     * provinceCode : null
     * cityInfo : null
     * detailsAddress : 杭州市萧山区林之语2号6栋11单元1801
     * default : false
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int userId;
    private String name;
    private String phone;
    private int areaCode;
    private String province;
    private String city;
    private String district;
    private Object street;
    private Object community;
    private String livingArea;
    private String address;
    private boolean isDefault;
    private Object provinceCode;
    private Object cityInfo;
    private String detailsAddress;
    @SerializedName("default")
    private boolean defaultX;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getCommunity() {
        return community;
    }

    public void setCommunity(Object community) {
        this.community = community;
    }

    public String getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(String livingArea) {
        this.livingArea = livingArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Object getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Object provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Object getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(Object cityInfo) {
        this.cityInfo = cityInfo;
    }

    public String getDetailsAddress() {
        return detailsAddress;
    }

    public void setDetailsAddress(String detailsAddress) {
        this.detailsAddress = detailsAddress;
    }

    public boolean isDefaultX() {
        return defaultX;
    }

    public void setDefaultX(boolean defaultX) {
        this.defaultX = defaultX;
    }
}
