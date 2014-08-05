package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchRate {
    // host_guest
    private String matchId;
    // company -> Rate
    private Map<String, Rate> rateMap = new HashMap<>();

    public List<String> getCompany() {
        List<String> ret = new ArrayList<>();
        ret.addAll(rateMap.keySet());
        Collections.sort(ret);
        return ret;
    }

    public Rate getRate(String com) {
        return rateMap.get(com);
    }

    public void addComRate(String com, Rate rate) {
        rateMap.put(com, rate);
    }

    public MatchRate() {
    }

    @Override
    public String toString() {
        return "MatchRate [" + (matchId != null ? "matchId=" + matchId + ", " : "")
                + (rateMap != null ? "rateMap=" + rateMap : "") + "]";
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public Map<String, Rate> getRateMap() {
        return rateMap;
    }

    public void setRateMap(Map<String, Rate> rateMap) {
        this.rateMap = rateMap;
    }
}
