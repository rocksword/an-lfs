package com.an.lfs.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Category {
    private static final Log logger = LogFactory.getLog(Category.class);
    // 1.2,1.4
    private String name;
    private int passNum;
    private int failNum;

    public void addPassNum() {
        this.passNum++;
    }

    public void addFailNum() {
        this.failNum++;
    }

    public Category(String name) {
        this.name = name;
    }

    public Category() {
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
