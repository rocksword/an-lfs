package com.an.lfs.vo;

public class Category {
    private String name;
    private int passNum;
    private int failNum;

    public void addPassNum() {
        this.passNum++;
    }

    public void addFailNum() {
        this.failNum++;
    }

    @Override
    public String toString() {
        return "Category [" + (name != null ? "name=" + name + ", " : "") + "passNum=" + passNum + ", failNum="
                + failNum + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }
}
