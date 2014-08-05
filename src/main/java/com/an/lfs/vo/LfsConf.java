package com.an.lfs.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LfsConf {
    private Map<String, List<String>> countryCompanies = new HashMap<>();

    public LfsConf() {
    }

    @Override
    public String toString() {
        return "LfsConf [" + (countryCompanies != null ? "countryCompanies=" + countryCompanies : "") + "]";
    }

    public Map<String, List<String>> getCountryCompanies() {
        return countryCompanies;
    }

    public void setCountryCompanies(Map<String, List<String>> countryCompanies) {
        this.countryCompanies = countryCompanies;
    }
}
