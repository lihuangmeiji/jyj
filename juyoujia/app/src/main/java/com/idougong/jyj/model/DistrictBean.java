package com.idougong.jyj.model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class DistrictBean implements IPickerViewData {

    /**
     * district : 萧山区
     */

    private List<String> livingArea;
    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<String> getLivingArea() {
        return livingArea;
    }

    public void setLivingArea(List<String> livingArea) {
        this.livingArea = livingArea;
    }

    @Override
    public String getPickerViewText() {
        return district;
    }
}
