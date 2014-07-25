package com.an.lfs.vo;

import java.util.List;

public class LfsConf {
    private List<String> countries;
    private List<List<String>> companys;

    public LfsConf() {
    }

    @Override
    public String toString() {
        return "LfsConf [countries=" + countries + ", companys=" + companys + "]";
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<List<String>> getCompanys() {
        return companys;
    }

    public void setCompanys(List<List<String>> companys) {
        this.companys = companys;
    }
}
