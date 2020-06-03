package com.idougong.jyj.model;

public class ShoppingSpeciesBean {

    /**
     * id : 13
     * archive : 0
     * createAt : 2019-06-09 18:34:10
     * updateAt : 2019-06-09 18:38:07
     * name : 酒水饮料
     * parentId : 0
     * level : 0
     * sort : 1
     * icon : http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/85f83b49-a390-41de-b055-bd95f9ee5ee8.jpg
     * fake : 0
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private String name;
    private int parentId;
    private int level;
    private int sort;
    private String icon;
    private int fake;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFake() {
        return fake;
    }

    public void setFake(int fake) {
        this.fake = fake;
    }
}
