package com.an.lfs;

public class LfsConf {
    private int parse;

    public LfsConf() {
    }

    @Override
    public String toString() {
        return "LfsConf [parse=" + parse + "]";
    }

    public int isParse() {
        return parse;
    }

    public void setParse(int parse) {
        this.parse = parse;
    }
}
