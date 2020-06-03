package com.idougong.jyj.model;

public class AfterpayAdvertiseBean {


    /**
     * id : 1
     * archive : 0
     * createAt : 2019-12-16 15:45:11
     * updateAt : null
     * title : 幸运大转盘
     * name : 幸运大转盘
     * picture : http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/consultation/3c7e4ade-6397-4cd0-97ba-0c41a4140eb6.png
     * target : http://gyj-dev-api.idougong.com/luckyTurntable/index.html
     * needLogin : true
     */

    private int id;
    private int archive;
    private String createAt;
    private Object updateAt;
    private String title;
    private String name;
    private String picture;
    private String target;
    private boolean needLogin;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }
}
