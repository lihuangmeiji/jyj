package com.idougong.jyj.model;

public class CustomerServiceBean {

    /**
     * id : 1
     * archive : 0
     * createAt : 2020-04-09 15:12:47
     * updateAt : 2020-04-09 15:12:49
     * code : qq_1241331834
     * phone : 0571-87323709
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private String code;
    private String phone;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
