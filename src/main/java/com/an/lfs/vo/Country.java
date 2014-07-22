package com.an.lfs.vo;

public enum Country {
    GER("ger"), //
    SPA("spa"), //
    ITA("ita"), //
    FRA("fra"), //
    ENG("eng");
    private String val;

    public String getVal() {
        return val;
    }

    private Country(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
