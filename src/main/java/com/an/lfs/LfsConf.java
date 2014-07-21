package com.an.lfs;

import java.util.List;

public class LfsConf {
    private List<String> companys;

    public LfsConf() {
    }

    @Override
    public String toString() {
        return "LfsConf [companys=" + companys + "]";
    }

    public List<String> getCompanys() {
        return companys;
    }

    public void setCompanys(List<String> companys) {
        this.companys = companys;
    }
}
