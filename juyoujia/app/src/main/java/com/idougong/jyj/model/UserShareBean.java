package com.idougong.jyj.model;

public class UserShareBean {

    /**
     * title : 居有家红包助力
     * content : 居有家十万元购票补贴等你拿
     * url : http://192.168.0.20:8002/help.html?id=user_id
     * icon : http://192.168.0.20:8002/images/icon.jpg
     */

    private String title;
    private String content;
    private String url;
    private String icon;
    private String platformType;

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
