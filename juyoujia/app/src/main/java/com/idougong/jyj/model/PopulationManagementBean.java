package com.idougong.jyj.model;

import java.util.List;

public class PopulationManagementBean {

    /**
     * floatingPopulationRegistrationId : null
     * userId : null
     * realName : 李鹏洋
     * idCode : 140423199310016410
     * imgFront : null
     * imgBack : null
     * cardInfo : null
     * status : null
     * validityPeriod : null
     * gender : null
     * address : null
     * careerKindId : null
     * teamName : null
     * cpId : 3
     * phone : 15835568257
     * careerKinds : [{"id":1,"archive":0,"createAt":"2019-01-26 15:14:51","updateAt":"2019-01-26 15:14:51","name":"木工","sort":1,"type":0},{"id":3,"archive":0,"createAt":"2019-01-26 15:14:51","updateAt":"2019-01-26 15:14:51","name":"焊工","sort":3,"type":0},{"id":6,"archive":0,"createAt":"2019-01-26 15:14:51","updateAt":"2019-01-26 15:14:51","name":"抹灰工","sort":6,"type":0},{"id":10,"archive":0,"createAt":"2019-01-26 15:14:51","updateAt":"2019-01-26 15:14:51","name":"防水工","sort":10,"type":0}]
     * cpName : 亚运佳苑训练馆
     * archive : null
     * createAt : null
     * updateAt : null
     */

    private int floatingPopulationRegistrationId;
    private int userId;
    private String realName;
    private String idCode;
    private String imgFront;
    private String imgBack;
    private String cardInfo;
    private int status;
    private String validityPeriod;
    private String gender;
    private String address;
    private String careerKindId;
    private String teamName;
    private int cpId;
    private String phone;
    private String cpName;
    private String archive;
    private String createAt;
    private String updateAt;
    private List<CareerKindsBean> careerKinds;

    public int getFloatingPopulationRegistrationId() {
        return floatingPopulationRegistrationId;
    }

    public void setFloatingPopulationRegistrationId(int floatingPopulationRegistrationId) {
        this.floatingPopulationRegistrationId = floatingPopulationRegistrationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getImgFront() {
        return imgFront;
    }

    public void setImgFront(String imgFront) {
        this.imgFront = imgFront;
    }

    public String getImgBack() {
        return imgBack;
    }

    public void setImgBack(String imgBack) {
        this.imgBack = imgBack;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCareerKindId() {
        return careerKindId;
    }

    public void setCareerKindId(String careerKindId) {
        this.careerKindId = careerKindId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getCpId() {
        return cpId;
    }

    public void setCpId(int cpId) {
        this.cpId = cpId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
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

    public List<CareerKindsBean> getCareerKinds() {
        return careerKinds;
    }

    public void setCareerKinds(List<CareerKindsBean> careerKinds) {
        this.careerKinds = careerKinds;
    }

    public static class CareerKindsBean {
        /**
         * id : 1
         * archive : 0
         * createAt : 2019-01-26 15:14:51
         * updateAt : 2019-01-26 15:14:51
         * name : 木工
         * sort : 1
         * type : 0
         */

        private int id;
        private int archive;
        private String createAt;
        private String updateAt;
        private String name;
        private int sort;
        private int type;

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

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
