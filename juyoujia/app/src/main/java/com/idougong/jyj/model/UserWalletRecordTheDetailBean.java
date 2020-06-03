package com.idougong.jyj.model;

import java.util.List;

public class UserWalletRecordTheDetailBean {


    /**
     * id : 16
     * archive : 0
     * createAt : 2019-12-13 17:16:51
     * updateAt : 2019-12-13 17:16:51
     * userId : 6
     * type : 2
     * amount : 10
     * typeDesc : 提现失败退回
     * targetId : 1
     * description : 拒绝
     * descriptionArr : ["拒绝"]
     * reason : null
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private int userId;
    private int type;
    private double amount;
    private String typeDesc;
    private int targetId;
    private String description;
    private String reason;
    private List<String> descriptionArr;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getDescriptionArr() {
        return descriptionArr;
    }

    public void setDescriptionArr(List<String> descriptionArr) {
        this.descriptionArr = descriptionArr;
    }
}
