package com.idougong.jyj.model;

import java.util.List;

public class HomeShoppingSpreeTwoBean {


    /**
     * id : 14
     * archive : 0
     * createAt : 2019-06-09 18:34:35
     * updateAt : 2020-04-16 15:21:11
     * name : 蔬菜
     * parentId : 0
     * level : 1
     * sort : 1
     * icon : http://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/icon/8d589502d2384c1da9dacdf18057ac88.png
     * fake : 0
     * tagIds : null
     * products : []
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
    private Object tagIds;
    private List<HomeShoppingSpreeBean> products;

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

    public Object getTagIds() {
        return tagIds;
    }

    public void setTagIds(Object tagIds) {
        this.tagIds = tagIds;
    }

    public List<HomeShoppingSpreeBean> getProducts() {
        return products;
    }

    public void setProducts(List<HomeShoppingSpreeBean> products) {
        this.products = products;
    }
}
