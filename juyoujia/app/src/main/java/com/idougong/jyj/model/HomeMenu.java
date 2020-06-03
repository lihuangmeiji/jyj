package com.idougong.jyj.model;

public class HomeMenu {

    /**
     * imTarget : http://gyj-api.idougong.com/h5common/gameCenter_v1/index.html
     * healthTarget : http://gyj-dev-api.idougong.com/h5common/kingSIM/index.html#/SIMIntroduce
     * familyTarget : null
     */

    private String imTarget;
    private String healthTarget;
    private String familyTarget;

    public String getImTarget() {
        return imTarget;
    }

    public void setImTarget(String imTarget) {
        this.imTarget = imTarget;
    }

    public String getHealthTarget() {
        return healthTarget;
    }

    public void setHealthTarget(String healthTarget) {
        this.healthTarget = healthTarget;
    }

    public String getFamilyTarget() {
        return familyTarget;
    }

    public void setFamilyTarget(String familyTarget) {
        this.familyTarget = familyTarget;
    }
}
