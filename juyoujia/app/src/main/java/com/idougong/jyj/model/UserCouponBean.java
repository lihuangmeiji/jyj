package com.idougong.jyj.model;

public class UserCouponBean {


    /**
     * id : 1
     * archive : 0
     * createAt : 2020-02-29 15:48:57
     * updateAt : null
     * userCouponId : 1
     * name : null
     * withAmount : 49
     * usedAmount : 10
     * startTime : null
     * endTime : null
     * validStartTime : 2020-02-29 00:00:00
     * validEndTime : 2020-03-31 23:59:59
     * enable : false
     * reason : 无法使用！
     * usedProportion
     *couponType
     * maxWithAmount
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int userCouponId;
    private String name;
    private double withAmount;
    private double usedAmount;
    private String startTime;
    private String endTime;
    private String validStartTime;
    private String validEndTime;
    private boolean enable;
    private String reason;
    private double usedProportion;
    private int couponType;
    private int maxWithAmount;

    public double getUsedProportion() {
        return usedProportion;
    }

    public void setUsedProportion(double usedProportion) {
        this.usedProportion = usedProportion;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public int getMaxWithAmount() {
        return maxWithAmount;
    }

    public void setMaxWithAmount(int maxWithAmount) {
        this.maxWithAmount = maxWithAmount;
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

    public int getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(int userCouponId) {
        this.userCouponId = userCouponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWithAmount() {
        return withAmount;
    }

    public void setWithAmount(double withAmount) {
        this.withAmount = withAmount;
    }

    public double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(String validStartTime) {
        this.validStartTime = validStartTime;
    }

    public String getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
