package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

public class RateMgr {
    // host_guest
    private String key;
    // company -> Rate
    private Map<String, Rate> rateMap = new HashMap<>();

    public void setRate(String com, Rate rate) {
        rateMap.put(com, rate);
    }

    public RateMgr() {
    }

    @Override
    public String toString() {
        return "RateMgr [key=" + key + ", rateMap=" + rateMap + "]";
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Rate> getRateMap() {
        return rateMap;
    }

    public void setRateMap(Map<String, Rate> rateMap) {
        this.rateMap = rateMap;
    }
}
