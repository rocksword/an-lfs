package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

public class MatchCategory {
    // 1.2 -> Category
    private Map<String, Category> hostCatMap = new HashMap<>();
    private Map<String, Category> guestCatMap = new HashMap<>();
    private Map<String, Category> middleCatMap = new HashMap<>();

    /**
     * @param hostCat
     * @param guestCat
     * @param middleCat
     * @param betResult
     */
    public void addCategory(String hostCat, String guestCat, String middleCat, BetResult betResult) {
        if (!hostCatMap.containsKey(hostCat)) {
            hostCatMap.put(hostCat, new Category(hostCat));
        }
        if (!guestCatMap.containsKey(guestCat)) {
            guestCatMap.put(guestCat, new Category(guestCat));
        }
        if (!middleCatMap.containsKey(middleCat)) {
            middleCatMap.put(middleCat, new Category(middleCat));
        }

        if (betResult.isPass()) {
            hostCatMap.get(hostCat).addPassNum();
            guestCatMap.get(guestCat).addPassNum();
            middleCatMap.get(middleCat).addPassNum();
        } else if (betResult.isFail()) {
            hostCatMap.get(hostCat).addFailNum();
            guestCatMap.get(guestCat).addFailNum();
            middleCatMap.get(middleCat).addFailNum();
        } else {
            //
        }
    }

    public MatchCategory() {
    }

    public Map<String, Category> getHostCatMap() {
        return hostCatMap;
    }

    public void setHostCatMap(Map<String, Category> hostCatMap) {
        this.hostCatMap = hostCatMap;
    }

    public Map<String, Category> getGuestCatMap() {
        return guestCatMap;
    }

    public void setGuestCatMap(Map<String, Category> guestCatMap) {
        this.guestCatMap = guestCatMap;
    }

    public Map<String, Category> getMiddleCatMap() {
        return middleCatMap;
    }

    public void setMiddleCatMap(Map<String, Category> middleCatMap) {
        this.middleCatMap = middleCatMap;
    }
}
