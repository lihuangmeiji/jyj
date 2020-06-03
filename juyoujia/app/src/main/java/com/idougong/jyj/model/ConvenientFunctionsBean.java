package com.idougong.jyj.model;

public class ConvenientFunctionsBean {

    /**
     * id : 37
     * archive : 0
     * createAt : 2020-04-07 14:28:22
     * updateAt : 2020-04-07 14:28:28
     * icon : https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/icon/8d589502d2384c1da9dacdf18057ac88.png
     * menu : null
     * name : 蔬菜
     * hot : null
     * type : 2
     * target : jyj://main/home/category?id=14
     * status : true
     * appType : 2
     * sort : 1
     * needLogin : false
     * version : 2
     * differateService : null
     */

    private int id;
    private int archive;
    private String createAt;
    private String updateAt;
    private String icon;
    private Object menu;
    private String name;
    private Object hot;
    private int type;
    private String target;
    private boolean status;
    private int appType;
    private int sort;
    private boolean needLogin;
    private int version;
    private Object differateService;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Object getMenu() {
        return menu;
    }

    public void setMenu(Object menu) {
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getHot() {
        return hot;
    }

    public void setHot(Object hot) {
        this.hot = hot;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Object getDifferateService() {
        return differateService;
    }

    public void setDifferateService(Object differateService) {
        this.differateService = differateService;
    }
}
